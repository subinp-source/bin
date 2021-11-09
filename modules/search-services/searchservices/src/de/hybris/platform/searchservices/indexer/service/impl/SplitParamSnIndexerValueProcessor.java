/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProcessor;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Implementation of {@link SnIndexerValueProvider} that allows to split string values.
 */
public class SplitParamSnIndexerValueProcessor implements SnIndexerValueProcessor
{
	public static final String SPLIT_PARAM = "split";
	public static final boolean SPLIT_PARAM_DEFAULT_VALUE = false;

	public static final String SPLIT_REGEX_PARAM = "splitRegex";
	public static final String SPLIT_REGEX_PARAM_DEFAULT_VALUE = "\\s+";

	@Override
	public Object process(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper, final Object source)
			throws SnIndexerException
	{
		final boolean split = resolveSplit(fieldWrapper);
		if (!split)
		{
			return source;
		}

		final SnField field = fieldWrapper.getField();

		if (field.getFieldType() != SnFieldType.STRING && field.getFieldType() != SnFieldType.TEXT)
		{
			throw new SnIndexerException("Split can only be applied on fields of type STRING or TEXT");
		}

		final String splitRegex = resolveSplitRegex(fieldWrapper);

		if (fieldWrapper.isMultiValued() && source instanceof Collection)
		{
			return ((Collection) source).stream().flatMap(value -> splitToStream(value, splitRegex)).collect(Collectors.toList());
		}
		else if ((fieldWrapper.isLocalized() || fieldWrapper.isQualified()) && source instanceof Map)
		{
			return ((Map<?, ?>) source).entrySet().stream()
					.collect(Collectors.toMap(Entry::getKey, entry -> split(entry.getValue(), splitRegex)));
		}
		else
		{
			return split(source, splitRegex);
		}
	}

	protected Stream<Object> splitToStream(final Object source, final String regex)
	{
		if (source instanceof String)
		{
			return Arrays.stream(((String) source).split(regex));
		}

		return Stream.of(source);
	}

	protected Object split(final Object source, final String regex)
	{
		if (source instanceof String)
		{
			return Arrays.asList(((String) source).split(regex));
		}

		return source;
	}

	protected boolean resolveSplit(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getBoolean(fieldWrapper.getValueProviderParameters(), SPLIT_PARAM, SPLIT_PARAM_DEFAULT_VALUE);
	}

	protected String resolveSplitRegex(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), SPLIT_REGEX_PARAM,
				SPLIT_REGEX_PARAM_DEFAULT_VALUE);
	}
}
