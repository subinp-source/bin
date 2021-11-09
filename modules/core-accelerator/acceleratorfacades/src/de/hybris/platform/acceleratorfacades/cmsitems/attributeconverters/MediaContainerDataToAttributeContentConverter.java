/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.attributeconverters;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.MEDIA_CONTAINER_UUID_FIELD;
import static java.util.Objects.isNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService;
import de.hybris.platform.cmsfacades.mediacontainers.MediaContainerFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts a Map<String, String> representation of a {@link MediaContainerModel} into an actual
 * {@link MediaContainerModel}
 */
@SuppressWarnings("java:S1874")
public class MediaContainerDataToAttributeContentConverter implements Converter<Map<String, Object>, MediaContainerModel>
{
	private CMSMediaFormatService mediaFormatService;
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private MediaContainerFacade mediaContainerFacade;
	private PermissionCRUDService permissionCRUDService;
	private ModelService modelService;

	@Override
	public MediaContainerModel convert(final Map<String, Object> source)
	{
		if (isNull(source))
		{
			return null;
		}

		if (!getPermissionCRUDService().canReadType(MediaFormatModel._TYPECODE))
		{
			throw throwTypePermissionException(PermissionsConstants.READ, MediaFormatModel._TYPECODE);
		}

		final Map<String, MediaFormatModel> mediaFormats = getMediaFormatService()
				.getMediaFormatsByComponentType(AbstractMediaContainerComponentModel.class).stream()
				.collect(toMap(MediaFormatModel::getQualifier, identity()));

		final String mediaContainerUuid = (String) source.get(MEDIA_CONTAINER_UUID_FIELD);
		final String qualifier = (String) source.get(MediaContainerModel.QUALIFIER);
		final Map<String, String> medias = (Map<String, String>) source.get(MediaContainerModel.MEDIAS);
		MediaContainerModel mediaContainerModel = null;
		if (Objects.nonNull(mediaContainerUuid))
		{
			mediaContainerModel = getUniqueItemIdentifierService().getItemModel(mediaContainerUuid, MediaContainerModel.class)
					.orElseThrow(() -> new UnknownIdentifierException(
							"Failed to find a MediaContainerModel for the given uuid" + mediaContainerUuid));
		}
		return getMediaContainerFunction(mediaFormats, mediaContainerModel, qualifier).apply(medias);
	}

	/**
	 * Get the Media Container function to convert a Map<String, MediaFormatModel> into a {@link MediaContainerModel}
	 *
	 * @param mediaFormats
	 *           the media format map.
	 * @param mediaContainer
	 *           the media container model
	 * @return a {@link MediaContainerModel}
	 */
	protected Function<Map<String, String>, MediaContainerModel> getMediaContainerFunction(
			final Map<String, MediaFormatModel> mediaFormats, final MediaContainerModel mediaContainer, final String qualifier)
	{
		// function to consume the localized value data
		return (formatMediaCodeMap) -> {
			// populate the media container
			final MediaContainerModel mediaContainerModel = Objects.isNull(mediaContainer)
					? getMediaContainerFacade().createMediaContainer(qualifier) // if qualifier is null, then it will be auto-generated
					: mediaContainer;

			mediaContainerModel.setMedias(formatMediaCodeMap.entrySet().stream() //
					.map(entry -> {
						final String mediaCode = entry.getValue();
						final MediaFormatModel mediaFormat = mediaFormats.get(entry.getKey());

						final MediaModel media = getUniqueItemIdentifierService().getItemModel(mediaCode, MediaModel.class)
								.orElseThrow(() -> new ConversionException(
										String.format("could not find a media for code %s in current catalog version", mediaCode)));
						media.setMediaFormat(mediaFormat);
						return media;

					}).collect(toList()));

			return mediaContainerModel;
		};

	}

	/**
	 * Throws {@link TypePermissionException}.
	 *
	 * @param permissionName
	 *           permission name
	 * @param itemType
	 *           item type code
	 */
	protected TypePermissionException throwTypePermissionException(final String permissionName, final String itemType)
	{
		throw PermissionExceptionUtils.createTypePermissionException(permissionName, itemType);
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

	protected CMSMediaFormatService getMediaFormatService()
	{
		return mediaFormatService;
	}

	@Required
	public void setMediaFormatService(final CMSMediaFormatService mediaFormatService)
	{
		this.mediaFormatService = mediaFormatService;
	}

	protected MediaContainerFacade getMediaContainerFacade()
	{
		return mediaContainerFacade;
	}

	@Required
	public void setMediaContainerFacade(final MediaContainerFacade mediaContainerFacade)
	{
		this.mediaContainerFacade = mediaContainerFacade;
	}

	protected PermissionCRUDService getPermissionCRUDService()
	{
		return permissionCRUDService;
	}

	@Required
	public void setPermissionCRUDService(final PermissionCRUDService permissionCRUDService)
	{
		this.permissionCRUDService = permissionCRUDService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
