/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.catalogs.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.CatalogData;
import de.hybris.platform.cmsfacades.data.CatalogVersionData;
import de.hybris.platform.cmsfacades.data.HomePageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@code java.util.Map.Entry<ContentCatalogModel, Set<CatalogVersionModel>} object to a {@link CatalogData}
 * dto
 */
public class EntryToContentCatalogDataPopulator extends EntryToCatalogDataPopulator
{
	private Converter<CatalogVersionModel, HomePageData> homePageDataConverter;

	@Override
	protected CatalogVersionData convertCatalogVersionModelToData(final CatalogVersionModel catalogVersionModel)
	{
		final CatalogVersionData catalogVersionData = super.convertCatalogVersionModelToData(catalogVersionModel);
		catalogVersionData.setHomepage(getHomePageDataConverter().convert(catalogVersionModel));
		return catalogVersionData;
	}

	protected Converter<CatalogVersionModel, HomePageData> getHomePageDataConverter()
	{
		return homePageDataConverter;
	}

	@Required
	public void setHomePageDataConverter(final Converter<CatalogVersionModel, HomePageData> homePageDataConverter)
	{
		this.homePageDataConverter = homePageDataConverter;
	}
}
