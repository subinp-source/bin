/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.exception;

/**
 * alipay exceptions
 */
public class AlipayException extends Exception
{
	private static final long serialVersionUID = 1L;

	public AlipayException()
	{
		super();
	}

	public AlipayException(final String s)
	{
		super(s);
	}

	public AlipayException(final Throwable throwable)
	{
		super(throwable);
	}

	public AlipayException(final String s, final Throwable throwable)
	{
		super(s, throwable);
	}

}
