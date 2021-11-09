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

import de.hybris.platform.ruleenginebackoffice.actions.RuleCreateFromTemplateComposer;

import com.hybris.backoffice.navigation.TreeNodeSelector;


/**
 * RuleCreatePromotionFromTemplateComposer is responsible for handling the create rule from template action.
 */
public class RuleCreatePromotionFromTemplateComposer extends RuleCreateFromTemplateComposer
{

	private static final String PROMOTION_RULES_NAVIGATION_NODE = "hmc_typenode_promotion_rules";

	@Override
	protected void navigateToTreeNode()
	{
		getInteractiveAction().sendOutputDataToSocket("nodeToNavigate",
				new TreeNodeSelector(PROMOTION_RULES_NAVIGATION_NODE, true));
	}
}
