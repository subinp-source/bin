/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationservices.service.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * 
 * @deprecated since 19.05 .
 */
@Deprecated(since = "1905", forRemoval= true )
@UnitTest
public class DefaultSmsChannelStrategyTest
{

	private DefaultSmsChannelStrategy smsChannelStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		smsChannelStrategy = new DefaultSmsChannelStrategy();
	}

	@Test
	public void testGetChannelValue()
	{
		final CustomerModel customer = new CustomerModel();

		Assert.assertEquals(StringUtils.EMPTY, smsChannelStrategy.getChannelValue(customer));
	}


}
