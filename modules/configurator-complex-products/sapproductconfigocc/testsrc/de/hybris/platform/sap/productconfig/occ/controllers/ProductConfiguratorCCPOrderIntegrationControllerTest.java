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
import de.hybris.platform.sap.productconfig.facades.ConfigurationOrderIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationOverviewWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
public class ProductConfiguratorCCPOrderIntegrationControllerTest
{
	private static final String CONFIG_ID = "config id";
	private static final String PRODUCT_CODE = "product A";
	private static final String ORDER_ID = "order Id";
	private static final int ENTRY_NUMBER = 0;
	private static final String CSTIC_NAME = "the cstic";
	private static final String VALUE_NAME = "the value";
	@Mock
	private ConfigurationOverviewFacade configOverviewFacade;
	@Mock
	private ConfigurationOrderIntegrationFacade configurationOrderIntegrationFacade;
	@Mock
	private DataMapper dataMapper;

	@InjectMocks
	private ProductConfiguratorCCPOrderIntegrationController classUnderTest;

	private ConfigurationOverviewData overviewData;
	private ConfigurationOverviewData overviewDataWithValues;
	private ConfigurationOverviewWsDTO wsDTO;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		overviewData = new ConfigurationOverviewData();
		overviewData.setId(CONFIG_ID);
		overviewData.setProductCode(PRODUCT_CODE);

		overviewDataWithValues = overviewData;
		wsDTO = new ConfigurationOverviewWsDTO();
		final List<CharacteristicGroup> groups = new ArrayList();
		final CharacteristicGroup group = new CharacteristicGroup();

		final List<CharacteristicValue> characteristicValues = new ArrayList();
		final CharacteristicValue value = new CharacteristicValue();
		value.setCharacteristic(CSTIC_NAME);
		value.setValue(VALUE_NAME);
		characteristicValues.add(value);
		group.setCharacteristicValues(characteristicValues);
		groups.add(group);
		overviewDataWithValues.setGroups(groups);

		when(configurationOrderIntegrationFacade.getConfiguration(ORDER_ID, ENTRY_NUMBER)).thenReturn(overviewData);
		when(configOverviewFacade.getOverviewForConfiguration(CONFIG_ID, overviewData)).thenReturn(overviewDataWithValues);
		when(dataMapper.map(overviewData, ConfigurationOverviewWsDTO.class)).thenReturn(wsDTO);
	}

	@Test
	public void testGetConfigurationOverviewForOrderEntry() {
		final ConfigurationOverviewWsDTO result = classUnderTest.getConfigurationOverviewForOrderEntry(ORDER_ID, ENTRY_NUMBER);
		assertNotNull(result);
		verify(configurationOrderIntegrationFacade, times(1)).getConfiguration(ORDER_ID, ENTRY_NUMBER);
		verify(configOverviewFacade, times(1)).getOverviewForConfiguration(CONFIG_ID, overviewData);
		verify(dataMapper, times(1)).map(overviewDataWithValues, ConfigurationOverviewWsDTO.class);
	}

	@Test(expected = NotFoundException.class)
	public void testGetConfigurationOverviewForOrderEntryKBVersionForEntryDoesNotExist()
	{
		when(configurationOrderIntegrationFacade.getConfiguration(ORDER_ID, ENTRY_NUMBER)).thenReturn(null);
		final ConfigurationOverviewWsDTO result = classUnderTest.getConfigurationOverviewForOrderEntry(ORDER_ID, ENTRY_NUMBER);
	}

}
