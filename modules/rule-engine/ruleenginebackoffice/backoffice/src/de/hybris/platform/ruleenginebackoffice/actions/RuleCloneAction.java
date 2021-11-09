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

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import org.zkoss.zul.Window;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * Action to clone a rule.
 */
public class RuleCloneAction extends AbstractInteractiveAction<AbstractRuleModel, Object>
{
	private static final String RULE_CLONE_TITLE = "rule.clone.title";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/clonerule.zul";

	@Override
	protected String getDialogTemplate(final ActionContext<AbstractRuleModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<AbstractRuleModel> context)
	{
		return RULE_CLONE_TITLE;
	}

	protected AbstractRuleModel getRuleToClone(final ActionContext<AbstractRuleModel> context)
	{
		return context.getData();
	}

	@Override
	protected void addDialogWindowAttribute(final ActionContext<AbstractRuleModel> context, final Window window)
	{
		window.setAttribute("ruleToClone", getRuleToClone(context));
	}
}
