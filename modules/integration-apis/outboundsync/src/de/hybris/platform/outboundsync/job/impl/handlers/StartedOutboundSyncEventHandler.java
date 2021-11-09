/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers;

import de.hybris.platform.outboundsync.events.OutboundSyncEvent;
import de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncEventHandler;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncState;

/**
 * Implementation of {@link OutboundSyncEventHandler} for events of type {@link StartedOutboundSyncEvent}.
 * Although this class is public it should not be extended and customized. Coordinating and managing events between different
 * handlers may be broken with a custom handler implementation.
 */
public class StartedOutboundSyncEventHandler implements OutboundSyncEventHandler<StartedOutboundSyncEvent>
{
	private StartedOutboundSyncEventHandler()
	{
		// non-instantiable to prevent subclassing and customizations
	}

	/**
	 * Instantiates this handler.
	 *
	 * @return new instance of the handler.
	 */
	public static StartedOutboundSyncEventHandler createHandler()
	{
		return new StartedOutboundSyncEventHandler();
	}

	@Override
	public Class<StartedOutboundSyncEvent> getHandledEventClass(){
		return StartedOutboundSyncEvent.class;
	}

	@Override
	public OutboundSyncState handle(final StartedOutboundSyncEvent event, final OutboundSyncState currentState)
	{
		return OutboundSyncState.Builder.from(currentState)
		                                .withStartTime(event.getStartTime())
		                                .withTotalItems(event.getItemCount())
		                                .build();
	}

}
