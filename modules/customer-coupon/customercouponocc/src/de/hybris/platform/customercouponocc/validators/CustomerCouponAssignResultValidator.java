/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponocc.validators;

import de.hybris.platform.customercouponfacades.emums.AssignCouponResult;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class CustomerCouponAssignResultValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return clazz == AssignCouponResult.class;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final AssignCouponResult result = (AssignCouponResult) object;

		if (result == AssignCouponResult.ASSIGNED)
		{
			errors.rejectValue("", "customer.coupon.claimed");
		}
		else if (result == AssignCouponResult.INEXISTENCE)
		{
			errors.rejectValue("", "customer.coupon.inexistence");
		}
	}

}
