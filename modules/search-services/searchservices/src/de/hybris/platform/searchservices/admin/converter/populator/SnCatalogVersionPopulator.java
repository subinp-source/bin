/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.converter.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.admin.data.SnCatalogVersion;


/**
 * Populates {@link SnCatalogVersion} from {@link CatalogVersionModel}.
 */
public class SnCatalogVersionPopulator implements Populator<CatalogVersionModel, SnCatalogVersion>
{
	@Override
	public void populate(final CatalogVersionModel source, final SnCatalogVersion target)
	{
		target.setCatalogId(source.getCatalog().getId());
		target.setVersion(source.getVersion());
	}
}
