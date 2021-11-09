/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservices.auth;

import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * This filter should be used after spring security filters and it is responsible for setting current authentication as
 * guest when user decided to do the checkout as a guest. During the guest checkout the userService gets current user as
 * 'anonymous', but cartService returns dedicated user.
 */
public class GuestRoleFilter extends OncePerRequestFilter
{
	private UserFacade userFacade;

	private CartService cartService;

	private AuthenticationEventPublisher authenticationEventPublisher;

	private String guestRole;

	protected static boolean canProcessAuthentication(final CustomerModel customerModel)
	{
		final Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		return currentAuth == null || !currentAuth.getClass().equals(GuestAuthenticationToken.class) || !customerModel.getUid()
				.equals(currentAuth.getName());
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse,
			final FilterChain filterchain) throws ServletException, IOException
	{
		final Optional<CustomerModel> guest = getGuest();
		guest.filter(GuestRoleFilter::canProcessAuthentication)//
				.map(CustomerModel::getUid)//
				.ifPresent(this::processAuthentication);

		filterchain.doFilter(httpservletrequest, httpservletresponse);
	}

	protected Optional<CustomerModel> getGuest()
	{
		if (getUserFacade().isAnonymousUser())
		{
			return getSessionCartUser().filter(this::isGuest);
		}
		return Optional.empty();
	}

	protected Optional<CustomerModel> getSessionCartUser()
	{
		if (cartService.hasSessionCart())
		{
			return Optional.ofNullable(cartService.getSessionCart().getUser())
					.filter(user -> CustomerModel.class.isAssignableFrom(user.getClass()))//
					.map(CustomerModel.class::cast);
		}
		return Optional.empty();
	}

	protected void processAuthentication(final String uid)
	{
		final Authentication authentication = createGuestAuthentication(uid);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		authenticationEventPublisher.publishAuthenticationSuccess(authentication);
	}

	protected Authentication createGuestAuthentication(final String uid)
	{
		final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(this.guestRole));
		return new GuestAuthenticationToken(uid, grantedAuthorities);
	}

	protected boolean isGuest(final CustomerModel cm)
	{
		return cm != null && CustomerType.GUEST.equals(cm.getType());
	}

	public AuthenticationEventPublisher getAuthenticationEventPublisher()
	{
		return authenticationEventPublisher;
	}

	@Required
	public void setAuthenticationEventPublisher(final AuthenticationEventPublisher authenticationEventPublisher)
	{
		this.authenticationEventPublisher = authenticationEventPublisher;
	}

	public String getGuestRole()
	{
		return guestRole;
	}

	@Required
	public void setGuestRole(final String guestRole)
	{
		this.guestRole = guestRole;
	}

	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}
