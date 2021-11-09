/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Parameter utilities.
 */
public class ParameterUtils
{
	private ParameterUtils()
	{
		// utility class
	}

	public static final int getInt(final Map<String, String> parameters, final String key, final int defaultValue)
	{
		int value = defaultValue;

		final String stringValue = getStringValue(parameters, key);
		if (stringValue != null)
		{
			value = Integer.parseInt(stringValue);
		}

		return value;
	}

	public static final long getLong(final Map<String, String> parameters, final String key, final long defaultValue)
	{
		long value = defaultValue;

		final String stringValue = getStringValue(parameters, key);
		if (stringValue != null)
		{
			value = Long.parseLong(stringValue);
		}

		return value;
	}

	public static final double getDouble(final Map<String, String> parameters, final String key, final double defaultValue)
	{
		double value = defaultValue;

		final String stringValue = getStringValue(parameters, key);
		if (stringValue != null)
		{
			value = Double.parseDouble(stringValue);
		}

		return value;
	}

	public static final boolean getBoolean(final Map<String, String> parameters, final String key, final boolean defaultValue)
	{
		boolean value = defaultValue;

		final String stringValue = getStringValue(parameters, key);
		if (stringValue != null)
		{
			value = Boolean.parseBoolean(stringValue);
		}

		return value;
	}

	public static final String getString(final Map<String, String> parameters, final String key, final String defaultValue)
	{
		String value = defaultValue;

		final String stringValue = getStringValue(parameters, key);
		if (stringValue != null)
		{
			value = stringValue;
		}

		return value;
	}

	protected static final String getStringValue(final Map<String, String> parameters, final String key)
	{
		String stringValue = null;

		if (parameters != null)
		{
			stringValue = StringUtils.trimToNull(parameters.get(key));
		}

		return stringValue;
	}
}
