/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.chineseprofileservices.sms.ChineseSmsService;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeStrategy;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link VerificationCodeStrategy}.
 */
public class ChineseVerificationCodeStrategy implements VerificationCodeStrategy
{
	private ChineseSmsService chineseSmsService;
	private static final String MOCK_VERIFICATION_CODE = "1234";

	@Override
	public String generate()
	{
		return MOCK_VERIFICATION_CODE;
	}

	@Override
	public void send(final String mobileNumber, final String code)
	{
		chineseSmsService.sendMsg(mobileNumber, code);
	}

	protected ChineseSmsService getChineseSmsService()
	{
		return chineseSmsService;
	}

	@Required
	public void setChineseSmsService(final ChineseSmsService chineseSmsService)
	{
		this.chineseSmsService = chineseSmsService;
	}
}
