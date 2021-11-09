/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies;

/**
 * A strategy that sending a verification code to customer's mobile.
 * 
 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
 */
@Deprecated(since = "1905", forRemoval= true )
public interface VerificationCodeSendingStrategy
{

	/**
	 * Send code to a mobile.
	 *
	 * @param mobileNumber
	 *           The target mobile number.
	 * @param code
	 *           Verification code.
	 */
	void send(final String mobileNumber, final String code);
}
