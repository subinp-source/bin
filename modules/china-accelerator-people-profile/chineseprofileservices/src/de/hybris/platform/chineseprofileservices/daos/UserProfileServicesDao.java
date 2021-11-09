/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.daos;

import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Handles items related to {@link MobileNumberVerificationModel}
 */
public interface UserProfileServicesDao
{
	/**
	 * Gets verification code by mobile number.
	 * 
	 * @param mobileNumber
	 *           mobile number
	 * @return verification code
	 */
	Optional<MobileNumberVerificationModel> findVerificationCodeByMobileNumber(String mobileNumber);

	/**
	 * Gets all expired verification codes.
	 * 
	 * @param expiredDate
	 *           expired date
	 * @return all expired verification codes
	 */
	List<MobileNumberVerificationModel> findExpiredVerificationCode(Date expiredDate);

}
