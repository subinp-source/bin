/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.util;

import de.hybris.platform.searchservices.core.SnRuntimeException;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * JSON utilities.
 */
public class JsonUtils
{

	private static final char VAR_ESCAPE = '\\';

	private static ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils()
	{
		// utility class
	}

	/**
	 * Converts an object to a JSON string.
	 *
	 * @param source
	 *           - the source object
	 *
	 * @return a JSON string
	 */
	public static final String toJson(final Object source)
	{
		if (source == null)
		{
			return null;
		}

		try
		{
			return objectMapper.writeValueAsString(source);
		}
		catch (final IOException e)
		{
			throw new SnRuntimeException(e);
		}
	}

	/**
	 * Converts a JSON string to an object.
	 *
	 * @param source
	 *           - the source JSON string
	 * @param valueType
	 *           - the value type of the target object
	 *
	 * @return an object
	 */
	public static final <T> T fromJson(final String source, final Class<T> valueType)
	{
		return fromJson(source, valueType, null);
	}

	/**
	 * Converts a JSON string to an object, using variable substitution.
	 *
	 * @param source
	 *           - the source JSON string
	 * @param valueType
	 *           - the value type of the target object
	 * @param parameters
	 *           - the parameters used for variable substitution
	 *
	 * @return an object
	 */
	public static final <T> T fromJson(final String source, final Class<T> valueType, final Map<String, String> parameters)
	{
		Objects.requireNonNull(valueType, "valueType cannot be null");

		if (source == null)
		{
			return null;
		}

		final String input = MapUtils.isEmpty(parameters) ? source
				: (new StringSubstitutor(parameters, StringSubstitutor.DEFAULT_VAR_START, StringSubstitutor.DEFAULT_VAR_END,
						VAR_ESCAPE)).replace(source);

		try
		{
			return objectMapper.readValue(input, valueType);
		}
		catch (final IOException e)
		{
			throw new SnRuntimeException(e);
		}
	}

	/**
	 * Loads a JSON string from a file and converts it to an object.
	 *
	 * @param resource
	 *           - the resource that contains the JSON string
	 * @param valueType
	 *           - the value type of the target object
	 *
	 * @return an object
	 */
	public static final <T> T loadJson(final String resource, final Class<T> valueType)
	{
		return loadJson(resource, valueType, null);
	}

	/**
	 * Loads a JSON string from a file and converts it to an object, using variable substitution.
	 *
	 * @param resource
	 *           - the resource that contains the JSON string
	 * @param valueType
	 *           - the value type of the target object
	 * @param parameters
	 *           - the parameters used for variable substitution
	 *
	 * @return an object
	 */
	public static final <T> T loadJson(final String resource, final Class<T> valueType, final Map<String, String> parameters)
	{
		Objects.requireNonNull(resource, "resource cannot be null");
		Objects.requireNonNull(valueType, "valueType cannot be null");

		final URL url = JsonUtils.class.getResource(resource);
		final String source = IOUtils.toString(url);
		return fromJson(source, valueType, parameters);
	}
}
