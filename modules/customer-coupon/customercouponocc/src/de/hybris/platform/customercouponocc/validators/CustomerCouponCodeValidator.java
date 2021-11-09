/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponocc.validators;

import de.hybris.platform.customercouponfacades.CustomerCouponFacade;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class CustomerCouponCodeValidator implements Validator
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

		if (getCustomerCouponFacade().getValidCouponForCode(couponCode) == null)
		{
			errors.rejectValue("", "customer.coupon.inexistence");
			return;
		}
		if (!getCustomerCouponFacade().isCouponOwnedByCurrentUser(couponCode))
		{
			errors.rejectValue("", "customer.coupon.notowned");
		}

	}

	protected CustomerCouponFacade getCustomerCouponFacade()
	{
		return customerCouponFacade;
	}

}
