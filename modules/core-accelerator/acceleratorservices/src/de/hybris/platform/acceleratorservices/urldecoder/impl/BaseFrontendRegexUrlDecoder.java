/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.urldecoder.impl;

import de.hybris.platform.acceleratorservices.urldecoder.FrontendUrlDecoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * Match url pattern using a regex.
 * 
 */
public abstract class BaseFrontendRegexUrlDecoder<T> implements FrontendUrlDecoder<T>, InitializingBean
{
	private String regex;
	private Pattern pattern;


	@Override
	public T decode(final String url)
	{
		final Matcher matcher = pattern.matcher(url);
		if (matcher.find())
		{
			return translateId(matcher.group(0));
		}
		return null;
	}


	protected abstract T translateId(String id);


	/**
	 * @param regex
	 *           the regex to set
	 */
	public void setRegex(final String regex)
	{
		Assert.hasLength(regex);
		this.regex = regex;
	}

	@Override
	public void afterPropertiesSet()
	{
		Assert.hasLength(regex);
		this.pattern = Pattern.compile(regex);
	}


}
