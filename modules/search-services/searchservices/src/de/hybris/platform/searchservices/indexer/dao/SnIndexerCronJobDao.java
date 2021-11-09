/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.dao;

import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.Date;
import java.util.Optional;


/**
 * DAO to retrieve {@link de.hybris.platform.cronjob.model.CronJobModel} related data.
 */
public interface SnIndexerCronJobDao
{
	/**
	 * Retrieves the maximum last starttime of any successful full indexer job for the given type.
	 *
	 * @param indexType
	 *           the index type
	 * @return optional start time
	 */
	Optional<Date> getMaxFullLastSuccessfulStartTime(final SnIndexTypeModel indexType);
}
