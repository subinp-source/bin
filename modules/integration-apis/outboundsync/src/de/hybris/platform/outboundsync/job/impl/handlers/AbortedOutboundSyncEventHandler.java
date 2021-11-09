/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers;

import de.hybris.platform.outboundsync.events.AbortedOutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncState;

/**
 * Implementation of {@link OutboundSyncEventHandler} for events of type {@link AbortedOutboundSyncEvent}
 */
public class AbortedOutboundSyncEventHandler implements OutboundSyncEventHandler<AbortedOutboundSyncEvent>
{
	private AbortedOutboundSyncEventHandler()
	{
		// non-instantiable
	}

	/**
	 * Instantiates this handler
	 *
	 * @return new handler instance
	 */
	public static AbortedOutboundSyncEventHandler createHandler()
	{
		return new AbortedOutboundSyncEventHandler();
	}

	@Override
	public Class<AbortedOutboundSyncEvent> getHandledEventClass(){
		return AbortedOutboundSyncEvent.class;
	}

	@Override
	public OutboundSyncState handle(final AbortedOutboundSyncEvent event, final OutboundSyncState currentState)
	{
		final int postAbortCount = currentState.getUnprocessedCount() + event.getChangesProcessed();
		return OutboundSyncState.Builder.from(currentState)
		                                .withUnprocessedCount(postAbortCount)
		                                .withAborted(true)
		                                .build();
	}
}
