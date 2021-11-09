/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SimplePricingConfigurationParameterTest
{
	private static final String PRODUCT_FALSE = "productFalse";
	private static final String PRODUCT_TRUE = "productTrue";
	private static final String ISO_CODE = "isoCode";
	SimplePricingConfigurationParameter classUnderTest = new SimplePricingConfigurationParameter();
	private final String sapCode = "USD";
	private final Map<String, Boolean> productDeltaDecision = new HashMap();
	@Mock
	private CurrencyModel currencyModel;
	@Mock
	private UnitModel unitModel;


	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(currencyModel.getIsocode()).thenReturn(ISO_CODE);
		Mockito.when(unitModel.getCode()).thenReturn(ISO_CODE);
		productDeltaDecision.put(PRODUCT_TRUE, true);
		productDeltaDecision.put(PRODUCT_FALSE, false);
		classUnderTest.setProductDeltaPriceDecision(productDeltaDecision);
	}

	@Test
	public void testConvertSapToIsoCode()
	{
		assertNull(classUnderTest.convertSapToIsoCode(sapCode));
	}

	@Test
	public void testDistributionChannelForConditions()
	{
		assertNull(classUnderTest.getDistributionChannelForConditions());
	}

	@Test
	public void testDivisionForConditions()
	{
		assertNull(classUnderTest.getDivisionForConditions());
	}

	@Test
	public void testSalesOrganization()
	{
		assertNull(classUnderTest.getSalesOrganization());
	}

	@Test
	public void testIsPricingSupported()
	{
		assertTrue(classUnderTest.isPricingSupported());
	}

	@Test
	public void tesRretrieveCurrencyIsoCode()
	{
		assertEquals(ISO_CODE, classUnderTest.retrieveCurrencyIsoCode(currencyModel));
		assertNull(classUnderTest.retrieveCurrencyIsoCode(null));
	}

	@Test
	public void testRetrieveCurrencySapCode()
	{
		assertEquals(ISO_CODE, classUnderTest.retrieveCurrencySapCode(currencyModel));
	}

	@Test
	public void testRetrieveUnitIsoCode()
	{
		assertEquals(ISO_CODE, classUnderTest.retrieveUnitIsoCode(unitModel));
		assertNull(classUnderTest.retrieveUnitIsoCode(null));
	}

	@Test
	public void testRetrieveUnitSapCode()
	{
		assertEquals(ISO_CODE, classUnderTest.retrieveUnitSapCode(unitModel));
	}

	@Test
	public void testShowBasePriceAndSelectedOptions()
	{
		assertTrue(classUnderTest.showBasePriceAndSelectedOptions());
	}

	@Test
	public void testShowDeltaPrices()
	{
		assertFalse(classUnderTest.showDeltaPrices());
	}

	@Test
	public void testShowDeltaPricesWithProductCode()
	{
		assertFalse(classUnderTest.showDeltaPrices(null));
		assertFalse(classUnderTest.showDeltaPrices("product not specified"));
		assertTrue(classUnderTest.showDeltaPrices(PRODUCT_TRUE));
		assertFalse(classUnderTest.showDeltaPrices(PRODUCT_FALSE));
	}
}
