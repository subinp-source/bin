/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.actions;

import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;

import javax.annotation.Resource;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

public class ItemTypeMatchIOIAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<String, String>
{
	@Resource
	private EditorAccessRights editorAccessRights;

	@Override
	public ActionResult<String> perform(final ActionContext<String> actionContext)
	{
		sendOutput("openItemTypeIOIModal", "");
		return new ActionResult<>(ActionResult.SUCCESS, "");
	}

	@Override
	public boolean canPerform(final ActionContext<String> ctx)
	{
		return editorAccessRights.isUserAdmin();
	}
}
