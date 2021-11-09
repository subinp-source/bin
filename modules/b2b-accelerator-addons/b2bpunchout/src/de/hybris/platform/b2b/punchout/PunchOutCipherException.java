/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout;

/**
 * Exception throw when Cipher has issues to encrypt or decrypt some text. Usually happens because of a bad key (e.g.:
 * wrong size, wrong data, does not apply to desired algorithm).
 */
public class PunchOutCipherException extends PunchOutException
{

	public PunchOutCipherException(final String message)
	{
		super(PunchOutResponseCode.FORBIDDEN, message);
	}

	public PunchOutCipherException(final String message, final Throwable cause)
	{
		super(PunchOutResponseCode.FORBIDDEN, message, cause);
	}

}
