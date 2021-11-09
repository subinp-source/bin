/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * JUnit test suite for {@link CustomerIdMatchingStrategy}
 */
@UnitTest
public class CustomerIdMatchingStrategyTest
{

	private static final String CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a23";
	private static final String UNKNOWN_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a24";
	private static final String INVALID_CUSTOMER_ID = "6a2a41a3-c54c-abcd-a2d2-0324e1c32a24";
	private static final String CUSTOMER_UID = "testuser@test.com";

	private CustomerIdMatchingStrategy strategy;
	@Mock
	private CustomerService customerService;
	@Mock
	private CustomerModel customer;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new CustomerIdMatchingStrategy(customerService);
	}

	@Test
	public void shouldGetUserForValidCustomerIdProperty()
	{
		given(customer.getCustomerID()).willReturn(CUSTOMER_ID);
		given(customerService.isUUID(CUSTOMER_ID)).willReturn(Boolean.TRUE);
		given(customerService.getCustomerByCustomerId(CUSTOMER_ID)).willReturn(customer);
		final Optional<CustomerModel> userOpt = strategy.getUserByProperty(CUSTOMER_ID, CustomerModel.class);
		verify(customerService).isUUID(CUSTOMER_ID);
		verify(customerService).getCustomerByCustomerId(CUSTOMER_ID);
		Assert.assertNotNull(userOpt);
		Assert.assertTrue(userOpt.isPresent());
		Assert.assertEquals(CUSTOMER_ID, userOpt.get().getCustomerID());
	}

	@Test
	public void shouldGetUserForUserModelClass()
	{
		given(customer.getUid()).willReturn(CUSTOMER_UID);
		given(customerService.isUUID(CUSTOMER_ID)).willReturn(Boolean.TRUE);
		given(customerService.getCustomerByCustomerId(CUSTOMER_ID)).willReturn(customer);
		final Optional<UserModel> userOpt = strategy.getUserByProperty(CUSTOMER_ID, UserModel.class);
		verify(customerService).isUUID(CUSTOMER_ID);
		verify(customerService).getCustomerByCustomerId(CUSTOMER_ID);
		Assert.assertNotNull(userOpt);
		Assert.assertTrue(userOpt.isPresent());
		Assert.assertEquals(CUSTOMER_UID, userOpt.get().getUid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullProperty()
	{
		strategy.getUserByProperty(null, CustomerModel.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullClazz()
	{
		strategy.getUserByProperty(CUSTOMER_ID, null);
	}

	@Test
	public void shouldGetEmptyForUnknownCustomerId()
	{
		given(customerService.isUUID(UNKNOWN_CUSTOMER_ID)).willReturn(Boolean.TRUE);
		given(customerService.getCustomerByCustomerId(UNKNOWN_CUSTOMER_ID)).willReturn(null);
		final Optional<CustomerModel> userOpt = strategy.getUserByProperty(UNKNOWN_CUSTOMER_ID, CustomerModel.class);
		verify(customerService).isUUID(UNKNOWN_CUSTOMER_ID);
		verify(customerService).getCustomerByCustomerId(UNKNOWN_CUSTOMER_ID);
		Assert.assertNotNull(userOpt);
		Assert.assertFalse(userOpt.isPresent());
	}


	@Test
	public void shouldGetEmptyForInvalidCustomerIdProperty()
	{
		given(customerService.isUUID(INVALID_CUSTOMER_ID)).willReturn(Boolean.FALSE);
		final Optional<CustomerModel> userOpt = strategy.getUserByProperty(INVALID_CUSTOMER_ID, CustomerModel.class);
		verify(customerService).isUUID(INVALID_CUSTOMER_ID);
		verify(customerService, never()).getCustomerByCustomerId(anyString());
		Assert.assertNotNull(userOpt);
		Assert.assertFalse(userOpt.isPresent());
	}
}
