/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.item;

import java.util.Locale;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts {@link String} presentation of a locale to a {@link Locale}. This implementation ignores any parts of the {@code Locale}
 * besides language and country because that is the level of locale support inside the ECP. Variant and extension parts won't make
 * any difference in the application.
 */
public class StringToLocaleConverter implements Converter<String, Locale>
{
	private static final String SEPARATOR = "-";

	@Override
	public Locale convert(final String tag)
	{
		return tag != null
				? Locale.forLanguageTag(standardized(tag))
				: null;
	}

	private String standardized(final String tag)
	{
		assert tag != null : "enters this method only for non-null condition";
		final String[] elements = tag.split("[_-]");
		switch (elements.length)
		{
			case 0:
				return "";
			case 1:
				return elements[0];
			default:
				return elements[0] + SEPARATOR + elements[1];
		}
	}
}
