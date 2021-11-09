/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.dto.OutboundItemDTO;
import de.hybris.platform.outboundsync.dto.impl.DeltaDetectionOutboundItemChange;
import de.hybris.platform.outboundsync.job.CountingChangesCollector;
import de.hybris.platform.outboundsync.job.ItemChangeSender;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel;

import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

public class StreamingChangesCollector implements CountingChangesCollector
{
	private static final Logger LOG = Log.getLogger(StreamingChangesCollector.class);

	private final OutboundSyncStreamConfigurationModel streamConfiguration;
	private final ItemChangeSender itemChangeSender;
	private final AtomicInteger numOfChanges;
	private final CronJobModel cronJobModel;

	public StreamingChangesCollector(@NotNull final ItemChangeSender itemChangeSender,
	                                 @NotNull final CronJobModel jobModel,
	                                 @NotNull final OutboundSyncStreamConfigurationModel streamConfiguration)
	{
		Preconditions.checkArgument(itemChangeSender != null, "ItemChangeSender cannot be null.");
		Preconditions.checkArgument(jobModel != null, "CronJobModel cannot be null");
		Preconditions.checkArgument(streamConfiguration != null, "OutboundSyncStreamConfigurationModel cannot be null.");

		this.itemChangeSender = itemChangeSender;
		this.streamConfiguration = streamConfiguration;
		cronJobModel = jobModel;
		numOfChanges = new AtomicInteger();
	}

	/**
	 * {@inheritDoc}
	 * Sends each individual change to the spring integration channel gateway.
	 */
	@Override
	public boolean collect(final ItemChangeDTO itemChangeDTO)
	{
		LOG.debug("Sending changes for itemChangeDTO: {}", itemChangeDTO);
		itemChangeSender.send(convert(itemChangeDTO));
		numOfChanges.incrementAndGet();
		return true;
	}

	private OutboundItemDTO convert(final ItemChangeDTO change)
	{
		return OutboundItemDTO.Builder.item()
		                              .withItem(new DeltaDetectionOutboundItemChange(change))
		                              .withIntegrationObjectPK(streamConfiguration.getOutboundChannelConfiguration()
		                                                                          .getIntegrationObject()
		                                                                          .getPk()
		                                                                          .getLong())
		                              .withChannelConfigurationPK(
				                              streamConfiguration.getOutboundChannelConfiguration().getPk().getLong())
		                              .withCronJobPK(cronJobModel.getPk())
		                              .build();
	}

	@Override
	public void finish()
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Detected {} changes of type {}", numOfChanges.get(),
					streamConfiguration.getItemTypeForStream().getCode());
		}
	}

	/**
	 * {@inheritDoc}
	 * This collector counts all changes processed during the lifetime of this specific instance. If
	 * "reset" is required, then a new instance of the collector must be created.
	 */
	@Override
	public int getNumberOfChangesCollected()
	{
		return numOfChanges.get();
	}
}
