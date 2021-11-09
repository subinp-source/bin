/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.productinterest.retention.hook;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.customerinterestsservices.retention.hook.CustomerInterestsCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link CustomerInterestsCleanupHook}
 */
@UnitTest
public class CustomerInterestsCleanupHookTest
{
	private CustomerInterestsCleanupHook customerInterestsCleanupHook;
	@Mock
	private ModelService modelService;
	@Mock
	private Collection<ProductInterestModel> interests;
	@Mock
	private CustomerModel customer;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		customerInterestsCleanupHook = new CustomerInterestsCleanupHook();
		customerInterestsCleanupHook.setModelService(modelService);
		Mockito.when(customer.getProductInterests()).thenReturn(interests);
		Mockito.doNothing().when(modelService).removeAll(interests);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCleanupRelatedObjects_param_null()
	{
		customerInterestsCleanupHook.cleanupRelatedObjects(null);
	}

	@Test
	public void testCleanupRelatedObjects_interests_Empty()
	{
		Mockito.when(customer.getProductInterests()).thenReturn(Collections.emptyList());
		customerInterestsCleanupHook.cleanupRelatedObjects(customer);
		Mockito.verify(modelService, Mockito.times(0)).removeAll(interests);
	}

	@Test
	public void testCleanupRelatedObjects()
	{
		customerInterestsCleanupHook.cleanupRelatedObjects(customer);
		Mockito.verify(modelService, Mockito.times(1)).removeAll(interests);
	}
}
