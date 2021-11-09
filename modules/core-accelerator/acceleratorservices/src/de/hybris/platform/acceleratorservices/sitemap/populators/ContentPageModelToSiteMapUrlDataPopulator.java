/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.sitemap.populators;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringEscapeUtils;


public class ContentPageModelToSiteMapUrlDataPopulator implements Populator<ContentPageModel, SiteMapUrlData>
{
	private UrlResolver<ContentPageModel> urlResolver;

	@Override
	public void populate(final ContentPageModel contentPageModel, final SiteMapUrlData siteMapUrlData) throws ConversionException
	{
		final String relUrl = StringEscapeUtils.escapeXml(getUrlResolver().resolve(contentPageModel));
		siteMapUrlData.setLoc(relUrl);
	}

	public UrlResolver<ContentPageModel> getUrlResolver()
	{
		return urlResolver;
	}

	public void setUrlResolver(final UrlResolver<ContentPageModel> urlResolver)
	{
		this.urlResolver = urlResolver;
	}

}
