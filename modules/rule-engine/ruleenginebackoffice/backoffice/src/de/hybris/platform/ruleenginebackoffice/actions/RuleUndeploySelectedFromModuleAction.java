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

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;


/**
 * Action to undeploy multiple rules from a user-specified rules module.
 */
public class RuleUndeploySelectedFromModuleAction extends AbstractRuleSetProcessingForModuleAction<Object>
{
	private static final String DIFFERENT_TYPES_MESSAGE = "message.ruleundeployselectedfrommoduleaction.differenttypes.warning";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/ruleundeployselectedfrommodule.zul";
	private static final String TITLE_RULE_UNDEPLOY_SELECTED_ACTION = "title.ruleundeployselectedfrommoduleaction";
	private static final String TITLE_RULE_UNDEPLOY_SELECTED_ACTION_INVALID = "title.ruleundeployselectedfrommoduleactionwarning";


	@Override
	protected String getDialogTemplate(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return TITLE_RULE_UNDEPLOY_SELECTED_ACTION;
	}

	/**
	 * returns the currently selected rules ({@link #getSelectedRules(ActionContext)}).
	 *
	 * @return the list of rules that will be undeployed by this action
	 */
	@Override
	protected List<AbstractRuleModel> getRulesToProcess(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return getSelectedRules(context);
	}

	@Override
	protected Predicate<AbstractRuleModel> filterSelectedRulesByStatus()
	{
		return r -> RuleStatus.PUBLISHED.equals(r.getStatus());
	}

	@Override
	protected String getDifferentRuleTypesAlertMessage()
	{
		return DIFFERENT_TYPES_MESSAGE;
	}

	@Override
	protected String getDifferentRuleTypesAlertTitle()
	{
		return TITLE_RULE_UNDEPLOY_SELECTED_ACTION_INVALID;
	}
}
