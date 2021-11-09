/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * JUnit test suite for {@link DefaultCustomerAccountDao}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerAccountDaoTest
{
	private static final String FIND_ORDERS_ADDITIONAL_FILTER_ENABLED = "commerceservices.find.orders.additional.filter.enabled";
	private static final String FIND_ORDERS_ADDITIONAL_FILTER = " AND {parent} IS NULL";

	private DefaultCustomerAccountDao customerAccountDao;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Before
	public void setUp() throws Exception
	{
		customerAccountDao = new DefaultCustomerAccountDao();
		customerAccountDao.setConfigurationService(configurationService);
		when(configurationService.getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testGetFindOrdersAdditionalFilterWhenAdditionalFilterIsDisabled()
	{
		when(configurationService.getConfiguration().getBoolean(FIND_ORDERS_ADDITIONAL_FILTER_ENABLED, false)).thenReturn(false);
		assertTrue("Additional filter is not disabled", customerAccountDao.getFindOrdersAdditionalFilter().isEmpty());
	}

	@Test
	public void testGetFindOrdersAdditionalFilterWhenAdditionalFilterIsEnabled()
	{
 		when(configurationService.getConfiguration().getBoolean(FIND_ORDERS_ADDITIONAL_FILTER_ENABLED, false)).thenReturn(true);
		assertEquals("Additional filter is not enabled", FIND_ORDERS_ADDITIONAL_FILTER, customerAccountDao.getFindOrdersAdditionalFilter());
	}
	
	@Test(expected = ConversionException.class)
	public void testGetFindOrdersAdditionalFilterWhenAdditionalFilterConversionException()
	{
 		when(configurationService.getConfiguration().getBoolean(FIND_ORDERS_ADDITIONAL_FILTER_ENABLED, false)).thenThrow(new ConversionException());
		customerAccountDao.getFindOrdersAdditionalFilter();
	}
}