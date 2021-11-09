/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.actions;

import de.hybris.platform.b2bacceleratorservices.event.ReplenishmentOrderPlacedEvent;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Sends Replenishment Order Placed Notification event.
 */
public class SendReplenishmentOrderPlacedNotification
{
	private EventService eventService;

	public void executeAction(final CartToOrderCronJobModel cartToOrder)
	{
		getEventService().publishEvent(new ReplenishmentOrderPlacedEvent(cartToOrder));
		Logger.getLogger(getClass()).info("Published cartToOrder: " + cartToOrder.getCode());
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected EventService getEventService()
	{
		return eventService;
	}
}
