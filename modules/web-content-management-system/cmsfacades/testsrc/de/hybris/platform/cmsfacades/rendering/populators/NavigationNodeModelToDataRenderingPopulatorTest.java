/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class NavigationNodeModelToDataRenderingPopulatorTest
{
	private static final String NODE_UID = "test-uid";
	private static final String NODE_NAME = "test-name";
	private static final String NODE_TITLE = "test-title";
	private static final String NODE_UUID = "node uuid";

	@InjectMocks
	private NavigationNodeModelToDataRenderingPopulator populator;

	@Mock
	private Converter<CMSNavigationEntryModel, NavigationEntryData> navigationEntryModelToDataConverter;
	@Mock
	private CMSNavigationNodeModel navigationNodeModel;
	@Mock
	private RenderingVisibilityService renderingVisibilityService;
	@Mock
	private CMSNavigationEntryModel visibleNavigationEntryModel;
	@Mock
	private ItemModel visibleNavigationEntryItemModel;
	@Mock
	private CMSNavigationEntryModel notVisibleNavigationEntryModel;
	@Mock
	private ItemModel notVisibleNavigationEntryItemModel;
	@Mock
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	private NavigationNodeData navigationNodeData;

	@Test
	public void shouldPopulateAllProperties()
	{
		// GIVEN
		navigationNodeData = new NavigationNodeData();
		when(navigationNodeModel.getUid()).thenReturn(NODE_UID);
		when(navigationNodeModel.getName()).thenReturn(NODE_NAME);
		when(navigationNodeModel.getTitle()).thenReturn(NODE_TITLE);
		when(navigationNodeModel.getEntries()).thenReturn(Arrays.asList(visibleNavigationEntryModel, notVisibleNavigationEntryModel));
		when(visibleNavigationEntryModel.getItem()).thenReturn(visibleNavigationEntryItemModel);
		when(notVisibleNavigationEntryModel.getItem()).thenReturn(notVisibleNavigationEntryItemModel);
		when(renderingVisibilityService.isVisible(visibleNavigationEntryItemModel)).thenReturn(true);
		when(renderingVisibilityService.isVisible(notVisibleNavigationEntryModel)).thenReturn(false);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(navigationNodeModel)).thenReturn(NODE_UUID);

		when(navigationEntryModelToDataConverter.convert(any())).thenReturn(new NavigationEntryData());

		// WHEN
		populator.populate(navigationNodeModel, navigationNodeData);

		// THEN
		assertThat(navigationNodeData.getUid(), equalTo(NODE_UID));
		assertThat(navigationNodeData.getName(), equalTo(NODE_NAME));
		assertThat(navigationNodeData.getLocalizedTitle(), equalTo(NODE_TITLE));
		assertThat(navigationNodeData.getEntries(), hasSize(1));
		assertThat(navigationNodeData.getUuid(), equalTo(NODE_UUID));
	}

}
