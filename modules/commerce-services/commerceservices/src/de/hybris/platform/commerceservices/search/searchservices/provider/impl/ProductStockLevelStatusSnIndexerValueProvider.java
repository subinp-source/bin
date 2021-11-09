/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product stock level status.
 */
public class ProductStockLevelStatusSnIndexerValueProvider extends
		AbstractSnIndexerValueProvider<ProductModel, ProductStockLevelStatusSnIndexerValueProvider.ProductStockLevelStatusData>
{
	public static final String ID = "productStockLevelStatusSnIndexerValueProvider";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(BaseStoreModel.class);

	public static final String MODE_VALUE_STATUS = "status";
	public static final String MODE_VALUE_AVAILABILITY = "availability";

	public static final String MODE_PARAM = "mode";
	public static final String MODE_PARAM_DEFAULT_VALUE = MODE_VALUE_STATUS;

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
			final ProductModel source, final ProductStockLevelStatusData data) throws SnIndexerException
	{
		if (MapUtils.isEmpty(data.getStockLevelStatus()))
		{
			return null;
		}

		final String mode = resolveMode(fieldWrapper);
		final Map<String, StockLevelStatus> stockLevelStatus = data.getStockLevelStatus();

		if (fieldWrapper.isQualified())
		{
			final Map<String, Object> value = new HashMap<>();

			final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
			for (final SnQualifier qualifier : qualifiers)
			{
				value.put(qualifier.getId(), extractStockLevelStatusValue(stockLevelStatus.get(qualifier.getId()), mode));
			}

			return value;
		}
		else
		{
			return extractStockLevelStatusValue(stockLevelStatus.get(DEFAULT_STORE_KEY), mode);
		}
	}

	protected Object extractStockLevelStatusValue(final StockLevelStatus stockLevelStatus, final String mode)
			throws SnIndexerException
	{
		if (stockLevelStatus == null)
		{
			return null;
		}

		if (StringUtils.equals(MODE_VALUE_STATUS, mode))
		{
			return stockLevelStatus.getCode();
		}
		else if (StringUtils.equals(MODE_VALUE_AVAILABILITY, mode))
		{
			return isStockAvailable(stockLevelStatus);
		}
		else
		{
			throw new SnIndexerException("Invalid mode: " + mode);
		}
	}

	protected boolean isStockAvailable(final StockLevelStatus stockLevelStatus)
	{
		return !StockLevelStatus.OUTOFSTOCK.equals(stockLevelStatus);
	}

	@Override
	protected ProductStockLevelStatusData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, StockLevelStatus> stockLevelStatus = loadStockLevelStatus(indexerContext, fieldWrappers, source);

		final ProductStockLevelStatusData data = new ProductStockLevelStatusData();
		data.setStockLevelStatus(stockLevelStatus);

		return data;
	}

	protected Map<String, StockLevelStatus> loadStockLevelStatus(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source)
	{
		final Map<String, StockLevelStatus> stockLevelStatus = new HashMap<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			if (fieldWrapper.isQualified())
			{
				loadQualifiedStockLevelStatus(fieldWrapper, source, stockLevelStatus);
			}
			else
			{
				loadDefaultStockLevelStatus(indexerContext, source, stockLevelStatus);
			}
		}

		return stockLevelStatus;
	}

	protected void loadQualifiedStockLevelStatus(final SnIndexerFieldWrapper fieldWrapper, final ProductModel source,
			final Map<String, StockLevelStatus> data)
	{
		final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
		for (final SnQualifier qualifier : qualifiers)
		{
			data.computeIfAbsent(qualifier.getId(), key -> {
				final BaseStoreModel store = qualifier.getAs(BaseStoreModel.class);
				return commerceStockService.getStockLevelStatusForProductAndBaseStore(source, store);
			});
		}
	}

	protected void loadDefaultStockLevelStatus(final SnIndexerContext indexerContext, final ProductModel source,
			final Map<String, StockLevelStatus> data)
	{
		data.computeIfAbsent(DEFAULT_STORE_KEY, key -> {
			final BaseStoreModel store = snStoreSelectionStrategy.getDefaultStore(indexerContext.getIndexType().getId())
					.orElseThrow();
			return commerceStockService.getStockLevelStatusForProductAndBaseStore(source, store);
		});
	}

	protected String resolveMode(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), MODE_PARAM, MODE_PARAM_DEFAULT_VALUE);
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

	protected static class ProductStockLevelStatusData
	{
		private Map<String, StockLevelStatus> stockLevelStatus;

		public Map<String, StockLevelStatus> getStockLevelStatus()
		{
			return stockLevelStatus;
		}

		public void setStockLevelStatus(final Map<String, StockLevelStatus> stockLevelStatus)
		{
			this.stockLevelStatus = stockLevelStatus;
		}
	}
}
