/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.EMPTY_STRING;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.MESSAGE_LENGTH;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.NOTIFICATION_TYPE;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.SUCCESS_MESSAGE;

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;

public abstract class RegisterIntegrationObjectController extends DefaultWidgetController
{
	@Resource
	protected transient RegisterIntegrationObjectDefaultService registerIntegrationObjectService;
	@Resource
	protected transient NotificationService notificationService;

	protected abstract void loadCredentials();

	protected abstract boolean isInputValid();

	protected abstract void createExposedDestinations();

	protected void registerIntegrationObject(final String socketId)
	{
		if (isInputValid())
		{
			try
			{
				createExposedDestinations();
			}
			catch (final Exception ex)
			{
				notificationService.notifyUser(getWidgetInstanceManager(),
						NOTIFICATION_TYPE,
						Level.FAILURE,
						getMessage(ex));
				sendOutput(socketId, "failed");
				return;
			}
			notificationService.notifyUser(getWidgetInstanceManager(),
					NOTIFICATION_TYPE,
					Level.SUCCESS,
					getLabel(SUCCESS_MESSAGE));

			sendOutput(socketId, "completed");
		}

	}

	protected void loadDestinationTargets(final Listbox destinationTarget)
	{
		registerIntegrationObjectService.readDestinationTargets().forEach((entry -> destinationTarget
				.appendChild(createListItem(entry))));
	}

	protected List<DestinationTargetModel> extractDestinationTargets(final Listbox destinationTarget)
	{
		return destinationTarget.getItems().stream()
		                        .filter(Listitem::isSelected)
		                        .map(entry -> (DestinationTargetModel) entry.getValue())
		                        .collect(Collectors.toList());
	}

	protected Listitem createListItem(final DestinationTargetModel item)
	{
		return new Listitem(item.getId(), item);
	}

	protected String getMessage(final Throwable throwable)
	{
		final String[] message = Objects.toString(throwable.getLocalizedMessage(), EMPTY_STRING).split(":");
		return message.length == MESSAGE_LENGTH ? message[1] : throwable.getLocalizedMessage();
	}

}
