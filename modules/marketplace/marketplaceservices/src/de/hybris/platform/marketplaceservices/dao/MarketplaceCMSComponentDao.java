/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSComponentDao;

import java.util.List;


/**
 * Marketplace CMS Component Dao
 */
public interface MarketplaceCMSComponentDao extends CMSComponentDao
{
	/**
	 * Find components in given content slot and catalog version
	 *
	 * @param contentSlotId
	 *           Id of content slot
	 * @param catalogVersions
	 *           List of catalogVersions
	 * @return List of component
	 */
	List<AbstractCMSComponentModel> findCMSComponentsByContentSlot(String contentSlotId,
			List<CatalogVersionModel> catalogVersions);

}
