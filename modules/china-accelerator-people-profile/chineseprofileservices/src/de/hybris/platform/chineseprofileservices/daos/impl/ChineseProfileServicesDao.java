/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chineseprofileservices.daos.UserProfileServicesDao;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public class ChineseProfileServicesDao extends DefaultGenericDao implements UserProfileServicesDao
{

	private static final String FIND_VERIFICATION_CODE_FOR_MOBILE_NUMBER = "SELECT {" + MobileNumberVerificationModel.PK
			+ "} FROM {" + MobileNumberVerificationModel._TYPECODE + " as m} " + " WHERE " + "{m.mobileNumber} =?mobileNumber";

	private static final String FIND_VERIFICATION_CODE_FOR_EXPIRED = "SELECT {" + MobileNumberVerificationModel.PK + "} FROM {"
			+ MobileNumberVerificationModel._TYPECODE + " as m} " + " WHERE " + "{m.creationtime} <?date";

	public ChineseProfileServicesDao()
	{
		super(MobileNumberVerificationModel._TYPECODE);
	}


	@Override
	public Optional<MobileNumberVerificationModel> findVerificationCodeByMobileNumber(final String mobileNumber)
	{
		validateParameterNotNull(mobileNumber, "Mobile number must not be null.");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_VERIFICATION_CODE_FOR_MOBILE_NUMBER);
		query.addQueryParameter("mobileNumber", mobileNumber);

		final List<MobileNumberVerificationModel> verificationModels = getFlexibleSearchService()
				.<MobileNumberVerificationModel> search(query).getResult();

		if (verificationModels != null && !verificationModels.isEmpty())
		{
			return Optional.of(verificationModels.get(0));
		}

		return Optional.empty();
	}


	@Override
	public List<MobileNumberVerificationModel> findExpiredVerificationCode(final Date expiredDate)
	{

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_VERIFICATION_CODE_FOR_EXPIRED);

		query.addQueryParameter("date", expiredDate);

		final List<MobileNumberVerificationModel> verificationModels = getFlexibleSearchService()
				.<MobileNumberVerificationModel> search(query).getResult();

		return verificationModels;
	}


}
