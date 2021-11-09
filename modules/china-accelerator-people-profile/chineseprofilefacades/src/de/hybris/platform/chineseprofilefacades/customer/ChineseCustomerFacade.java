/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.customer;

import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;
import de.hybris.platform.chineseprofileservices.data.VerificationData;

import java.util.Date;
import java.util.Optional;


/**
 * Customer facade interface. Its main purpose is to retrieve chinese customer related DTOs using existing services.
 */
public interface ChineseCustomerFacade extends de.hybris.platform.commercefacades.customer.CustomerFacade
{
	/**
	 * Saves email language for current user.
	 *
	 * @param languageISO
	 *           email language
	 */
	void saveEmailLanguageForCurrentUser(String languageISO);

	/**
	 * Generates a verification code.
	 *
	 * @return Verification Code
	 */
	String generateVerificationCode();

	/**
	 * Sends verification code.
	 *
	 * @param data
	 *           verification data
	 */
	void sendVerificationCode(VerificationData data);

	/**
	 * Sends verification code.
	 *
	 * @param mobileNumber
	 *           the mobile number to send the verification code
	 * @param verificationCode
	 *           the verification code for the mobile number
	 * 
	 */
	void sendVerificationCode(final String mobileNumber, final String verificationCode);

	/**
	 * Saves code in session.
	 *
	 * @param data
	 *           the value will be store in session.
	 * @param name
	 *           name of key in session.
	 */
	void saveVerificationCodeInSession(VerificationData data, String name);

	/**
	 * Removes verification code from session.
	 *
	 * @param name
	 *           name of key in session.
	 */
	void removeVerificationCodeFromSession(String name);

	/**
	 * Sets customer's mobile number.
	 *
	 * @param data
	 *           the data contains mobile number.
	 */
	void saveMobileNumber(VerificationData data);

	/**
	 * Gets verification code's timeout.
	 *
	 * @param key
	 *           The configuration key.
	 * @return timeout in seconds
	 */
	int getVerificationCodeTimeout(String key);


	/**
	 * Checks whether the mobile number has been registered.
	 *
	 * @param mobileNumber
	 *           mobile number
	 * @return return true if the mobile number is not registered, false otherwise
	 */
	boolean isMobileNumberUnique(String mobileNumber);

	/**
	 * Unbinds the mobile for customer.
	 */
	void unbindMobileNumber();

	/**
	 * Saves customer with email language.
	 * 
	 * @param languageISO
	 *           email language iso code
	 */
	void saveCurrentUserWithEmailLanguage(String languageISO);

	/**
	 * Saves verification code.
	 *
	 * @param data
	 *           the value will be stored.
	 */
	void saveVerificationCode(MobileNumberVerificationData data);

	/**
	 * Gets verification code for mobile number.
	 * 
	 * @param mobileNumber
	 *           mobile number to get verification code
	 * @return verification code
	 */
	Optional<MobileNumberVerificationData> getVerificationCode(String mobileNumber);

	/**
	 * Removes mobile number verification code.
	 * 
	 * @param mobileNumber
	 *           mobile number to get verification code
	 */
	void removeVerificationCode(final String mobileNumber);

	/**
	 * Saves mobile number to customer.
	 * 
	 * @param mobileNumber
	 *           mobile number to be saved
	 */
	void saveMobileNumber(final String mobileNumber);

	/**
	 * Checks whether verification code is expired.
	 * 
	 * @param date
	 *           current time
	 * @return true if verification code is expired
	 */
	boolean isVerificationCodeExpired(final Date date);
}
