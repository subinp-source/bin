/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.actions;

import de.hybris.platform.b2b.process.approval.actions.SetBookingLineEntries;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;


/**
 * The AcceleratorBookingLineEntries.
 */
public class AcceleratorBookingLineEntries extends SetBookingLineEntries
{
	/** The Constant LOG. */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AcceleratorBookingLineEntries.class);

	@Override
	public Transition executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();
		modelService.refresh(order);

		if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			return Transition.OK;
		}
		else
		{
			return super.executeAction(process);
		}
	}
}
