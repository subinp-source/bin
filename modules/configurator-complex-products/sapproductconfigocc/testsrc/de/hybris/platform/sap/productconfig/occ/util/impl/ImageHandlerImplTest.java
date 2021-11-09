/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.GroupWsDTO;
import de.hybris.platform.sap.productconfig.occ.controllers.AbstractProductConfiguratorCCPControllerTCBase;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ImageHandlerImplTest extends AbstractProductConfiguratorCCPControllerTCBase
{

	@InjectMocks
	ImageHandlerImpl classUnderTest;

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

		initializeDataMapperMock();
	}

	@Test
	public void testConvertImages()
	{
		final ConfigurationWsDTO configurationWs = new ConfigurationWsDTO();
		classUnderTest.convertImages(updatedConfigurationAsFacadeDTO, backendUpdatedWsConfiguration);
		final List<ImageWsDTO> images = csticWs.getImages();
		assertNotNull(images);
		assertEquals(1, images.size());
	}

	@Test
	public void testConvertImagesInCstic()
	{
		classUnderTest.convertImagesInCstic(cstic, csticWs);
		final List<ImageWsDTO> images = csticWs.getImages();
		assertNotNull(images);
	}

	@Test(expected = IllegalStateException.class)
	public void testConvertImagesInCsticListNoMatchFound()
	{
		csticWs.setName("Unknown");
		classUnderTest.convertImagesInCstic(cstic, csticWsList);
	}

	@Test
	public void testConvertImagesInGroup()
	{
		classUnderTest.convertImagesInGroup(group, groupsWs);
		final List<ImageWsDTO> images = csticWs.getImages();
		assertNotNull(images);
	}

	@Test
	public void testConvertImagesInGroupCheckSubGroups()
	{
		final UiGroupData rootGroup = new UiGroupData();
		rootGroup.setId(ROOT_GROUP_ID);
		rootGroup.setSubGroups(Arrays.asList(group));
		final GroupWsDTO rootWsGroup = new GroupWsDTO();
		rootWsGroup.setSubGroups(groupsWs);
		rootWsGroup.setId(ROOT_GROUP_ID);
		classUnderTest.convertImagesInGroup(rootGroup, Arrays.asList(rootWsGroup));
		final List<ImageWsDTO> images = csticWs.getImages();
		assertNotNull(images);
	}

	@Test(expected = IllegalStateException.class)
	public void testConvertImagesInGroupNoMatchFound()
	{
		group.setId("Unknown");
		classUnderTest.convertImagesInGroup(group, groupsWs);
	}

	@Test
	public void testConvertImagesInValue()
	{
		classUnderTest.convertImagesInValue(csticValue, csticValueWs);
		final List<ImageWsDTO> images = csticValueWs.getImages();
		assertNotNull(images);
		assertEquals(1, images.size());
	}

	@Test
	public void testConvertImagesInValueList()
	{
		classUnderTest.convertImagesInValue(csticValue, csticValueListWs);
		final List<ImageWsDTO> images = csticValueWs.getImages();
		assertNotNull(images);
	}

	@Test(expected = IllegalStateException.class)
	public void testConvertImagesInValueListNoMatch()
	{
		csticValueWs.setKey("Unknown");
		classUnderTest.convertImagesInValue(csticValue, csticValueListWs);
	}

}
