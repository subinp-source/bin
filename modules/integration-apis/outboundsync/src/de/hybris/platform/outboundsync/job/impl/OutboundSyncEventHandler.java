/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.outboundsync.events.OutboundSyncEvent;

import javax.validation.constraints.NotNull;

/**
 * Handler for calculating an {@link OutboundSyncState} based on a {@link OutboundSyncEvent} and, if it exists, a
 * {@link OutboundSyncState} with the previous status.
 */
public interface OutboundSyncEventHandler<T extends OutboundSyncEvent>
{
	/**
	 * Returns the type of event that can be handled.
	 * @return the {@link OutboundSyncEvent} type class that the handler can handle.
	 */
	Class<T> getHandledEventClass();

	/**
	 * Handles the event and creates a new {@link OutboundSyncState} that is calculated based on the information received in
	 * a {@link OutboundSyncEvent} and the current state if it exists.
	 *
	 * @param event {@link OutboundSyncEvent} containing initial information about a synchronization job.
	 * @param currentState {@link OutboundSyncState} with the current status of the sync job if it has already been started.
	 * @return a new {@link OutboundSyncState} calculated from the parameters received.
	 */
	@NotNull OutboundSyncState handle(@NotNull T event, @NotNull OutboundSyncState currentState);
}
