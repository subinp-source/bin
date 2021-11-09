/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacestore.service.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;

/**
 * Marketplace core data import service
 *
 */
public class MarketplaceCoreDataImportService extends CoreDataImportService
{
	@Override
	protected void importContentCatalog(final String extensionName, final String contentCatalogName)
	{
		super.importContentCatalog(extensionName, contentCatalogName);

		getSetupImpexService().importImpexFile(String.format(
				"/%s/import/coredata/contentCatalogs/marketplaceContentCatalog/marketplace-cms-responsive-content.impex",
				extensionName), false);
	}
}
