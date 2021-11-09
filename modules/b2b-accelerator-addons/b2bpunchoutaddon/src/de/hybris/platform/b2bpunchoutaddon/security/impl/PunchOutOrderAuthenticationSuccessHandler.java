/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bpunchoutaddon.security.impl;

import de.hybris.platform.commercefacades.customer.CustomerFacade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * Authentication handler invoked after a successful authentication of a Punch Out user for a Order Request.
 */
public class PunchOutOrderAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private CustomerFacade customerFacade;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
		getCustomerFacade().loginSuccess();
	}

	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public void setCustomerFacade(final CustomerFacade customerFacade) {
		this.customerFacade = customerFacade;
	}

}
