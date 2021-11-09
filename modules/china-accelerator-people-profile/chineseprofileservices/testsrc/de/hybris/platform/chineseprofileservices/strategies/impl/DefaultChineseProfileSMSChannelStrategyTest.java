/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DefaultChineseProfileSMSChannelStrategyTest
{
	private DefaultChineseProfileSMSChannelStrategy smsChannelStrategy;
	private static final String TEST_MOBILE_NUMBER = "15900000000";


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		smsChannelStrategy = new DefaultChineseProfileSMSChannelStrategy();
	}

	@Test
	public void testGetChannelValueWithMobileNumber()
	{
		final CustomerModel customer = new CustomerModel();
		customer.setMobileNumber(TEST_MOBILE_NUMBER);

		Assert.assertEquals(TEST_MOBILE_NUMBER, smsChannelStrategy.getChannelValue(customer));
	}

	@Test
	public void testGetChannelValueWithoutMobileNumber()
	{
		final CustomerModel customer = new CustomerModel();
		Assert.assertNull(smsChannelStrategy.getChannelValue(customer));

	}

}
