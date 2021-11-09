/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.attributevalidators;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.MEDIA_CONTAINER_UUID_FIELD;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.SESSION_CLONE_COMPONENT_SOURCE_MAP;
import static java.util.Locale.ENGLISH;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.cmsitems.CloneComponentContextProvider;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MediaContainerAttributeContentValidatorTest
{

	private static final String VALID_MEDIA_CODE = "valid-media-code";
	private static final String INVALID_MEDIA_CODE = "invalid-media-code";
	private static final String WIDESCREEN = "widescreen";
	private static final String MOBILE = "mobile";
	private static final String EN = "en";

	@Mock
	private ValidationErrorsProvider validationErrorsProvider;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CMSMediaContainerService cmsMediaContainerService;
	@Mock
	private CloneComponentContextProvider cloneComponentContextProvider;

	private final List<String> cmsRequiredMediaFormatQualifiers = Arrays.asList(WIDESCREEN);

	@InjectMocks
	private MediaContainerAttributeContentValidator validator;

	@Mock
	private AttributeDescriptorModel attributeDescriptor;
	@Mock
	private MediaModel mediaModel;
	@Mock
	private ValidationErrors validationErrors;
	@Mock
	private LanguageData languageData;
	@Mock
	private CatalogVersionModel catalogVersion;

	@Before
	public void setup()
	{
		validator.setCmsRequiredMediaFormatQualifiers(cmsRequiredMediaFormatQualifiers);

		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);

		when(uniqueItemIdentifierService.getItemModel(VALID_MEDIA_CODE, MediaModel.class)).thenReturn(Optional.of(mediaModel));
		when(uniqueItemIdentifierService.getItemModel(INVALID_MEDIA_CODE, MediaModel.class)).thenReturn(Optional.empty());
		when(Boolean.valueOf(languageData.isRequired())).thenReturn(Boolean.TRUE);
		when(languageData.getIsocode()).thenReturn(EN);
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(languageData));
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(null);
	}

	@Test
	public void testWhenMediaFormatIsPresentWithValidMediaCode_shouldNotAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		validator.validate(localizedContainerMap, attributeDescriptor);
		verifyZeroInteractions(validationErrorsProvider);
	}

	@Test
	public void testWhenOnlyOptionalMediaFormatIsPresentWithValidMediaCode_shouldAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(MOBILE, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		doReturn(MediaModel.MEDIAFORMAT).when(attributeDescriptor).getQualifier();

		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		assertThat(errors, not(empty()));
		assertThat(errors.get(0).getErrorCode(), equalTo(CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N));
		assertThat(errors.get(0).getField(), equalTo(MediaModel.MEDIAFORMAT));
		assertThat(errors.get(0).getLanguage(), equalTo(ENGLISH.toLanguageTag()));
	}

	@Test
	public void testWhenMediaFormatIsPresentWithInValidMediaCode_shouldAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, INVALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		doReturn(MediaModel.CODE).when(attributeDescriptor).getQualifier();

		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		assertThat(errors, not(empty()));
		assertThat(errors.get(0).getErrorCode(), equalTo(CmsfacadesConstants.INVALID_MEDIA_FORMAT_MEDIA_CODE_L10N));
		assertThat(errors.get(0).getField(), equalTo(MediaModel.CODE));
		assertThat(errors.get(0).getLanguage(), equalTo(ENGLISH.toLanguageTag()));
	}

	@Test
	public void testWhenMediaContainerQualifierAlreadyExists_shouldAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MediaContainerModel.QUALIFIER, "media-container-qualifier");
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);
		containerMap.put(MediaContainerModel.CATALOGVERSION, "catalog-version-uuid");

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		doReturn(Optional.of(catalogVersion)).when(uniqueItemIdentifierService).getItemModel(any(), any());
		doThrow(new AmbiguousIdentifierException("found many items with the same qualifier")).when(cmsMediaContainerService)
				.getMediaContainerForQualifier(any(), any());
		doReturn(MediaModel.CODE).when(attributeDescriptor).getQualifier();

		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		assertThat(errors, not(empty()));
		assertThat(errors.get(0).getErrorCode(), equalTo(CmsfacadesConstants.MEDIA_CONTAINER_WITH_QUALIFIER_ALREADY_EXIST_L10N));
		assertThat(errors.get(0).getField(), equalTo(AbstractMediaContainerComponentModel.MEDIA));
		assertThat(errors.get(0).getLanguage(), equalTo(ENGLISH.toLanguageTag()));
	}

	@Test
	public void testWhenMediaContainerUuidInvalid_shouldAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MEDIA_CONTAINER_UUID_FIELD, "invalid-uuid");
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		doReturn(Optional.of(catalogVersion)).when(uniqueItemIdentifierService).getItemModel(any(), any());
		doReturn(Optional.empty()).when(uniqueItemIdentifierService).getItemModel("invalid-uuid", MediaContainerModel.class);

		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		assertThat(errors, not(empty()));
		assertThat(errors.get(0).getErrorCode(), equalTo(CmsfacadesConstants.FIELD_INVALID_UUID_L10N));
		assertThat(errors.get(0).getField(), equalTo(MEDIA_CONTAINER_UUID_FIELD));
		assertThat(errors.get(0).getLanguage(), equalTo(ENGLISH.toLanguageTag()));
	}

	@Test
	public void testWhenChangingExistingMediaContainerQualifier_shouldAddError()
	{
		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MEDIA_CONTAINER_UUID_FIELD, "media-container-uuid");
		containerMap.put(MediaContainerModel.QUALIFIER, "new-container-qualifier");
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		final MediaContainerModel mediaContainer = Mockito.mock(MediaContainerModel.class);
		doReturn("old-container-qualifier").when(mediaContainer).getQualifier();
		doReturn(Optional.of(catalogVersion)).when(uniqueItemIdentifierService).getItemModel(any(), any());
		doReturn(Optional.of(mediaContainer)).when(uniqueItemIdentifierService).getItemModel("media-container-uuid",
				MediaContainerModel.class);

		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		assertThat(errors, not(empty()));
		assertThat(errors.get(0).getErrorCode(), equalTo(CmsfacadesConstants.MEDIA_CONTAINER_UUID_NOT_MODIFIABLE_L10N));
		assertThat(errors.get(0).getField(), equalTo(MEDIA_CONTAINER_UUID_FIELD));
		assertThat(errors.get(0).getLanguage(), equalTo(ENGLISH.toLanguageTag()));
	}

	@Test
	public void shouldNotAddErrorWhenExistingQualifierChangedAndCloningInProgress()
	{
		// GIVEN
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(new HashMap<>());

		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MEDIA_CONTAINER_UUID_FIELD, "media-container-uuid");
		containerMap.put(MediaContainerModel.QUALIFIER, "new-container-qualifier");
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		final MediaContainerModel mediaContainer = Mockito.mock(MediaContainerModel.class);
		doReturn("old-container-qualifier").when(mediaContainer).getQualifier();
		doReturn(Optional.of(catalogVersion)).when(uniqueItemIdentifierService).getItemModel(any(), any());
		doReturn(Optional.of(mediaContainer)).when(uniqueItemIdentifierService).getItemModel("media-container-uuid",
				MediaContainerModel.class);

		// WHEN
		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		// THEN
		assertThat(errors, empty());
	}

	@Test
	public void shouldAddErrorWhenMediaContainerWithProvidedQualifierExistsAndCloningInProgress()
	{
		// GIVEN
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(new HashMap<>());

		final Map<String, Object> formatMap = new HashMap<>();
		formatMap.put(WIDESCREEN, VALID_MEDIA_CODE);

		final Map<String, Object> containerMap = new HashMap<>();
		containerMap.put(MEDIA_CONTAINER_UUID_FIELD, "media-container-uuid");
		containerMap.put(MediaContainerModel.QUALIFIER, "new-container-qualifier");
		containerMap.put(MediaContainerModel.MEDIAS, formatMap);
		containerMap.put(MediaContainerModel.CATALOGVERSION, "catalog-version-uuid");

		final Map<String, Map<String, Object>> localizedContainerMap = new HashMap<>();
		localizedContainerMap.put(EN, containerMap);

		final MediaContainerModel mediaContainer = Mockito.mock(MediaContainerModel.class);
		final MediaContainerModel existingMediaContainer = Mockito.mock(MediaContainerModel.class);
		doReturn("media-container-uuid").when(mediaContainer).getQualifier();
		doReturn(Optional.of(catalogVersion)).when(uniqueItemIdentifierService).getItemModel(any(), any());
		doReturn(Optional.of(mediaContainer)).when(uniqueItemIdentifierService).getItemModel("media-container-uuid",
				MediaContainerModel.class);
		doReturn(existingMediaContainer).when(cmsMediaContainerService).getMediaContainerForQualifier(any(), any());

		// WHEN
		final List<ValidationError> errors = validator.validate(localizedContainerMap, attributeDescriptor);

		// THEN
		assertThat(errors, not(empty()));
	}
}
