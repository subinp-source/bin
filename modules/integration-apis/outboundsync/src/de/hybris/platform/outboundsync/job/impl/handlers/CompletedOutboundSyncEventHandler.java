/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers;

import de.hybris.platform.outboundsync.events.CompletedOutboundSyncEvent;
import de.hybris.platform.outboundsync.events.OutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncState;

/**
 * Implementation of {@link OutboundSyncEventHandler} for events of type {@link CompletedOutboundSyncEvent}
 */
public class CompletedOutboundSyncEventHandler implements OutboundSyncEventHandler<CompletedOutboundSyncEvent>
{
	private CompletedOutboundSyncEventHandler()
	{
		// non-instantiable
	}

	/**
	 * Instantiates this handler
	 *
	 * @return new handler instance
	 */
	public static CompletedOutboundSyncEventHandler createHandler()
	{
		return new CompletedOutboundSyncEventHandler();
	}

	@Override
	public Class<CompletedOutboundSyncEvent> getHandledEventClass()
	{
		return CompletedOutboundSyncEvent.class;
	}

	@Override
	public OutboundSyncState handle(final CompletedOutboundSyncEvent event, final OutboundSyncState currentState)
	{
		final int successCount = event.isSuccess()
				? (currentState.getSuccessCount() + event.getChangesCompleted())
				: currentState.getSuccessCount();
		final int errorCount = event.isSuccess()
				? currentState.getErrorCount()
				: (currentState.getErrorCount() + event.getChangesCompleted());

		return OutboundSyncState.Builder.from(currentState)
		                                .withSuccessCount(successCount)
		                                .withErrorCount(errorCount)
		                                .build();
	}
}
