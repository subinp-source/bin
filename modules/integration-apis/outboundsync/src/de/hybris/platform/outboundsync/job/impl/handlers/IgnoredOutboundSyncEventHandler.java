/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers;

import de.hybris.platform.outboundsync.events.IgnoredOutboundSyncEvent;
import de.hybris.platform.outboundsync.events.OutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncState;

/**
 * Implementation of {@link OutboundSyncEventHandler} for events of type {@link IgnoredOutboundSyncEvent}
 */
public class IgnoredOutboundSyncEventHandler implements OutboundSyncEventHandler<IgnoredOutboundSyncEvent>
{
	private IgnoredOutboundSyncEventHandler()
	{
		// non-instantiable
	}

	/**
	 * Instantiates this handler
	 *
	 * @return new handler instance
	 */
	public static IgnoredOutboundSyncEventHandler createHandler()
	{
		return new IgnoredOutboundSyncEventHandler();
	}

	@Override
	public Class<IgnoredOutboundSyncEvent> getHandledEventClass(){
		return IgnoredOutboundSyncEvent.class;
	}

	@Override
	public OutboundSyncState handle(final IgnoredOutboundSyncEvent event, final OutboundSyncState currentState)
	{
		return OutboundSyncState.Builder.from(currentState)
		                                .withSuccessCount(currentState.getSuccessCount() + 1)
		                                .build();
	}
}
