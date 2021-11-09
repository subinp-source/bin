/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.b2b.process.approval.event.ApprovalProcessStartEvent;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.task.RetryLaterException;
import org.springframework.beans.factory.annotation.Required;


/**
 * This action is executed at the beginning of a b2b order approval process.
 */
public class ApprovalProcessStartAction extends AbstractSimpleB2BApproveOrderDecisionAction
{
	private EventService eventService;

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	@Override
	public Transition executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		getEventService().publishEvent(new ApprovalProcessStartEvent(process));
		return Transition.OK;
	}
}
