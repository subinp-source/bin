/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.auditreport.imp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.platform.auditreport.model.CreateAuditReportCronJobModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;


@RunWith(MockitoJUnitRunner.class)
@NullSafeWidget
@ExtensibleWidget(level = ExtensibleWidget.ALL)
public class PersonalDataReportProcessItemRenderingStrategyTest
		extends AbstractCockpitngUnitTest<PersonalDataReportProcessItemRenderingStrategy>
{

	@Spy
	private PersonalDataReportProcessItemRenderingStrategy strategy;

	@Mock
	private CreateAuditReportCronJobModel reportCronJob;

	@Mock
	private CronJobHistoryModel cronJobHistoryModel;

	@Test
	public void canHandle()
	{
		assertThat(strategy.canHandle(null)).isFalse();
		assertThat(strategy.canHandle(new CronJobHistoryModel())).isFalse();

		when(cronJobHistoryModel.getCronJob()).thenReturn(reportCronJob);
		assertThat(strategy.canHandle(cronJobHistoryModel)).isTrue();
	}

}
