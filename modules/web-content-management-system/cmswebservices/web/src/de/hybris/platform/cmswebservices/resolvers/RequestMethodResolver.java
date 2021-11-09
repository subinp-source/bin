/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.resolvers;

import javax.servlet.http.HttpServletRequest;


/**
 * Service to resolve a method name for a request. Useful to identify the intention of the request.
 * For example, some POST methods actually do only GET operations.
 */
public interface RequestMethodResolver
{
	/**
	 * Method should return a "GET" for a particular "POST" request otherwise it returns the method name from the request.
	 *
	 * @param request the request
	 * @return the method name
	 */
	String resolvePostToGet(final HttpServletRequest request);
}
