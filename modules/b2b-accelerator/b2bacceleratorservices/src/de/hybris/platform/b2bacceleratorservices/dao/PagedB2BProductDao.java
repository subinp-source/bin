/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


public interface PagedB2BProductDao<M> extends PagedGenericDao<M>
{
	/**
	 * Finds all visible {@link de.hybris.platform.core.model.product.ProductModel} for a given list of skus
	 * 
	 * @param skus
	 * @param pageableData
	 * @return A paged result of products
	 */
	SearchPageData<ProductModel> findPagedProductsForSkus(Collection<String> skus, PageableData pageableData);
}
