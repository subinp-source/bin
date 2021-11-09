/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_OAUTH_SOCKET_IN;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_OAUTH_SOCKET_OUT;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.MANDATORY_INPUT;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.REGISTER_BUTTON;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.REQUIRED_DESTINATION;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.SELECT_ONLY_ONE;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.VALIDATE_LABEL;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;

public class RegisterIntegrationObjectWithOAuthCredentialController extends RegisterIntegrationObjectController
{
	private Listbox destinationTarget;
	private Combobox credential;
	private Combobox user;

	@SocketEvent(socketId = ICC_OAUTH_SOCKET_IN)
	public void loadWidgetInfo(final InboundChannelConfigurationModel inboundChannelConfiguration)
	{
		loadDestinationTargets(destinationTarget);
		loadCredentials();
		loadUsers();
		this.setValue(ICC_OAUTH_SOCKET_IN, inboundChannelConfiguration);
	}

	@Override
	protected void loadCredentials()
	{
		registerIntegrationObjectService.readExposedOAuthCredentials().forEach((entry -> credential
				.appendChild(createComboItem(entry.getId(), entry))));
	}

	@Override
	protected boolean isInputValid()
	{
		return isDestinationNotEmpty() && isCredentialNotEmpty() && isCredentialValid();
	}

	@ViewEvent(componentID = REGISTER_BUTTON, eventName = Events.ON_CLICK)
	public void registerIntegrationObject()
	{
		registerIntegrationObject(ICC_OAUTH_SOCKET_OUT);
	}

	@Override
	protected void createExposedDestinations()
	{
		registerIntegrationObjectService.createExposedDestinations(extractDestinationTargets(destinationTarget),
				extractICC(),
				extractOAuthCredential());
	}

	private boolean isDestinationNotEmpty()
	{
		if (destinationTarget.getItems().stream()
		                     .noneMatch(Listitem::isSelected))
		{
			Messagebox.show(getLabel(REQUIRED_DESTINATION),
					getLabel(VALIDATE_LABEL), 1, Messagebox.ERROR);
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean isCredentialNotEmpty()
	{
		if (user.getSelectedItem() == null && credential.getSelectedItem() == null)
		{
			Messagebox.show(getLabel(MANDATORY_INPUT),
					getLabel(VALIDATE_LABEL), 1, Messagebox.ERROR);
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean isCredentialValid()
	{
		if (user.getSelectedItem() != null && credential.getSelectedItem() != null)
		{
			Messagebox.show(getLabel(SELECT_ONLY_ONE),
					getLabel(VALIDATE_LABEL), 1, Messagebox.ERROR);
			return false;
		}
		else
		{
			return true;
		}
	}

	private AbstractCredentialModel extractOAuthCredential()
	{
		return credential.getSelectedItem() != null ? credential.getSelectedItem().getValue() :
				registerIntegrationObjectService.createOAuthCredential(user.getSelectedItem().getValue());
	}

	private InboundChannelConfigurationModel extractICC()
	{
		return this.getValue(ICC_OAUTH_SOCKET_IN,
				InboundChannelConfigurationModel.class);
	}

	private void loadUsers()
	{
		registerIntegrationObjectService.readEmployees().forEach((entry -> user
				.appendChild(createComboItem(entry.getUid(), entry))));
	}
}

