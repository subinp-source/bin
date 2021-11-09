/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.productinterest.cronjob;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customerinterestsservices.productinterest.daos.ProductInterestDao;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ExpiredInterestsCleanUpJobTest
{

	private ExpiredInterestsCleanUpJob expiredInterestsCleanUpJob;

	@Mock
	ProductInterestDao productInterestDao;

	@Mock
	ModelService modelService;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		expiredInterestsCleanUpJob = new ExpiredInterestsCleanUpJob();
		expiredInterestsCleanUpJob.setProductInterestDao(productInterestDao);
		expiredInterestsCleanUpJob.setModelService(modelService);
		Mockito.when(productInterestDao.findExpiredProductInterests()).thenReturn(Collections.emptyList());
	}

	@Test
	public void testPerform()
	{
		final PerformResult result = expiredInterestsCleanUpJob.perform(new CronJobModel());
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verify(productInterestDao).findExpiredProductInterests();
		verify(modelService).removeAll(Collections.emptyList());
	}

}