/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy;
import de.hybris.platform.commerceservices.strategies.PickupAvailabilityStrategy;
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
 * Implementation of {@link SnIndexerValueProvider} for product pickup availability.
 */
public class ProductPickupAvailabilitySnIndexerValueProvider extends
		AbstractSnIndexerValueProvider<ProductModel, ProductPickupAvailabilitySnIndexerValueProvider.ProductPickupAvailabilityData>
{
	public static final String ID = "productPickupAvailabilitySnIndexerValueProvider";

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(BaseStoreModel.class);

	protected static final String DEFAULT_STORE_KEY = null;

	private PickupAvailabilityStrategy pickupAvailabilityStrategy;
	private SnStoreSelectionStrategy snStoreSelectionStrategy;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ProductModel source, final ProductPickupAvailabilityData data) throws SnIndexerException
	{
		if (MapUtils.isEmpty(data.getPickupAvailability()))
		{
			return null;
		}

		final Map<String, Boolean> pickupAvailability = data.getPickupAvailability();

		if (fieldWrapper.isQualified())
		{
			final Map<String, Boolean> value = new HashMap<>();

			final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
			for (final SnQualifier qualifier : qualifiers)
			{
				value.put(qualifier.getId(), pickupAvailability.get(qualifier.getId()));
			}

			return value;
		}
		else
		{
			return pickupAvailability.get(DEFAULT_STORE_KEY);
		}
	}

	@Override
	protected ProductPickupAvailabilityData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, Boolean> pickupAvailability = loadPickupAvailability(indexerContext, fieldWrappers, source);

		final ProductPickupAvailabilityData data = new ProductPickupAvailabilityData();
		data.setPickupAvailability(pickupAvailability);

		return data;
	}

	protected Map<String, Boolean> loadPickupAvailability(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source)
	{
		final Map<String, Boolean> pickupAvailability = new HashMap<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			if (fieldWrapper.isQualified())
			{
				loadQualifiedPickupAvailability(fieldWrapper, source, pickupAvailability);
			}
			else
			{
				loadDefaultPickupAvailability(indexerContext, source, pickupAvailability);
			}
		}

		return pickupAvailability;
	}

	protected void loadQualifiedPickupAvailability(final SnIndexerFieldWrapper fieldWrapper, final ProductModel source,
			final Map<String, Boolean> data)
	{
		final List<SnQualifier> qualifiers = fieldWrapper.getQualifiers();
		for (final SnQualifier qualifier : qualifiers)
		{
			data.computeIfAbsent(qualifier.getId(), key -> {
				final BaseStoreModel store = qualifier.getAs(BaseStoreModel.class);
				return pickupAvailabilityStrategy.isPickupAvailableForProduct(source, store);
			});
		}
	}

	protected void loadDefaultPickupAvailability(final SnIndexerContext indexerContext, final ProductModel source,
			final Map<String, Boolean> data)
	{
		data.computeIfAbsent(DEFAULT_STORE_KEY, key -> {
			final BaseStoreModel store = snStoreSelectionStrategy.getDefaultStore(indexerContext.getIndexType().getId())
					.orElseThrow(() -> new ProductSearchStrategyRuntimeException("Default store for index type has not been found!"));
			return pickupAvailabilityStrategy.isPickupAvailableForProduct(source, store);
		});
	}

	public PickupAvailabilityStrategy getPickupAvailabilityStrategy()
	{
		return pickupAvailabilityStrategy;
	}

	@Required
	public void setPickupAvailabilityStrategy(final PickupAvailabilityStrategy pickupAvailabilityStrategy)
	{
		this.pickupAvailabilityStrategy = pickupAvailabilityStrategy;
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

	protected static class ProductPickupAvailabilityData
	{
		private Map<String, Boolean> pickupAvailability;

		public Map<String, Boolean> getPickupAvailability()
		{
			return pickupAvailability;
		}

		public void setPickupAvailability(final Map<String, Boolean> pickupAvailability)
		{
			this.pickupAvailability = pickupAvailability;
		}
	}
}
