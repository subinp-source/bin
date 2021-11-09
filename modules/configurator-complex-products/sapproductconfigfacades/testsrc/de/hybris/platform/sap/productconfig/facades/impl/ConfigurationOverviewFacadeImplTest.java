/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigurationMessageMapper;
import de.hybris.platform.sap.productconfig.facades.ConfigurationTestData;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.populator.ConfigurationOverviewPopulator;
import de.hybris.platform.sap.productconfig.facades.populator.VariantOverviewPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.intf.PricingService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class ConfigurationOverviewFacadeImplTest
{
	public ConfigurationOverviewFacadeImpl classUnderTest;

	private static final String CONFIG_ID = "config_id";
	private static final String PRODUCT_CODE = "4711";

	@Mock
	private ConfigurationOverviewPopulator mockConfigurationOverviewPopulator;
	@Mock
	private VariantOverviewPopulator mockVariantOverviewPopulator;
	@Mock
	private ProductConfigurationService mockProductConfigurationService;
	@Mock
	private ProductService mockProductService;
	@Mock
	private PricingService mockPricingService;
	@Mock
	private ConfigurationMessageMapper mockConfigMessageMapper;

	private final ConfigModel configModel = ConfigurationTestData.createConfigModel();;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest = new ConfigurationOverviewFacadeImpl();
		classUnderTest.setConfigurationOverviewPopulator(mockConfigurationOverviewPopulator);
		classUnderTest.setVariantOverviewPopulator(mockVariantOverviewPopulator);
		classUnderTest.setConfigurationService(mockProductConfigurationService);
		classUnderTest.setProductService(mockProductService);
		classUnderTest.setPricingService(mockPricingService);
		classUnderTest.setConfigMessagesMapper(mockConfigMessageMapper);
		when(mockProductConfigurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(configModel);
	}

	@Test
	public void testGetOverviewForConfigurationNull()
	{
		ConfigurationOverviewData configOverviewData = null;
		configOverviewData = classUnderTest.getOverviewForConfiguration(CONFIG_ID, configOverviewData);
		assertNotNull(configOverviewData);
		verify(mockConfigMessageMapper).mapMessagesFromModelToData(any(ConfigurationOverviewData.class), any(ConfigModel.class));
	}

	@Test
	public void testGetVaraintForProductVariantNull()
	{
		ConfigurationOverviewData configOverviewData = null;
		configOverviewData = classUnderTest.getOverviewForProductVariant(PRODUCT_CODE, configOverviewData);
		assertNotNull(configOverviewData);
	}

	@Test
	public void testGetVaraintForProductVariantNonNull()
	{
		final ConfigurationOverviewData oldConfigOverviewData = new ConfigurationOverviewData();
		final ConfigurationOverviewData configOverviewData = classUnderTest.getOverviewForProductVariant(PRODUCT_CODE,
				oldConfigOverviewData);
		assertSame(oldConfigOverviewData, configOverviewData);
	}


	@Test
	public void testGetOverviewForConfiguration_pricingServiceActive()
	{
		when(Boolean.valueOf(mockPricingService.isActive())).thenReturn(Boolean.TRUE);
		classUnderTest.getOverviewForConfiguration(CONFIG_ID, null);
		verify(mockPricingService).fillOverviewPrices(configModel);
		verify(mockConfigMessageMapper, times(1)).mapMessagesFromModelToData(any(ConfigurationOverviewData.class),
				any(ConfigModel.class));
	}

	@Test
	public void testGetOverviewForConfiguration_pricingServiceInactive()
	{
		when(Boolean.valueOf(mockPricingService.isActive())).thenReturn(Boolean.FALSE);
		classUnderTest.getOverviewForConfiguration(CONFIG_ID, null);
		verify(mockPricingService, Mockito.times(0)).fillOverviewPrices(configModel);
		verify(mockConfigMessageMapper, times(1)).mapMessagesFromModelToData(any(ConfigurationOverviewData.class),
				any(ConfigModel.class));

	}
}
