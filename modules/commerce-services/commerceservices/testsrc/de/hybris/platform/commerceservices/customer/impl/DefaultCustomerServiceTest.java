/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.dao.CustomerDao;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * JUnit test suite for {@link DefaultCustomerService}
 */
@UnitTest
public class DefaultCustomerServiceTest
{

	private static final String TEST_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21";
	private static final String TEST_UID = "testcustomer1@test.com";

	private static final String DUPLICATED_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a22";
	private static final String UNKNOWN_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-111111111111";

	private DefaultCustomerService customerService;

	@Mock
	private CustomerDao customerDao;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		customerService = new DefaultCustomerService(customerDao,"^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
	}

	@Test
	public void shouldGetCustomerByCustomerId()
	{
		final CustomerModel testCustomer = mock(CustomerModel.class);
		given(testCustomer.getCustomerID()).willReturn(TEST_CUSTOMER_ID);
		given(testCustomer.getUid()).willReturn(TEST_UID);

		given(customerDao.findCustomerByCustomerId(TEST_CUSTOMER_ID)).willReturn(testCustomer);

		final UserModel customer = customerService.getCustomerByCustomerId(TEST_CUSTOMER_ID);
		Assert.assertEquals(testCustomer.getUid(), customer.getUid());
		verify(customerDao).findCustomerByCustomerId(TEST_CUSTOMER_ID);

	}

	@Test
	public void shouldReturnNullIfUnknownCustomerId()
	{
		given(customerDao.findCustomerByCustomerId(UNKNOWN_CUSTOMER_ID)).willReturn(null);
		final UserModel customer = customerService.getCustomerByCustomerId(UNKNOWN_CUSTOMER_ID);
		Assert.assertNull(customer);
		verify(customerDao).findCustomerByCustomerId(UNKNOWN_CUSTOMER_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfCustomerIdIsNull()
	{
		given(customerDao.findCustomerByCustomerId(null)).willThrow(IllegalArgumentException.class);
		customerService.getCustomerByCustomerId(null);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void shouldThrowExceptionIfCustomerIdIsDuplicated()
	{
		given(customerDao.findCustomerByCustomerId(DUPLICATED_CUSTOMER_ID)).willThrow(AmbiguousIdentifierException.class);
		customerService.getCustomerByCustomerId(DUPLICATED_CUSTOMER_ID);
	}

}
