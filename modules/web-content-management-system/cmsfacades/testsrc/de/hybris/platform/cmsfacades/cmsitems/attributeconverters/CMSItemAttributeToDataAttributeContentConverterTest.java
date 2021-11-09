/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSItemAttributeToDataAttributeContentConverterTest
{

	private static final String ITEM_UID = "ITEM_UID";

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSItemModel cmsItem;

	@InjectMocks
	private final UniqueIdentifierAttributeToDataContentConverter<CMSItemModel> converter = new UniqueIdentifierAttributeToDataContentConverter<CMSItemModel>()
	{
		// Intentionally left empty.
	};


	@Before
	public void setup()
	{
		final ItemData itemData = new ItemData();
		itemData.setItemId(ITEM_UID);

		when(uniqueItemIdentifierService.getItemData(cmsItem)).thenReturn(Optional.of(itemData));

	}

	@Test
	public void whenConvertNullValueReturnsNull()
	{
		assertThat(converter.convert(null), nullValue());
	}

	@Test
	public void shouldConvertValidCMSItemModel()
	{
		final String value = converter.convert(cmsItem);

		assertThat(value, is(ITEM_UID));
	}


}
