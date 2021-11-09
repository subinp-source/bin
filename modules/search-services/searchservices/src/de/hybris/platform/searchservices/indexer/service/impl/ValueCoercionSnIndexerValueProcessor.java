/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProcessor;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Implementation of {@link SnIndexerValueProvider} that adjusts the value based on the field configuration.
 */
public class ValueCoercionSnIndexerValueProcessor implements SnIndexerValueProcessor
{
	@Override
	public Object process(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper, final Object source)
			throws SnIndexerException
	{
		if (source == null)
		{
			return null;
		}

		Object target = adjustToMultiValuedConfiguration(fieldWrapper, source);

		// this is a workaround until we fix support for localized/qualified multi-valued fields
		if (fieldWrapper.isLocalized() || fieldWrapper.isQualified())
		{
			final Map<Object, Object> newTarget = new HashMap<>();

			for (final Entry<Object, Object> entry : ((Map<Object, Object>) target).entrySet())
			{
				final Object value = extractValue(entry.getValue());
				if (value != null)
				{
					newTarget.put(entry.getKey(), value);
				}
			}

			target = newTarget;
		}

		return target;
	}

	protected Object extractValue(final Object source)
	{
		if (!(source instanceof Collection))
		{
			return source;
		}
		else if (CollectionUtils.isNotEmpty((Collection) source))
		{
			return ((Collection) source).iterator().next();
		}
		else
		{
			return null;
		}
	}

	protected Object adjustToMultiValuedConfiguration(final SnIndexerFieldWrapper fieldWrapper, final Object source)
	{
		if (fieldWrapper.isMultiValued())
		{
			if (!(source instanceof Collection))
			{
				return List.of(source);
			}
			else if (CollectionUtils.isNotEmpty((Collection) source))
			{
				return source;
			}
			else
			{
				return null;
			}
		}
		else
		{
			if (!(source instanceof Collection))
			{
				return source;
			}
			else if (CollectionUtils.isNotEmpty((Collection) source))
			{
				return ((Collection) source).iterator().next();
			}
			else
			{
				return null;
			}
		}
	}
}
