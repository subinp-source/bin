/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.customerlocation.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorfacades.customerlocation.CustomerLocationFacade;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorservices.customer.impl.DefaultCustomerLocationService;
import de.hybris.platform.acceleratorservices.store.data.UserLocationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


@UnitTest
public class DefaultCustomerLocationFacadeTest
{

	private static final String SEARCH_TEXT = "searchText";
	private static final String BLANK_STRING = "";

	@InjectMocks
	private final CustomerLocationFacade customerLocationFacade = new DefaultCustomerLocationFacade();


	@Mock
	private final CustomerLocationService customerLocationService = new DefaultCustomerLocationService();

	@Spy
	private final UserLocationData userLocationData = new UserLocationData();


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testNullSearchText()
	{
		userLocationData.setSearchTerm(null);
		customerLocationFacade.setUserLocationData(userLocationData);
		verify(userLocationData, times(1)).setSearchTerm(BLANK_STRING);
	}

	@Test
	public void testEmptySearchText()
	{
		userLocationData.setSearchTerm(" ");
		customerLocationFacade.setUserLocationData(userLocationData);
		verify(userLocationData, times(1)).setSearchTerm(BLANK_STRING);
	}

	@Test
	public void testSetPreferredStoreLocation()
	{
		userLocationData.setSearchTerm(SEARCH_TEXT);
		customerLocationFacade.setUserLocationData(userLocationData);
		verify(userLocationData, times(2)).setSearchTerm(SEARCH_TEXT);
	}
}
