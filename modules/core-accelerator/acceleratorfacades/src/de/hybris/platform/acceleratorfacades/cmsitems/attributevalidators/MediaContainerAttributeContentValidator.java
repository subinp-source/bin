/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.attributevalidators;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.MEDIA_CONTAINER_UUID_FIELD;
import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_INVALID_UUID_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MEDIA_FORMAT_REQUIRED_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.INVALID_MEDIA_FORMAT_MEDIA_CODE_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.MEDIA_CONTAINER_UUID_NOT_MODIFIABLE_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.MEDIA_CONTAINER_WITH_QUALIFIER_ALREADY_EXIST_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.SESSION_CLONE_COMPONENT_SOURCE_MAP;
import static org.springframework.util.StringUtils.isEmpty;

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.cmsitems.CloneComponentContextProvider;
import de.hybris.platform.cmsfacades.cmsitems.attributevalidators.AbstractAttributeContentValidator;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Media Container attribute content validator adds validation errors when value Map fails to meet format and media,
 * required languages and media content.
 */
@SuppressWarnings("java:S1874")
public class MediaContainerAttributeContentValidator extends AbstractAttributeContentValidator<Map<String, Map<String, Object>>>
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private List<String> cmsRequiredMediaFormatQualifiers;
	private LanguageFacade languageFacade;
	private CMSMediaContainerService cmsMediaContainerService;
	private CloneComponentContextProvider cloneComponentContextProvider;

	@Override
	public List<ValidationError> validate(final Map<String, Map<String, Object>> value, final AttributeDescriptorModel attribute)
	{
		final List<ValidationError> errors = new ArrayList<>();

		if (value == null)
		{
			// validate all required languages
			getLanguageFacade().getLanguages().stream() //
					.filter(LanguageData::isRequired) //
					.forEach(languageData -> validateMediaFormat(attribute, languageData.getIsocode(), null, errors));
		}
		else
		{
			// validate media formats
			getLanguageFacade().getLanguages().stream().filter(LanguageData::isRequired).forEach(language -> {
				final Map<String, Object> localizedMediaContainer = value.get(language.getIsocode());
				final Map<String, String> formatUuidMap = (Map<String, String>) localizedMediaContainer
						.get(MediaContainerModel.MEDIAS);
				validateMediaFormat(attribute, language.getIsocode(), formatUuidMap, errors);
			});
			// validate media codes
			value.entrySet().stream().forEach(entry -> {
				final String language = entry.getKey();
				final Map<String, Object> localizedMediaContainer = entry.getValue();

				validateMediaContainerForLanguage(attribute, errors, language, localizedMediaContainer);
			});
		}
		return errors;
	}

	protected void validateMediaContainerForLanguage(final AttributeDescriptorModel attribute, final List<ValidationError> errors,
			final String language, final Map<String, Object> localizedMediaContainer)
	{
		if (Objects.nonNull(localizedMediaContainer))
		{
			final String qualifier = (String) localizedMediaContainer.get(MediaContainerModel.QUALIFIER);
			final String catalogVersionUuid = (String) localizedMediaContainer.get(MediaContainerModel.CATALOGVERSION);
			final String mediaContainerUuid = (String) localizedMediaContainer.get(MEDIA_CONTAINER_UUID_FIELD);

			final boolean qualifierExists = testQualifierExists(qualifier, catalogVersionUuid);

			final Map<String, String> formatUuidMap = (Map<String, String>) localizedMediaContainer.get(MediaContainerModel.MEDIAS);

			if (isCloneComponentFlow() && Objects.nonNull(qualifier) && qualifierExists)
			{
				// create media container flow during cloning
				errors.add(newValidationErrorBuilder() //
						.field(AbstractMediaContainerComponentModel.MEDIA) //
						.errorCode(MEDIA_CONTAINER_WITH_QUALIFIER_ALREADY_EXIST_L10N) //
						.language(language) //
						.errorArgs(new Object[]
								{ language }) //
						.rejectedValue(qualifier) //
						.build());
			}
			else
			// For the old media widget, we will automatically create the media container (qualifier and mediaContainerUuid are NULL)
			if (Objects.nonNull(qualifier) && Objects.isNull(mediaContainerUuid) && qualifierExists)
			{
				// create media container flow
				errors.add(newValidationErrorBuilder() //
						.field(AbstractMediaContainerComponentModel.MEDIA) //
						.errorCode(MEDIA_CONTAINER_WITH_QUALIFIER_ALREADY_EXIST_L10N) //
						.language(language) //
						.errorArgs(new Object[]
						{ language }) //
						.rejectedValue(qualifier) //
						.build());
			}
			else if (Objects.nonNull(mediaContainerUuid))
			{
				// update media container flow
				validateMediaContainer(errors, language, qualifier, mediaContainerUuid);
			}

			validateAllMediaCodes(attribute, language, formatUuidMap, errors);
		}
	}

	/**
	 * Validates that the media container uuid is valid or the qualifier of an existing media container is not being
	 * modified
	 *
	 * @param errors
	 *           the list of errors
	 * @param language
	 *           the String Locale of this MediaContainer.
	 * @param qualifier
	 *           the media container qualifier
	 * @param mediaContainerUuid
	 *           the media container uuid
	 */
	protected void validateMediaContainer(final List<ValidationError> errors, final String language, final String qualifier,
			final String mediaContainerUuid)
	{
		final Optional<MediaContainerModel> originalModelOptional = getUniqueItemIdentifierService()
				.getItemModel(mediaContainerUuid, MediaContainerModel.class);

		if (originalModelOptional.isPresent())
		{
			if (!isCloneComponentFlow() && Objects.nonNull(qualifier) && !qualifier.equals(originalModelOptional.get().getQualifier()))
			{
				errors.add(newValidationErrorBuilder() //
						.field(MEDIA_CONTAINER_UUID_FIELD) //
						.errorCode(MEDIA_CONTAINER_UUID_NOT_MODIFIABLE_L10N) //
						.language(language) //
						.errorArgs(new Object[]
						{ language }) //
						.rejectedValue(MEDIA_CONTAINER_UUID_FIELD) //
						.build());
			}
		}
		else
		{
			errors.add(newValidationErrorBuilder() //
					.field(MEDIA_CONTAINER_UUID_FIELD) //
					.errorCode(FIELD_INVALID_UUID_L10N) //
					.language(language) //
					.errorArgs(new Object[]
					{ language }) //
					.rejectedValue(MEDIA_CONTAINER_UUID_FIELD) //
					.build());
		}
	}

	@SuppressWarnings(
	{ "squid:S1166", "squid:S3655" })
	protected boolean testQualifierExists(final String qualifier, final String catalogVersionUuid)
	{
		if (Objects.nonNull(qualifier) && Objects.nonNull(catalogVersionUuid))
		{
			final CatalogVersionModel catalogVersion = getUniqueItemIdentifierService()
					.getItemModel(catalogVersionUuid, CatalogVersionModel.class).get();

			final Predicate<String> predicate = (final String value) -> {
				try
				{
					final MediaContainerModel mediaContainerModel = getCmsMediaContainerService().getMediaContainerForQualifier(value,
							catalogVersion);
					return Objects.nonNull(mediaContainerModel);
				}
				catch (final UnknownIdentifierException e)
				{
					return false;
				}
				catch (final AmbiguousIdentifierException e)
				{
					return true;
				}
			};
			return predicate.test(qualifier);
		}
		return false;
	}

	/**
	 * Validates all media codes from the media container
	 *
	 * @param attribute
	 *           the mediaContainer attribute
	 * @param language
	 *           the String Locale of this MediaContainer.
	 * @param formatUuidMap
	 *           the mediaFormatUuidMap containing the media container data
	 * @param errors
	 *           the list of errors
	 */
	protected void validateAllMediaCodes(final AttributeDescriptorModel attribute, final String language,
			final Map<String, String> formatUuidMap, final List<ValidationError> errors)
	{
		if (formatUuidMap == null)
		{
			return;
		}
		formatUuidMap.entrySet() //
				.stream() //
				.filter(entry -> Objects.nonNull(entry.getValue())) //
				.forEach(entry -> {
					final String format = entry.getKey();
					final String mediaCode = entry.getValue();
					final Optional<MediaModel> mediaModel = getUniqueItemIdentifierService().getItemModel(mediaCode, MediaModel.class);
					if (!mediaModel.isPresent())
					{
						errors.add(newValidationErrorBuilder() //
								.field(attribute.getQualifier()) //
								.errorCode(INVALID_MEDIA_FORMAT_MEDIA_CODE_L10N) //
								.language(language) //
								.errorArgs(new Object[]
						{ language, format }) //
								.rejectedValue(formatUuidMap) //
								.build());
					}
				});
	}

	/**
	 * Validate required media formats
	 *
	 * @param attribute
	 *           the mediaContainer attribute
	 * @param language
	 *           the String Locale of this MediaContainer.
	 * @param formatUuidMap
	 *           the mediaFormatUuidMap containing the media container data
	 * @param errors
	 *           the list of errors
	 */
	protected void validateMediaFormat(final AttributeDescriptorModel attribute, final String language,
			final Map<String, String> formatUuidMap, final List<ValidationError> errors)
	{
		getCmsRequiredMediaFormatQualifiers().forEach(format -> {
			if (formatUuidMap == null || isEmpty(formatUuidMap.get(format)))
			{
				errors.add(newValidationErrorBuilder() //
						.field(attribute.getQualifier()) //
						.errorCode(FIELD_MEDIA_FORMAT_REQUIRED_L10N) //
						.language(language) //
						.errorArgs(new Object[]
				{ language, format }) //
						.rejectedValue(formatUuidMap) //
						.build());
			}
		});
	}

	/**
	 * Verifies whether it's currently a clone component flow.
	 * The SESSION_CLONE_COMPONENT_SOURCE_MAP session parameter is only populated if it's a clone flow.
	 * @return true if it's a clone component flow, false otherwise.
	 */
	protected boolean isCloneComponentFlow()
	{
		final Object item = getCloneComponentContextProvider().findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP);
		return Objects.nonNull(item);
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected List<String> getCmsRequiredMediaFormatQualifiers()
	{
		return cmsRequiredMediaFormatQualifiers;
	}

	@Required
	public void setCmsRequiredMediaFormatQualifiers(final List<String> cmsRequiredMediaFormatQualifiers)
	{
		this.cmsRequiredMediaFormatQualifiers = cmsRequiredMediaFormatQualifiers;
	}

	protected LanguageFacade getLanguageFacade()
	{
		return languageFacade;
	}

	@Required
	public void setLanguageFacade(final LanguageFacade languageFacade)
	{
		this.languageFacade = languageFacade;
	}

	protected CMSMediaContainerService getCmsMediaContainerService()
	{
		return cmsMediaContainerService;
	}

	@Required
	public void setCmsMediaContainerService(final CMSMediaContainerService cmsMediaContainerService)
	{
		this.cmsMediaContainerService = cmsMediaContainerService;
	}

	public CloneComponentContextProvider getCloneComponentContextProvider()
	{
		return cloneComponentContextProvider;
	}

	@Required
	public void setCloneComponentContextProvider(
			final CloneComponentContextProvider cloneComponentContextProvider)
	{
		this.cloneComponentContextProvider = cloneComponentContextProvider;
	}
}
