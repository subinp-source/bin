/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.scripting;

/**
 * Throw this exception when the scheme is not supported by the {@link LogicLocation}
 */
public class UnsupportedLogicLocationSchemeException extends Exception
{
	private final String scheme;

	/**
	 * Instantiates the exception with the scheme that is unsupported
	 * @param scheme Unsupported scheme
	 */
	public UnsupportedLogicLocationSchemeException(final String scheme)
	{
		super(String.format("%s is unsupported", scheme));
		this.scheme = scheme;
	}

	/**
	 * Gets the scheme that caused the exception
	 * @return scheme
	 */
	public String getScheme()
	{
		return scheme;
	}
}
