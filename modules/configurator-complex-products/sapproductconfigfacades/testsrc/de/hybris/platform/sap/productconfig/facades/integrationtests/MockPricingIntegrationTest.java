/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.integrationtests;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationPricingFacade;
import de.hybris.platform.sap.productconfig.facades.PriceValueUpdateData;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.testutil.MockPricingTestHelper;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * The system will not return prices as part of the configuration requests. Only when dedicated pricing calls are done,
 * pricing information is returned. The latter is especially needed for OCC support
 */
@SuppressWarnings("javadoc")
@IntegrationTest
public class MockPricingIntegrationTest extends CPQFacadeLayerTest
{

	@Resource(name = "sapProductConfigOverviewFacade")
	protected ConfigurationOverviewFacade overviewFacade;

	@Resource(name = "sapProductConfigPricingFacade")
	protected ConfigurationPricingFacade pricingFacade;

	protected MockPricingTestHelper mockPricingTestHelper = new MockPricingTestHelper();

	private final ConfigurationOverviewData overview = new ConfigurationOverviewData();

	private ConfigurationData configuration;

	@Override
	public void initProviders()
	{
		ensureMockProvider();
	}

	@Before
	public void setUp() throws Exception
	{
		prepareCPQData();

		configuration = cpqFacade.getConfiguration(KB_KEY_CONF_CAMERA_SL);
	}

	@Test
	public void testConfigurationCallPriceSummary()
	{
		assertEquals(BigDecimal.ZERO, configuration.getPricing().getBasePrice().getValue());
		assertEquals(BigDecimal.ZERO, configuration.getPricing().getCurrentTotal().getValue());
	}


	@Test
	public void testConfigurationCallAssignableValuePrices()
	{
		mockPricingTestHelper.checkConfigurationCallAssignableValuePrices(configuration, BigDecimal.ZERO);
	}


	@Test
	public void testPricingCallPriceSummary()
	{
		final PricingData summary = pricingFacade.getPriceSummary(configuration.getConfigId());
		assertEquals(MockPricingTestHelper.BASE_PRICE, summary.getBasePrice().getValue());
		assertEquals(MockPricingTestHelper.BASE_PRICE, summary.getCurrentTotal().getValue());
	}

	@Test
	public void testPricingCallAssignableValuePrices()
	{
		final List<PriceValueUpdateData> valuePrices = pricingFacade.getValuePrices(MockPricingTestHelper.CSTIC_UI_KEYS,
				configuration.getConfigId());
		assertEquals(1, valuePrices.size());
		assertEquals(MockPricingTestHelper.VALUE_PRICE,
				valuePrices.get(0).getPrices().get(MockPricingTestHelper.VALUE_NAME).getPriceValue().getValue());
	}

	@Test
	public void testAssignedValuePrices()
	{
		facadeConfigValueHelper.setCsticValue(configuration, MockPricingTestHelper.GROUP_NAME, MockPricingTestHelper.CSTIC_NAME,
				MockPricingTestHelper.VALUE_NAME, true);
		cpqFacade.updateConfiguration(configuration);
		configuration = cpqFacade.getConfiguration(configuration);
		final ConfigurationOverviewData overviewData = overviewFacade.getOverviewForConfiguration(configuration.getConfigId(),
				overview);
		mockPricingTestHelper.checkAssignedValuePrices(overviewData);
	}


}
