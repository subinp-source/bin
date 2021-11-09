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

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import org.apache.log4j.Logger;

import java.util.Collections;

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.SUCCESS;
import static de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.EventType.TRIGGER;
import static de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.MESSAGE_SOURCE;
import static java.util.Objects.isNull;


/**
 * Push the Current Rule into the Engine.
 */
public class PushRuleToEngineAction extends AbstractPushRulesToEngineAction
		implements CockpitAction<AbstractRuleEngineRuleModel, String>
{
	private static final String CONFIRMATION_MESSAGE = "rule.engine.push.rule.confirmation.message";
	private static final Logger LOG = Logger.getLogger(PushRuleToEngineAction.class);

	@Override
	public boolean canPerform(final ActionContext<AbstractRuleEngineRuleModel> arg0)
	{
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<AbstractRuleEngineRuleModel> context)
	{
		return context.getLabel(CONFIRMATION_MESSAGE);
	}

	@Override
	public boolean needsConfirmation(final ActionContext<AbstractRuleEngineRuleModel> context)
	{
		return context.getData() != null;
	}

	@Override
	public ActionResult<String> perform(final ActionContext<AbstractRuleEngineRuleModel> context)
	{
		final AbstractRuleEngineRuleModel abstractRuleEngineRule = context.getData();
		if (abstractRuleEngineRule instanceof DroolsRuleModel)
		{
			final DroolsRuleModel droolsRule = (DroolsRuleModel) abstractRuleEngineRule;
			if (droolsRule.getKieBase() == null)
			{
				return new ActionResult(ActionResult.ERROR, context.getLabel("ruleenginebackoffice_RulesUpdated_error", new Object[]
				{ String.format("Rule %s doesn't have any KIE base.", droolsRule.getCode()) }));
			}
			final DroolsKIEModuleModel kieModule = droolsRule.getKieBase().getKieModule();
			if (isNull(kieModule))
			{
				return new ActionResult(ActionResult.ERROR, context.getLabel("ruleenginebackoffice_RulesUpdated_error", new Object[]
				{ String.format("Rule %s is not associated to any KIE module.", droolsRule.getCode()) }));
			}
			final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher()
					.triggerModuleInitialization(kieModule.getName());
			LOG.debug("Triggered Rule modules update process.");
			getNotificationService().notifyUser(MESSAGE_SOURCE, TRIGGER, SUCCESS, ruleEngineCronJob.getCode());
			getEventQueue()
					.publishEvent(new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));

			return getActionResult(context, Collections.emptyList());
		}
		else
		{
			return new ActionResult(ActionResult.ERROR, context.getLabel("ruleenginebackoffice_RulesUpdated_error", new Object[]
			{ String.format("Unexpected type of Rule. Should be %s but was %s", DroolsRuleModel.class.getName(),
					abstractRuleEngineRule != null ? abstractRuleEngineRule.getClass().getName() : null) }));
		}

	}
}
