/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

public final class IntegrationObjectMetadataViewerAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<String, String>
{
	@Override
	public ActionResult<String> perform(final ActionContext<String> actionContext)
	{
		sendOutput("openMetadataModal", "");
		return new ActionResult<>(ActionResult.SUCCESS, "");
	}

	@Override
	public boolean canPerform(final ActionContext<String> ctx)
	{
		return true;
	}

}
