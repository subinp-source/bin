/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import static org.mockito.BDDMockito.given;

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

/**
 * JUnit test suite for {@link AlwaysNegativeUserPropertyMatchingStrategy}
 */
@UnitTest
public class AlwaysNegativeUserPropertyMatchingStrategyTest
{
	private static final String CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a23";
	private AlwaysNegativeUserPropertyMatchingStrategy strategy;
	@Mock
	private CustomerModel customer;
	@Mock
	private CustomerService customerService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new AlwaysNegativeUserPropertyMatchingStrategy();
	}

	@Test
	public void getCustomerByPropertyShouldReturnEmptyOptional()
	{
		given(customerService.getCustomerByCustomerId(CUSTOMER_ID)).willReturn(customer);

		final Optional<CustomerModel> userOptional = strategy.getUserByProperty(CUSTOMER_ID, CustomerModel.class);
		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test
	public void getUserByPropertyShouldReturnEmptyOptional()
	{
		given(customerService.getCustomerByCustomerId(CUSTOMER_ID)).willReturn(customer);

		final Optional<UserModel> userOptional = strategy.getUserByProperty(CUSTOMER_ID, UserModel.class);
		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullProperty()
	{
		strategy.getUserByProperty(null, UserModel.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullClazz()
	{
		strategy.getUserByProperty(CUSTOMER_ID, null);
	}
	
}
