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
package de.hybris.platform.ruleenginebackoffice.widgets.listview.actions;

import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;

import java.util.List;

import javax.annotation.Resource;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.util.notifications.NotificationService;


/**
 * The class encapsulates common logic of Rule publishing.
 */
public abstract class AbstractPushRulesToEngineAction
{
	@Resource(name = "cockpitEventQueue")
	private CockpitEventQueue cockpitQueue;

	@Resource
	private RuleEngineCronJobLauncher ruleEngineCronJobLauncher;

	@Resource
	private NotificationService notificationService;

	protected ActionResult<String> getActionResult(final ActionContext<?> context,
			final List<RuleEngineActionResult> ruleEngineActionResults)
	{
		final StringBuilder moduleNames = new StringBuilder();
		final StringBuilder errorMsgs = new StringBuilder();
		boolean success = true;
		for (final RuleEngineActionResult ruleEngineActionResult : ruleEngineActionResults)
		{
			moduleNames.append(ruleEngineActionResult.getModuleName()).append(",");
			if (ruleEngineActionResult.isActionFailed())
			{
				success = false;
				errorMsgs.append(ruleEngineActionResult.getMessagesAsString(MessageLevel.ERROR)).append("\n");
			}
		}
		if (moduleNames.length() > 0)
		{
			moduleNames.deleteCharAt(moduleNames.length() - 1);
		}
		if (success)
		{
			return new ActionResult(ActionResult.SUCCESS, context.getLabel("ruleenginebackoffice_RulesUpdated_success", new Object[]
			{ moduleNames }));
		}
		else
		{
			return new ActionResult(ActionResult.ERROR, context.getLabel("ruleenginebackoffice_RulesUpdated_error", new Object[]
			{ moduleNames, errorMsgs.toString() }));

		}
	}

	protected RuleEngineCronJobLauncher getRuleEngineCronJobLauncher()
	{
		return ruleEngineCronJobLauncher;
	}

	protected CockpitEventQueue getEventQueue()
	{
		return cockpitQueue;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
