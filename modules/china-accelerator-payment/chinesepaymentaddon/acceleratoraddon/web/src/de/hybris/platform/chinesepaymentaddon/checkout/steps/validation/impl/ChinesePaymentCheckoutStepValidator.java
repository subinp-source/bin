/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentaddon.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ChinesePaymentCheckoutStepValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return PaymentDetailsForm.class == aClass;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (object != null && ((PaymentDetailsForm) object).getPaymentId() == null)
		{
			errors.rejectValue("PaymentId", "payment.PaymentId.invalid");
		}
	}

}
