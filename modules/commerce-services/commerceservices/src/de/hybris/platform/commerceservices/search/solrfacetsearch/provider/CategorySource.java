/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.Collection;


/**
 * CategorySource. Retrieves a collection of categories to index for a specific model & index configuration.
 */
public interface CategorySource
{
	/**
	 * Returns a collection of {@link CategoryModel} of a given indexedProperty that are fetched from the model based on
	 * the indexConfig.
	 * 
	 * @param indexConfig
	 *           index config
	 * @param indexedProperty
	 *           indexed property
	 * @param model
	 *           model
	 * @return Collection of categories
	 */
	Collection<CategoryModel> getCategoriesForConfigAndProperty(IndexConfig indexConfig, IndexedProperty indexedProperty,
			Object model);
}
