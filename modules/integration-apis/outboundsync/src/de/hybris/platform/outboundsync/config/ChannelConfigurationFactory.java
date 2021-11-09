/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config;

import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel;

import java.util.List;

/**
 * Interface that enables the automatic generation of outboundsync components when a new {@link OutboundChannelConfigurationModel} is created.
 */
public interface ChannelConfigurationFactory
{
	/**
	 * Creates the {@link StreamConfigurationContainerModel}
	 *
	 * @param channel the {@link OutboundChannelConfigurationModel}
	 * @return the new {@link StreamConfigurationContainerModel}
	 */
	OutboundSyncStreamConfigurationContainerModel createStreamContainer(OutboundChannelConfigurationModel channel);

	/**
	 * Create the {@link StreamConfigurationModel}s for each IntegrationObjectItem
	 *
	 * @param channel the {@link OutboundChannelConfigurationModel} to associate with each {@link StreamConfigurationModel}
	 * @param streamConfigContainer the {@link StreamConfigurationContainerModel} to associate with each {@link StreamConfigurationModel}
	 * @return a list of {@link StreamConfigurationModel}s
	 */
	List<StreamConfigurationModel> createStreams(OutboundChannelConfigurationModel channel, StreamConfigurationContainerModel streamConfigContainer);

	/**
	 * Creates the {@link OutboundSyncJobModel}
	 *
	 * @param channel the {@link OutboundChannelConfigurationModel} to associate with the new outboundsync component
	 * @param streamConfigContainer the {@link OutboundSyncStreamConfigurationContainerModel} to associate with the new outboundsync component
	 * @return the new {@link OutboundSyncJobModel}
	 */
	OutboundSyncJobModel createJob(OutboundChannelConfigurationModel channel, OutboundSyncStreamConfigurationContainerModel streamConfigContainer);

	/**
	 * Creates the {@link OutboundSyncCronJobModel}
	 *
	 * @param channel the {@link OutboundChannelConfigurationModel} to associate with the new outboundsync component
	 * @param job the {@link OutboundSyncJobModel} to associate with the new outboundsync component
	 * @return the new {@link OutboundSyncCronJobModel}
	 */
	OutboundSyncCronJobModel createCronJob(OutboundChannelConfigurationModel channel, OutboundSyncJobModel job);
}
