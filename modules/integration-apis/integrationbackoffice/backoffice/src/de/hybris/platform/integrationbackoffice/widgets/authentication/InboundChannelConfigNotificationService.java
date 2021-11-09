/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationbackoffice.widgets.authentication;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

/**
 * Service dedidcated to the construction of backoffice banner notifications related to Inbound Channel Configuration
 */
public class InboundChannelConfigNotificationService
{
	private final NotificationService notificationService;

	/**
	 * Default constructor
	 *
	 * @param notificationService basic notification service
	 */
	public InboundChannelConfigNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	private NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * Creates a success notification with a link to the persisted data.
	 *
	 * @param widgetInstanceManager Widget context
	 * @param iccModel              Inbound Channel Configuration model that was persisted
	 */
	public void createICCToExposedDestinationSuccessNotification(final WidgetInstanceManager widgetInstanceManager, final
	InboundChannelConfigurationModel iccModel)
	{
		getNotificationService().notifyUser(widgetInstanceManager, IntegrationbackofficeConstants.NOTIFICATION_TYPE_LINK,
				NotificationEvent.Level.SUCCESS, iccModel);
	}
}
