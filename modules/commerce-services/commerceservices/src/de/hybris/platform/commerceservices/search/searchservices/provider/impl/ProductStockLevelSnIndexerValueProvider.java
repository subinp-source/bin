/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product stock level status.
 */
public class ProductStockLevelSnIndexerValueProvider
		extends AbstractSnIndexerValueProvider<ProductModel, ProductStockLevelSnIndexerValueProvider.ProductStockLevelData>
{
	public static final String ID = "productStockLevelSnIndexerValueProvider";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(BaseStoreModel.class);

	protected static final String DEFAULT_STORE_KEY = null;

	private CommerceStockService commerceStockService;
	private SnStoreSelectionStrategy snStoreSelectionStrategy;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ProductModel source, final ProductStockLevelData data) throws SnIndexerException
	{
		if (MapUtils.isEmpty(data.getStockLevel()))
		{
			return null;
		}

		final Map<String, Long> stockLevel = data.getStockLevel();

		if (fieldWrapper.isQualified())
		{
			final Map<String, Long> value = new HashMap<>();

			final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
			for (final SnQualifier qualifier : qualifiers)
			{
				value.put(qualifier.getId(), stockLevel.get(qualifier.getId()));
			}

			return value;
		}
		else
		{
			return stockLevel.get(DEFAULT_STORE_KEY);
		}
	}

	@Override
	protected ProductStockLevelData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, Long> stockLevel = loadStockLevelStatus(indexerContext, fieldWrappers, source);

		final ProductStockLevelData data = new ProductStockLevelData();
		data.setStockLevel(stockLevel);

		return data;
	}

	protected Map<String, Long> loadStockLevelStatus(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source)
	{
		final Map<String, Long> stockLevel = new HashMap<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			if (fieldWrapper.isQualified())
			{
				loadQualifiedStockLevel(fieldWrapper, source, stockLevel);
			}
			else
			{
				loadDefaultStockLevel(indexerContext, source, stockLevel);
			}
		}

		return stockLevel;
	}

	protected void loadQualifiedStockLevel(final SnIndexerFieldWrapper fieldWrapper, final ProductModel source,
			final Map<String, Long> data)
	{
		final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
		for (final SnQualifier qualifier : qualifiers)
		{
			data.computeIfAbsent(qualifier.getId(), key -> {
				final BaseStoreModel store = qualifier.getAs(BaseStoreModel.class);
				return commerceStockService.getStockLevelForProductAndBaseStore(source, store);
			});
		}
	}

	protected void loadDefaultStockLevel(final SnIndexerContext indexerContext, final ProductModel source,
			final Map<String, Long> data)
	{
		data.computeIfAbsent(DEFAULT_STORE_KEY, key -> {
			final BaseStoreModel store = snStoreSelectionStrategy.getDefaultStore(indexerContext.getIndexType().getId())
					.orElseThrow(() -> new ProductSearchStrategyRuntimeException("Default store for index type has not been found!"));
			return commerceStockService.getStockLevelForProductAndBaseStore(source, store);
		});
	}

	public CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	public SnStoreSelectionStrategy getSnStoreSelectionStrategy()
	{
		return snStoreSelectionStrategy;
	}

	@Required
	public void setSnStoreSelectionStrategy(final SnStoreSelectionStrategy snStoreSelectionStrategy)
	{
		this.snStoreSelectionStrategy = snStoreSelectionStrategy;
	}

	protected static class ProductStockLevelData
	{
		private Map<String, Long> stockLevelStatus;

		public Map<String, Long> getStockLevel()
		{
			return stockLevelStatus;
		}

		public void setStockLevel(final Map<String, Long> stockLevelStatus)
		{
			this.stockLevelStatus = stockLevelStatus;
		}
	}
}
