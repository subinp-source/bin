/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.scripting;

/**
 * This exception is thrown when the {@link LogicLocation} fails to be created
 */
public class CannotCreateLogicLocationException extends Exception
{
	private final String uri;

	/**
	 * Instantiates a {@link CannotCreateLogicLocationException}
	 * @param uri URI that failed to be created
	 */
	public CannotCreateLogicLocationException(final String uri)
	{
		super(String.format("Cannot create a LogicLocation from the URI %s", uri));
		this.uri = uri;
	}

	/**
	 * Get the URI that failed to be created
	 * @return URI as a String
	 */
	public String getUri()
	{
		return uri;
	}
}
