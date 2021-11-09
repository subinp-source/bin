/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.sitemap.populators;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringEscapeUtils;


public class CustomPageToSiteMapUrlDataPopulator implements Populator<String, SiteMapUrlData>
{
	private UrlResolver<String> urlResolver;

	@Override
	public void populate(final String source, final SiteMapUrlData siteMapUrlData) throws ConversionException
	{
		final String relUrl = StringEscapeUtils.escapeXml(getUrlResolver().resolve(source));
		siteMapUrlData.setLoc(relUrl);
	}

	public UrlResolver<String> getUrlResolver()
	{
		return urlResolver;
	}

	public void setUrlResolver(final UrlResolver<String> urlResolver)
	{
		this.urlResolver = urlResolver;
	}
}
