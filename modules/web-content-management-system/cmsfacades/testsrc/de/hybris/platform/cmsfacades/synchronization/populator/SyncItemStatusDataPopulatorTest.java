/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.populator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.ItemTypeData;
import de.hybris.platform.cmsfacades.data.SyncItemInfoJobStatusData;
import de.hybris.platform.cmsfacades.data.SyncItemStatusData;
import de.hybris.platform.cmsfacades.data.SynchronizationItemDetailsData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SyncItemStatusDataPopulatorTest
{

	private static final String SYNC_STATUS = "SYNC_STATUS";
	private static final String ITEM_TYPE = "ITEM_TYPE";
	private static final String ITEM_ID = "ITEM_ID";
	private static final String ITEM_NAME = "ITEM_NAME";
	private static final String CATALOG_VERSION_ITEM_ID = "CATALOG_VERSION_ITEM_ID";

	@Mock
	private Converter<SyncItemInfoJobStatusData, ItemTypeData> itemTypeConverter;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@InjectMocks
	private SyncItemStatusDataPopulator populator;

	private final SynchronizationItemDetailsData source = mock(SynchronizationItemDetailsData.class);
	private final SyncItemStatusData target = new SyncItemStatusData();
	private final ItemTypeData itemType = mock(ItemTypeData.class);
	private final ItemData itemData = mock(ItemData.class);
	private final ItemData catalogVersionItemData = mock(ItemData.class);
	private final Optional<ItemData> optionalItemData = Optional.of(itemData);
	private final Optional<ItemData> optionalCatalogVersionItemData = Optional.of(catalogVersionItemData);
	private final CMSItemModel itemModel = mock(CMSItemModel.class);
	private final Date modifiedTime = new Date();

	@Before
	public void setup()
	{

		// source mocks
		when(source.getItem()).thenReturn(itemModel);
		when(source.getRelatedItemStatuses()).thenReturn(Arrays.asList(buildStatus(), buildStatus()));
		when(source.getSyncStatus()).thenReturn(SYNC_STATUS);
		when(itemModel.getModifiedtime()).thenReturn(modifiedTime);

		// itemData mocks
		when(itemData.getItemId()).thenReturn(ITEM_ID);
		when(itemData.getItemType()).thenReturn(ITEM_TYPE);
		when(itemData.getName()).thenReturn(ITEM_NAME);

		// catalog version item data mocks
		when(catalogVersionItemData.getItemId()).thenReturn(CATALOG_VERSION_ITEM_ID);
		when(itemModel.getCatalogVersion()).thenReturn(catalogVersionModel);

		// uniqueItemIdentifierService mocks
		when(uniqueItemIdentifierService.getItemData(itemModel)).thenReturn(optionalItemData);
		when(uniqueItemIdentifierService.getItemData(itemModel.getCatalogVersion())).thenReturn(optionalCatalogVersionItemData);

		// itemTypeConverter mocks
		when(itemTypeConverter.convert(any())).thenReturn(itemType);
	}

	protected SyncItemInfoJobStatusData buildStatus()
	{
		final SyncItemInfoJobStatusData status = new SyncItemInfoJobStatusData();
		status.setItem(itemModel);
		status.setSyncStatus(SYNC_STATUS);
		return status;
	}

	@Test
	public void testPopulatorWillNotAddDuplicateItemsOutOfSync()
	{
		when(source.getRelatedItemStatuses()).thenReturn(Arrays.asList(buildStatus(), buildStatus()));
		populator.populate(source, target);
		assertThat(target.getDependentItemTypesOutOfSync().size(), is(1));
	}

	@Test
	public void testPopulatorWithoutDependentItemList()
	{
		when(source.getRelatedItemStatuses()).thenReturn(null);
		populator.populate(source, target);
		assertThat(target.getDependentItemTypesOutOfSync().size(), is(0));
	}

	@Test
	public void testPopulatorWithUniqueIdentifierService()
	{
		populator.populate(source, target);
		assertThat(target.getItemId(), is(ITEM_ID));
		assertThat(target.getItemType(), is(ITEM_TYPE));
		assertThat(target.getName(), is(ITEM_NAME));
	}

	@Test
	public void shouldPopulateCatalogVersionUuid()
	{
		populator.populate(source, target);
		assertThat(target.getCatalogVersionUuid(), is(CATALOG_VERSION_ITEM_ID));
	}
}
