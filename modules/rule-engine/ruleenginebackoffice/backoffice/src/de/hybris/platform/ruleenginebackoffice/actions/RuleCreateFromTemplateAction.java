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

import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;

import org.zkoss.zul.Window;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * Action to create a source rule by template.
 */
public class RuleCreateFromTemplateAction extends AbstractInteractiveAction<AbstractRuleTemplateModel, Object>
{
	private static final String RULE_CREATE_TITLE = "rule.createfromtemplate.title";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/createrulefromtemplate.zul";

	@Override
	protected String getDialogTemplate(final ActionContext<AbstractRuleTemplateModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<AbstractRuleTemplateModel> context)
	{
		return RULE_CREATE_TITLE;
	}

	protected AbstractRuleTemplateModel getRuleTemplate(final ActionContext<AbstractRuleTemplateModel> context)
	{
		return context.getData();
	}

	@Override
	protected void addDialogWindowAttribute(final ActionContext<AbstractRuleTemplateModel> context, final Window window)
	{
		window.setAttribute("ruleTemplate", getRuleTemplate(context));
	}
}
