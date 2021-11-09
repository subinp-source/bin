/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentocc.validator;

import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates PaymentMode code.
 */
public class PaymentModeCodeValidator implements Validator
{
	private static final int MAX_LENGTH = 128;

	private final ChineseCheckoutFacade chineseCheckoutFacade;

	public PaymentModeCodeValidator(final ChineseCheckoutFacade chineseCheckoutFacade)
	{
		this.chineseCheckoutFacade = chineseCheckoutFacade;
	}

	@Override
	public boolean supports(final Class clazz)
	{
		return String.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final String paymentModeCode = (String) target;
		if (StringUtils.isBlank(paymentModeCode) || StringUtils.length(paymentModeCode) > MAX_LENGTH)
		{
			errors.reject(paymentModeCode, new String[]
			{ String.valueOf(MAX_LENGTH) }, "The paymentModeCode is required and must be between 1 and {0} characters long.");
		}
		else if (Objects.isNull(chineseCheckoutFacade.getPaymentModeByCode(paymentModeCode)))
		{
			errors.reject("chinesepayment.param.invalid", new String[]
			{ "paymentModeCode" }, "The {0} is not valid.");
		}
	}

}
