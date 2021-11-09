/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout;

/**
 * Business exception representing PunchOut issues
 */
public class PunchOutException extends RuntimeException
{
	public static final String PUNCHOUT_EXCEPTION_MESSAGE = "PunchOut Exception";

	private final String errorCode;

	public PunchOutException(final String errorCode, final String message)
	{
		this(errorCode, message, null);
	}

	public PunchOutException(final String errorCode, final String message, final Throwable cause)
	{
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode()
	{
		return errorCode;
	}
}
