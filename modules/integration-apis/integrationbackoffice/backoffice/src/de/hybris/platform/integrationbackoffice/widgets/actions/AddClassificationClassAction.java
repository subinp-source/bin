/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;


import javax.annotation.Resource;

/**
 * Action responsible for sending current integration object to success output. The action is available only for Product type and its subtypes.
 */
public final class AddClassificationClassAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<String, String>
{
	@Resource
	private EditorAccessRights editorAccessRights;
	@Resource
	private ReadService readService;

	@Override
	public ActionResult<String> perform(final ActionContext<String> ctx)
	{
		String type = ctx.getData();
		return new ActionResult<>(ActionResult.SUCCESS, type);
	}

	@Override
	public boolean canPerform(ActionContext<String> ctx)
	{
		String type = ctx.getData();
		return type != null && readService.isProductType(type) && editorAccessRights.isUserAdmin();

	}

}