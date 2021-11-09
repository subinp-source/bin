/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationbackoffice.actions;

import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.TEMPLATE_DESTINATION_TARGET_SOCKET_ID;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

import org.apache.commons.lang.BooleanUtils;


public class RegisterDestinationTargetAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<DestinationTargetModel, String>
{
	@Override
	public ActionResult<String> perform(final ActionContext<DestinationTargetModel> actionContext)
	{
		sendOutput(TEMPLATE_DESTINATION_TARGET_SOCKET_ID, actionContext.getData());
		return new ActionResult<>(ActionResult.SUCCESS);
	}

	@Override
	public boolean canPerform(final ActionContext<DestinationTargetModel> ctx)
	{
		return ctx.getData() != null && BooleanUtils.isTrue(ctx.getData().getTemplate());
	}
}
