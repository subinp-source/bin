/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.outboundsync.events.OutboundSyncEvent;

import javax.validation.constraints.NotNull;

/**
 * An object controlling and representing state of a single outbound sync job. Implementations make decisions about whether the
 * job is finished and what's the result of the job execution.
 */
public interface OutboundSyncJob
{
	/**
	 * Applies an event and recalculates state of the context outbound sync job.
	 * @param event a job state changing event.
	 */
	<T extends OutboundSyncEvent>  void applyEvent(@NotNull T event);

	/**
	 * Retrieves state of the context job after the last event was applied.
	 * @return current state of the context outbound sync job.
	 */
	@NotNull OutboundSyncState getCurrentState();

	/**
	 * Registers a state observer.
	 * @param observer an observer that is going to be notified whenever the job state changes.
	 */
	void addStateObserver(@NotNull OutboundSyncStateObserver observer);
}
