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

import de.hybris.platform.ruleengineservices.model.SourceRuleModel;

import java.util.List;


/**
 * RuleUndeployFromModuleComposer is responsible for handling of Rule Undeploy Action dialog box.
 */
public class RuleUndeployFromModuleComposer extends AbstractRuleUndeployFromModuleComposer<SourceRuleModel>
{
	@Override
	protected void refreshWidget()
	{
		final List<SourceRuleModel> rulesToUndeploy = getRulesToUndeploy();

		if (rulesToUndeploy.size() == 1)
		{
			getInteractiveAction().sendOutputDataToSocket("selectItem", rulesToUndeploy.get(0));
		}
	}
}
