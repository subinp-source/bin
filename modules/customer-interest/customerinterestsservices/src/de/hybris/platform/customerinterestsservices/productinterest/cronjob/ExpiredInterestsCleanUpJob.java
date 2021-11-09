/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.productinterest.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customerinterestsservices.productinterest.daos.ProductInterestDao;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Required;


/**
 * Find out all the expired interests and remove them
 */
public class ExpiredInterestsCleanUpJob extends AbstractJobPerformable<CronJobModel>
{
	private ProductInterestDao productInterestDao;

	@Override
	public PerformResult perform(final CronJobModel job)
	{
		super.modelService.removeAll(getProductInterestDao().findExpiredProductInterests());
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected ProductInterestDao getProductInterestDao()
	{
		return productInterestDao;
	}

	@Required
	public void setProductInterestDao(final ProductInterestDao productInterestDao)
	{
		this.productInterestDao = productInterestDao;
	}

}
