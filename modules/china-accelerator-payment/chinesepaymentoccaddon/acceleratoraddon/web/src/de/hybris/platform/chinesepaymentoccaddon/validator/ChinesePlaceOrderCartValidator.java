/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentoccaddon.validator;

import de.hybris.platform.chinesepaymentoccaddon.exceptions.CartVoucherExpiredException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates cart before placing order.
 */
public class ChinesePlaceOrderCartValidator implements Validator
{
	private final VoucherFacade voucherFacade;

	public ChinesePlaceOrderCartValidator(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CartData.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final CartData cart = (CartData) target;

		if (!cart.isCalculated())
		{
			errors.reject("chinesepayment.cart.notCalculated", "Cart is not calculated.");
		}

		if (cart.getDeliveryMode() == null)
		{
			errors.reject("chinesepayment.cart.deliveryModeNotSet", "Delivery mode is not set.");
		}

		if (cart.getChinesePaymentInfo() == null)
		{
			errors.reject("chinesepayment.cart.chinesePaymentInfoNotSet", "Chinese payment info is not set.");
		}

		validateCartCoupon(cart);
	}

	private void validateCartCoupon(final CartData cart)
	{
		final CartVoucherExpiredException exception = new CartVoucherExpiredException("cartVoucherExpired");
		final List<String> appliedCoupons = cart.getAppliedVouchers();
		appliedCoupons.stream().filter(couponCode -> !voucherFacade.checkVoucherCode(couponCode)).collect(Collectors.toList())
				.forEach(invalidCoupon -> exception.addInvalidVoucher(invalidCoupon));
		if (CollectionUtils.isNotEmpty(exception.getInvalidVouchers()))
		{
			throw exception;
		}
	}
}
