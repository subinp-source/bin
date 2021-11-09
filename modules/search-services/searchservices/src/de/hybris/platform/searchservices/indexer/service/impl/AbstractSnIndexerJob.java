/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.dao.SnIndexerCronJobDao;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link JobPerformable} for running indexer operations.
 */
public abstract class AbstractSnIndexerJob<T extends AbstractSnIndexerCronJobModel> extends AbstractJobPerformable<T>
{
	private SnIndexerService snIndexerService;
	private SnIndexerCronJobDao snIndexerCronJobDao;
	private SnIndexerItemSourceFactory snIndexerItemSourceFactory;

	@Override
	public boolean isAbortable()
	{
		return false;
	}

	@Override
	public boolean isPerformable()
	{
		return true;
	}

	/**
	 * Retrieve the lastSuccessfulStartTime from the cronjob. If the cronjob didn't finish successfully before, retrieve
	 * the maximum of the lastSuccessfulStartTime from the full indexing cronjobs for the same {@link SnIndexTypeModel}.
	 * If both are not available, return an empty result.
	 *
	 * @param cronJob
	 *           the cronjob
	 * @return the last successful start time, or empty
	 */
	protected Optional<Date> getLastSuccessfulStartTime(final T cronJob)
	{
		return Optional.ofNullable(cronJob.getLastSuccessfulStartTime())
				.or(() -> snIndexerCronJobDao.getMaxFullLastSuccessfulStartTime(cronJob.getIndexType()));
	}

	/**
	 * Persist the start time of the cronjob as last successful start time.
	 *
	 * @param cronJob
	 *           the cronjob
	 */
	protected void saveLastSuccessfulStartTime(final T cronJob)
	{
		cronJob.setLastSuccessfulStartTime(cronJob.getStartTime());
		modelService.save(cronJob);
	}

	public SnIndexerService getSnIndexerService()
	{
		return snIndexerService;
	}

	@Required
	public void setSnIndexerService(final SnIndexerService snIndexerService)
	{
		this.snIndexerService = snIndexerService;
	}

	public SnIndexerCronJobDao getSnIndexerCronJobDao()
	{
		return snIndexerCronJobDao;
	}

	@Required
	public void setSnIndexerCronJobDao(final SnIndexerCronJobDao snIndexerCronJobDao)
	{
		this.snIndexerCronJobDao = snIndexerCronJobDao;
	}

	public SnIndexerItemSourceFactory getSnIndexerItemSourceFactory()
	{
		return snIndexerItemSourceFactory;
	}

	@Required
	public void setSnIndexerItemSourceFactory(final SnIndexerItemSourceFactory snIndexerItemSourceFactory)
	{
		this.snIndexerItemSourceFactory = snIndexerItemSourceFactory;
	}
}
