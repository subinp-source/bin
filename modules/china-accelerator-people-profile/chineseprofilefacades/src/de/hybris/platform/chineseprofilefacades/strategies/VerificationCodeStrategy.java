/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.strategies;

import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;

import java.util.Optional;


/**
 * Provides a strategy that handles verification code.
 */
public interface VerificationCodeStrategy
{
	/**
	 * Saves mobile number verification code.
	 * 
	 * @param data
	 *           mobile number verificaion data
	 */
	void saveVerificationCode(MobileNumberVerificationData data);

	/**
	 * Removes verification code according to mobile number.
	 * 
	 * @param mobileNumber
	 *           mobile number to get the verification code
	 */
	void removeVerificationCode(String mobileNumber);

	/**
	 * Gets verification code by mobile number.
	 */
	Optional<MobileNumberVerificationData> getVerificationCode(String mobileNumber);

}
