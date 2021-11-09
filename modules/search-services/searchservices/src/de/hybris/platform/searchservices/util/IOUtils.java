/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.util;

import de.hybris.platform.searchservices.core.SnRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Input/Output utilities.
 */
public class IOUtils
{

	private IOUtils()
	{
		// utility class
	}

	/**
	 * Loads the contents at the given url as a string.
	 *
	 * @param url
	 *           - the resource that contains the JSON string
	 *
	 * @return the contents at the given url as a string
	 */
	public static final String toString(final URL url)
	{
		return toString(url, StandardCharsets.UTF_8.name());
	}

	/**
	 * Loads the contents at the given url as a string.
	 *
	 * @param url
	 *           - the resource that contains the JSON string
	 * @param encoding
	 *           - the encoding
	 *
	 * @return the contents at the given url as a string
	 */
	public static final String toString(final URL url, final String encoding)
	{
		try (InputStream inputStream = url.openStream())
		{
			final byte[] bytes = inputStream.readAllBytes();
			return new String(bytes, encoding);
		}
		catch (final IOException e)
		{
			throw new SnRuntimeException(e);
		}
	}
}
