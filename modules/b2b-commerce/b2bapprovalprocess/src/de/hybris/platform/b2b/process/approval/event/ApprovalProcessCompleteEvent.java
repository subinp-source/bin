/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.event;

import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * An event is fired when b2b approval process is completed successfully.
 */
public class ApprovalProcessCompleteEvent extends AbstractEvent
{

	private B2BApprovalProcessModel b2BApprovalProcessModel;

	public ApprovalProcessCompleteEvent(final B2BApprovalProcessModel b2BApprovalProcessModel)
	{
		super();
		this.b2BApprovalProcessModel = b2BApprovalProcessModel;
	}

	public B2BApprovalProcessModel getB2BApprovalProcessModel()
	{
		return b2BApprovalProcessModel;
	}

}
