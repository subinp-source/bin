/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.actions;


import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;

import javax.annotation.Resource;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;


public class InitializeModuleAction implements CockpitAction<AbstractRulesModuleModel, Object>
{

	@Resource(name = "cockpitEventQueue")
	private CockpitEventQueue eventQueue;

	@Resource
	private NotificationService notificationService;

	@Resource
	private RuleEngineCronJobLauncher ruleEngineCronJobLauncher;

	@Override
	public boolean canPerform(final ActionContext<AbstractRulesModuleModel> ctx)
	{
		return ctx.getData() instanceof DroolsKIEModuleModel;
	}

	@Override
	public ActionResult<Object> perform(final ActionContext<AbstractRulesModuleModel> context)
	{
		final DroolsKIEModuleModel module = (DroolsKIEModuleModel) context.getData();

		final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher().triggerModuleInitialization(module.getName());

		getNotificationService().notifyUser(RuleEngineBackofficeConstants.NotificationSource.MESSAGE_SOURCE,
				RuleEngineBackofficeConstants.NotificationSource.EventType.TRIGGER, NotificationEvent.Level.SUCCESS,
				ruleEngineCronJob.getCode());

		getEventQueue().publishEvent(
				new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));

		return new ActionResult(ActionResult.SUCCESS, module);
	}

	protected RuleEngineCronJobLauncher getRuleEngineCronJobLauncher()
	{
		return ruleEngineCronJobLauncher;
	}

	protected CockpitEventQueue getEventQueue()
	{
		return eventQueue;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
