/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.attributeconverters;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.MEDIA_CONTAINER_UUID_FIELD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MediaContainerAttributeToDataContentConverterTest
{
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private PermissionCRUDService permissionCRUDService;
	@InjectMocks
	private MediaContainerAttributeToDataContentConverter mediaContainerDataConverter;

	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private MediaContainerModel mediaContainer;
	@Mock
	private MediaModel media;
	@Mock
	private MediaFormatModel mediaFormat;
	@Mock
	private ItemData mediaItemData;
	@Mock
	private ItemData mediaContainerItemData;
	@Mock
	private ItemData catalogVersionItemData;

	@Test
	public void shouldConvertMediaContainerModelToData()
	{
		// GIVEN
		doReturn(true).when(permissionCRUDService).canReadType(anyString());

		doReturn(mediaFormat).when(media).getMediaFormat();

		doReturn("media-format-id").when(mediaFormat).getQualifier();

		doReturn(Arrays.asList(media)).when(mediaContainer).getMedias();
		doReturn("media-container-id").when(mediaContainer).getQualifier();
		doReturn(catalogVersion).when(mediaContainer).getCatalogVersion();

		doReturn("media-uuid").when(mediaItemData).getItemId();
		doReturn("media-container-uuid").when(mediaContainerItemData).getItemId();
		doReturn("catalog-version-uuid").when(catalogVersionItemData).getItemId();
		doReturn(Optional.of(mediaItemData)).when(uniqueItemIdentifierService).getItemData(media);
		doReturn(Optional.of(mediaContainerItemData)).when(uniqueItemIdentifierService).getItemData(mediaContainer);
		doReturn(Optional.of(catalogVersionItemData)).when(uniqueItemIdentifierService).getItemData(catalogVersion);

		// WHEN
		final Map<String, Object> convertedMap = mediaContainerDataConverter.convert(mediaContainer);

		// THEN
		assertThat(convertedMap, hasEntry(equalTo(MediaContainerModel.MEDIAS), not(nullValue())));
		assertThat(convertedMap, hasEntry(equalTo(MediaContainerModel.QUALIFIER), equalTo("media-container-id")));
		assertThat(convertedMap, hasEntry(equalTo(MediaContainerModel.CATALOGVERSION), equalTo("catalog-version-uuid")));
		assertThat(convertedMap, hasEntry(equalTo(MEDIA_CONTAINER_UUID_FIELD), equalTo("media-container-uuid")));
	}

}
