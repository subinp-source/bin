/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaContainerDao;
import de.hybris.platform.cmsfacades.util.builder.MediaContainerModelBuilder;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Arrays;
import java.util.List;


public class MediaContainerModelMother extends AbstractModelMother<MediaContainerModel>
{
	public static final String MEDIA_CONTAINER_QUALIFIER = "simple-responsive-media-container";
	public static final String EMPTY_MEDIA_CONTAINER_QUALIFIER = "empty-simple-responsive-media-container";

	private CMSMediaContainerDao cmsMediaContainerDao;
	private MediaModelMother mediaModelMother;

	protected MediaContainerModel createMediaContainerModelWithQualifier(final CatalogVersionModel catalogVersion,
			final String qualifier, final List<MediaModel> mediaList)
	{
		return getOrSaveAndReturn(() -> getCmsMediaContainerDao().getMediaContainerForQualifier(qualifier, catalogVersion), () -> {
			return MediaContainerModelBuilder.aModel() //
					.withCatalogVersion(catalogVersion) //
					.withQualifier(qualifier) //
					.withMediaList(mediaList).build();
		});
	}

	public MediaContainerModel createEmptyMediaContainerModelWithQualifier(final CatalogVersionModel catalogVersion,
			final String qualifier)
	{
		return getOrSaveAndReturn(() -> getCmsMediaContainerDao().getMediaContainerForQualifier(qualifier, catalogVersion), () -> {
			return MediaContainerModelBuilder.aModel() //
					.withCatalogVersion(catalogVersion) //
					.withQualifier(qualifier).build();
		});
	}

	public MediaContainerModel createEmptyMediaContainerModel(final CatalogVersionModel catalogVersion)
	{
		return createEmptyMediaContainerModelWithQualifier(catalogVersion, EMPTY_MEDIA_CONTAINER_QUALIFIER);
	}

	public MediaContainerModel createMediaContainerModelWithLogos(final CatalogVersionModel catalogVersion)
	{
		final MediaModel logoWidescreen = getMediaModelMother().createWidescreenLogoMediaModel(catalogVersion);
		final MediaModel logoMobile = getMediaModelMother().createMobileLogoMediaModel(catalogVersion);

		return createMediaContainerModelWithQualifier(catalogVersion, MEDIA_CONTAINER_QUALIFIER,
				Arrays.asList(logoWidescreen, logoMobile));
	}

	public CMSMediaContainerDao getCmsMediaContainerDao()
	{
		return cmsMediaContainerDao;
	}

	public void setCmsMediaContainerDao(final CMSMediaContainerDao cmsMediaContainerDao)
	{
		this.cmsMediaContainerDao = cmsMediaContainerDao;
	}

	public MediaModelMother getMediaModelMother()
	{
		return mediaModelMother;
	}

	public void setMediaModelMother(final MediaModelMother mediaModelMother)
	{
		this.mediaModelMother = mediaModelMother;
	}
}
