/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.service;

import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

import java.util.Collections;
import java.util.List;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.Product;
import com.hybris.platform.merchandising.yaas.CategoryHierarchy;

/**
 * MerchCatalogService is a service for the purpose of making catalog queries.
 *
 */
public interface MerchCatalogService {
        /**
         * getCategories is a method for retrieving the category hierarchy (including subcategories).
         * @param baseSite the base site we wish to retrieve the category hierarchy for.
         * @param catalog  the catalog we wish to retrieve the category hierarchy from.
         * @param catalogVersion the catalog version we wish to retrieve the category hierarchy for.
         * @param baseCategoryUrl the URL we wish to use to access the category from.
         * @return a List of {@link CategoryHierarchy} representing the categories.
         * @deprecated see com.hybris.merchandising.service.MerchCatalogService.getCategories(MerchProductDirectoryConfigModel)
         */
        @Deprecated(since = "1911", forRemoval = true)
        List<CategoryHierarchy> getCategories(final String baseSite, final String catalog, final String catalogVersion,
                        final String baseCatalogPageUrl);

        /**
         * Method to retrieve all the {@Product} associated to {@link IndexerBatchContext}.
         *
         * @param indexerBatchContext represents a context valid for the duration of an indexer batch.
         * @param merchProductDirectoryConfigModel the config model being used to retrieve products for.
         * @return List of {@Product} that represents the merchandising properties
         * @throws IndexerException in case of errors.
         */
        default List<Product> getProducts(final IndexerBatchContext indexerBatchContext,
                        final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel) throws IndexerException
        {
        	return Collections.emptyList();
        }
        

        /**
         * Retrieves a list of {@link CategoryHierarchy} objects representing the category hierarchy.
         * @param merchProductDirectoryConfigModel - the {@link MerchProductDirectoryConfigModel} to retrieve categories for.
         * @return a List representing the category hierarchy.
         * @throws IndexerException in case of errors.
         */
        default List<CategoryHierarchy> getCategories(final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel) 
        		throws IndexerException
        {
        	return Collections.emptyList();
        }

}
