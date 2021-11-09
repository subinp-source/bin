/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent;
import de.hybris.platform.outboundsync.job.ChangesCollectorFactory;
import de.hybris.platform.outboundsync.job.CountingChangesCollector;
import de.hybris.platform.outboundsync.job.ItemChangeSender;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

/**
 * This {@link JobPerformable} collects
 * the changes specified in the {@link StreamConfiguration} and send them out
 * via the {@link ItemChangeSender}.
 */
public class OutboundSyncCronJobPerformable extends AbstractJobPerformable<OutboundSyncCronJobModel>
{
	private static final Logger LOG = Log.getLogger(OutboundSyncCronJobPerformable.class);

	private ChangeDetectionService changeDetectionService;
	private ChangesCollectorFactory changesCollectorFactory;
	private EventService eventService;
	private OutboundSyncJobRegister jobRegister;

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	@Override
	public PerformResult perform(final OutboundSyncCronJobModel cronJob)
	{
		final var startTime = new Date();
		try
		{
			final OutboundSyncJob job = jobRegister.getNewJob(cronJob);
			final int processedCnt = processChangesForContainer(cronJob);
			eventService.publishEvent(new StartedOutboundSyncEvent(cronJob.getPk(), startTime, processedCnt));
			return job.getCurrentState().asPerformResult();
		}
		catch (final RuntimeException e)
		{
			logError(cronJob, "Error occurred while running job {}", cronJob.getCode(), e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	/**
	 * Process changes for all stream configurations in the container
	 *
	 * @param cronJob a cron job processing changes
	 * @return number of changes processed by the job or 0, if there are not changes detected.
	 */
	protected int processChangesForContainer(final OutboundSyncCronJobModel cronJob)
	{
		final OutboundSyncJobModel job = cronJob.getJob();
		Preconditions.checkArgument(job != null,
				"Misconfigured '" + cronJob.getCode() + "' cron job: referenced job model cannot be null");
		final OutboundSyncStreamConfigurationContainerModel streams = job.getStreamConfigurationContainer();
		Preconditions.checkArgument(streams != null,
				"Misconfigured '" + job.getCode() + "' job: referenced stream container cannot be null");
		Preconditions.checkArgument(streams.getConfigurations() != null, "Stream configurations cannot be null");
		logDebug(cronJob, "Collecting and sending changes for each stream configuration");
		return streams.getConfigurations().stream()
		              .map(OutboundSyncStreamConfigurationModel.class::cast)
		              .map(stream -> processChanges(cronJob, stream))
		              .reduce(0, Integer::sum);
	}

	/**
	 * Processes the changes from the given stream configuration
	 *
	 * @param cronJob     a cron job processing changes in the specified stream
	 * @param deltaStream a stream containing changes to be processed by the job
	 * @return number of changes processed for the specified stream or 0, if the stream has no changes detected.
	 */
	protected int processChanges(final OutboundSyncCronJobModel cronJob,
	                             final OutboundSyncStreamConfigurationModel deltaStream)
	{
		final StreamConfiguration streamCfg = getStreamConfiguration(deltaStream);
		final CountingChangesCollector collector = changesCollectorFactory.createCountingCollector(cronJob, deltaStream);

		logDebug(cronJob, "Processing changes for stream '{}'", deltaStream.getStreamId());
		getChangeDetectionService().collectChangesForType(deltaStream.getItemTypeForStream(), streamCfg, collector);
		return collector.getNumberOfChangesCollected();
	}

	protected StreamConfiguration getStreamConfiguration(final OutboundSyncStreamConfigurationModel deltaStream)
	{
		return StreamConfiguration.buildFor(deltaStream.getStreamId())
		                          .withItemSelector(deltaStream.getWhereClause());
	}

	private void logDebug(final CronJobModel job, final String msg, final Object... args)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(logMessage(job, msg), args);
		}
	}

	private void logError(final CronJobModel job, final String msg, final Object... args)
	{
		if (LOG.isErrorEnabled())
		{
			LOG.error(logMessage(job, msg), args);
		}
	}

	private String logMessage(final CronJobModel model, final String msg)
	{
		return "[" + model.getCode() + "]: " + msg;
	}

	protected ChangeDetectionService getChangeDetectionService()
	{
		return changeDetectionService;
	}

	@Required
	public void setChangeDetectionService(final ChangeDetectionService changeDetectionService)
	{
		this.changeDetectionService = changeDetectionService;
	}

	/**
	 * @deprecated not used anymore.
	 */
	@Deprecated(since = "1905.2003-CEP", forRemoval = true)
	public void setItemChangeSender(final ItemChangeSender itemChangeSender)
	{
		// left empty for backwards compatibility
	}

	/**
	 * @deprecated use {@link #setChangesCollectorFactory(ChangesCollectorFactory)} instead
	 */
	@Deprecated(since = "1905.2003-CEP", forRemoval = true)
	public void setGettableChangesCollectorProvider(final GettableChangesCollectorProvider changesCollectorProvider)
	{
		// left empty for backwards compatibility
	}

	@Required
	public void setChangesCollectorFactory(final ChangesCollectorFactory changesCollectorFactory)
	{
		this.changesCollectorFactory = changesCollectorFactory;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	/**
	 * Injects specific {@link OutboundSyncJobRegister} implementation.
	 *
	 * @param register an implementation to use
	 */
	@Required
	public void setJobRegister(@NotNull final OutboundSyncJobRegister register)
	{
		jobRegister = register;
	}
}
