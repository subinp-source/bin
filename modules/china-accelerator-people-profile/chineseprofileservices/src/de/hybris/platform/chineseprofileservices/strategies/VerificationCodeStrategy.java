/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies;

/**
 * Provides a strategy that handles verification code generating and sending.
 */
public interface VerificationCodeStrategy
{

	/**
	 * Generates a verification code.
	 *
	 * @return verification code
	 */
	String generate();

	/**
	 * Sends code to a mobile.
	 *
	 * @param mobileNumber
	 *           The target mobile number.
	 * @param code
	 *           Verification code.
	 */
	void send(final String mobileNumber, final String code);

}
