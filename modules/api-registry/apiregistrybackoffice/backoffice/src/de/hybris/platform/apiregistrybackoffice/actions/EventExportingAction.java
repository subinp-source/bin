/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

/**
 * Action in 'EventConfiguration' listview, triggers eventexportwidget
 */
public class EventExportingAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
	public static final String OPEN_WIDGET = "openWidget";

	@Override
	public ActionResult<Object> perform(ActionContext<Object> actionContext)
	{
		sendOutput(OPEN_WIDGET, actionContext.getData());
		return new ActionResult<>(ActionResult.SUCCESS);
	}
}
