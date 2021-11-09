/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.interceptors;

import javax.servlet.http.HttpServletRequest;


public interface SecurePortalRequestProcessor
{
	/**
	 * get other request parameters
	 * 
	 * @param request
	 * @return request parameters
	 */
	String getOtherRequestParameters(final HttpServletRequest request);

	/**
	 * whether we want to skip secure check in secureportal
	 */
	boolean skipSecureCheck();
}
