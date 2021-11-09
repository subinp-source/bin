/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.PriceDataPair;
import de.hybris.platform.sap.productconfig.facades.PriceValueUpdateData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticValueWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticWsDTO;
import de.hybris.platform.sap.productconfig.occ.GroupWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.Mockito;

public abstract class AbstractProductConfiguratorCCPControllerTCBase
{

	protected static final String PRODUCT_CODE = "product";
	protected static final String GROUP1_ID = "1-WCEM_DEPENDENCY_PC.MONITOR";
	protected static final String CONFIG_ID = "configId";
	protected static final String GROUP2_ID = "1-WCEM_DEPENDENCY_PC.SOFTWARE";
	protected static final String GROUP3_ID = "1-WCEM_DEPENDENCY_PC.ACCESSORY";
	protected static final String GROUP3_1_ID = "1-WCEM_DEPENDENCY_PC_ACCESORY.GROUP1";
	protected static final String GROUP3_1_1_ID = "1-WCEM_ACCESSORYGROUP1.GROUP1";
	protected static final String GROUP3_2_ID = "1-WCEM_DEPENDENCY_PC_ACCESORY.GROUP2";
	protected static final String ROOT_GROUP_ID = "RootId";
	protected static final String VALUE_KEY = "ValueKey";

	protected final ConfigurationWsDTO updatedConfiguration = new ConfigurationWsDTO();
	protected final ConfigurationData updatedConfigurationAsFacadeDTO = new ConfigurationData();

	@Mock
	protected ConfigurationFacade configurationFacade;

	@Mock
	protected DataMapper dataMapper;

	protected final ConfigurationData backendUpdatedConfiguration = new ConfigurationData();
	protected final ConfigurationWsDTO backendUpdatedWsConfiguration = new ConfigurationWsDTO();
	protected final List<ImageData> imageListCstic = new ArrayList<>();
	protected final ImageData imageCstic = new ImageData();
	protected final List<ImageWsDTO> imageListCsticWs = new ArrayList<>();
	protected final ImageWsDTO imageWs = new ImageWsDTO();
	protected CsticData cstic;
	protected CsticWsDTO csticWs;
	protected List<CsticWsDTO> csticWsList;
	protected UiGroupData group;
	protected List<GroupWsDTO> groupsWs;
	protected CsticValueData csticValue;
	protected CsticValueWsDTO csticValueWs;
	protected final List<ImageData> imageListValue = new ArrayList<>();
	protected final ImageData imageValue = new ImageData();
	protected final List<CsticValueWsDTO> csticValueListWs = new ArrayList<>();
	protected final PriceValueUpdateData valuePrice = new PriceValueUpdateData();
	protected final CsticSupplementsWsDTO attributeSupplement = new CsticSupplementsWsDTO();
	protected final Map<String, PriceDataPair> valuePrices = new HashMap<String, PriceDataPair>();
	protected final PriceDataPair valuePricePair = new PriceDataPair();
	protected final PriceData obsoletePriceValue = new PriceData();
	protected final PriceData priceValue = new PriceData();
	protected final BigDecimal priceValueAsDecimal = BigDecimal.valueOf(1234);
	protected final PriceWsDTO priceWs = new PriceWsDTO();

	protected void prepareImageLists()
	{
		imageListCstic.add(imageCstic);
		imageListValue.add(imageValue);
		imageListCsticWs.add(imageWs);
	}

	protected void prepareValues()
	{
		csticValue = new CsticValueData();
		csticValue.setMedia(imageListValue);
		csticValue.setKey(VALUE_KEY);

		csticValueWs = new CsticValueWsDTO();
		csticValueWs.setKey(VALUE_KEY);
		csticValueListWs.add(csticValueWs);
	}

	protected void initializeDataMapperMock()
	{
		Mockito.when(dataMapper.map(updatedConfiguration, ConfigurationData.class)).thenReturn(updatedConfigurationAsFacadeDTO);
		Mockito.when(dataMapper.map(backendUpdatedConfiguration, ConfigurationWsDTO.class))
				.thenReturn(backendUpdatedWsConfiguration);
		Mockito.when(dataMapper.map(valuePrice, CsticSupplementsWsDTO.class)).thenReturn(attributeSupplement);
		Mockito.when(dataMapper.map(priceValue, PriceWsDTO.class)).thenReturn(priceWs);
		Mockito.when(dataMapper.map(obsoletePriceValue, PriceWsDTO.class)).thenReturn(priceWs);
		Mockito.when(dataMapper.mapAsList(imageListCstic, ImageWsDTO.class, null)).thenReturn(imageListCsticWs);
		Mockito.when(dataMapper.mapAsList(imageListValue, ImageWsDTO.class, null)).thenReturn(imageListCsticWs);
	}

	protected void prepareUpdatedConfiguration()
	{
		final List<UiGroupData> groups = new ArrayList();
		groups.add(createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"));
		updatedConfigurationAsFacadeDTO.setConfigId(CONFIG_ID);
		updatedConfigurationAsFacadeDTO.setGroups(groups);
	}

	protected void prepareBackendUpdatedConfigurationWithTwoGroups()
	{
		final List<UiGroupData> groups = new ArrayList();
		groups.add(createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"));
		groups.add(createGroupWithOneCstic(GROUP2_ID, "CSTIC2", "VALUE2"));
		backendUpdatedConfiguration.setConfigId(CONFIG_ID);
		backendUpdatedConfiguration.setGroups(groups);
	}

	protected void prepareBackendUpdatedConfigurationWithTwoGroupsWsRepresentation()
	{
		final List<GroupWsDTO> groups = new ArrayList();
		groups.add(createWsGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"));
		groups.add(createWsGroupWithOneCstic(GROUP2_ID, "CSTIC2", "VALUE2"));
		backendUpdatedConfiguration.setConfigId(CONFIG_ID);
		backendUpdatedWsConfiguration.setGroups(groups);

	}

	protected void prepareGroupsAndCstics()
	{
		group = backendUpdatedConfiguration.getGroups().get(0);
		cstic = group.getCstics().get(0);

		groupsWs = backendUpdatedWsConfiguration.getGroups();
		csticWsList = groupsWs.get(0).getAttributes();
		csticWs = csticWsList.get(0);
	}

	protected GroupWsDTO createWsGroupWithOneCstic(final String groupId, final String csticName, final String valueName)
	{
		final GroupWsDTO group = new GroupWsDTO();
		group.setId(groupId);
		final List<CsticWsDTO> cstics = new ArrayList();
		cstics.add(createCsticWsData(csticName, UiType.RADIO_BUTTON, valueName));
		group.setAttributes(cstics);
		return group;
	}

	protected CsticWsDTO createCsticWsData(final String name, final UiType type, final String value)
	{
		final CsticWsDTO cstic = new CsticWsDTO();
		cstic.setName(name);
		cstic.setType(type);
		cstic.setValue(value);
		cstic.setDomainValues(Arrays.asList(csticValueWs));
		return cstic;
	}



	protected UiGroupData createGroupWithOneCstic(final String groupId, final String csticName, final String valueName)
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupType(GroupType.CSTIC_GROUP);
		group.setId(groupId);
		final List<CsticData> cstics = new ArrayList();
		cstics.add(createCsticData(csticName, UiType.RADIO_BUTTON, valueName));
		group.setCstics(cstics);
		return group;
	}

	protected CsticData createCsticData(final String name, final UiType type, final String value)
	{
		final CsticData cstic = new CsticData();
		cstic.setName(name);
		cstic.setType(type);
		cstic.setValue(value);
		cstic.setMedia(imageListCstic);
		cstic.setDomainvalues(Arrays.asList(csticValue));
		return cstic;
	}

}
