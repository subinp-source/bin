/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.wizards;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


public class SnRunIndexerWizardRunActionHandler implements FlowActionHandler
{
	protected static final String SELECTED_INDEXER_CRON_JOB_CODE_KEY = "snSelectedIndexerCronJobCode";

	private CronJobService cronJobService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final String cronJobCode = adapter.getWidgetInstanceManager().getModel().getValue(SELECTED_INDEXER_CRON_JOB_CODE_KEY,
				String.class);
		final CronJobModel cronJob = cronJobService.getCronJob(cronJobCode);
		cronJobService.performCronJob(cronJob);

		adapter.done();
	}

	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}
}
