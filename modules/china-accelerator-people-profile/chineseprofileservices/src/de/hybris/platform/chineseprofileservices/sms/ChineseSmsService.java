/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.sms;

/**
 * SMS service.
 */
public interface ChineseSmsService
{

	/**
	 * Send a message to a mobile.
	 *
	 * @param mobileNumber
	 *           The target mobile number.
	 * @param msg
	 *           The message to be sent.
	 */
	void sendMsg(final String mobileNumber, final String msg);
}
