/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleengine.cleanup;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Simple sanity test that only invokes the drools rule maintenance cleanup job
 * (for database compatibility tests to ensure the flexible search queries work properly)
 */
@IntegrationTest
public class CleanupDroolsRulesCronjobIT extends ServicelayerTest
{

	@Resource
	private CronJobService cronJobService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/ruleengine/import/essentialdata-jobs.impex", "utf-8");
	}

	@Test
	public void shouldSuccessfullyExecuteDroolsRuleMaintenanceCronjob()
	{
		final CronJobModel cronJob = cronJobService.getCronJob("droolsRulesMaintenanceCleanupJob");
		cronJobService.performCronJob(cronJob, true);
	}

}
