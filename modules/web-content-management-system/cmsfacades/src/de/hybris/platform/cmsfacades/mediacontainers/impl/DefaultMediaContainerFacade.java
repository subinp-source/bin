/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.mediacontainers.impl;

import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.cmsfacades.mediacontainers.MediaContainerFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link MediaContainerFacade} interface
 */
public class DefaultMediaContainerFacade implements MediaContainerFacade
{
	protected static final String MEDIA_CONTAINER_QUALIFIER = "mediaContainer_";

	private PersistentKeyGenerator mediaQualifierIdGenerator;
	private List<String> cmsRequiredMediaFormatQualifiers;

	private CMSMediaContainerService cmsMediaContainerService;
	private CMSAdminSiteService cmsAdminSiteService;
	private PermissionCRUDService permissionCRUDService;
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public MediaContainerModel createMediaContainer()
	{
		return createMediaContainer(null);
	}

	@Override
	public MediaContainerModel createMediaContainer(String qualifier)
	{
		if (!getPermissionCRUDService().canCreateTypeInstance(MediaContainerModel._TYPECODE))
		{
			PermissionExceptionUtils.createTypePermissionException(PermissionsConstants.CREATE, MediaContainerModel._TYPECODE);
		}

		if (Objects.isNull(qualifier))
		{
			qualifier = generateMediaContainerQualifier();
		}

		final MediaContainerModel mediaContainerModel = new MediaContainerModel();
		mediaContainerModel.setQualifier(qualifier);
		mediaContainerModel.setCatalogVersion(getCmsAdminSiteService().getActiveCatalogVersion());
		return mediaContainerModel;
	}

	@Override
	public MediaContainerData getMediaContainerForQualifier(final String qualifier) throws CMSItemNotFoundException
	{
		if (!getPermissionCRUDService().canReadType(MediaContainerModel._TYPECODE))
		{
			PermissionExceptionUtils.createTypePermissionException(PermissionsConstants.READ, MediaContainerModel._TYPECODE);
		}

		try
		{
			final MediaContainerModel model = getCmsMediaContainerService().getMediaContainerForQualifier(qualifier,
					getCmsAdminSiteService().getActiveCatalogVersion());
			return converModelToData(model);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			throw new CMSItemNotFoundException("Cannot find media container with qualifier \"" + qualifier + "\".", e);
		}
	}

	@Override
	public SearchResult<MediaContainerData> findMediaContainers(final String text, final PageableData pageableData)
	{
		// check read permission
		if (!getPermissionCRUDService().canReadType(MediaContainerModel._TYPECODE))
		{
			PermissionExceptionUtils.createTypePermissionException(PermissionsConstants.READ, MediaContainerModel._TYPECODE);
		}

		final SearchResult<MediaContainerModel> searchResults = getCmsMediaContainerService()
				.findMediaContainersForCatalogVersion(text, getCmsAdminSiteService().getActiveCatalogVersion(), pageableData);

		final List<MediaContainerData> convertedResults = searchResults.getResult().stream() //
				.map(this::converModelToData) //
				.collect(Collectors.toList());
		return new SearchResultImpl<>(convertedResults, searchResults.getTotalCount(), searchResults.getRequestedCount(),
				searchResults.getRequestedStart());
	}

	/**
	 * Converts the media container model to the associated data representation
	 *
	 * @param model
	 *           {@code MediaContainerModel} to be converted
	 * @return the {@code MediaContainerData} representation of the given model
	 */
	protected MediaContainerData converModelToData(final MediaContainerModel model)
	{
		final MediaContainerData data = new MediaContainerData();
		getUniqueItemIdentifierService().getItemData(model.getCatalogVersion())
				.ifPresent(itemData -> data.setCatalogVersion(itemData.getItemId()));
		data.setQualifier(model.getQualifier());
		final Map<String, String> mediaMap = new HashMap<>();
		model.getMedias().forEach(media -> getUniqueItemIdentifierService().getItemData(media)
				.ifPresent(itemData -> mediaMap.put(media.getMediaFormat().getQualifier(), itemData.getItemId())));
		data.setFormatMediaCodeMap(mediaMap);
		data.setThumbnailUrl(getMediaForRequiredFormat(model.getMedias()));
		data.setMediaContainerUuid(getUniqueItemIdentifierService().getItemData(model).get().getItemId());
		return data;
	}

	/**
	 * Provides the media URL for the defined required format.
	 * <li>When no required format is defined, then the first media URL found is returned.
	 * <li>When no media is found for the required media format, then {@code NULL} is returned.
	 *
	 * @param medias
	 * @return the media URL for the required media format; can be {@code NULL}
	 */
	protected String getMediaForRequiredFormat(final Collection<MediaModel> medias)
	{
		if (getCmsRequiredMediaFormatQualifiers().isEmpty() && !medias.isEmpty())
		{
			// pick the first available media
			return medias.iterator().next().getURL();
		}
		else
		{
			final String requiredFormat = getCmsRequiredMediaFormatQualifiers().iterator().next();
			return medias.stream() //
					.filter(media -> requiredFormat.equals(media.getMediaFormat().getQualifier())) //
					.map(MediaModel::getURL) //
					.findFirst() //
					.orElse(null);
		}
	}

	/**
	 * Builds a unique media container qualifier
	 *
	 * @return the new qualifier
	 */
	protected String generateMediaContainerQualifier()
	{
		return MEDIA_CONTAINER_QUALIFIER + getMediaQualifierIdGenerator().generate();
	}

	protected PersistentKeyGenerator getMediaQualifierIdGenerator()
	{
		return mediaQualifierIdGenerator;
	}

	@Required
	public void setMediaQualifierIdGenerator(final PersistentKeyGenerator mediaQualifierIdGenerator)
	{
		this.mediaQualifierIdGenerator = mediaQualifierIdGenerator;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
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

	protected CMSMediaContainerService getCmsMediaContainerService()
	{
		return cmsMediaContainerService;
	}

	@Required
	public void setCmsMediaContainerService(final CMSMediaContainerService cmsMediaContainerService)
	{
		this.cmsMediaContainerService = cmsMediaContainerService;
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
}
