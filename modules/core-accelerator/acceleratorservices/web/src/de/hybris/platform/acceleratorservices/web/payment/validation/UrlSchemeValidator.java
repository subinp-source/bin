/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.web.payment.validation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;


public class UrlSchemeValidator
{
	private static final Logger LOG = Logger.getLogger(UrlSchemeValidator.class);

	private UrlSchemeValidator()
	{
	}

	public static boolean validate(final String url)
	{
		if (url == null)
		{
			return false;
		}
		try
		{
			final URI uri = new URI(url);
			if (!uri.isAbsolute())
			{
				return true;
			}
			final String scheme = uri.getScheme();
			return "http".equals(scheme) || "https".equals(scheme);
		}
		catch (final URISyntaxException e)
		{
			LOG.error("UrlSchebmeValidator error", e);
			return false;
		}
	}
}
