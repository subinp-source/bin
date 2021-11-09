/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.impl;

import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierProvider;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Required;


/**
 * Qualifier provider for stores.
 *
 * <p>
 * It supports the following types:
 * <ul>
 * <li>{@link BaseStoreModel}
 * </p>
 */
public class StoreSnQualifierProvider implements SnQualifierProvider
{
	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(BaseStoreModel.class);
	protected static final String STORE_QUALIFIERS_KEY = StoreSnQualifierProvider.class.getName() + ".storeQualifiers";

	private BaseStoreService baseStoreService;
	private SnStoreSelectionStrategy snStoreSelectionStrategy;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses()
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	public List<SnQualifier> getAvailableQualifiers(final SnContext context)
	{
		Objects.requireNonNull(context, "context is null");

		List<SnQualifier> qualifiers = (List<SnQualifier>) context.getAttributes().get(STORE_QUALIFIERS_KEY);
		if (qualifiers == null)
		{
			final List<BaseStoreModel> stores = snStoreSelectionStrategy.getStores(context.getIndexType().getId());
			qualifiers = CollectionUtils.emptyIfNull(stores).stream().map(this::createQualifier).collect(Collectors.toList());

			context.getAttributes().put(STORE_QUALIFIERS_KEY, qualifiers);
		}

		return qualifiers;
	}

	@Override
	public List<SnQualifier> getCurrentQualifiers(final SnContext context)
	{
		Objects.requireNonNull(context, "context is null");

		final BaseStoreModel store = baseStoreService.getCurrentBaseStore();

		if (store == null)
		{
			return Collections.emptyList();
		}

		return List.of(createQualifier(store));
	}

	protected StoreSnQualifier createQualifier(final BaseStoreModel source)
	{
		return new StoreSnQualifier(source);
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
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

	protected static class StoreSnQualifier implements SnQualifier
	{
		private final BaseStoreModel store;

		public StoreSnQualifier(final BaseStoreModel store)
		{
			Objects.requireNonNull(store, "store is null");

			this.store = store;
		}

		public BaseStoreModel getStore()
		{
			return store;
		}

		@Override
		public String getId()
		{
			return store.getUid();
		}

		@Override
		public boolean canGetAs(final Class<?> qualifierClass)
		{
			for (final Class<?> supportedQualifierClass : SUPPORTED_QUALIFIER_CLASSES)
			{
				if (qualifierClass.isAssignableFrom(supportedQualifierClass))
				{
					return true;
				}
			}

			return false;
		}

		@Override
		public <Q> Q getAs(final Class<Q> qualifierClass)
		{
			Objects.requireNonNull(qualifierClass, "qualifierClass is null");

			if (qualifierClass.isAssignableFrom(BaseStoreModel.class))
			{
				return (Q) store;
			}

			throw new IllegalArgumentException("Qualifier class not supported");
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}

			if (obj == null || this.getClass() != obj.getClass())
			{
				return false;
			}

			final StoreSnQualifier that = (StoreSnQualifier) obj;
			return new EqualsBuilder().append(this.store, that.store).isEquals();
		}

		@Override
		public int hashCode()
		{
			return store.hashCode();
		}
	}
}
