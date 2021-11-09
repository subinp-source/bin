/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;


public interface CmsWebServicesDao<T>
{
	T getByUidAndCatalogVersion(String code, CatalogVersionModel catalogVersion);
}
