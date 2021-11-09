/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.header;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriTemplate;


/**
 * Utility class to create the location URI which is added in the header when creating a resource using POST.
 */
public class LocationHeaderResource
{
	/**
	 * Create the location URI to be added to the header when creating a resource using POST
	 *
	 * @param request
	 * @param childIdentifier
	 * @return the location header URI
	 */
	public String createLocationForChildResource(final HttpServletRequest request, final Object childIdentifier)
	{
		final StringBuffer url = request.getRequestURL();
		final UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		return template.expand(childIdentifier).toASCIIString();
	}
}
