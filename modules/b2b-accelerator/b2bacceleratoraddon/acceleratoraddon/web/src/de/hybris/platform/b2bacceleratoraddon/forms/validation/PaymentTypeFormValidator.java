/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.forms.validation;

import de.hybris.platform.b2bacceleratoraddon.forms.PaymentTypeForm;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link PaymentTypeForm}.
 */
@Component("paymentTypeFormValidator")
public class PaymentTypeFormValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return PaymentTypeForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (object instanceof PaymentTypeForm)
		{
			final PaymentTypeForm paymentTypeForm = (PaymentTypeForm) object;

			if (CheckoutPaymentType.ACCOUNT.getCode().equals(paymentTypeForm.getPaymentType())
					&& StringUtils.isBlank(paymentTypeForm.getCostCenterId()))
			{
				errors.rejectValue("costCenterId", "general.required");
			}
		}
	}

}
