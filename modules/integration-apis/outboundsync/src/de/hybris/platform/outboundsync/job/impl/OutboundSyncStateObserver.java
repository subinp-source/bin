/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;

import javax.validation.constraints.NotNull;

/**
 * Allows implementors to be notified about outbound sync job state changes.
 */
public interface OutboundSyncStateObserver
{
	/**
	 * Notifies the observer about the outbound sync job state change.
	 * @param model outbound sync cron job model, for which the state changed. Implementors should not rely on this
	 *              model correctly reflecting persisted state because the model may be stale. It's just to identify
	 *              what job has changed.
	 * @param state new state the job has changed to.
	 */
	void stateChanged(@NotNull OutboundSyncCronJobModel model, @NotNull OutboundSyncState state);
}
