/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.marketplacefacades.vendor.data.VendorData;


/**
 * A facade for product searching in vendor homepage.
 */
public interface VendorProductSearchFacade extends ProductSearchFacade<ProductData>
{

	/**
	 * get categories data from facet data for setting to vendor data
	 *
	 * @param vendorCode
	 *           the target vendor data to set categories
	 * @return the vendor data contains categories data
	 */
	VendorData getVendorCategories(String vendorCode);
}
