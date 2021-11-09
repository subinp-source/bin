/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.integrationtest;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author pohl
 */
final class TestDataFactory
{

	private TestDataFactory()
	{
		// no instances mate!
	}

	static CatalogVersionModel someCatalogVersion(final ModelService modelService)
	{
		final CatalogVersionModel ret = modelService.create(CatalogVersionModel.class);
		ret.setVersion("test");
		ret.setCatalog(TestDataFactory.someCatalog(modelService));
		modelService.save(ret);
		return ret;
	}

	static CatalogModel someCatalog(final ModelService modelService)
	{
		final CatalogModel ret = modelService.create(CatalogModel.class);
		ret.setId("testCatalog_" + System.currentTimeMillis());
		modelService.save(ret);
		return ret;
	}
}
