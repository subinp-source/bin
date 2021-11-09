/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentocc.validator;

import de.hybris.platform.chinesepaymentfacades.payment.strategies.OrderPayableCheckStrategy;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;

import java.util.Objects;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates Order code.
 */
public class ChineseOrderValidator implements Validator
{

	private final OrderPayableCheckStrategy chineseOrderPayableCheckStrategy;
	private final CheckoutCustomerStrategy checkoutCustomerStrategy;

	public ChineseOrderValidator(final OrderPayableCheckStrategy chineseOrderPayableCheckStrategy,
			final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.chineseOrderPayableCheckStrategy = chineseOrderPayableCheckStrategy;
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	@Override
	public boolean supports(final Class clazz)
	{
		return OrderData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final OrderData orderData = (OrderData) target;
		if (Objects.nonNull(orderData) && !chineseOrderPayableCheckStrategy.isOrderPayable(orderData))
		{
			errors.reject("chinesepayment.order.alreadyPaid", new String[]
			{ checkoutCustomerStrategy.isAnonymousCheckout() ? orderData.getGuid() : orderData.getCode() },
					"The order has already been paid.");
		}
	}
}
