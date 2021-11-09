/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorocc.urlresolver.impl;


import de.hybris.platform.acceleratorservices.urlresolver.impl.DefaultSiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;


/**
 * Implementation of the UrlResolutionService customised for commerce web services
 */
public class WsSiteBaseUrlResolutionService extends DefaultSiteBaseUrlResolutionService
{

	@Override
	public String getWebsiteUrlForSite(final BaseSiteModel site, final String encodingAttributes, final boolean secure,
			final String path)
	{
		String urlForSite = super.getWebsiteUrlForSite(site, encodingAttributes, secure, path);
		if (urlForSite == null)
		{
			urlForSite = lookupConfig("webroot.commercewebservices." + site.getUid() + (secure ? ".https" : ".http"));
		}
		return urlForSite;
	}
}
