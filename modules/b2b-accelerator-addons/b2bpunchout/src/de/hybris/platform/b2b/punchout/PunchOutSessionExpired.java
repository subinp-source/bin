/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout;

/**
 *
 */
public class PunchOutSessionExpired extends PunchOutException
{

	/**
	 * Constructor.
	 * 
	 * @param message
	 *           the error message
	 */
	public PunchOutSessionExpired(final String message)
	{
		super(PunchOutResponseCode.CONFLICT, message);
	}

}
