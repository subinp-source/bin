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

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleenginebackoffice.actions.RuleCompileAllForModuleAction;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.Set;
import java.util.function.Predicate;


public class RuleCompileAllPromotionsForModuleAction extends RuleCompileAllForModuleAction
{
	private static final String DEFAULT_DIALOG_TEMPLATE = "/rulecompileallpromotionsformodule.zul";
	private static final String TITLE_RULECOMPILESELECTEDACTION = "title.rulecompileselectedpromotionsformoduleaction";

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

	/**
	 * returns a filter (used inside of {@link #getSelectedRules(ActionContext)})
	 */
	@Override
	protected Predicate<AbstractRuleModel> filterSelectedRules()
	{
		return r -> r instanceof PromotionSourceRuleModel;
	}
}
