/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.actions;

import de.hybris.platform.b2b.process.approval.actions.InformOfOrderRejection;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * Checks for order in process.
 */
public class B2BAcceleratorInformOfOrderApprovalRejection extends InformOfOrderRejection
{
	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = Logger.getLogger(InformOfOrderRejection.class);

	@Override
	public void executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		try
		{
			final OrderModel order = process.getOrder();
			Assert.notNull(order, String.format("Order of BusinessProcess %s should have be set for accelerator", process));
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Process for accelerator: %s in step %s order: %s user: %s ", process.getCode(), getClass(),
						order.getUnit(), order.getUser().getUid()));
			}
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(e.getMessage(), e);
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
