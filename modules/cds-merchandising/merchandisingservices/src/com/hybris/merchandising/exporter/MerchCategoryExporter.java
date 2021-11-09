/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.exporter;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;

/**
 * MerchCategoryExporter is a class to handle the export of category information from the
 * Hybris product catalog for import into Merch v2.
 *
 */
public interface MerchCategoryExporter {
	/**
	 * exportCategories is a method for retrieving the category hierarchy and
	 * invoking a call to Merch v2 to persist the hierarchy.
	 */
	void exportCategories();

	/**
	 * exportCategoriesForCurrentBaseSite is a method for retrieving the category hierarchy and
	 * invoking a call to Merch v2 to persist the hierarchy.
	 */
	void exportCategoriesForCurrentBaseSite();

    /**
     * exportCategories is a method for retrieving the categories to synchronise for a given
     * product directory.
     * @param productDirectory the product directory we wish to extract categories for.
     */
    default void exportCategories(MerchProductDirectoryConfigModel productDirectory) {
    	//No Op.
    }
}
