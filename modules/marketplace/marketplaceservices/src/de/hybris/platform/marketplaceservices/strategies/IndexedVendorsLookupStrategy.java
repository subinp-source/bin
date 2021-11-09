/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.ordersplitting.model.VendorModel;


/**
 *
 */
public interface IndexedVendorsLookupStrategy
{
	/**
	 * Get the vendors need to shown in index page
	 *
	 * @param pageableData
	 *           the pagination data
	 * @return search page data of vendors
	 */
	SearchPageData<VendorModel> getIndexVendors(PageableData pageableData);
}
