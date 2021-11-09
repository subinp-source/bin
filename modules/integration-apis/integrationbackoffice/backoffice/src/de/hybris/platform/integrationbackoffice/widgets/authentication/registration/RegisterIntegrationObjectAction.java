/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_BASIC_SOCKET_IN;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_OAUTH_SOCKET_IN;

import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

public class RegisterIntegrationObjectAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<InboundChannelConfigurationModel, String>
{
	@Override
	public ActionResult<String> perform(final ActionContext<InboundChannelConfigurationModel> actionContext)
	{
		if (actionContext.getData().getAuthenticationType().equals(AuthenticationType.OAUTH))
		{
			sendOutput(ICC_OAUTH_SOCKET_IN, actionContext.getData());
		}
		else
		{
			sendOutput(ICC_BASIC_SOCKET_IN, actionContext.getData());
		}
		return new ActionResult<>(ActionResult.SUCCESS);
	}

}
