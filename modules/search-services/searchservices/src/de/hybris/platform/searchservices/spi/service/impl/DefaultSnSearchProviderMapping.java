/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service.impl;

import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderConfigurationLoadStrategy;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderMapping;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnSearchProviderMapping}.
 */
public class DefaultSnSearchProviderMapping implements SnSearchProviderMapping
{
	private String itemType;
	private String type;
	private SnSearchProviderConfigurationLoadStrategy loadStrategy;
	private SnSearchProvider<AbstractSnSearchProviderConfiguration> searchProvider;

	@Override
	public String getItemType()
	{
		return itemType;
	}

	@Required
	public void setItemType(final String itemType)
	{
		this.itemType = itemType;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Required
	public void setType(final String type)
	{
		this.type = type;
	}

	@Override
	public SnSearchProviderConfigurationLoadStrategy getLoadStrategy()
	{
		return loadStrategy;
	}

	@Required
	public void setLoadStrategy(final SnSearchProviderConfigurationLoadStrategy loadStrategy)
	{
		this.loadStrategy = loadStrategy;
	}

	@Override
	public SnSearchProvider<AbstractSnSearchProviderConfiguration> getSearchProvider()
	{
		return searchProvider;
	}

	@Required
	public void setSearchProvider(final SnSearchProvider<AbstractSnSearchProviderConfiguration> searchProvider)
	{
		this.searchProvider = searchProvider;
	}
}
