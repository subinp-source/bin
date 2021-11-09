/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.converters.Converters;
import java.util.List;


public class CustomPageSiteMapGenerator extends AbstractSiteMapGenerator<String>
{
	@Override
	public List<SiteMapUrlData> getSiteMapUrlData(final List<String> models)
	{
		return Converters.convertAll(models, getSiteMapUrlDataConverter());
	}

	@Override
	protected List<String> getDataInternal(final CMSSiteModel siteModel)
	{
		return (List<String>) siteModel.getSiteMapConfig().getCustomUrls();
	}
}
