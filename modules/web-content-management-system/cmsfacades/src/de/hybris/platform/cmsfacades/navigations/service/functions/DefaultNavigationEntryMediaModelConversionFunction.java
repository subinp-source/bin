/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.functions;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for conversion of {@link NavigationEntryData} into {@link MediaModel}.
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryMediaModelConversionFunction implements Function<NavigationEntryData, MediaModel>
{

	private MediaService mediaService;
	private CMSAdminSiteService cmsAdminSiteService;

	@Override
	public MediaModel apply(final NavigationEntryData navigationEntryData)
	{
		try
		{
			return getMediaService().getMedia(getCmsAdminSiteService().getActiveCatalogVersion(), navigationEntryData.getItemId());
		}
		catch (final AmbiguousIdentifierException | UnknownIdentifierException e)
		{
			throw new ConversionException("Invalid Media: " + navigationEntryData.getItemId(), e);
		}
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
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
}
