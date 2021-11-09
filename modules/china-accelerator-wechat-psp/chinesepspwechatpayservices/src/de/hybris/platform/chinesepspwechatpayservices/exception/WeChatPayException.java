/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.exception;

/**
 * Custom exception class to handle exceptions while executing HTTP request
 */
public class WeChatPayException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public WeChatPayException()
	{
		super();
	}

	public WeChatPayException(String s)
	{
		super(s);
	}

	public WeChatPayException(Throwable throwable)
	{
		super(throwable);
	}

	public WeChatPayException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

}
