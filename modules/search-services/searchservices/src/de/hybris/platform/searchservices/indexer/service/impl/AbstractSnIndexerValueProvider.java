/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.document.data.SnDocument;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProcessor;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Base class for value providers.
 *
 * @param <T>
 *           the type of the model
 * @param <D>
 *           the type of the data
 */
public abstract class AbstractSnIndexerValueProvider<T extends ItemModel, D> implements SnIndexerValueProvider<T>
{
	private List<SnIndexerValueProcessor> valueProcessors;

	@Override
	public void provide(final SnIndexerContext indexerContext, final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final T source, final SnDocument target) throws SnIndexerException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("indexerContext", indexerContext);
		ServicesUtil.validateParameterNotNullStandardMessage("fieldWrappers", fieldWrappers);
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		final D data = loadData(indexerContext, fieldWrappers, source);

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final Object value = getFieldValue(indexerContext, fieldWrapper, source, data);
			final Object processedValue = processValue(indexerContext, fieldWrapper, value);

			target.setFieldValue(fieldWrapper.getField(), processedValue);
		}
	}

	/**
	 * Loads data for the given indexer context.
	 *
	 * @param indexerContext
	 *           - the current indexer context
	 * @param fieldWrappers
	 *           - the field wrappers
	 * @param source
	 *           - the source model
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	protected D loadData(final SnIndexerContext indexerContext, final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final T source) throws SnIndexerException
	{
		return null;
	}

	/**
	 * Returns the value for a given field and source model.
	 *
	 * @param indexerContext
	 *           - the current indexer context
	 * @param fieldWrapper
	 *           - the field wrapper
	 * @param source
	 *           - the source model
	 * @param data
	 *           - the data
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	protected abstract Object getFieldValue(final SnIndexerContext indexerContext,
			final SnIndexerFieldWrapper fieldWrapper, final T source, final D data) throws SnIndexerException;

	protected Object processValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final Object source) throws SnIndexerException
	{
		Object target = source;

		if (CollectionUtils.isNotEmpty(valueProcessors))
		{
			for (final SnIndexerValueProcessor valueProcessor : valueProcessors)
			{
				target = valueProcessor.process(indexerContext, fieldWrapper, target);
			}
		}

		return target;
	}

	public List<SnIndexerValueProcessor> getValueProcessors()
	{
		return valueProcessors;
	}

	public void setValueProcessors(final List<SnIndexerValueProcessor> valueProcessors)
	{
		this.valueProcessors = valueProcessors;
	}
}
