/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.security;

/**
 * Token data for the SecureToken processor.
 */
public class SecureToken
{
	private final String data;
	private final long timeStamp;

	public SecureToken(final String data, final long timeStamp)
	{
		this.data = data;
		this.timeStamp = timeStamp;
	}

	public String getData()
	{
		return data;
	}

	public long getTimeStamp()
	{
		return timeStamp;
	}
}
