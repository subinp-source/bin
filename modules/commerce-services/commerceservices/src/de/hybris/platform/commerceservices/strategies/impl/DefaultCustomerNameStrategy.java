/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;

import org.apache.commons.lang.StringUtils;


public class DefaultCustomerNameStrategy implements CustomerNameStrategy
{
	private static final String SEPARATOR_SPACE = " ";

	@Override
	public String[] splitName(final String name)
	{
		final String trimmedName = StringUtils.trimToNull(name);
		return new String[] { StringUtils.substringBeforeLast(trimmedName, SEPARATOR_SPACE),
				StringUtils.substringAfterLast(trimmedName, SEPARATOR_SPACE) };
	}

	@Override
	public String getName(final String firstName, final String lastName)
	{
		final String result = StringUtils.trimToEmpty(firstName) + SEPARATOR_SPACE + StringUtils.trimToEmpty(lastName);
		return StringUtils.trimToNull(result);
	}
}
