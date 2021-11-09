/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao.impl;

import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.cmsfacades.util.dao.SyncJobDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultSyncJobDao implements SyncJobDao
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<CatalogVersionSyncJobModel> getSyncJobsByCode(final String code)
	{

		final String queryString = "SELECT {pk} FROM {CatalogVersionSyncJob} WHERE {code}=?code";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		return getFlexibleSearchService().<CatalogVersionSyncJobModel>search(query).getResult();
	}
	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
