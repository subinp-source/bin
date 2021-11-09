/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.preview.strategies.impl;

import de.hybris.platform.acceleratorcms.preview.strategies.PreviewContextInformationLoaderStrategy;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;

import org.springframework.beans.factory.annotation.Required;


public class CatalogVersionsPreviewStrategy implements PreviewContextInformationLoaderStrategy
{

	private CatalogVersionService catalogVersionService;

	@Override
	public void initContextFromPreview(final PreviewDataModel preview)
	{
		getCatalogVersionService().setSessionCatalogVersions(preview.getCatalogVersions());
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}
