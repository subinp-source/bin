/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.actions.replenishment;

import de.hybris.platform.b2bacceleratorservices.event.ReplenishmentOrderConfirmationEvent;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Action for confirming orders.
 */
public class ConfirmationAction extends AbstractProceduralAction<ReplenishmentProcessModel>
{
	private EventService eventService;

	@Override
	public void executeAction(final ReplenishmentProcessModel process) throws Exception
	{
		final BusinessProcessParameterModel orderParameter = getProcessParameterHelper().getProcessParameterByName(process, "order");
		final OrderModel order = (OrderModel) orderParameter.getValue();
		getModelService().refresh(order);
		this.getEventService().publishEvent(new ReplenishmentOrderConfirmationEvent(order));
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
