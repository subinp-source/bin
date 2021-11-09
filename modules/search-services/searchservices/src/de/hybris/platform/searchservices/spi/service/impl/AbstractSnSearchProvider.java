/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service.impl;

import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;


/**
 * Base class for search providers.
 */
public abstract class AbstractSnSearchProvider<T extends AbstractSnSearchProviderConfiguration> implements SnSearchProvider<T>
{
	@Override
	public T getSearchProviderConfiguration(final SnContext context)
	{
		final SnIndexConfiguration indexConfiguration = context.getIndexConfiguration();
		return (T) indexConfiguration.getSearchProviderConfiguration();
	}
}
