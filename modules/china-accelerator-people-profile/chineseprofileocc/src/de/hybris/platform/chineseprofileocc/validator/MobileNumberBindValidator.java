/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chineseprofileocc.validator;

import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.chineseprofilefacades.customer.ChineseCustomerFacade;
import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for binding mobile number.
 */
public class MobileNumberBindValidator implements Validator
{
	private final ChineseCustomerFacade chineseCustomerFacade;
	private final AddressFacade chineseAddressFacade;

	private static final String MOBILE_NUMBER = "mobileNumber";
	private static final String VERIFICATION_CODE = "verificationCode";



	public MobileNumberBindValidator(final ChineseCustomerFacade chineseCustomerFacade,
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
		final String verificationCode = verificationData.getVerificationCode();
		if (StringUtils.isBlank(mobileNumber) || chineseAddressFacade.isInvalidCellphone(mobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.invalid");
			return;
		}

		if (StringUtils.isNotBlank(chineseCustomerFacade.getCurrentCustomer().getMobileNumber()))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.alreadyBound");
			return;
		}

		if (!chineseCustomerFacade.isMobileNumberUnique(mobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.registered");
			return;
		}

		final Optional<MobileNumberVerificationData> savedData = chineseCustomerFacade.getVerificationCode(mobileNumber);

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
				chineseCustomerFacade.removeVerificationCode(mobileNumber);
				errors.rejectValue(VERIFICATION_CODE, "verificationCode.timeout");
				return;
			}
		});

	}

}
