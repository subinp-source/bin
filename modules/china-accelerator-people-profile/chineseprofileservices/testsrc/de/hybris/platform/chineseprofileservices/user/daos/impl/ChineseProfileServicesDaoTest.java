/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.user.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.chineseprofileservices.daos.impl.ChineseProfileServicesDao;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ChineseProfileServicesDaoTest extends ServicelayerTransactionalTest
{
	@Resource(name = "userProfileDao")
	private ChineseProfileServicesDao chineseProfileDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	private static final String MOBILE_NUMBER = "13800000001";
	private static final String VERIFICATION_CODE = "1234";

	private static final String MOBILE_NUMBER_EXPIRED = "13812345678";

	private static final Long EXPIRED_TIME = 24 * 60 * 60 * 1000L;

	@Before
	public void prepare()
	{
	}


	@Test
	public void testFindVerificationCodeByMobileNumber()
	{
		final MobileNumberVerificationModel model = new MobileNumberVerificationModel();
		model.setMobileNumber(MOBILE_NUMBER);
		model.setVerificationCode(VERIFICATION_CODE);
		modelService.save(model);
		
		final Optional<MobileNumberVerificationModel> code = chineseProfileDao.findVerificationCodeByMobileNumber(MOBILE_NUMBER);
		assertEquals(MOBILE_NUMBER, code.get().getMobileNumber());
		assertEquals(VERIFICATION_CODE, code.get().getVerificationCode());
	}


	@Test
	public void testFindExpiredVerificationCode()
	{
		final MobileNumberVerificationModel expiredModel = new MobileNumberVerificationModel();
		expiredModel.setMobileNumber(MOBILE_NUMBER_EXPIRED);
		expiredModel.setVerificationCode(VERIFICATION_CODE);
		expiredModel.setCreationtime(new Date(System.currentTimeMillis() - EXPIRED_TIME));
		modelService.save(expiredModel);
		final List<MobileNumberVerificationModel> codes = chineseProfileDao.findExpiredVerificationCode(new Date());
		assertTrue(codes.size() >= 1);
		assertEquals(MOBILE_NUMBER_EXPIRED, codes.get(0).getMobileNumber());
	}

}
