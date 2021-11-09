/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.chineseprofileservices.sms.ChineseSmsService;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeSendingStrategy;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeStrategy;

import org.springframework.beans.factory.annotation.Required;



/**
 * A demo implementation for MobileVerificationCodeSendingStrategy.
 * 
 * @deprecated Since 1905. Use {@link ChineseVerificationCodeStrategy} instead.
 * 
 */
@Deprecated(since = "1905", forRemoval= true )
public class ChineseVerificationCodeSendingStrategy implements VerificationCodeSendingStrategy
{

	private ChineseSmsService chineseSmsService;

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
