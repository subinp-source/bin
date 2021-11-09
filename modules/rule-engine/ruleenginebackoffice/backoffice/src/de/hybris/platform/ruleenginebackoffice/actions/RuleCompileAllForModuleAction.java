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
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.List;
import java.util.Set;


/**
 * Action to compile multiple rules for a user-specified rules module.
 */
public class RuleCompileAllForModuleAction extends AbstractRuleSetProcessingForModuleAction<Object>
{
	private static final String DIFFERENT_TYPES_MESSAGE = "rule.compileandpublish.publish.differenttypes.warning";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/rulecompileallformodule.zul";
	private static final String TITLE_RULECOMPILESELECTEDACTION = "title.rulecompileselectedformoduleaction";
	private static final String TITLE_RULECOMPILEALLPROMOTIONSACTION_INVALID = "title.rulecompileallactionwarning";

	@Override
	protected String getDialogTemplate(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return TITLE_RULECOMPILESELECTEDACTION;
	}

	@Override
	protected String getDifferentRuleTypesAlertMessage()
	{
		return DIFFERENT_TYPES_MESSAGE;
	}

	@Override
	protected String getDifferentRuleTypesAlertTitle()
	{
		return TITLE_RULECOMPILEALLPROMOTIONSACTION_INVALID;
	}

	/**
	 * returns currently selected rules ({@link #getSelectedRules(ActionContext)})
	 *
	 * @return the list of rules that will be compiled by this action
	 */
	@Override
	protected List<AbstractRuleModel> getRulesToProcess(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return getSelectedRules(context);
	}
}
