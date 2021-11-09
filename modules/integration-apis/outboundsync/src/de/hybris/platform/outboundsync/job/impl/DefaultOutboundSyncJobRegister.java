/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.config.impl.OutboundSyncConfiguration;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;

/**
 * Implementation of the {@link OutboundSyncJobRegister} that is used by default when no custom implementation is injected.
 */
public class DefaultOutboundSyncJobRegister implements OutboundSyncJobRegister, OutboundSyncStateObserver
{
	private static final Logger LOG = Log.getLogger(DefaultOutboundSyncJobRegister.class);
	private static final String SELECT_OUTBOUNDSYNCCRONJOB_BY_PK = "SELECT {PK} FROM {OutboundSyncCronJob} WHERE {PK}=?pk";

	private final Map<PK, OutboundSyncJobStateAggregator> allRunningJobs;
	private final ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private OutboundSyncConfiguration outboundSyncConfiguration;

	public DefaultOutboundSyncJobRegister(@NotNull final ModelService modelService)
	{
		this.modelService = modelService;
		allRunningJobs = new ConcurrentHashMap<>();
	}

	@Override
	public @NotNull OutboundSyncJob getNewJob(@NotNull final OutboundSyncCronJobModel jobModel)
	{
		unregister(jobModel);
		return getJob(jobModel);
	}

	@Override
	public Optional<OutboundSyncJob> getJob(@NotNull final PK jobPk)
	{
		final OutboundSyncJobStateAggregator aggregator = allRunningJobs.get(jobPk);
		if (aggregator == null)
		{
			return findJobModel(jobPk)
					.map(this::getJob);
		}
		return Optional.of(aggregator);
	}

	private Optional<OutboundSyncCronJobModel> findJobModel(final @NotNull PK jobPk)
	{
		LOG.debug("Searching for a job model corresponding to {} PK", jobPk);
		try
		{
			final Object item = modelService.get(jobPk);
			return Optional.ofNullable(item)
			               .filter(OutboundSyncCronJobModel.class::isInstance)
			               .map(OutboundSyncCronJobModel.class::cast);
		}
		catch (final ModelLoadingException e)
		{
			return Optional.empty();
		}
	}

	@Override
	public @NotNull OutboundSyncJob getJob(@NotNull final OutboundSyncCronJobModel jobModel)
	{
		return getRegisteredJob(jobModel);
	}

	private OutboundSyncJobStateAggregator getRegisteredJob(final @NotNull OutboundSyncCronJobModel job)
	{
		return allRunningJobs.computeIfAbsent(job.getPk(), pk -> createJobStateAggregator(job));
	}

	protected OutboundSyncJobStateAggregator createJobStateAggregator(final @NotNull OutboundSyncCronJobModel job)
	{
		LOG.debug("Registering new running job {}", job);
		final OutboundSyncJobStateAggregator aggregator = OutboundSyncJobStateAggregator.create(job);
		aggregator.addStateObserver(this);
		return aggregator;
	}

	@Override
	public void stateChanged(@NotNull final OutboundSyncCronJobModel model, @NotNull final OutboundSyncState state)
	{
		if (state.isAllItemsProcessed())
		{
			unregister(model);
		}
		persistJobState(model, state);
	}

	private void unregister(final OutboundSyncCronJobModel model)
	{
		LOG.debug("Unregistering job {}", model);
		allRunningJobs.remove(model.getPk());
	}

	protected void persistJobState(@NotNull final OutboundSyncCronJobModel job, @NotNull final OutboundSyncState state)
	{
		waitBeforeUpdateCronjob();

		final OutboundSyncCronJobModel updatedCronjobModel = searchOutboundSyncCronJobModel(
				job);
		if (updatedCronjobModel == null)
		{
			throw new ModelPersistenceException("The OutboundSynCronJob that we are trying to update does not exist.");
		}

		updatedCronjobModel.setStartTime(state.getStartTime());
		updatedCronjobModel.setResult(state.getCronJobResult());
		updatedCronjobModel.setStatus(state.getCronJobStatus());
		if (state.isAllItemsProcessed())
		{
			updatedCronjobModel.setEndTime(state.getEndTime());
			updatedCronjobModel.setRequestAbort(null);
		}
		LOG.info("Persisting job {} state {}", updatedCronjobModel, state);
		modelService.save(updatedCronjobModel);
	}

	private void waitBeforeUpdateCronjob()
	{
		try
		{
			MILLISECONDS.sleep(getOutboundSyncConfiguration().getOutboundSyncCronjobModelSearchSleep());
		}
		catch (final InterruptedException e)
		{
			LOG.debug("Sleep could not be completed");
		}
	}

	private OutboundSyncCronJobModel searchOutboundSyncCronJobModel(final OutboundSyncCronJobModel job)
	{
		final Map<String, Object> params = ImmutableMap.of("pk", job.getPk());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(SELECT_OUTBOUNDSYNCCRONJOB_BY_PK, params);
		flexibleSearchQuery.setDisableCaching(true);
		final SearchResult<OutboundSyncCronJobModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
		return searchResult.getResult().isEmpty() ? null : searchResult.getResult().get(0);
	}

	private FlexibleSearchService getFlexibleSearchService()
	{
		if (flexibleSearchService == null)
		{
			flexibleSearchService = Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
		}
		return flexibleSearchService;
	}

	private OutboundSyncConfiguration getOutboundSyncConfiguration()
	{
		if (outboundSyncConfiguration == null)
		{
			outboundSyncConfiguration = Registry.getApplicationContext().getBean("outboundSyncConfiguration", OutboundSyncConfiguration.class);
		}
		return outboundSyncConfiguration;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public void setOutboundSyncConfiguration(final OutboundSyncConfiguration outboundSyncConfiguration)
	{
		this.outboundSyncConfiguration = outboundSyncConfiguration;
	}
}
