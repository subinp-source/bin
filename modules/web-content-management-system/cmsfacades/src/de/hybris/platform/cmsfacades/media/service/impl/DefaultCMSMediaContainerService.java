/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaContainerDao;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@code CMSMediaContainerService}
 */
public class DefaultCMSMediaContainerService implements CMSMediaContainerService
{
	private CMSMediaContainerDao cmsMediaContainerDao;

	@Override
	public MediaContainerModel getMediaContainerForQualifier(final String qualifier, final CatalogVersionModel catalogVersion)
	{
		return getCmsMediaContainerDao().getMediaContainerForQualifier(qualifier, catalogVersion);
	}

	@Override
	public SearchResult<MediaContainerModel> findMediaContainersForCatalogVersion(final String text,
			final CatalogVersionModel catalogVersion, final PageableData pageableData)
	{
		return getCmsMediaContainerDao().findMediaContainersForCatalogVersion(text, catalogVersion, pageableData);
	}

	protected CMSMediaContainerDao getCmsMediaContainerDao()
	{
		return cmsMediaContainerDao;
	}

	@Required
	public void setCmsMediaContainerDao(final CMSMediaContainerDao cmsMediaContainerDao)
	{
		this.cmsMediaContainerDao = cmsMediaContainerDao;
	}

}
