/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service;

import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;


/**
 * Implementations of this interface are responsible for getting the applicable instance of {@link SnSearchProvider}.
 */
public interface SnSearchProviderFactory
{
	/**
	 * Returns the {@link SnSearchProvider} instance for the given context.
	 *
	 * @param context
	 *           - the context
	 *
	 * @return {@link SnSearchProvider} instance
	 */
	SnSearchProvider getSearchProviderForContext(SnContext context);

	/**
	 * Returns the {@link SnSearchProviderMapping} instance for the given configuration model.
	 *
	 * @param configurationModel
	 *           - the configuration model
	 *
	 * @return SnSearchProviderMapping the mapping
	 */
	SnSearchProviderMapping getSearchProviderMappingForConfigurationModel(
			AbstractSnSearchProviderConfigurationModel configurationModel);

	/**
	 * Returns the {@link SnSearchProviderMapping} instance for the given configuration.
	 *
	 * @param configuration
	 *           - the configuration
	 *
	 * @return SnSearchProviderMapping the mapping
	 */
	SnSearchProviderMapping getSearchProviderMappingForConfiguration(AbstractSnSearchProviderConfiguration configuration);
}
