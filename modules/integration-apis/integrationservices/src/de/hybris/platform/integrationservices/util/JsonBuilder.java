/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper to build a string in JSON format.
 */
public class JsonBuilder
{
	private static final int START_OFFSET = 2;

	private final Map<String, Object> fields;

	private JsonBuilder()
	{
		this.fields = new HashMap<>();
	}

	/**
	 * Creates new instance of this builder.
	 * @return new builder instance, which has no fields specified and results in {@code "{}"} JSON when built.
	 */
	public static JsonBuilder json()
	{
		return new JsonBuilder();
	}

	/**
	 * Specifies field named "code" for the JSON output. This is the same as calling {@code withField("code", code)}
	 * @param code value for the "code" field
	 * @return a builder with the field specified
	 */
	public JsonBuilder withCode(final String code)
	{
		return withField("code", code);
	}

	/**
	 * Specifies field named "id" for the JSON output. This is the same as calling {@code withField("id", code)}
	 * @param id value for the "id" field
	 * @return a builder with the field specified
	 */
	public JsonBuilder withId(final String id)
	{
		return withField("id", id);
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code address} in {@code {"address": {...}}}
	 * @param builder nested JSON object to be used as value for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final JsonBuilder builder)
	{
		fields.put(field, builder);
		return this;
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code address} in {@code {"address": {...}}}
	 * @param number numeric value for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final Number number)
	{
		fields.put(field, number);
		return this;
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code address} in {@code {"address": {...}}}
	 * @param value boolean value for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final boolean value)
	{
		fields.put(field, value);
		return this;
	}

	/**
	 * Specifies date field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code address} in {@code {"address": {...}}}
	 * @param date date value for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final Date date)
	{
		final String value = date != null
				? ("/Date(" + date.getTime() +")/")
				: null;
		return withField(field, value);
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code reviews} in {@code {"reviews": [...]}}
	 * @param values a collection of values for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withFieldValues(final String field, final Object... values)
	{
		return withField(field, Arrays.asList(values));
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code reviews} in {@code {"reviews": [...]}}
	 * @param values nested JSON objects to be used as a collection value for the field
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final Collection<?> values)
	{
		fields.put(field, values);
		return this;
	}

	/**
	 * Specifies a character field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code country} in {@code {"country": "USA"}}
	 * @param value value of the JSON field, which prints after the ":" separator. For example, {@code 'a'} in {@code {"char": "a"}}
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final Character value)
	{
		final String str = value != null
				? String.valueOf(value)
				: null;
		return withField(field, str);
	}

	/**
	 * Specifies field value for the JSON output.
	 * @param field name of the JSON field, which prints before the ":" separator. For example, {@code country} in {@code {"country": "USA"}}
	 * @param value value of the JSON field, which prints after the ":" separator. For example, {@code USA} in {@code {"country": "USA"}}
	 * @return a builder with the field specified.
	 */
	public JsonBuilder withField(final String field, final String value)
	{
		fields.put(field, value);
		return this;
	}

	/**
	 * Specifies the localized attributes for the JSON output.
	 * @param attributes An array of {@link Map}s with the name of the localized attribute as the key, and the localized value as the value
	 * @return a builder with the specified localized attributes.
	 */
	@SafeVarargs
	public final JsonBuilder withLocalizedAttributes(final Map<String, String>... attributes)
	{
		fields.put("localizedAttributes", Arrays.asList(attributes));
		return this;
	}

	public String build()
	{
		final String concatenated = fields.entrySet().stream()
				.map(e -> '"' + e.getKey() + "\": " + valueOf(e.getValue()))
				.reduce("", (p, n) -> p + ", " + n);
		final String jsonFields = concatenated.length() > 0
				? concatenated.substring(START_OFFSET) // skip initial ", "
				: concatenated;
		return "{" + jsonFields + "}";
	}

	private String valueOf(final Object v)
	{
		final String strValue = String.valueOf(v);
		if (v instanceof String || v instanceof StringBuilder || v instanceof StringBuffer)
		{
			return isJson(strValue) ? strValue : ('"' + strValue + '"');
		}
		if (v instanceof JsonBuilder)
		{
			return ((JsonBuilder)v).build();
		}
		if (v instanceof Map)
		{
			return mapValue((Map) v);
		}
		if (v instanceof Collection)
		{
			return "[" + collectionValue((Collection) v) + "]";
		}
		return strValue;
	}

	private String mapValue(final Map<?, ?> m)
	{
		final JsonBuilder jsonBuilder = json();
		m.forEach((k, v) -> jsonBuilder.withField(k.toString(), v.toString()));
		return jsonBuilder.build();
	}

	private String collectionValue(final Collection<?> c)
	{
		final String elements = c.stream()
				.map(this::valueOf)
				.reduce("", (p, n) -> p + ", " + n);
		return elements.length() > 0
				? elements.substring(START_OFFSET) // skip initial ", "
				: elements;
	}

	private static boolean isJson(final String str)
	{
		return (str.startsWith("{") && str.endsWith("}"))
				|| (str.startsWith("[") && str.endsWith("]"));
	}
}
