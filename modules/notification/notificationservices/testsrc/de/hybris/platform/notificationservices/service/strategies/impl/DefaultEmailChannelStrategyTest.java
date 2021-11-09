/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationservices.service.strategies.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultEmailChannelStrategyTest
{
	private DefaultEmailChannelStrategy emailStrategy;

	@Before
	public void setUp()
	{
		emailStrategy = new DefaultEmailChannelStrategy();
	}
	@Test
	public void testGetChannelValue()
	{
		final String testEmail = "test@hybris.com";
		final CustomerModel customer = mock(CustomerModel.class);
		customer.setCustomerID(testEmail);
		when(customer.getContactEmail()).thenReturn(testEmail);
		Assert.assertEquals(testEmail, emailStrategy.getChannelValue(customer));
	}

}
