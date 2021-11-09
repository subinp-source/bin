/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.customercouponoccaddon.validators;

import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class CustomerCouponSubscribleValidator implements Validator
{

	@Resource(name = "customerCouponFacade")
	private CustomerCouponFacade customerCouponFacade;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return clazz == String.class;
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final String couponCode = (String) obj;
		final CustomerCouponData customerCoupon = getCustomerCouponFacade().getCustomerCouponForCode(couponCode);
		if (Objects.nonNull(customerCoupon) && customerCoupon.isNotificationOn())
		{
			errors.rejectValue("", "customer.coupon.alreadysubscribed");
		}
	}

	protected CustomerCouponFacade getCustomerCouponFacade()
	{
		return customerCouponFacade;
	}
}
