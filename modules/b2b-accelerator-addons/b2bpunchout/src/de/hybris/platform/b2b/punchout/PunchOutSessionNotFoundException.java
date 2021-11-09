/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout;

/**
 *
 */
public class PunchOutSessionNotFoundException extends PunchOutException
{

	/**
	 * Constructor.
	 *
	 * @param message
	 *           the error message
	 */
	public PunchOutSessionNotFoundException(final String message)
	{
		super(PunchOutResponseCode.CONFLICT, message);
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *           the error message
	 * @param cause
	 *           the cause
	 */
	public PunchOutSessionNotFoundException(final String message, final Throwable cause)
	{
		super(PunchOutResponseCode.CONFLICT, message, cause);
	}

}
