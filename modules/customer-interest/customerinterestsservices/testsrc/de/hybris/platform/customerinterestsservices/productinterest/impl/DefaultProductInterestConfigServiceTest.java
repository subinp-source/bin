/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.customerinterestsservices.productinterest.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


/**
 * Test cases for {@DefaultProductInterestConfigService}
 */
@UnitTest
public class DefaultProductInterestConfigServiceTest
{

	@Spy
	private final DefaultProductInterestConfigService productInterestConfigService = new DefaultProductInterestConfigService();
	private static final int CONFIGURED_EXPIRY_DAY = 30;
	private static final int DEFAULT_EXPIRY_DAY = 90;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetProductInterestExpiryDay()
	{
		doReturn(CONFIGURED_EXPIRY_DAY).when(productInterestConfigService).getConfiguredExpiryDay();
		final int expiryDay = productInterestConfigService.getProductInterestExpiryDay();
		assertEquals(CONFIGURED_EXPIRY_DAY, expiryDay);
	}

	@Test
	public void testGetProductInterestExpiryDayWithWrongConfig()
	{
		doThrow(new NumberFormatException()).when(productInterestConfigService).getConfiguredExpiryDay();
		final int expiryDay = productInterestConfigService.getProductInterestExpiryDay();
		assertEquals(DEFAULT_EXPIRY_DAY, expiryDay);
	}

	@Test
	public void testGetProductInterestExpiryDate()
	{
		doReturn(CONFIGURED_EXPIRY_DAY).when(productInterestConfigService).getConfiguredExpiryDay();
		final Date now = new Date();
		final Date expiryDate = productInterestConfigService.getProductInterestExpiryDate(now);
		assertEquals(new DateTime(now).plusDays(CONFIGURED_EXPIRY_DAY).toDate(), expiryDate);
	}

}
