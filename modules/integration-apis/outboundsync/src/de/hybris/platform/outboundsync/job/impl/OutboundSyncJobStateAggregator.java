/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.events.OutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.handlers.AbortedOutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.handlers.CompletedOutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.handlers.IgnoredOutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.handlers.StartedOutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.handlers.SystemErrorOutboundSyncEventHandler;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * This aggregator keeps track of an outbound sync job by aggregating {@link OutboundSyncEvent}s indicating the job progress.
 */
public class OutboundSyncJobStateAggregator implements OutboundSyncJob
{
	private static final Logger LOG = Log.getLogger(OutboundSyncJobStateAggregator.class);
	private static final OutboundSyncEventHandler DEFAULT_HANDLER = new UnknownOutboundSyncEventHandler();
	private static final OutboundSyncState INITIAL_STATE = OutboundSyncState.Builder.initialOutboundSyncState().build();
	private static final Map<Class<? extends OutboundSyncEvent>, OutboundSyncEventHandler<? extends OutboundSyncEvent>> eventHandlers =
			Stream.of(
					CompletedOutboundSyncEventHandler.createHandler(), IgnoredOutboundSyncEventHandler.createHandler(),
					StartedOutboundSyncEventHandler.createHandler(), AbortedOutboundSyncEventHandler.createHandler(),
					SystemErrorOutboundSyncEventHandler.createHandler())
			      .collect(Collectors.toMap(OutboundSyncEventHandler::getHandledEventClass, it -> it));
	private final OutboundSyncCronJobModel cronJobModel;
	private final List<OutboundSyncStateObserver> stateObservers = new LinkedList<>();
	private OutboundSyncState currentJobState;

	private OutboundSyncJobStateAggregator(@NotNull final OutboundSyncCronJobModel model)
	{
		Preconditions.checkArgument(model != null, OutboundSyncCronJobModel.class.getSimpleName() + " cannot be null");

		cronJobModel = model;
		currentJobState = INITIAL_STATE;
	}

	/**
	 * Instantiates this state aggregator.
	 *
	 * @param model a job model representing the cron job, for which state has to be managed by this aggregator.
	 */
	public static OutboundSyncJobStateAggregator create(@NotNull final OutboundSyncCronJobModel model)
	{
		return new OutboundSyncJobStateAggregator(model);
	}

	@Override
	public synchronized <T extends OutboundSyncEvent> void applyEvent(final T event)
	{
		logDebug("Processing {}", event);
		final OutboundSyncEventHandler<T> handler = eventHandlers.getOrDefault(event.getClass(), DEFAULT_HANDLER);
		final OutboundSyncState newState = handler.handle(event, currentJobState);
		logDebug("{}", newState);
		if (newState.isChangedFrom(currentJobState))
		{
			stateObservers.forEach(o -> notifyObserver(o, newState));
		}
		currentJobState = newState;
	}

	private void notifyObserver(final OutboundSyncStateObserver o, final OutboundSyncState state)
	{
		try
		{
			o.stateChanged(cronJobModel, state);
		}
		catch (final RuntimeException e)
		{
			logError("Observer {} failed to process state change to {}", o, state, e);
		}
	}

	@Override
	public synchronized OutboundSyncState getCurrentState()
	{
		return currentJobState;
	}

	/**
	 * Registers an observer to be notified whenever this aggregate state changes, i.e. the cron job status changes,
	 * cron job result changes or {@link de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent} was applied.
	 *
	 * @param observer an observer to notify
	 */
	@Override
	public void addStateObserver(final OutboundSyncStateObserver observer)
	{
		stateObservers.add(observer);
	}

	private void logDebug(final String msg, final Object... args)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(logMessage(msg), args);
		}
	}

	private void logError(final String msg, final Object... args)
	{
		if (LOG.isErrorEnabled())
		{
			LOG.error(logMessage(msg), args);
		}
	}

	private String logMessage(final String msg)
	{
		return "[" + cronJobModel.getCode() + "]: " + msg;
	}

	private static class UnknownOutboundSyncEventHandler implements OutboundSyncEventHandler<OutboundSyncEvent>
	{
		@Override
		public Class<OutboundSyncEvent> getHandledEventClass()
		{
			return OutboundSyncEvent.class;
		}

		@Override
		public OutboundSyncState handle(final OutboundSyncEvent event, final OutboundSyncState currentState)
		{
			LOG.warn("No applicable handler found to process {}", event);
			return currentState;
		}
	}
}
