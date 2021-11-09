/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service;

import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;


/**
 * Mapping for a specific search provider type.
 */
public interface SnSearchProviderMapping
{
	/**
	 * Returns the item type.
	 *
	 * @return the item type
	 */
	String getItemType();

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	String getType();

	/**
	 * Returns the load strategy.
	 *
	 * @return the load strategy
	 */
	SnSearchProviderConfigurationLoadStrategy getLoadStrategy();

	/**
	 * Returns the search provider.
	 *
	 * @return the search provider
	 */
	SnSearchProvider<AbstractSnSearchProviderConfiguration> getSearchProvider();
}
