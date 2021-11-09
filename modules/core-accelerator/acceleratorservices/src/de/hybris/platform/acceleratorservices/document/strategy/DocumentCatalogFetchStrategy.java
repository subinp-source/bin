/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.document.strategy;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * Strategy for fetching CatalogVersionModel.
 */
public interface DocumentCatalogFetchStrategy
{

	/**
	 * @param businessProcessModel
	 * 		businessProcessModel for which the catalogVersion should be fetched
	 * @return {@link CatalogVersionModel} instance
	 */
	CatalogVersionModel fetch(final BusinessProcessModel businessProcessModel);

}
