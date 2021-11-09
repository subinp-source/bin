/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.forms.validation;

import de.hybris.platform.chineseprofile.constants.WebConstants;
import de.hybris.platform.chineseprofile.forms.VerificationCodeForm;
import de.hybris.platform.chineseprofilefacades.customer.ChineseCustomerFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Verify the mobile number before binding and unbinding.
 */
@Component("mobileNumberValidator")
public class MobileNumberValidator implements Validator
{

	private static final String TIMEOUT_MESSAGE_KEY = "register.verificationCode.timeout";
	private static final String INVALID_MESSAGE_KEY = "register.verificationCode.invalid";
	private static final String VERIFICATION_CODE = "verificationCode";
	private static final String MOBILE_NUMBER = "mobileNumber";

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "chineseCustomerFacade")
	private ChineseCustomerFacade customerFacade;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return VerificationCodeForm.class == clazz;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final VerificationCodeForm form = (VerificationCodeForm) object;
		final String verificationCode = form.getVerificationCode();
		final String mobileNumber = form.getMobileNumber();
		final String codeType = form.getCodeType();

		if (CodeType.BINDING.getType().equals(codeType))
		{
			validateMobileNumber(mobileNumber, errors);
		}
		validateVerificationCode(verificationCode, WebConstants.VERIFICATION_CODE_FOR_MOBILE_BINDING, errors);
	}

	/**
	 * Verify the binding mobile number before saving.
	 */
	protected void validateMobileNumber(final String mobileNumber, final Errors errors)
	{
		if (StringUtils.isBlank(mobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.invalid");
			return;
		}

		if (!customerFacade.isMobileNumberUnique(mobileNumber))
		{
			errors.rejectValue(MOBILE_NUMBER, "register.mobileNumber.registered");
		}
	}

	/**
	 * Verify the verification code before saving.
	 */
	protected void validateVerificationCode(final String verificationCode, final String key, final Errors errors)
	{
		final VerificationData verification = sessionService.getAttribute(key);
		if (ObjectUtils.isEmpty(verification) || StringUtils.isBlank(verificationCode)
				|| !verificationCode.equals(verification.getVerificationCode()))
		{
			errors.rejectValue(VERIFICATION_CODE, INVALID_MESSAGE_KEY);
			return;
		}

		if (isVerificationCodeExpired(verification.getTime()))
		{
			customerFacade.removeVerificationCodeFromSession(key);
			errors.rejectValue(VERIFICATION_CODE, TIMEOUT_MESSAGE_KEY);
		}
	}

	/**
	 * Check the verification code is expired.
	 *
	 * @param date
	 *           The time when verification code was creating.
	 * @return whether is expired.
	 */
	protected boolean isVerificationCodeExpired(final Date date)
	{
		final Date currentTime = new Date();
		final long timeout = customerFacade.getVerificationCodeTimeout(WebConstants.VERIFICATION_CODE_OUT_TIME_KEY) * 1000;
		return date.getTime() + timeout < currentTime.getTime();
	}

	private enum CodeType
	{
		BINDING("binding"), UNBINDING("unbinding");

		private final String type;

		private CodeType(final String type)
		{
			this.type = type;
		}

		public String getType()
		{
			return type;
		}
	}
}
