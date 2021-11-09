/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.processor;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * A service to extract the service name from the {@link ODataContext}
 */
public interface ServiceNameExtractor
{
	/**
	 * Extracts the service name from the context
	 *
	 * @param context Context containing the URL with the service name
	 * @return The service name, which is also code of the integration object presented by the endpoint.
	 */
	String extract(ODataContext context);
	
	/**
	 * Extracts the service name from the pathInfo
	 *
	 * @param httpServletRequest {@link javax.servlet.http.HttpServletRequest} with the information needed.
	 * @return a {@link String} The service name, which is also code of the integration object presented by the endpoint.
	 */
	String extract(final HttpServletRequest httpServletRequest);

	/**
	 * Extracts the service name from the context and supplies the integration key value, if it's known
	 *
	 * @param context Context containing the URL with the service name
	 * @param integrationKey The integration key to be used as context for logging, exception throwing, etc.
	 * @return The service name, which is also code of the integration object presented by the endpoint.
	 *
	 * @deprecated Since 1905. Use {@link ServiceNameExtractor#extract(ODataContext) }
	 */
	@Deprecated(since = "1905", forRemoval= true )
	String extract(ODataContext context, String integrationKey);
}
