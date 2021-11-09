/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationOverviewWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductConfigOverviewWsControllerTest
{
	private static final String CONFIG_ID = "config id";
	private static final String PRODUCT_CODE = "product A";
	@Mock
	private ConfigurationOverviewFacade configOverviewFacade;
	@Mock
	private DataMapper dataMapper;
	@Mock
	private ConfigurationFacade configFacade;
	@InjectMocks
	private ProductConfiguratorCCPController classUnderTest;

	private ConfigurationOverviewData overviewData;


	@Before
	public void setup()
	{
		overviewData = new ConfigurationOverviewData();
		overviewData.setId(CONFIG_ID);
		overviewData.setProductCode(PRODUCT_CODE);
		when(configOverviewFacade.getOverviewForConfiguration(CONFIG_ID, null)).thenReturn(overviewData);
		when(configFacade.getNumberOfErrors(CONFIG_ID)).thenReturn(0);
		when(dataMapper.map(overviewData, ConfigurationOverviewWsDTO.class)).thenReturn(new ConfigurationOverviewWsDTO());

		classUnderTest.setConfigOverviewFacade(configOverviewFacade);
		classUnderTest.setConfigFacade(configFacade);

	}

	@Test
	public void testGetOverview()
	{
		final ConfigurationOverviewWsDTO result = classUnderTest.getConfigurationOverview(CONFIG_ID);
		assertNotNull(result);

		verify(configOverviewFacade, times(1)).getOverviewForConfiguration(CONFIG_ID, null);
		verify(dataMapper, times(1)).map(overviewData, ConfigurationOverviewWsDTO.class);
	}


}
