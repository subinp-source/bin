/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.uniqueidentifier.functions;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.core.model.media.MediaContainerModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultMediaContainerModelUniqueIdentifierConverterTest
{
	private static final String CATALOG_ID = "catalog-id";
	private static final String CATALOG_VERSION = "catalog-version-id";

	@Mock
	private ObjectFactory<ItemData> itemDataDataFactory;
	@Mock
	private CMSMediaContainerService cmsMediaContainerService;
	@Mock
	private CatalogVersionService catalogVersionService;

	@InjectMocks
	private DefaultMediaContainerModelUniqueIdentifierConverter converter;

	@Mock
	private CatalogModel catalog;
	@Mock
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp()
	{
		when(catalog.getId()).thenReturn(CATALOG_ID);
		when(catalogVersion.getCatalog()).thenReturn(catalog);
		when(catalogVersion.getVersion()).thenReturn(CATALOG_VERSION);
		when(itemDataDataFactory.getObject()).thenReturn(new ItemData());
	}

	@Test
	public void shouldConvertMediaContainerModelToItemData()
	{
		final MediaContainerModel model = new MediaContainerModel();
		model.setCatalogVersion(catalogVersion);
		model.setQualifier("my-container-id");

		final ItemData data = converter.convert(model);

		assertThat(data.getItemId(), not(nullValue()));
		assertThat(data.getItemType(), equalTo(model.getItemtype()));
		assertThat(data.getName(), equalTo(model.getQualifier()));
	}
}
