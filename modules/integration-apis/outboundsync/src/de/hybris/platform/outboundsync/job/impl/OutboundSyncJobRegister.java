/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;

import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * A registry of currently running outbound sync jobs.
 */
public interface OutboundSyncJobRegister
{
	/**
	 * Retrieves new instance of the outbound sync job object for the specified job model. Each subsequent call always returns
	 * new job instance. On the other hand, because it always returns a fresh job object instance, it guarantees the previous and
	 * possibly stale state of the job is discarded. For this reason, it makes sense to use this method only when it's reliably
	 * the first retrieval of the outbound sync job from this register. Otherwise, #getJob should be used.
	 *
	 * @param jobModel job entity model representing the cron job being executed.
	 * @return a state aggregator for the specified job model. If a state aggregator has been requested for the job model before,
	 * exactly same instance of aggregator is returned for the subsequent calls; otherwise a new instance of the aggregator
	 * is created and registered.
	 */
	@NotNull OutboundSyncJob getNewJob(@NotNull OutboundSyncCronJobModel jobModel);

	/**
	 * Retrieves the outbound sync job object corresponding to the specified job model.
	 *
	 * @param jobModel job entity model representing the cron job being executed.
	 * @return an outbound sync job for the specified job model. If that job has been requested by same job model before,
	 * exactly same instance of the outbound sync job is returned for the subsequent calls; otherwise a new instance of the job
	 * is created and registered.
	 */
	@NotNull OutboundSyncJob getJob(@NotNull OutboundSyncCronJobModel jobModel);

	/**
	 * Retrieves the outbound sync job object corresponding to the specified job model primary key.
	 *
	 * @param jobPk primary key of the job entity model, for which the aggregator needs to be retrieved.
	 * @return an Optional with the outbound sync job for the specified job model primary key. If the job has
	 * been requested for the job model before, exactly same instance of the job is returned in the {@code Optional}
	 * for the subsequent calls; otherwise a new instance of the outbound sync job is created and registered. If the primary key does
	 * not correspond to an {@link OutboundSyncCronJobModel}, then {@code Optional.empty()} is returned.
	 */
	Optional<OutboundSyncJob> getJob(@NotNull PK jobPk);
}
