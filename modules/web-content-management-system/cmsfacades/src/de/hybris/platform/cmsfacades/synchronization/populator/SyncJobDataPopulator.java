/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.populator;

import de.hybris.platform.cmsfacades.data.SyncJobData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;


/**
 * This populator will populate the {@link SyncJobData} from the {@link CronJobModel}.
 */
public class SyncJobDataPopulator implements Populator<Optional<CronJobModel>, SyncJobData>
{

	@Override
	public void populate(final Optional<CronJobModel> source, final SyncJobData target) throws ConversionException
	{

		source.ifPresent(src -> {
			target.setEndDate(src.getEndTime());
			target.setStartDate(src.getStartTime());
			target.setSyncStatus(src.getStatus().name());
			target.setSyncResult(src.getResult().name());
			target.setLastModifiedDate(src.getModifiedtime());
			target.setCreationDate(src.getCreationtime());
			target.setCode(src.getCode());
		});
	}
}
