/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofilefacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@UnitTest
public class MobileNumberVerificationPopulatorTest
{
	private MobileNumberVerificationModel source;

	private MobileNumberVerificationData target;

	private MobileNumberVerificationPopulator populator;

	private static final String MOBILE_NUMBER = "13800000000";
	private static final String VERIFICATION_CODE = "1234";

	private static final Date date = new Date();

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		source = new MobileNumberVerificationModel();
		source.setMobileNumber(MOBILE_NUMBER);
		source.setVerificationCode(VERIFICATION_CODE);

		target = new MobileNumberVerificationData();
		populator = new MobileNumberVerificationPopulator();
	}

	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getMobileNumber(), MOBILE_NUMBER);
		Assert.assertEquals(target.getVerificationCode(), VERIFICATION_CODE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullSource()
	{
		source = null;
		populator.populate(source, target);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTarget()
	{
		target = null;
		populator.populate(source, target);
	}

}
