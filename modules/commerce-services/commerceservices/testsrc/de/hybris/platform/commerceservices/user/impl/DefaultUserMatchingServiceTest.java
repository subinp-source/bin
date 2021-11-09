/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.user.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.strategies.UserPropertyMatchingStrategy;
import de.hybris.platform.commerceservices.strategies.impl.user.AlwaysNegativeUserPropertyMatchingStrategy;
import de.hybris.platform.commerceservices.strategies.impl.user.CustomerIdMatchingStrategy;
import de.hybris.platform.commerceservices.strategies.impl.user.UserUIDMatchingStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@UnitTest
public class DefaultUserMatchingServiceTest
{

	private static final String TEST1_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21";
	private static final String TEST1_UID = "testcustomer_1@test.com";
	private static final String TEST2_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a22";
	private static final String TEST2_UID = "testcustomer_2@test.com";
	private static final String TEST_EXT_CUSTOMER_ID = "0000000158";

	@Mock
	private CustomerIdMatchingStrategy customerIdMatchingStrategy;
	@Mock
	private UserUIDMatchingStrategy userMatchingStrategy;
	@Mock
	private AlwaysNegativeUserPropertyMatchingStrategy externalUserMatchingStrategy;

	private DefaultUserMatchingService userMatchingService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final CustomerModel customer1 = mock(CustomerModel.class);
		final CustomerModel customer2 = mock(CustomerModel.class);

		given(customer1.getCustomerID()).willReturn(TEST1_CUSTOMER_ID);
		given(customer1.getUid()).willReturn(TEST1_UID);
		given(customer2.getCustomerID()).willReturn(TEST2_CUSTOMER_ID);
		given(customer2.getUid()).willReturn(TEST2_UID);
		
		given(customerIdMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.of(customer1));
		given(customerIdMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, UserModel.class)).willReturn(Optional.of(customer1));
		given(customerIdMatchingStrategy.getUserByProperty(TEST2_UID, CustomerModel.class)).willReturn(Optional.empty());
		given(customerIdMatchingStrategy.getUserByProperty(TEST2_UID, UserModel.class)).willReturn(Optional.empty());
		given(customerIdMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.empty());
		given(customerIdMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, UserModel.class)).willReturn(Optional.empty());

		given(userMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.empty());
		given(userMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, UserModel.class)).willReturn(Optional.empty());
		given(userMatchingStrategy.getUserByProperty(TEST2_UID, CustomerModel.class)).willReturn(Optional.of(customer2));
		given(userMatchingStrategy.getUserByProperty(TEST2_UID, UserModel.class)).willReturn(Optional.of(customer2));
		given(userMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.empty());
		given(userMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, UserModel.class)).willReturn(Optional.empty());

		given(externalUserMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.empty());
		given(externalUserMatchingStrategy.getUserByProperty(TEST1_CUSTOMER_ID, UserModel.class)).willReturn(Optional.empty());
		given(externalUserMatchingStrategy.getUserByProperty(TEST2_UID, CustomerModel.class)).willReturn(Optional.empty());
		given(externalUserMatchingStrategy.getUserByProperty(TEST2_UID, UserModel.class)).willReturn(Optional.empty());
		given(externalUserMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, CustomerModel.class)).willReturn(Optional.empty());
		given(externalUserMatchingStrategy.getUserByProperty(TEST_EXT_CUSTOMER_ID, UserModel.class)).willReturn(Optional.empty());

		final List<UserPropertyMatchingStrategy> matchingStrategies = Lists.<UserPropertyMatchingStrategy>newArrayList(customerIdMatchingStrategy, userMatchingStrategy,
				externalUserMatchingStrategy);

		userMatchingService = new DefaultUserMatchingService();
		userMatchingService.setMatchingStrategies(matchingStrategies);
	}

	@Test
	public void shouldGetUserByUid()
	{
		final UserModel user = userMatchingService.getUserByProperty(TEST2_UID, UserModel.class);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST2_UID, user.getUid());
	}

	@Test
	public void shouldGetUserByCustomerId()
	{
		final UserModel user = userMatchingService.getUserByProperty(TEST1_CUSTOMER_ID, UserModel.class);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST1_UID, user.getUid());
	}

	@Test
	public void shouldGetCustomerByUid()
	{
		final CustomerModel user = userMatchingService.getUserByProperty(TEST2_UID, CustomerModel.class);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST2_CUSTOMER_ID, user.getCustomerID());
	}

	@Test
	public void shouldGetCustomerByCustomerId()
	{
		final CustomerModel user = userMatchingService.getUserByProperty(TEST1_CUSTOMER_ID, CustomerModel.class);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST1_UID, user.getUid());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionForExternalCustomerId()
	{
		userMatchingService.getUserByProperty(TEST_EXT_CUSTOMER_ID, CustomerModel.class);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfNoStrategies()
	{
		final DefaultUserMatchingService userMatchingService = new DefaultUserMatchingService();
		userMatchingService.setMatchingStrategies(Lists.emptyList());
		userMatchingService.getUserByProperty(TEST1_CUSTOMER_ID, CustomerModel.class);
	}

}
