/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import static de.hybris.platform.cmsocc.properties.suppliers.AbstractSmarteditItemPropertiesSupplier.CLASSES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collection;
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
public class SmartEditContentSlotPropertiesSupplierTest
{
	private static final String SMART_EDIT_COMPONENT_CLASS = "smartEditComponent";
	private static final String SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE = "componentType";
	private static final String SMARTEDIT_COMPONENT_ID_ATTRIBUTE = "componentId";
	private static final String SMARTEDIT_COMPONENT_UUID_ATTRIBUTE = "componentUuid";
	private static final String SMARTEDIT_CATALOG_VERSION_UUID_ATTRIBUTE = "catalogVersionUuid";

	private static final String ITEM_UID = "uid";
	private static final String ITEM_UUID = "uuid";
	private static final String ITEM_TYPE = "itemType";
	private static final String CATALOG_VERSION_UUID = "cv_uuid";


	@InjectMocks
	private SmartEditContentSlotPropertiesSupplier supplier;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSItemModel itemModel;
	@Mock
	private ItemData itemData;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private ItemData catalogVersionData;
	@Mock
	private SessionService sessionService;
	@Mock
	private Object previewTicket;

	@Before()
	public void setUp()
	{
		when(itemModel.getCatalogVersion()).thenReturn(catalogVersionModel);
		when(uniqueItemIdentifierService.getItemData(itemModel)).thenReturn(Optional.of(itemData));
		when(uniqueItemIdentifierService.getItemData(catalogVersionModel)).thenReturn(Optional.of(catalogVersionData));

		when(itemModel.getUid()).thenReturn(ITEM_UID);
		when(itemData.getItemId()).thenReturn(ITEM_UUID);
		when(itemModel.getItemtype()).thenReturn(ITEM_TYPE);
		when(catalogVersionData.getItemId()).thenReturn(CATALOG_VERSION_UUID);

		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(previewTicket);
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
		final Map<String, Object> result = supplier.getProperties(itemModel);

		// THEN
		assertThat(result.get(SMARTEDIT_COMPONENT_ID_ATTRIBUTE), is(ITEM_UID));
		assertThat(result.get(SMARTEDIT_COMPONENT_UUID_ATTRIBUTE), is(ITEM_UUID));
		assertThat(result.get(SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE), is(ITEM_TYPE));
		assertThat(result.get(SMARTEDIT_CATALOG_VERSION_UUID_ATTRIBUTE), is(CATALOG_VERSION_UUID));
		assertThat((Collection<Object>) result.get(CLASSES), contains(SMART_EDIT_COMPONENT_CLASS));
	}
}
