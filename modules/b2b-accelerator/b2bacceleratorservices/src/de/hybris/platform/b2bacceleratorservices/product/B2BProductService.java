/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.product;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


/**
 * B2B specific product service interface.
 */
public interface B2BProductService
{
	/**
	 * Gets all visible {@link ProductModel} for a given collection of skus.
	 *
	 * @deprecated Since 6.0. Use {@link #getProductsForSkus(Collection, PageableData)} instead.
	 * @param skus
	 *           String collection of product ids
	 * @param pageableData
	 *           Pagination information
	 * @return List of paginated {@link ProductModel} objects
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	SearchPageData<ProductModel> findProductsForSkus(Collection<String> skus, PageableData pageableData);

	/**
	 * Gets all visible {@link ProductModel} for a given collection of skus.
	 *
	 * @since 6.0
	 *
	 * @param skus
	 *           String collection of product ids
	 * @param pageableData
	 *           Pagination information
	 * @return List of paginated {@link ProductModel} objects
	 */
	SearchPageData<ProductModel> getProductsForSkus(Collection<String> skus, PageableData pageableData);
}
