/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bpunchoutaddon.services;

import de.hybris.platform.b2b.punchout.PunchOutSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Provides authentication service to Punch Out users.
 */
public interface PunchOutUserAuthenticationService
{

	/**
	 * Authenticates a user.
	 * 
	 * @param userId
	 *           The user to be authenticated.
	 * @param isSeamlessLogin
	 *           True if this is a seamless login request.
	 * @param punchOutSession
	 *           The punch out session.
	 * @param response
	 *           Servlet response.
	 * @param request
	 *           Servlet request.
	 */
	void authenticate(String userId, boolean isSeamlessLogin, PunchOutSession punchOutSession, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * Logs out the user.
	 */
	void logout();


}
