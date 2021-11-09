/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticValueSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.util.ImageHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfiguratorCCPControllerTest extends AbstractProductConfiguratorCCPControllerTCBase
{

	@Mock
	protected ImageHandler imageHandler;

	@InjectMocks
	ProductConfiguratorCCPController classUnderTest;

	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);

		prepareValues();
		prepareImageLists();
		prepareUpdatedConfiguration();
		prepareBackendUpdatedConfigurationWithTwoGroups();
		prepareBackendUpdatedConfigurationWithTwoGroupsWsRepresentation();
		prepareGroupsAndCstics();

		valuePrice.setPrices(valuePrices);
		valuePrices.put(VALUE_KEY, valuePricePair);
		valuePricePair.setObsoletePriceValue(obsoletePriceValue);
		valuePricePair.setPriceValue(priceValue);
		priceValue.setValue(priceValueAsDecimal);
		priceWs.setValue(priceValueAsDecimal);

		initializeDataMapperMock();
		Mockito.when(configurationFacade.getConfiguration(updatedConfigurationAsFacadeDTO)).thenReturn(backendUpdatedConfiguration);
	}

	@Test
	public void testUpdateConfiguration()
	{
		final ConfigurationWsDTO configurationAfterUpdate = classUnderTest.updateConfiguration(updatedConfiguration.getConfigId(),
				updatedConfiguration);
		assertEquals(backendUpdatedWsConfiguration, configurationAfterUpdate);
	}

	@Test
	public void testFilterGroups()
	{
		final String requestedGroupId = GROUP2_ID;
		classUnderTest.filterGroups(backendUpdatedConfiguration, requestedGroupId);
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		assertEquals(2, groups.size());
		assertEquals(0, groups.get(0).getCstics().size());
		assertEquals(1, groups.get(1).getCstics().size());
	}

	@Test
	public void testFilterGroupsRequestedGroupIdNull()
	{
		final String requestedGroupId = null;
		classUnderTest.filterGroups(backendUpdatedConfiguration, requestedGroupId);
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		assertEquals(2, groups.size());
		assertEquals(1, groups.get(0).getCstics().size());
		assertEquals(1, groups.get(1).getCstics().size());
	}

	@Test
	public void testFilterGroupsWithListRequestedGroupIdNull()
	{
		final String requestedGroupId = null;
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		classUnderTest.filterGroups(groups, requestedGroupId);
		assertEquals(2, groups.size());
		assertEquals(1, groups.get(0).getCstics().size());
		assertEquals(1, groups.get(1).getCstics().size());
	}


	@Test
	public void testFilterGroupsMultilevelRequestedSubSubGroup()
	{
		prepareBackendUpdatedConfigurationMultiLevel();
		final String requestedGroupId = GROUP3_1_1_ID;
		classUnderTest.filterGroups(backendUpdatedConfiguration, requestedGroupId);
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		assertEquals(3, groups.size());
		assertEquals(0, groups.get(0).getCstics().size());
		assertEquals(0, groups.get(1).getCstics().size());
		final List<UiGroupData> subGroups = groups.get(2).getSubGroups();
		assertEquals(2, subGroups.size());
		assertEquals(1, subGroups.get(0).getSubGroups().size());
		assertEquals(1, subGroups.get(0).getSubGroups().get(0).getCstics().size());
		assertEquals(0, subGroups.get(1).getCstics().size());
	}

	@Test
	public void testFilterGroupsMultilevelRequestedSubGroup()
	{
		prepareBackendUpdatedConfigurationMultiLevel();
		final String requestedGroupId = GROUP3_2_ID;
		classUnderTest.filterGroups(backendUpdatedConfiguration, requestedGroupId);
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		assertEquals(3, groups.size());
		assertEquals(0, groups.get(0).getCstics().size());
		assertEquals(0, groups.get(1).getCstics().size());
		final List<UiGroupData> subGroups = groups.get(2).getSubGroups();
		assertEquals(2, subGroups.size());
		assertEquals(1, subGroups.get(0).getSubGroups().size());
		assertEquals(0, subGroups.get(0).getSubGroups().get(0).getCstics().size());
		assertEquals(1, subGroups.get(1).getCstics().size());
	}

	@Test
	public void testFilterGroupsMultilevelRequestedRootLevelGroup()
	{
		prepareBackendUpdatedConfigurationMultiLevel();
		final String requestedGroupId = GROUP2_ID;
		classUnderTest.filterGroups(backendUpdatedConfiguration, requestedGroupId);
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		assertEquals(3, groups.size());
		assertEquals(0, groups.get(0).getCstics().size());
		assertEquals(1, groups.get(1).getCstics().size());
		final List<UiGroupData> subGroups = groups.get(2).getSubGroups();
		assertEquals(2, subGroups.size());
		assertEquals(1, subGroups.get(0).getSubGroups().size());
		assertEquals(0, subGroups.get(0).getSubGroups().get(0).getCstics().size());
		assertEquals(0, subGroups.get(1).getCstics().size());
	}

	@Test
	public void testHasSubGroups()
	{
		final UiGroupData instanceGroup = createInstanceGroup(GROUP3_ID);
		final List<UiGroupData> subGroups = new ArrayList();
		instanceGroup.setSubGroups(subGroups);
		final UiGroupData subInstanceGroup = createInstanceGroup(GROUP3_1_ID);
		subGroups.add(subInstanceGroup);
		assertTrue(classUnderTest.hasSubGroups(instanceGroup));
	}

	@Test
	public void testHasSubGroupsEmpyList()
	{
		final UiGroupData instanceGroup = createInstanceGroup(GROUP3_ID);
		final List<UiGroupData> subGroups = new ArrayList();
		instanceGroup.setSubGroups(subGroups);
		assertFalse(classUnderTest.hasSubGroups(instanceGroup));
	}

	@Test
	public void testHasSubGroupsFalse()
	{
		final UiGroupData instanceGroup = createInstanceGroup(GROUP3_ID);
		assertFalse(classUnderTest.hasSubGroups(instanceGroup));
	}

	@Test
	public void testDeleteCstics()
	{
		final UiGroupData group = createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1");
		classUnderTest.deleteCstics(group);
		assertEquals(0, group.getCstics().size());
	}

	@Test
	public void testIsNotRequestedGroup()
	{
		final String requestedGroupId = GROUP2_ID;
		assertTrue(classUnderTest.isNotRequestedGroup(createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"), requestedGroupId));
	}

	@Test
	public void testIsNotRequestedGroupFalse()
	{
		final String requestedGroupId = GROUP1_ID;
		assertFalse(classUnderTest.isNotRequestedGroup(createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"), requestedGroupId));
	}

	@Test
	public void testIsNotRequestedGroupNull()
	{
		final String requestedGroupId = null;
		assertFalse(classUnderTest.isNotRequestedGroup(createGroupWithOneCstic(GROUP1_ID, "CSTIC1", "VALUE1"), requestedGroupId));
	}

	@Test
	public void testDetermineFirstGroupIdWithEmptyGroups()
	{
		updatedConfigurationAsFacadeDTO.setGroups(Collections.emptyList());
		final String result = classUnderTest.determineFirstGroupId(updatedConfigurationAsFacadeDTO.getGroups());
		assertNull(result);
	}

	@Test
	public void testDetermineFirstGroupIdWithEmptySubGroups()
	{
		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		group.setSubGroups(null);
		groups.add(group);
		updatedConfigurationAsFacadeDTO.setGroups(groups);
		final String result = classUnderTest.determineFirstGroupId(updatedConfigurationAsFacadeDTO.getGroups());
		assertNull(result);
	}

	@Test
	public void testDetermineFirstGroupIdWithSubGroups()
	{
		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		final List<UiGroupData> subGroups = new ArrayList<>();
		final UiGroupData subGroup = new UiGroupData();
		final String subGroupId = "subGroup_1";
		subGroup.setId(subGroupId);
		final List<CsticData> cstics = new ArrayList<>();
		for (int i = 1; i <= 5; i++)
		{
			final CsticData cstic = new CsticData();
			cstic.setName("cstic_" + i);
			cstics.add(cstic);
		}
		subGroup.setCstics(cstics);
		subGroups.add(subGroup);
		group.setSubGroups(subGroups);
		groups.add(group);
		updatedConfigurationAsFacadeDTO.setGroups(groups);

		final String result = classUnderTest.determineFirstGroupId(updatedConfigurationAsFacadeDTO.getGroups());
		assertNotNull(result);
		assertEquals(subGroupId, result);
	}

	@Test
	public void testDetermineFirstGroupIdWithEmptyCstics()
	{
		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		final List<UiGroupData> subGroups = new ArrayList<>();
		final UiGroupData subGroup = new UiGroupData();
		final String subGroupId = "subGroup_1";
		subGroup.setId(subGroupId);
		final List<CsticData> cstics = new ArrayList<>();
		subGroups.add(subGroup);
		group.setSubGroups(subGroups);
		groups.add(group);
		updatedConfigurationAsFacadeDTO.setGroups(groups);

		final String result = classUnderTest.determineFirstGroupId(updatedConfigurationAsFacadeDTO.getGroups());
		assertNull(result);
	}


	@Test
	public void testCreateAttributeSupplementDTO()
	{
		final CsticSupplementsWsDTO attributeSupplementDTO = classUnderTest.createAttributeSupplementDTO(valuePrice);
		assertNotNull(attributeSupplementDTO);
		final List<CsticValueSupplementsWsDTO> priceSupplements = attributeSupplementDTO.getPriceSupplements();
		assertNotNull(priceSupplements);
	}

	@Test
	public void testCreateAttributeSupplementDTOCoversNullPriceSupplements()
	{
		valuePrice.setPrices(null);
		final CsticSupplementsWsDTO attributeSupplementDTO = classUnderTest.createAttributeSupplementDTO(valuePrice);
		assertNotNull(attributeSupplementDTO);
		assertNotNull(attributeSupplementDTO.getPriceSupplements());
	}

	@Test
	public void testCreatePriceSupplements()
	{
		final List<CsticValueSupplementsWsDTO> priceSupplements = classUnderTest.createPriceSupplements(valuePrice.getPrices());
		assertNotNull(priceSupplements);
	}

	@Test
	public void testConvertEntrytoWsDTO()
	{
		final CsticValueSupplementsWsDTO valueSupplementsWsDTO = classUnderTest
				.convertEntrytoWsDTO(valuePrice.getPrices().entrySet().iterator().next());
		assertNotNull(valueSupplementsWsDTO);
		assertEquals(VALUE_KEY, valueSupplementsWsDTO.getAttributeValueKey());
		final PriceWsDTO priceWsDTO = valueSupplementsWsDTO.getPriceValue();
		assertNotNull(priceWsDTO);
		assertEquals(priceValueAsDecimal, priceWsDTO.getValue());
	}

	@Test(expected = NullPointerException.class)
	public void testConvertEntrytoWsDTONullNotAllowed()
	{
		classUnderTest.convertEntrytoWsDTO(null);
	}

	@Test
	public void testConvertEntrytoWsDTOObsoletePrice()
	{
		final CsticValueSupplementsWsDTO valueSupplementsWsDTO = classUnderTest
				.convertEntrytoWsDTO(valuePrice.getPrices().entrySet().iterator().next());
		assertEquals(priceValueAsDecimal, valueSupplementsWsDTO.getObsoletePriceValue().getValue());
	}


	private void prepareBackendUpdatedConfigurationMultiLevel()
	{
		final List<UiGroupData> groups = backendUpdatedConfiguration.getGroups();
		final UiGroupData instanceGroup = createInstanceGroup(GROUP3_ID);
		groups.add(instanceGroup);
		final List<UiGroupData> subGroups = new ArrayList();
		instanceGroup.setSubGroups(subGroups);
		final UiGroupData subInstanceGroup = createInstanceGroup(GROUP3_1_ID);
		subGroups.add(subInstanceGroup);
		subGroups.add(createGroupWithOneCstic(GROUP3_2_ID, "CSTIC3", "VALUE3"));
		final List<UiGroupData> subSubGroups = new ArrayList();
		subInstanceGroup.setSubGroups(subSubGroups);
		subSubGroups.add(createGroupWithOneCstic(GROUP3_1_1_ID, "CSTIC4", "VALUE4"));
	}


	private UiGroupData createInstanceGroup(final String groupId)
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupType(GroupType.INSTANCE);
		group.setId(groupId);
		return group;
	}


}
