/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.strategies.impl;

import de.hybris.platform.chinesepaymentfacades.payment.strategies.OrderPayableCheckStrategy;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;

import java.util.Objects;


/**
 * Default implementation of {@link OrderPayableCheckStrategy}
 */
public class ChineseOrderPayableCheckStrategy implements OrderPayableCheckStrategy
{

	@Override
	public boolean isOrderPayable(final OrderData orderData)
	{

		return Objects.nonNull(orderData) && checkOrderStatus(orderData) && checkOrderPaymentStatus(orderData);
	}

	protected boolean checkOrderStatus(final OrderData orderData)
	{
		return Objects.nonNull(orderData.getStatus()) && !OrderStatus.CANCELLED.equals(orderData.getStatus());
	}

	protected boolean checkOrderPaymentStatus(final OrderData orderData)
	{
		return Objects.nonNull(orderData.getPaymentStatus()) && PaymentStatus.NOTPAID.equals(orderData.getPaymentStatus());
	}
}
