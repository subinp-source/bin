/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.exceptions.ProductSearchStrategyRuntimeException;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnStoreSelectionStrategy;
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnStoreSelectionStrategy}.
 */
public class DefaultSnStoreSelectionStrategy implements SnStoreSelectionStrategy
{
	private SnIndexTypeDao snIndexTypeDao;

	@Override
	public Optional<BaseStoreModel> getDefaultStore(final String indexTypeId)
	{
		final SnIndexTypeModel indexType = snIndexTypeDao.findIndexTypeById(indexTypeId)
				.orElseThrow(() -> new ProductSearchStrategyRuntimeException("Product index type has not been found!"));
		return getDefaultStore(indexType);
	}

	@Override
	public Optional<BaseStoreModel> getDefaultStore(final SnIndexTypeModel indexType)
	{
		if (CollectionUtils.isNotEmpty(indexType.getStores()))
		{
			return Optional.of(indexType.getStores().get(0));
		}

		if (CollectionUtils.isNotEmpty(indexType.getSites()))
		{
			for (final BaseSiteModel site : indexType.getSites())
			{
				if (CollectionUtils.isNotEmpty(site.getStores()))
				{
					return Optional.of(site.getStores().get(0));
				}
			}
		}

		return Optional.empty();
	}

	@Override
	public List<BaseStoreModel> getStores(final String indexTypeId)
	{
		final SnIndexTypeModel indexType = snIndexTypeDao.findIndexTypeById(indexTypeId)
				.orElseThrow(() -> new ProductSearchStrategyRuntimeException("Product index type has not been found!"));
		return getStores(indexType);
	}

	@Override
	public List<BaseStoreModel> getStores(final SnIndexTypeModel indexType)
	{
		final Set<BaseStoreModel> stores = new LinkedHashSet<>();

		if (CollectionUtils.isNotEmpty(indexType.getStores()))
		{
			stores.addAll(indexType.getStores());
		}

		if (CollectionUtils.isNotEmpty(indexType.getSites()))
		{
			for (final BaseSiteModel site : indexType.getSites())
			{
				if (CollectionUtils.isNotEmpty(site.getStores()))
				{
					stores.addAll(site.getStores());
				}
			}
		}

		return List.copyOf(stores);
	}

	public SnIndexTypeDao getSnIndexTypeDao()
	{
		return snIndexTypeDao;
	}

	@Required
	public void setSnIndexTypeDao(final SnIndexTypeDao snIndexTypeDao)
	{
		this.snIndexTypeDao = snIndexTypeDao;
	}
}
