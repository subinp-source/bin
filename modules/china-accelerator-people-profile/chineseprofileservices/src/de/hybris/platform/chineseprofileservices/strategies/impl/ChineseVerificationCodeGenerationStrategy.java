/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeGenerationStrategy;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Required;


/**
 * A simple generation strategy by using Random.
 * 
 * @deprecated Since 1905. Use {@link ChineseVerificationCodeStrategy} instead.
 */
@Deprecated(since = "1905", forRemoval= true )
public class ChineseVerificationCodeGenerationStrategy implements VerificationCodeGenerationStrategy
{

	private int length;
	private static final int CODE_LENTH = 4;
	private static final int BOUND = 10;

	@Override
	public String generate()
	{
		final SecureRandom random = new SecureRandom();
		final StringBuilder builder = new StringBuilder();
		if (length <= 0)
		{
			length = CODE_LENTH;
		}
		for (int i = 0; i < length; i++)
		{
			builder.append(random.nextInt(BOUND));
		}
		return builder.toString();
	}

	protected int getLength()
	{
		return length;
	}

	@Required
	public void setLength(final int length)
	{
		this.length = length;
	}


}
