/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentSlotModelToDataRenderingPopulatorTest
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private final String SLOT_UID = "some slot uid";
	private final String SLOT_NAME = "some slot name";
	private final String SLOT_POSITION = "some slot position";
	private final String CATALOG_VERSION_UUID = "some catalog version";
	private final String SLOT_UUID = "some slot uuid";

	@Mock
	private AbstractCMSComponentModel componentModel1;

	@Mock
	private AbstractCMSComponentModel componentModel2;

	@Mock
	private AbstractCMSComponentModel componentModel3;

	@Mock
	private AbstractCMSComponentData convertedComponentData1;

	@Mock
	private AbstractCMSComponentData convertedComponentData2;

	@Mock
	private AbstractCMSComponentData convertedComponentData3;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private ContentSlotModel contentSlotModel;

	@Mock
	private ContentSlotData contentSlotData;

	@Mock
	private RenderingVisibilityService renderingVisibilityService;

	@Mock
	private Converter<AbstractCMSComponentModel, AbstractCMSComponentData> cmsComponentRenderingDataConverter;

	@Mock
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	@Mock
	private Populator<ItemModel, Map<String, Object>> customPropertiesPopulator;

	@Mock
	private RenderingCacheService<AbstractCMSComponentData> renderingCacheService;

	@Captor
	protected ArgumentCaptor<Map<String, Object>> captor;

	@Spy
	@InjectMocks
	private ContentSlotModelToDataRenderingPopulator contentSlotRenderingPopulator;

	private PageContentSlotData pageContentSlotData;

	// --------------------------------------------------------------------------
	// Test Setup
	// --------------------------------------------------------------------------
	@Before
	@SuppressWarnings("unchecked")
	public void setUp()
	{
		// Slot Data
		pageContentSlotData = new PageContentSlotData();
		when(contentSlotData.getContentSlot()).thenReturn(contentSlotModel);
		when(contentSlotData.getUid()).thenReturn(SLOT_UID);
		when(contentSlotData.getName()).thenReturn(SLOT_NAME);
		when(contentSlotData.getPosition()).thenReturn(SLOT_POSITION);
		when(contentSlotData.isOverrideSlot()).thenReturn(false); // By default is not an override slot.
		when(contentSlotData.isFromMaster()).thenReturn(false); // By default is not coming from master.

		// Catalog Version
		when(contentSlotModel.getCatalogVersion()).thenReturn(catalogVersion);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(catalogVersion)).thenReturn(CATALOG_VERSION_UUID);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(contentSlotModel)).thenReturn(SLOT_UUID);

		// Component Converter
		when(renderingCacheService.cacheOrElse(any(), any())).then(params -> {
			final AbstractCMSComponentModel renderingData = params.getArgumentAt(0, AbstractCMSComponentModel.class);

			if (renderingData.equals(componentModel1))
			{
				return convertedComponentData1;
			}
			else if (renderingData.equals(componentModel2))
			{
				return convertedComponentData2;
			}

			return convertedComponentData3;
		});

		// Rendering Utils
		when(renderingVisibilityService.isVisible(any())).thenReturn(true);
	}


	// --------------------------------------------------------------------------
	// Tests
	// --------------------------------------------------------------------------
	@Test
	public void whenPopulatorIsCalled_ThenItPopulatesAllTheRequiredProperties()
	{
		// WHEN
		contentSlotRenderingPopulator.populate(contentSlotData, pageContentSlotData);

		// THEN
		assertThat(pageContentSlotData.getSlotId(), is(SLOT_UID));
		assertThat(pageContentSlotData.getName(), is(SLOT_NAME));
		assertThat(pageContentSlotData.getPosition(), is(SLOT_POSITION));
		assertThat(pageContentSlotData.getCatalogVersion(), is(CATALOG_VERSION_UUID));
		assertThat(pageContentSlotData.isSlotShared(), is(false)); // Default should be false.
		assertThat(pageContentSlotData.getSlotUuid(), is(SLOT_UUID));
	}

	@Test
	public void whenPopulatorIsCalledThenOtherPropertiesFieldIsPopulated()
	{
		// WHEN
		contentSlotRenderingPopulator.populate(contentSlotData, pageContentSlotData);

		// THEN
		verify(customPropertiesPopulator).populate(eq(contentSlotModel), captor.capture());
		assertThat(pageContentSlotData.getOtherProperties(), is(captor.getValue()));
	}

	@Test
	public void givenSlotHasNoComponents_WhenPopulatorIsCalled_ThenItSetsAnEmptyCollection()
	{
		// GIVEN
		setComponentsInSlot();

		// WHEN
		contentSlotRenderingPopulator.populate(contentSlotData, pageContentSlotData);

		// THEN
		assertThat(pageContentSlotData.getComponents().isEmpty(), is(true));
	}

	@Test
	public void givenSlotHasNonRestrictedComponents_WhenPopulatorIsCalled_ThenItSetsTheConvertedComponents()
	{
		// GIVEN
		setComponentsInSlot(componentModel1, componentModel2, componentModel3);

		// WHEN
		contentSlotRenderingPopulator.populate(contentSlotData, pageContentSlotData);

		// THEN
		assertThat(pageContentSlotData.getComponents(),
				contains(convertedComponentData1, convertedComponentData2, convertedComponentData3));
	}

	@Test
	public void givenSlotHasSomeRestrictedComponents_WhenPopulatorIsCalled_ThenItOnlySetsTheAllowedConvertedComponents()
	{
		// GIVEN
		setComponentsInSlot(componentModel1, componentModel2, componentModel3);
		markComponentAsDisallowed(componentModel3);

		// WHEN
		contentSlotRenderingPopulator.populate(contentSlotData, pageContentSlotData);

		// THEN
		assertThat(pageContentSlotData.getComponents(), contains(convertedComponentData1, convertedComponentData2));
	}

	// --------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------
	protected void setSlotAsOverride()
	{
		when(contentSlotData.isOverrideSlot()).thenReturn(true);
	}

	protected void setSlotAsFromMaster()
	{
		when(contentSlotData.isFromMaster()).thenReturn(true);
	}

	protected void setComponentsInSlot(final AbstractCMSComponentModel... components)
	{
		when(contentSlotModel.getCmsComponents()).thenReturn(Arrays.asList(components));
	}

	protected void markComponentAsDisallowed(final AbstractCMSComponentModel componentModel)
	{
		when(renderingVisibilityService.isVisible(componentModel)).thenReturn(false);
	}
}
