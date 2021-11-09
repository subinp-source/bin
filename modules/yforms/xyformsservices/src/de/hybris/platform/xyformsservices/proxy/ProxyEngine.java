/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.proxy;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Proxies HTTP content.
 */
public interface ProxyEngine
{
	/**
	 * Proxies content
	 *
	 * @param request
	 *           The {@link HttpServletRequest} associated to the call
	 * @param response
	 *           The {@link HttpServletResponse} associated to the call
	 * @param namespace
	 *           Used for HTML element's id generation
	 * @param url
	 *           The url to be called
	 * @param forceGetMethod
	 *           Useful when proxying content, when false it uses the same method used in the original request.
	 * @param headers
	 *           Extra headers to be passed to the connection, useful for authentication headers.
	 * @throws ProxyException
	 */
	void proxy(HttpServletRequest request, HttpServletResponse response, String namespace, String url, boolean forceGetMethod,
			final Map<String, String> headers) throws ProxyException;

	/**
	 * Extracts the namespace coming from client.
	 *
	 * @param request
	 */
	public String extractNamespace(HttpServletRequest request);
}
