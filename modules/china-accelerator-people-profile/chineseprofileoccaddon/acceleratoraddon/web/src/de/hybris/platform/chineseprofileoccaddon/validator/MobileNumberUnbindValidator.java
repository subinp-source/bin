/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chineseprofileoccaddon.validator;

import de.hybris.platform.chineseprofilefacades.customer.ChineseCustomerFacade;
import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for unbinding mobile number.
 */
public class MobileNumberUnbindValidator implements Validator
{
	private final ChineseCustomerFacade chineseCustomerFacade;
	private static final String MOBILE_NUMBER = "mobileNumber";
	private static final String VERIFICATION_CODE = "verificationCode";


	public MobileNumberUnbindValidator(final ChineseCustomerFacade chineseCustomerFacade)
	{
		this.chineseCustomerFacade = chineseCustomerFacade;
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
		final String boundMobileNumber = chineseCustomerFacade.getCurrentCustomer().getMobileNumber();
		final String verificationCode = verificationData.getVerificationCode();
		if (StringUtils.isBlank(boundMobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.notBound");
			return;
		}

		final Optional<MobileNumberVerificationData> savedData = chineseCustomerFacade.getVerificationCode(boundMobileNumber);
		if (!savedData.isPresent())
		{
			errors.rejectValue(VERIFICATION_CODE, "verificationCode.notSend");
			return;
		}
		savedData.ifPresent(data -> {

			if (StringUtils.isBlank(verificationCode) || !verificationCode.equals(data.getVerificationCode()))
			{
				errors.rejectValue(VERIFICATION_CODE, "verificationCode.invalid");
				return;
			}

			if (chineseCustomerFacade.isVerificationCodeExpired(data.getCreationTime()))
			{
				chineseCustomerFacade.removeVerificationCode(boundMobileNumber);
				errors.rejectValue(VERIFICATION_CODE, "verificationCode.timeout");
				return;
			}
		});

	}

}
