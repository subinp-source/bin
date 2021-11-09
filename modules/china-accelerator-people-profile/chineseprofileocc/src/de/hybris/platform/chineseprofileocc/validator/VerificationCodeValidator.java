/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileocc.validator;

import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.chineseprofilefacades.customer.ChineseCustomerFacade;
import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates verification code.
 */
public class VerificationCodeValidator implements Validator
{

	private final ChineseCustomerFacade chineseCustomerFacade;
	private final AddressFacade chineseAddressFacade;

	private static final String MOBILE_NUMBER = "mobileNumber";

	public VerificationCodeValidator(final ChineseCustomerFacade chineseCustomerFacade,
			final AddressFacade chineseAddressFacade)
	{

		this.chineseCustomerFacade = chineseCustomerFacade;
		this.chineseAddressFacade = chineseAddressFacade;
	}


	@Override
	public boolean supports(final Class<?> clazz)
	{
		return MobileNumberVerificationData.class == (clazz);
	}


	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final MobileNumberVerificationData verificationData = (MobileNumberVerificationData) obj;
		Assert.notNull(errors, "Errors object must not be null.");

		final String mobileNumber = verificationData.getMobileNumber();
		if (StringUtils.isBlank(mobileNumber) || chineseAddressFacade.isInvalidCellphone(mobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.invalid");
			return;
		}
		chineseCustomerFacade.getVerificationCode(mobileNumber).ifPresent(data -> {
			if (!chineseCustomerFacade.isVerificationCodeExpired(data.getCreationTime()))
			{
				errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.binding");
				return;
			}

		});

	}



}
