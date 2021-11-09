/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.Collection;
import java.util.Set;


/**
 * ProductSource. Retrieves a collection of product to index for a specific model & index configuration.
 */
public interface ProductSource
{
	/**
	 * Returns a collection of {@link ProductModel} of a given indexedProperty that are fetched from the model based on
	 * the indexConfig.
	 * 
	 * @param indexConfig
	 *           index config
	 * @param indexedProperty
	 *           indexed property
	 * @param model
	 *           model
	 * @return Collection of products
	 */
	Collection<ProductModel> getProductsForConfigAndProperty(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model);


	/**
	 * Gets all the products in the a model this could include all its variants.
	 * 
	 * @param productModel
	 *           productModel
	 * @return Collection of products
	 */
	Set<ProductModel> getProducts(final Object productModel);

}
