/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.marketplaceservices.model.VendorUserModel;
import de.hybris.platform.marketplaceservices.strategies.VendorActivationStrategy;
import de.hybris.platform.marketplaceservices.strategies.VendorDeactivationStrategy;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultVendorActivationDeactivationStrategyTest extends ServicelayerTransactionalTest
{

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "vendorActivationStrategy")
	private VendorActivationStrategy vendorActivationStrategy;

	@Resource(name = "vendorDeactivationStrategy")
	private VendorDeactivationStrategy vendorDeactivationStrategy;

	private VendorModel vendor;

	private VendorUserModel vendorUser;

	@Before
	public void prepare()
	{
		vendorUser = new VendorUserModel();
		vendorUser.setUid("testvendoruser");

		vendor = new VendorModel();
		vendor.setCode("Test");
		vendor.setVendorUsers(Collections.singletonList(vendorUser));
		vendorUser.setVendor(vendor);
		modelService.save(vendor);
	}

	@Test
	public void testActivate()
	{
		vendorActivationStrategy.activateVendor(vendor);
		Assert.assertTrue(vendor.isActive());
		Assert.assertFalse(vendorUser.isLoginDisabled());
	}

	@Test
	public void testDeactivate()
	{
		vendorDeactivationStrategy.deactivateVendor(vendor);
		Assert.assertFalse(vendor.isActive());
		Assert.assertTrue(vendorUser.isLoginDisabled());
	}
}
