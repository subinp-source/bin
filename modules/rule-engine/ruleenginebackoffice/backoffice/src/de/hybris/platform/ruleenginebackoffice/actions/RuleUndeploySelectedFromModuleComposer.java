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
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.search.data.pageable.Pageable;

import java.util.Set;


/**
 * RuleUndeploySelectedFromModuleComposer is responsible for handling the undeploy selected rules action with the
 * environment chooser.
 */
public class RuleUndeploySelectedFromModuleComposer extends AbstractRuleUndeployFromModuleComposer<Set>
{
	@Override
	protected void refreshWidget()
	{
		final DefaultWidgetModel widgetModel = (DefaultWidgetModel) getContext().getParameter(ActionContext.PARENT_WIDGET_MODEL);
		final Pageable pageable = (Pageable) widgetModel.get("pageable");
		pageable.refresh();
		getInteractiveAction().sendOutputDataToSocket("refreshPageableObject", pageable);
	}
}
