/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemConverter;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.core.model.ItemModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSComponentModelToDataRenderingPopulatorTest
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private final String CATALOG_VERSION_UUID = "version";
	private final String COMPONENT_UID = "some uid";
	private final String COMPONENT_UUID = "component uuid";
	private final String COMPONENT_NAME = "some component name";
	private final String COMPONENT_TYPE_CODE = "some component type code";
	private final Date MODIFIED_TIME = new Date(System.currentTimeMillis());

	@Mock
	private AbstractCMSComponentModel component;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CMSItemConverter converter;

	@Mock
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	@InjectMocks
	private CMSComponentModelToDataRenderingPopulator componentRenderingPopulator;

	private Map<String, Object> otherPropertiesMap;

	// --------------------------------------------------------------------------
	// Test Setup
	// --------------------------------------------------------------------------
	@Before
	public void setUp()
	{
		when(component.getUid()).thenReturn(COMPONENT_UID);
		when(component.getName()).thenReturn(COMPONENT_NAME);
		when(component.getItemtype()).thenReturn(COMPONENT_TYPE_CODE);
		when(component.getModifiedtime()).thenReturn(MODIFIED_TIME);
		when(component.getCatalogVersion()).thenReturn(catalogVersion);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(catalogVersion)).thenReturn(CATALOG_VERSION_UUID);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(component)).thenReturn(COMPONENT_UUID);

		otherPropertiesMap = new HashMap<>();
		otherPropertiesMap.put("KEY", "VALUE");
		when(converter.convert(component)).thenReturn(otherPropertiesMap);
	}

	// --------------------------------------------------------------------------
	// Tests
	// --------------------------------------------------------------------------
	@Test
	public void whenPopulatorCalled_ItPopulatesAllTheProperties()
	{
		// GIVEN
		final AbstractCMSComponentData componentData = new AbstractCMSComponentData();

		// WHEN
		componentRenderingPopulator.populate(component, componentData);

		// THEN
		assertThat(componentData.getUid(), is(COMPONENT_UID));
		assertThat(componentData.getTypeCode(), is(COMPONENT_TYPE_CODE));
		assertThat(componentData.getName(), is(COMPONENT_NAME));
		assertThat(componentData.getModifiedtime(), is(MODIFIED_TIME));
		assertThat(componentData.getCatalogVersion(), is(CATALOG_VERSION_UUID));
		assertThat(componentData.getOtherProperties(), is(otherPropertiesMap));
		assertThat(componentData.getUuid(), is(COMPONENT_UUID));
	}
}
