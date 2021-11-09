/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.event;

import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * An event that is fired when a B2B order approval process starts
 */
public class ApprovalProcessStartEvent extends AbstractEvent
{
	private B2BApprovalProcessModel b2BApprovalProcessModel;

	public ApprovalProcessStartEvent(final B2BApprovalProcessModel b2BApprovalProcessModel)
	{
		super();
		this.b2BApprovalProcessModel = b2BApprovalProcessModel;
	}

	public B2BApprovalProcessModel getB2BApprovalProcessModel()
	{
		return b2BApprovalProcessModel;
	}
}
