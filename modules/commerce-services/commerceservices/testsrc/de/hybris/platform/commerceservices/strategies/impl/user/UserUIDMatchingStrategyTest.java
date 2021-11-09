/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * JUnit test suite for {@link UserUIDMatchingStrategy}
 */
@UnitTest
public class UserUIDMatchingStrategyTest
{

	private static final String USER_ID = "testuser@test.com";
	private static final String UNKNOWN_USER_ID = "unknownuser@test.com";

	private UserUIDMatchingStrategy strategy;
	@Mock
	private UserService userService;
	@Mock
	private UserModel user;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new UserUIDMatchingStrategy(userService);
	}

	@Test
	public void shouldGetUserForValidUidProperty()
	{
		given(user.getUid()).willReturn(USER_ID);
		given(userService.getUserForUID(USER_ID, UserModel.class)).willReturn(user);
		final Optional<UserModel> userOpt = strategy.getUserByProperty(USER_ID, UserModel.class);
		verify(userService).getUserForUID(USER_ID, UserModel.class);
		Assert.assertNotNull(userOpt);
		Assert.assertTrue(userOpt.isPresent());
		Assert.assertEquals(USER_ID, userOpt.get().getUid());
	}

	@Test
	public void shouldGetCustomerForValidUidProperty()
	{
		final CustomerModel customer = mock(CustomerModel.class);
		given(customer.getUid()).willReturn(USER_ID);
		given(userService.getUserForUID(USER_ID, CustomerModel.class)).willReturn(customer);
		final Optional<CustomerModel> userOpt = strategy.getUserByProperty(USER_ID, CustomerModel.class);
		verify(userService).getUserForUID(USER_ID, CustomerModel.class);
		Assert.assertNotNull(userOpt);
		Assert.assertTrue(userOpt.isPresent());
		Assert.assertEquals(USER_ID, userOpt.get().getUid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullProperty()
	{
		strategy.getUserByProperty(null, UserModel.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNullClazz()
	{
		strategy.getUserByProperty(USER_ID, null);
	}

	@Test
	public void shouldGetEmptyForUnknownUid()
	{
		given(userService.getUserForUID(UNKNOWN_USER_ID, UserModel.class)).willThrow(UnknownIdentifierException.class);
		final Optional<UserModel> userOpt = strategy.getUserByProperty(UNKNOWN_USER_ID, UserModel.class);
		verify(userService).getUserForUID(UNKNOWN_USER_ID, UserModel.class);
		Assert.assertNotNull(userOpt);
		Assert.assertFalse(userOpt.isPresent());
	}
}
