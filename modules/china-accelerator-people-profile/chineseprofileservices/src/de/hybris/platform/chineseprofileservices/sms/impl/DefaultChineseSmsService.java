/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.sms.impl;

import de.hybris.platform.chineseprofileservices.sms.ChineseSmsService;

import org.apache.log4j.Logger;


/**
 * Default implementation.
 */
public class DefaultChineseSmsService implements ChineseSmsService
{

	private static final Logger LOGGER = Logger.getLogger(DefaultChineseSmsService.class);

	/**
	 * Here is an empty implementation.
	 */
	@Override
	public void sendMsg(final String mobileNumber, final String msg)
	{
		if (LOGGER.isInfoEnabled())
		{
			LOGGER.info("Mobile Number : " + mobileNumber);
			LOGGER.info("Message : " + msg);
			LOGGER.info("Message send success!");
		}
	}

}
