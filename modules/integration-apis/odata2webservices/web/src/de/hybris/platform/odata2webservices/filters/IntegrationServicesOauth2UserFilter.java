/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.filters;

import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.oauth2.HybrisOauth2UserFilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Filter for integration services authentication that sets the correct user in the session derived from the client authenticated
 */
public class IntegrationServicesOauth2UserFilter implements Filter
{
	private final UserService userService;
	private final FlexibleSearchService flexibleSearchService;
	private final HybrisOauth2UserFilter hybrisOauth2UserFilter;

	private static final Log LOG = Log.getLogger(IntegrationServicesOauth2UserFilter.class);

	public IntegrationServicesOauth2UserFilter(final UserService userService,
	                                           final FlexibleSearchService flexibleSearchService,
	                                           final HybrisOauth2UserFilter hybrisOauth2UserFilter)
	{
		this.userService = userService;
		this.flexibleSearchService = flexibleSearchService;
		this.hybrisOauth2UserFilter = hybrisOauth2UserFilter;
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isOauthAndClientCredentials(auth))
		{
			try
			{
				final IntegrationClientCredentialsDetailsModel clientDetailsModel = findClientDetailsModel(
						(String) auth.getPrincipal());

				if (clientDetailsModel.getUser() == null)
				{
					logAndSendError(response, "Provided credentials are not associated to a valid user account");
				}
				else
				{
					userService.setCurrentUser(clientDetailsModel.getUser());
				}
			}
			catch (final ModelNotFoundException e)
			{
				logAndSendError(response, "The client in the request was not of type IntegrationServicesClientDetails");
			}
		}
		else
		{
			hybrisOauth2UserFilter.doFilter(request, response, chain);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		// Not relevant for this filter
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException
	{
		// Not relevant for this filter
	}

	private boolean isOauthAndClientCredentials(final Authentication auth)
	{
		return auth instanceof OAuth2Authentication && ((OAuth2Authentication) auth).isClientOnly();
	}

	private IntegrationClientCredentialsDetailsModel findClientDetailsModel(final String principal)
	{
		final IntegrationClientCredentialsDetailsModel clientDetails = new IntegrationClientCredentialsDetailsModel();
		clientDetails.setClientId(principal);
		return flexibleSearchService.getModelByExample(clientDetails);
	}

	private void logAndSendError(final ServletResponse response, final String message) throws IOException
	{
		LOG.error(message);
		((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}