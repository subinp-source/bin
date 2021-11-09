/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.customer;

import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Extended to support more function.
 */
public interface ChineseCustomerAccountService extends de.hybris.platform.commerceservices.customer.CustomerAccountService
{

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
	 *           the data that contains sending info.
	 */
	void sendVerificationCode(final VerificationData data);

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
	 * Updates customer's mobile number.
	 *
	 * @param customerModel
	 *           customer to be updated
	 */
	void updateMobileNumber(final CustomerModel customerModel);

	/**
	 * Finds a customer by using mobile number.
	 *
	 * @param mobileNumber
	 *           the number to query
	 * @return an Optional containing the customer, or an empty Optional if no customer is found.
	 */
	Optional<CustomerModel> getCustomerForMobileNumber(final String mobileNumber);


	/**
	 * Gets mobile number verification code.
	 *
	 * @param mobileNumber
	 *           the number to query
	 * @return an Optional containing the mobile number and verification code.
	 */
	Optional<MobileNumberVerificationModel> getMobileNumberVerificationCode(String mobileNumber);

	/**
	 * Checks whether the verification code is expired.
	 *
	 * @param date
	 *           the verification code generate time
	 * @return true if the verification code is expired
	 */
	boolean isVerificationCodeExpired(final Date date);

	/**
	 * Gets all expired verification codes.
	 *
	 * @param expiredDate
	 *           expired date
	 * @return all expired verification codes
	 */
	List<MobileNumberVerificationModel> findExpiredVerificationCode(Date expiredDate);

	/**
	 * Gets verification code expired date.
	 *
	 * @return verification code expired date
	 */
	Date getVerificationCodeExpiredDate();

	/**
	 * Gets the default email language.
	 * 
	 * @return default email language
	 */
	String getDefaultEmailLanguage();

}
