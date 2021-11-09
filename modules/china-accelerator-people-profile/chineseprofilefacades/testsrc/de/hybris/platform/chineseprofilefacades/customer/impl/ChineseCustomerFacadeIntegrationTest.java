/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.customer.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;
import de.hybris.platform.chineseprofilefacades.strategies.VerificationCodeStrategy;
import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.UserDao;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ChineseCustomerFacadeIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource(name = "chineseCustomerFacade")
	private DefaultChineseCustomerFacade customerFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "chineseCustomerAccountService")
	private ChineseCustomerAccountService chineseCustomerAccountService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "chineseUserDao")
	private UserDao userDao;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "verificationCodeSavingStrategy")
	private VerificationCodeStrategy verificationCodeSavingStrategy;

	private VerificationData verificationData;

	private final String key = "VerificationData";

	private MobileNumberVerificationData mobileVerificationData;

	private static final String MOBILE_NUMBER = "13800000000";
	private static final String VERIFICATION_CODE = "1234";

	@Before
	public void prepare()
	{

		verificationData = new VerificationData();
		verificationData.setMobileNumber("13512345678");
		verificationData.setTime(new Date());
		verificationData.setVerificationCode("7485");

		final CustomerModel customer = new CustomerModel();
		customer.setLoginDisabled(false);
		customer.setUid("test@gmail.com");
		customer.setMobileNumber("12345678910");
		customer.setEncodedPassword("123456123456");
		modelService.save(customer);

		userService.setCurrentUser(customer);

		mobileVerificationData = new MobileNumberVerificationData();
		mobileVerificationData.setMobileNumber(MOBILE_NUMBER);
		mobileVerificationData.setVerificationCode(VERIFICATION_CODE);
	}

	@Test
	public void test_generateVerificationCode()
	{
		final String code = customerFacade.generateVerificationCode();
		Assert.assertEquals(4, code.length());
	}

	@Test
	public void test_saveVerificationCode()
	{
		customerFacade.saveVerificationCodeInSession(verificationData, key);
		final VerificationData data = sessionService.getAttribute(key);
		Assert.assertEquals(verificationData, data);
	}

	@Test
	public void test_removeVerificationCode()
	{
		customerFacade.removeVerificationCodeFromSession(key);
		final VerificationData data = sessionService.getAttribute(key);
		Assert.assertNull(data);
	}

	@Test
	public void test_getVerificationCodeTimeout()
	{
		final int val = customerFacade.getVerificationCodeTimeout("verification.code.time.out");
		Assert.assertEquals(90, val);
	}

	@Test
	public void test_isMobileNumberUnique()
	{
		final boolean flag = customerFacade.isMobileNumberUnique("13512345678");
		Assert.assertEquals(true, flag);
	}

	@Test
	public void testSaveEmailLanguage()
	{
		customerFacade.saveCurrentUserWithEmailLanguage("ja");
		Assert.assertEquals("ja", customerFacade.getCurrentCustomer().getEmailLanguage());
	}

	@Test
	public void testSaveGetVerificationCode()
	{
		customerFacade.saveVerificationCode(mobileVerificationData);
		final Optional<MobileNumberVerificationData> savedData = customerFacade.getVerificationCode(MOBILE_NUMBER);
		Assert.assertEquals(MOBILE_NUMBER, savedData.get().getMobileNumber());
		Assert.assertEquals(VERIFICATION_CODE, savedData.get().getVerificationCode());
	}


	@Test
	public void testRemoveVerificationCode()
	{
		customerFacade.saveVerificationCode(mobileVerificationData);
		customerFacade.removeVerificationCode(MOBILE_NUMBER);
		final Optional<MobileNumberVerificationData> savedData = customerFacade.getVerificationCode(MOBILE_NUMBER);
		Assert.assertEquals(Optional.empty(), savedData);

	}

}
