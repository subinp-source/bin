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

import de.hybris.platform.ruleenginebackoffice.actions.RuleCloneAction;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * Action to clone a promotion rule.
 */
public class RuleClonePromotionAction extends RuleCloneAction
{
	private static final String RULE_CLONE_TITLE = "promotion.rule.clone.title";
	private static final String DEFAULT_DIALOG_TEMPLATE = "/clonepromotionrule.zul";

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
}
