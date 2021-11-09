/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

public class CSRFTokenFetchingException extends RuntimeException
{
	public CSRFTokenFetchingException(final String msg)
	{
		super(msg);
	}

	public CSRFTokenFetchingException(final String msg, final Throwable t)
	{
		super(msg, t);
	}
}
