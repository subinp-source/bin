/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnProductIndexTypeSelectionStrategy;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnProductIndexTypeSelectionStrategy} that checks:
 * <ul>
 * <li>SnIndexType that is bound with current base store</li>
 * </ul>
 */
public class DefaultSnProductIndexTypeSelectionStrategy implements SnProductIndexTypeSelectionStrategy
{
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;

	@Override
	public Optional<String> getProductIndexTypeId()
	{
		SnIndexTypeModel productIndexType = null;

		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		if (currentBaseStore != null && currentBaseStore.getProductIndexType() != null)
		{
			productIndexType = currentBaseStore.getProductIndexType();
		}

		if (productIndexType == null)
		{
			final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
			if (currentBaseSite != null && currentBaseSite.getProductIndexType() != null)
			{
				productIndexType = currentBaseSite.getProductIndexType();
			}
		}

		if (productIndexType != null)
		{
			return Optional.of(productIndexType.getId());
		}

		return Optional.empty();
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
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
}
