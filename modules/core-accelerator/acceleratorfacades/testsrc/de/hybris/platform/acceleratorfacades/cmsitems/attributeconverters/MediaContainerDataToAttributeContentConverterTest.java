/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.attributeconverters;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.MEDIA_CONTAINER_UUID_FIELD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService;
import de.hybris.platform.cmsfacades.mediacontainers.MediaContainerFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;

import java.util.Arrays;
import java.util.HashMap;
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
public class MediaContainerDataToAttributeContentConverterTest
{
	private static final String DESKTOP_FORMAT = "desktop-format";
	private static final String DESKTOP_MEDIA_CODE = "desktop-media-code";
	private static final String DESKTOP_MEDIA_UUID = "desktop-media-uuid";
	private static final String MEDIA_CONTAINER_QUALIFIER = "media-container-qualifier";
	private static final String MEDIA_CONTAINER_UUID = "media-container-uuid";
	private static final String WIDESCREEN_FORMAT = "widescreen-format";
	private static final String WIDESCREEN_MEDIA_CODE = "widescreen-media-code";
	private static final String WIDESCREEN_MEDIA_UUID = "widescreen-media-uuid";

	@Mock
	private CMSMediaFormatService mediaFormatService;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private MediaContainerFacade mediaContainerFacade;
	@Mock
	private PermissionCRUDService permissionCRUDService;
	@Mock
	private ModelService modelService;

	@InjectMocks
	private MediaContainerDataToAttributeContentConverter mediaContainerDataToModelConverter;

	@Mock
	private MediaFormatModel desktopFormat;
	@Mock
	private MediaFormatModel widescreenFormat;
	@Mock
	private MediaModel desktopMedia;
	@Mock
	private MediaModel widescreenMedia;
	@Mock
	private MediaContainerModel mediaContainer;

	@Before
	public void setUp()
	{
		doReturn(true).when(permissionCRUDService).canReadType(anyString());

		doReturn(DESKTOP_FORMAT).when(desktopFormat).getQualifier();
		doReturn(WIDESCREEN_FORMAT).when(widescreenFormat).getQualifier();
		doReturn(Arrays.asList(desktopFormat, widescreenFormat)).when(mediaFormatService).getMediaFormatsByComponentType(any());

		doReturn(DESKTOP_MEDIA_CODE).when(desktopMedia).getCode();
		doReturn(WIDESCREEN_MEDIA_CODE).when(widescreenMedia).getCode();

		doReturn(Optional.of(DESKTOP_MEDIA_UUID)).when(uniqueItemIdentifierService).getItemData(desktopMedia);
		doReturn(Optional.of(WIDESCREEN_MEDIA_UUID)).when(uniqueItemIdentifierService).getItemData(widescreenMedia);

		doReturn(Optional.of(desktopMedia)).when(uniqueItemIdentifierService).getItemModel(DESKTOP_MEDIA_UUID, MediaModel.class);
		doReturn(Optional.of(widescreenMedia)).when(uniqueItemIdentifierService).getItemModel(WIDESCREEN_MEDIA_UUID,
				MediaModel.class);

		doReturn(MEDIA_CONTAINER_QUALIFIER).when(mediaContainer).getQualifier();
		doReturn(Arrays.asList(desktopMedia, widescreenMedia)).when(mediaContainer).getMedias();
	}

	@Test
	public void shouldConvertDataMapToModelWithExistingMediaContainerUuid()
	{
		// this flow re-uses an existing media container
		doReturn(Optional.of(mediaContainer)).when(uniqueItemIdentifierService).getItemModel(MEDIA_CONTAINER_UUID,
				MediaContainerModel.class);

		final Map<String, Object> mediasMap = new HashMap<>();
		mediasMap.put(DESKTOP_FORMAT, DESKTOP_MEDIA_UUID);
		mediasMap.put(WIDESCREEN_FORMAT, WIDESCREEN_MEDIA_UUID);

		final Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(MEDIA_CONTAINER_UUID_FIELD, MEDIA_CONTAINER_UUID);
		dataMap.put(MediaContainerModel.QUALIFIER, MEDIA_CONTAINER_QUALIFIER);
		dataMap.put(MediaContainerModel.MEDIAS, mediasMap);

		final MediaContainerModel convertedModel = mediaContainerDataToModelConverter.convert(dataMap);

		assertThat(convertedModel.getQualifier(), equalTo(MEDIA_CONTAINER_QUALIFIER));
		assertThat(convertedModel.getMedias(), hasSize(2));
	}

	@Test
	public void shouldConvertDataMapToModelAndCreateNewMediaContainerWithQualifier()
	{
		// this flow creates a new media container using a specific qualifier
		doReturn(mediaContainer).when(mediaContainerFacade).createMediaContainer(MEDIA_CONTAINER_QUALIFIER);

		final Map<String, Object> mediasMap = new HashMap<>();
		mediasMap.put(DESKTOP_FORMAT, DESKTOP_MEDIA_UUID);
		mediasMap.put(WIDESCREEN_FORMAT, WIDESCREEN_MEDIA_UUID);

		final Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(MediaContainerModel.QUALIFIER, MEDIA_CONTAINER_QUALIFIER);
		dataMap.put(MediaContainerModel.MEDIAS, mediasMap);

		final MediaContainerModel convertedModel = mediaContainerDataToModelConverter.convert(dataMap);

		assertThat(convertedModel.getQualifier(), equalTo(MEDIA_CONTAINER_QUALIFIER));
		assertThat(convertedModel.getMedias(), hasSize(2));
		verify(mediaContainerFacade).createMediaContainer(MEDIA_CONTAINER_QUALIFIER);
	}

	@Test
	public void shouldConvertDataMapToModelAndCreateNewMediaContainerWithAutoGeneratedQualifier()
	{
		// this flow creates a new media container using a specific qualifier
		doReturn(mediaContainer).when(mediaContainerFacade).createMediaContainer(null);

		final Map<String, Object> mediasMap = new HashMap<>();
		mediasMap.put(DESKTOP_FORMAT, DESKTOP_MEDIA_UUID);
		mediasMap.put(WIDESCREEN_FORMAT, WIDESCREEN_MEDIA_UUID);

		final Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(MediaContainerModel.MEDIAS, mediasMap);

		final MediaContainerModel convertedModel = mediaContainerDataToModelConverter.convert(dataMap);

		assertThat(convertedModel.getQualifier(), equalTo(MEDIA_CONTAINER_QUALIFIER));
		assertThat(convertedModel.getMedias(), hasSize(2));
		verify(mediaContainerFacade).createMediaContainer(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldFailConversionWhenMediaContainerUuidInvalid()
	{
		doReturn(Optional.empty()).when(uniqueItemIdentifierService).getItemModel("invalid-media-container-uuid",
				MediaContainerModel.class);

		final Map<String, Object> mediasMap = new HashMap<>();
		mediasMap.put(DESKTOP_FORMAT, DESKTOP_MEDIA_UUID);
		mediasMap.put(WIDESCREEN_FORMAT, WIDESCREEN_MEDIA_UUID);

		final Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(MEDIA_CONTAINER_UUID_FIELD, "invalid-media-container-uuid");
		dataMap.put(MediaContainerModel.MEDIAS, mediasMap);

		mediaContainerDataToModelConverter.convert(dataMap);
	}

}
