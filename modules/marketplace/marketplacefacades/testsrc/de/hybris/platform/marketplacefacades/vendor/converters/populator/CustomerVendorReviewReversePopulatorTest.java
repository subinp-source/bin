/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades.vendor.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.VendorReviewData;
import de.hybris.platform.marketplaceservices.model.CustomerVendorReviewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
@UnitTest
public class CustomerVendorReviewReversePopulatorTest
{
	private static final String COMMENT = "Vendor Review Comment";
	private static final double SATISFACTION = 3.5;
	private static final double DELIVERY = 4.5;
	private static final double COMMUNICATION = 4;
	private static final double DELTA = 0.0001;

	private CustomerVendorReviewReversePopulator customerVendorReviewPopulator;
	private VendorReviewData vendorReviewData;

	@Before
	public void setUp()
	{
		//MockitoAnnotations.initMocks(this);
		vendorReviewData = new VendorReviewData();
		vendorReviewData.setComment(COMMENT);
		vendorReviewData.setDelivery(DELIVERY);
		vendorReviewData.setSatisfaction(SATISFACTION);
		vendorReviewData.setCommunication(COMMUNICATION);

		customerVendorReviewPopulator = new CustomerVendorReviewReversePopulator();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateVendorDataWithSourceNull()
	{
		customerVendorReviewPopulator.populate(null, new CustomerVendorReviewModel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateVendorDataWithTargetNull()
	{
		customerVendorReviewPopulator.populate(vendorReviewData, null);
	}

	@Test
	public void testPopulateVendorReviewData()
	{
		final CustomerVendorReviewModel vendorReviewModel = new CustomerVendorReviewModel();
		customerVendorReviewPopulator.populate(vendorReviewData, vendorReviewModel);
		Assert.assertEquals(COMMENT, vendorReviewModel.getComment());
		Assert.assertEquals(SATISFACTION, vendorReviewModel.getSatisfaction(), DELTA);
		Assert.assertEquals(DELIVERY, vendorReviewModel.getDelivery(), DELTA);
		Assert.assertEquals(COMMUNICATION, vendorReviewModel.getCommunication(), DELTA);
	}

}
