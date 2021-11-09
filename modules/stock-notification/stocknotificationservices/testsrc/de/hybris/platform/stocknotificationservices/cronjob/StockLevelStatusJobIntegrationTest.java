/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.cronjob;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.task.SyncTaskExecutor;



@IntegrationTest
public class StockLevelStatusJobIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private StockLevelStatusJob stockLevelStatusJob;

	@Before
	public void setup() throws IOException, ImpExException
	{
		importCsv("/stocknotificationservices/test/impex/stocklevelstatusjob-test-data.impex", "utf-8");
	}

	@Test
	public void testBackInStockProductsSize()
	{
		final SyncTaskExecutor syncTaskExecutor = new SyncTaskExecutor();

		stockLevelStatusJob.setTaskExecutor(syncTaskExecutor);

		final PerformResult result = stockLevelStatusJob.perform(new CronJobModel());

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		final List<ProductInterestModel> interests = stockLevelStatusJob.getInStockProductInterests();
		Assert.assertEquals(5, interests.size());
	}

}
