/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.sitemap.populators;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.apache.commons.lang.StringEscapeUtils;


public class PointOfServiceModellToSiteMapUrlDataPopulator implements Populator<PointOfServiceModel, SiteMapUrlData>
{
	private UrlResolver<PointOfServiceModel> urlResolver;

	@Override
	public void populate(final PointOfServiceModel pointOfServiceModel, final SiteMapUrlData siteMapUrlData)
			throws ConversionException
	{
		final String relUrl = StringEscapeUtils.escapeXml(getUrlResolver().resolve(pointOfServiceModel));
		siteMapUrlData.setLoc(relUrl);
	}

	public UrlResolver<PointOfServiceModel> getUrlResolver()
	{
		return urlResolver;
	}

	public void setUrlResolver(final UrlResolver<PointOfServiceModel> urlResolver)
	{
		this.urlResolver = urlResolver;
	}
}
