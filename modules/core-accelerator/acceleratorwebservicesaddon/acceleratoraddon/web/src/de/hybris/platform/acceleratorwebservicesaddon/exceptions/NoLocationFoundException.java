/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorwebservicesaddon.exceptions;

/**
 * Thrown when location was not found while setting user location
 */

public class NoLocationFoundException extends Exception
{
	/**
	 * @param location
	 */
	public NoLocationFoundException(final String location)
	{
		super("Location: " + location + " could not be found");
	}

	public NoLocationFoundException(final String location, final Throwable rootCause)
	{
		super("Location: " + location + " could not be found", rootCause);
	}
}
