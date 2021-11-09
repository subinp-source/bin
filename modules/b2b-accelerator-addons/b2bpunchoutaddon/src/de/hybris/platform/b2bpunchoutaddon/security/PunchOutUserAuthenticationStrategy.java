/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bpunchoutaddon.security;

import de.hybris.platform.core.model.user.UserModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Authentication strategy for Punch Out users.
 */
public interface PunchOutUserAuthenticationStrategy
{

	/**
	 * Authenticates a user into the system.
	 * 
	 * @param user
	 *           the user
	 * @param request
	 *           the HTTP request
	 * @param response
	 *           the HTTP response
	 */
	void authenticate(UserModel user, HttpServletRequest request, HttpServletResponse response);

	/**
	 * Logs out a user from the system.
	 */
	void logout();

}
