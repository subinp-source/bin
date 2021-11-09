/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product attributes.
 */
public class ProductAttributeSnIndexerValueProvider
		extends AbstractProductSnIndexerValueProvider<ProductModel, ProductAttributeSnIndexerValueProvider.ProductData>
{
	public static final String ID = "productAttributeSnIndexerValueProvider";

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
			final ProductModel source, final ProductData data) throws SnIndexerException
	{
		try
		{
			final String expression = resolveExpression(fieldWrapper);
			final String productSelector = resolveProductSelector(fieldWrapper);
			final Set<ProductModel> products = data.getProducts().get(productSelector);

			if (CollectionUtils.isEmpty(products))
			{
				return null;
			}

			if (fieldWrapper.isLocalized())
			{
				final List<Locale> locales = fieldWrapper.getQualifiers().stream().map(qualifier -> qualifier.getAs(Locale.class))
						.collect(Collectors.toList());
				return snExpressionEvaluator.evaluate(products, expression, locales);
			}
			else
			{
				return snExpressionEvaluator.evaluate(products, expression);
			}
		}
		catch (final SnException e)
		{
			throw new SnIndexerException(e);
		}
	}

	@Override
	protected ProductData loadData(final SnIndexerContext indexerContext, final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final ProductModel source) throws SnIndexerException
	{
		final Map<String, Set<ProductModel>> products = collectProducts(fieldWrappers, source);

		final ProductData data = new ProductData();
		data.setProducts(products);

		return data;
	}

	protected String resolveExpression(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils
				.getString(fieldWrapper.getValueProviderParameters(), EXPRESSION_PARAM, fieldWrapper.getField().getId());
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

	protected static class ProductData
	{
		private Map<String, Set<ProductModel>> products;

		public Map<String, Set<ProductModel>> getProducts()
		{
			return products;
		}

		public void setProducts(final Map<String, Set<ProductModel>> products)
		{
			this.products = products;
		}
	}
}
