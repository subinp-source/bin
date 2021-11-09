/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Helper class for strategy tests.
 */
public class CommerceStrategyTestHelper
{

	private static final String BASE_SITE_MODEL = "baseSiteModel";

	public BaseSiteModel createSite(final ModelService modelService, final BaseSiteService siteService)
	{
		final BaseSiteModel siteModel = modelService.create(BaseSiteModel.class);
		siteModel.setName(BASE_SITE_MODEL);
		siteModel.setUid(BASE_SITE_MODEL);
		modelService.saveAll();
		siteService.setCurrentBaseSite(siteModel, false);
		return siteModel;
	}

	public BaseStoreModel createStore(final String name, final ModelService modelService)
	{
		final BaseStoreModel store1 = modelService.create(BaseStoreModel.class);
		store1.setUid(name);
		store1.setNet(true);
		return store1;
	}
}
