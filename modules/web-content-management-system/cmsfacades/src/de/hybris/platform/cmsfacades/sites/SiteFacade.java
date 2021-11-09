/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.sites;


import de.hybris.platform.cmsfacades.data.SiteData;

import java.util.List;


/**
 * simplification of site related interactions.
 */
public interface SiteFacade
{
	/**
	 * Lists all sites for which user has at-least read access to one of the non-active catalog versions.
	 *
	 * @return All sites that are configured; never <tt>null</tt>
	 */
	List<SiteData> getAllSiteData();

	/**
	 * Lists all sites that are configured for the given list of catalogIds where the catalog id represents the lowest
	 * level catalog in the hierarchy for a site.
	 *
	 * @param catalogIds
	 *           - the catalog identifiers
	 * @return All sites where the catalog ids are the lowest catalog in the catalog hierarchy; never <tt>null</tt>
	 */
	List<SiteData> getSitesForCatalogs(final List<String> catalogIds);

}
