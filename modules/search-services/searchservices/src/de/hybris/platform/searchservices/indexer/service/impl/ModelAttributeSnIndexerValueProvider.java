/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnIndexerValueProvider} for model attributes.
 */
public class ModelAttributeSnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void>
{
	public static final String ID = "modelAttributeSnIndexerValueProvider";

	public static final String EXPRESSION_PARAM = "expression";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);

	private SnExpressionEvaluator snExpressionEvaluator;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ItemModel source, final Void data) throws SnIndexerException
	{
		try
		{
			final String expression = resolveExpression(fieldWrapper);

			if (fieldWrapper.isLocalized())
			{
				final List<Locale> locales = fieldWrapper.getQualifiers().stream().map(qualifier -> qualifier.getAs(Locale.class))
						.collect(Collectors.toList());
				return snExpressionEvaluator.evaluate(source, expression, locales);
			}
			else
			{
				return snExpressionEvaluator.evaluate(source, expression);
			}
		}
		catch (final SnException e)
		{
			throw new SnIndexerException(e);
		}
	}

	protected String resolveExpression(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), EXPRESSION_PARAM,
				fieldWrapper.getField().getId());
	}

	public SnExpressionEvaluator getSnExpressionEvaluator()
	{
		return snExpressionEvaluator;
	}

	@Required
	public void setSnExpressionEvaluator(final SnExpressionEvaluator snExpressionEvaluator)
	{
		this.snExpressionEvaluator = snExpressionEvaluator;
	}
}
