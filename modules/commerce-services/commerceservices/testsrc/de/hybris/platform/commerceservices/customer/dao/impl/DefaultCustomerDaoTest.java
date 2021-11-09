/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.customer.dao.CustomerDao;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit test suite for {@link DefaultCustomerDao}
 */
@IntegrationTest
public class DefaultCustomerDaoTest extends ServicelayerTest
{
	private static final String TEST_UID = "testcustomer1@test.com";
	private static final String TEST_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21";
	private static final String DUPLICATED_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-0324e1c32a22";
	private static final String UNKNOWN_CUSTOMER_ID = "6a2a41a3-c54c-4ce8-a2d2-111111111111";

	@Resource
	private CustomerDao customerDao;

	@Before
	public void setUp() throws Exception
	{

		createCoreData();
		createDefaultUsers();
		importCsv("/commerceservices/test/testCustomerMatching.impex", "utf-8");
	}

	@Test
	public void shouldGetCustomerByCustomerId()
	{
		final CustomerModel customer = customerDao.findCustomerByCustomerId(TEST_CUSTOMER_ID);
		Assert.assertNotNull(customer);
		Assert.assertEquals(customer.getUid(), TEST_UID);
		Assert.assertNotNull(customer.getCustomerID());
		Assert.assertEquals(customer.getCustomerID(), TEST_CUSTOMER_ID);
	}

	@Test
	public void shouldReturnNullForUnknownCustomerId()
	{
		final CustomerModel customer = customerDao.findCustomerByCustomerId(UNKNOWN_CUSTOMER_ID);
		Assert.assertNull(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfCustomerIdIsNull()
	{
		customerDao.findCustomerByCustomerId(null);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void shouldThrowExceptionIfCustomerIdIsDuplicated()
	{
		customerDao.findCustomerByCustomerId(DUPLICATED_CUSTOMER_ID);
	}
}
