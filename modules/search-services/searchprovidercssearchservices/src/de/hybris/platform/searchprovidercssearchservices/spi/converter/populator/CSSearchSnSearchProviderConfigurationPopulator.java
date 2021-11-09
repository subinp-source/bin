/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.spi.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchprovidercssearchservices.model.CSSearchSnSearchProviderConfigurationModel;
import de.hybris.platform.searchprovidercssearchservices.spi.data.CSSearchSnSearchProviderConfiguration;


/**
 * Populates {@link CSSearchSnSearchProviderConfiguration} from {@link CSSearchSnSearchProviderConfigurationModel}.
 */
public class CSSearchSnSearchProviderConfigurationPopulator
		implements Populator<CSSearchSnSearchProviderConfigurationModel, CSSearchSnSearchProviderConfiguration>
{
	@Override
	public void populate(final CSSearchSnSearchProviderConfigurationModel source,
			final CSSearchSnSearchProviderConfiguration target)
	{
		target.setDestinationId(source.getConsumedDestination().getId());
		target.setDestinationTargetId(source.getConsumedDestination().getDestinationTarget().getId());
	}
}
