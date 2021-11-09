/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.url.impl;

import org.springframework.beans.factory.annotation.Required;


public class DefaulCustomPageUrlResolver extends AbstractUrlResolver<String>
{
	private final String CACHE_KEY = DefaulCustomPageUrlResolver.class.getName();

	private String pattern;

	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
	}

	@Override
	protected String getKey(final String source)
	{
		return CACHE_KEY + "." + source.hashCode();
	}

	@Override
	protected String resolveInternal(final String source)
	{
		// default url is /
		// Replace pattern values
		String url = getPattern();

		return url + source;
	}
}
