/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;

import javax.annotation.Resource;

public final class IntegrationObjectAuditReportDownloadAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<String, String>
{

	@Resource
	private EditorAccessRights editorAccessRights;

	@Override
	public ActionResult<String> perform(ActionContext<String> actionContext)
	{
		sendOutput("requestAuditReport", "");
		return new ActionResult<>(ActionResult.SUCCESS, "");
	}

	@Override
	public boolean canPerform(ActionContext<String> ctx)
	{
		return editorAccessRights.isUserAdmin();
	}
}
