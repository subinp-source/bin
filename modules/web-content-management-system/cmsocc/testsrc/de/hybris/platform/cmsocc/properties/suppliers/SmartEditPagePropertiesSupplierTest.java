/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import static de.hybris.platform.cmsocc.properties.suppliers.AbstractSmarteditItemPropertiesSupplier.CLASSES;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SmartEditPagePropertiesSupplierTest
{
	private static final String CSS_CODE_PREFIX_UID = "smartedit-page-uid-";
	private static final String CSS_CODE_PREFIX_UUID = "smartedit-page-uuid-";
	private static final String CSS_CODE_PREFIX_CATALOG_VERSION_UUID = "smartedit-catalog-version-uuid-";

	private static final String ITEM_UID = "itemUid";
	private static final String ITEM_UUID = "itemUuid";
	private static final String CATALOG_VERSION_UUID = "catalogVersionUuid";

	@InjectMocks
	private SmartEditPagePropertiesSupplier supplier;
	@Mock
	private SessionService sessionService;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private CMSItemModel itemModel;
	@Mock
	private Object previewTicket;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private ItemData itemData;
	@Mock
	private ItemData catalogVersionData;


	@Before
	public void setUp()
	{
		when(itemModel.getCatalogVersion()).thenReturn(catalogVersionModel);
		when(uniqueItemIdentifierService.getItemData(itemModel)).thenReturn(Optional.of(itemData));
		when(uniqueItemIdentifierService.getItemData(catalogVersionModel)).thenReturn(Optional.of(catalogVersionData));

		when(itemModel.getUid()).thenReturn(ITEM_UID);
		when(itemData.getItemId()).thenReturn(ITEM_UUID);
		when(catalogVersionData.getItemId()).thenReturn(CATALOG_VERSION_UUID);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfItemDataCanNotBeRetrievedForItemModel()
	{
		// GIVEN
		when(uniqueItemIdentifierService.getItemData(itemModel)).thenReturn(Optional.empty());

		// WHEN
		supplier.getProperties(itemModel);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfItemDataCanNotBeRetrievedForCatalogVersion()
	{
		// GIVEN
		when(uniqueItemIdentifierService.getItemData(catalogVersionModel)).thenReturn(Optional.empty());

		// WHEN
		supplier.getProperties(itemModel);
	}

	@Test
	public void shouldPopulateAttributes()
	{
		// WHEN
		final Map<String, Object> properties = supplier.getProperties(itemModel);

		// THEN
		final List<String> result = Arrays.asList( //
				CSS_CODE_PREFIX_UID + itemModel.getUid(), //
				CSS_CODE_PREFIX_UUID + itemData.getItemId(), //
				CSS_CODE_PREFIX_CATALOG_VERSION_UUID + catalogVersionData.getItemId() //
		);
		assertEquals(properties.get(CLASSES), result);
	}
}
