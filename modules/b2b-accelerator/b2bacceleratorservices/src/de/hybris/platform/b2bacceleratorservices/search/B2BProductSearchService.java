/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.search;

import de.hybris.platform.b2bacceleratorservices.product.B2BProductService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


/**
 * @deprecated Since 6.0. Use {@link de.hybris.platform.b2bacceleratorservices.product.B2BProductService} instead.
 *
 * @param <T>
 *           type of item to be searched that extends {@link ProductModel}.
 *
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BProductSearchService<T extends ProductModel>
{
	/**
	 * Gets all visible {@link de.hybris.platform.core.model.product.ProductModel} for a given collection of SKUs.
	 *
	 * @deprecated Since 6.0. Use {@link B2BProductService#getProductsForSkus(Collection, PageableData)} instead.
	 *
	 * @param skus
	 *           collection of product IDs
	 * @param pageableData
	 *           pagination information
	 * @return List of paginated {@link ProductModel} objects
	 *
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	SearchPageData<T> findProductsBySkus(Collection<String> skus, PageableData pageableData);

}
