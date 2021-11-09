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

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.SUCCESS;
import static de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.MESSAGE_SOURCE;
import static de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.EventType.TRIGGER;

import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;

import java.util.Collections;

import org.apache.log4j.Logger;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;


/**
 * Push the Current Rules into the Engine.
 */
public class PushRulesToEngineAction extends AbstractPushRulesToEngineAction implements CockpitAction<String, String>
{
	private static final String CONFIRMATION_MESSAGE = "rule.engine.push.rules.confirmation.message";
	private static final Logger LOG = Logger.getLogger(PushRulesToEngineAction.class);

	@Override
	public boolean canPerform(final ActionContext<String> arg0)
	{
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<String> context)
	{
		return context.getLabel(CONFIRMATION_MESSAGE);
	}

	@Override
	public boolean needsConfirmation(final ActionContext<String> context)
	{
		return true;
	}

	@Override
	public ActionResult<String> perform(final ActionContext<String> context)
	{
		final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher().triggerAllModulesInitialization();
		LOG.debug("Triggered Rule modules update process.");
		getNotificationService().notifyUser(MESSAGE_SOURCE, TRIGGER, SUCCESS, ruleEngineCronJob.getCode());
		getEventQueue()
				.publishEvent(new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));
		return getActionResult(context, Collections.emptyList());
	}
}
