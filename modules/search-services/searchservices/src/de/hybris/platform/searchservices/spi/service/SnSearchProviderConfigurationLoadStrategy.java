/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service;

import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;


/**
 * Interface for the search provider configuration load strategy.
 */
public interface SnSearchProviderConfigurationLoadStrategy<T extends AbstractSnSearchProviderConfigurationModel, R extends AbstractSnSearchProviderConfiguration>
{

	/**
	 * Loads the search provider configuration.
	 *
	 * @param searchProviderConfiguration
	 *           - the configuration to be loaded
	 */
	R load(T searchProviderConfiguration);

}
