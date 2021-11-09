/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel;

public interface StreamIdentifierGenerator
{
	/**
	 *
	 * @param channel {@link OutboundChannelConfigurationModel} to help derive a name that will be returned
	 * @param item {@link IntegrationObjectItemModel} to help derive a name that will be returned
	 * @return a streamId to associate the {@link OutboundSyncStreamConfigurationModel} with the channel
	 */
	String generate(OutboundChannelConfigurationModel channel, IntegrationObjectItemModel item);
}
