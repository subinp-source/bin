/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPricingStrategy;
import de.hybris.platform.util.PriceValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultProductConfigFindPriceHookTest
{
	@InjectMocks
	private DefaultProductConfigFindPriceHook classUnderTest;

	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;

	@Mock
	private ProductConfigurationPricingStrategy productConfigurationPricingStrategy;

	@Mock
	private AbstractOrderEntryModel entry;

	@Mock
	private ProductModel product;

	private final PriceValue configurablePriceValue = new PriceValue("EUR", 2.0, true);


	@Before
	public void setup()
	{
		given(entry.getProduct()).willReturn(product);
		given(productConfigurationPricingStrategy.calculateBasePriceForConfiguration(entry)).willReturn(configurablePriceValue);
	}

	@Test
	public void testFindCustomBasePrice()
	{
		final PriceValue defaultPrice = null;
		final PriceValue returnedPriceValue = classUnderTest.findCustomBasePrice(entry, defaultPrice);
		assertEquals(configurablePriceValue, returnedPriceValue);
	}

	@Test
	public void testIsApplicable()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(product)).willReturn(true);
		assertTrue(classUnderTest.isApplicable(entry));
	}

	@Test
	public void testIsApplicableNotApplacable()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(product)).willReturn(false);
		assertFalse(classUnderTest.isApplicable(entry));
	}

}

