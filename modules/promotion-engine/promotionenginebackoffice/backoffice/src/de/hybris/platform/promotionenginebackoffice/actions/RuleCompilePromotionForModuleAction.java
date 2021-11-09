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

import de.hybris.platform.ruleenginebackoffice.actions.RuleCompileForModuleAction;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * Action to compile a single rule for a user-specified environment.
 */
public class RuleCompilePromotionForModuleAction extends RuleCompileForModuleAction
{

	private static final String DEFAULT_DIALOG_TEMPLATE = "/rulecompilepromotionformodule.zul";
	private static final String TITLE_RULECOMPILEACTION = "title.rulecompilepromotionformoduleaction";

	@Override
	protected String getDialogTemplate(final ActionContext<AbstractRuleModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<AbstractRuleModel> context)
	{
		return TITLE_RULECOMPILEACTION;
	}

}
