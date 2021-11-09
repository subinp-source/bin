/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.evaluator.impl;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.marketplaceservices.data.impl.MarketplaceRestrictionDataImpl;
import de.hybris.platform.marketplaceservices.model.restrictions.CMSVendorRestrictionModel;
import de.hybris.platform.ordersplitting.model.VendorModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



public class CMSVendorRestrictionEvaluatorTest
{
	private CMSVendorRestrictionEvaluator cmsVendorRestrictionEvaluator;

	private VendorModel vendorModel1;
	@Mock
	private VendorModel vendorModel2;

	private CMSVendorRestrictionModel vendorRestrictionModel;

	private RestrictionData notMarketrestrictionData;
	@Mock
	private MarketplaceRestrictionDataImpl marketrestrictionData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cmsVendorRestrictionEvaluator = new CMSVendorRestrictionEvaluator();
		vendorRestrictionModel = new CMSVendorRestrictionModel();
		vendorModel1 = new VendorModel();
		vendorModel1.setCode("test_vendor_1");
		vendorRestrictionModel.setVendor(vendorModel1);
	}

	@Test
	public void testNotMarketplaceRestrictionData()
	{
		Assert.assertTrue(cmsVendorRestrictionEvaluator.evaluate(vendorRestrictionModel, notMarketrestrictionData));
	}

	@Test
	public void testWithMarketplaceRestrictionData()
	{
		Mockito.when(marketrestrictionData.getVendor()).thenReturn(vendorModel2);
		Mockito.when(vendorModel2.getCode()).thenReturn("test_vendor_2");
		Assert.assertFalse(cmsVendorRestrictionEvaluator.evaluate(vendorRestrictionModel, marketrestrictionData));
	}
}
