/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service.impl;


import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.index.SnIndexException;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnIndexService}.
 */
public class DefaultSnIndexService implements SnIndexService
{
	private SnContextFactory snContextFactory;
	private SnSearchProviderFactory snSearchProviderFactory;

	@Override
	public String getDefaultIndexId(final String indexTypeId) throws SnIndexException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("indexTypeId", indexTypeId);

		return indexTypeId;
	}

	@Override
	public void deleteIndexForId(final String indexTypeId, final String indexId) throws SnIndexException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("indexTypeId", indexTypeId);
		ServicesUtil.validateParameterNotNullStandardMessage("indexId", indexId);

		try
		{
			final SnContext context = snContextFactory.createContext(indexTypeId);
			final SnSearchProvider<?> searchProvider = snSearchProviderFactory.getSearchProviderForContext(context);
			searchProvider.deleteIndex(context, indexId);
		}
		catch (final SnException e)
		{
			throw new SnIndexException();
		}
	}

	public SnContextFactory getSnContextFactory()
	{
		return snContextFactory;
	}

	@Required
	public void setSnContextFactory(final SnContextFactory snContextFactory)
	{
		this.snContextFactory = snContextFactory;
	}

	public SnSearchProviderFactory getSnSearchProviderFactory()
	{
		return snSearchProviderFactory;
	}

	@Required
	public void setSnSearchProviderFactory(final SnSearchProviderFactory snSearchProviderFactory)
	{
		this.snSearchProviderFactory = snSearchProviderFactory;
	}
}
