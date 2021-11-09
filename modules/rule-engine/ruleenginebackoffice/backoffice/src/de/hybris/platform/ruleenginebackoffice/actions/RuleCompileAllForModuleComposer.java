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

import de.hybris.platform.ruleengine.enums.RuleType;

import java.util.Set;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.search.data.pageable.Pageable;


/**
 * RuleCompileAllForModuleComposer is responsible for handling the compile all rules action with the environment
 * chooser.
 */
public class RuleCompileAllForModuleComposer extends AbstractRuleCompileForModuleComposer<Set>
{
	@Override
	protected void onSuccess(final String moduleName, final String previousModuleVersion, final String moduleVersion)
	{
		final DefaultWidgetModel widgetModel = (DefaultWidgetModel) getContext().getParameter(ActionContext.PARENT_WIDGET_MODEL);
		final Pageable pageable = (Pageable) widgetModel.get("pageable");
		pageable.refresh();

		getInteractiveAction().sendOutputDataToSocket("refreshPageableObject", pageable);
	}

	@Override
	protected RuleType getRuleType()
	{
		return getRulesToProcess().isEmpty() ? null : getRuleService().getEngineRuleTypeForRuleType(
				getRulesToProcess().get(0).getClass());
	}
}
