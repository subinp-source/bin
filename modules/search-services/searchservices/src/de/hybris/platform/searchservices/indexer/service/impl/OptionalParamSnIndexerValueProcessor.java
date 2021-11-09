/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProcessor;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.Collection;
import java.util.Map;


/**
 * Implementation of {@link SnIndexerValueProvider} that throws and exception for non optional fields without values.
 */
public class OptionalParamSnIndexerValueProcessor implements SnIndexerValueProcessor
{
	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	@Override
	public Object process(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper, final Object source)
			throws SnIndexerException
	{
		final SnField field = fieldWrapper.getField();

		if (!isOptional(fieldWrapper))
		{
			if (source == null)
			{
				throw new SnIndexerException("No value for field " + field.getId());
			}
			else if (fieldWrapper.isMultiValued() && source instanceof Collection && ((Collection) source).isEmpty())
			{
				throw new SnIndexerException("Empty collection value for field " + field.getId());
			}
			else if ((fieldWrapper.isLocalized() || fieldWrapper.isQualified()) && source instanceof Map && ((Map) source).isEmpty())
			{
				throw new SnIndexerException("Empty map value for field " + field.getId());
			}
		}

		return source;
	}

	protected boolean isOptional(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getBoolean(fieldWrapper.getValueProviderParameters(), OPTIONAL_PARAM, OPTIONAL_PARAM_DEFAULT_VALUE);
	}
}
