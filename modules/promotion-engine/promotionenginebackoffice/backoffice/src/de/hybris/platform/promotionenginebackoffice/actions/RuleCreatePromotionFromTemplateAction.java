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
package de.hybris.platform.promotionenginebackoffice.actions;

import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleTemplateModel;
import de.hybris.platform.ruleenginebackoffice.actions.AbstractInteractiveAction;

import org.zkoss.zul.Window;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * Action to create a promotion source rule by template.
 */
public class RuleCreatePromotionFromTemplateAction extends AbstractInteractiveAction<PromotionSourceRuleTemplateModel, Object>
{
	private static final String RULE_CREATE_TITLE = "rule.createfromtemplate.title";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/createpromotionfromtemplate.zul";

	@Override
	protected String getDialogTemplate(final ActionContext<PromotionSourceRuleTemplateModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<PromotionSourceRuleTemplateModel> context)
	{
		return RULE_CREATE_TITLE;
	}

	protected PromotionSourceRuleTemplateModel getRuleTemplate(final ActionContext<PromotionSourceRuleTemplateModel> context)
	{
		return context.getData();
	}

	@Override
	protected void addDialogWindowAttribute(final ActionContext<PromotionSourceRuleTemplateModel> context, final Window window)
	{
		window.setAttribute("ruleTemplate", getRuleTemplate(context));
	}
}
