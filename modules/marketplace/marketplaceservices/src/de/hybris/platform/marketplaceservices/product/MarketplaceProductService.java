/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.product;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * Service with VendorUser related methods
 */
public interface MarketplaceProductService
{
	/**
	 * Get all product by vendor
	 *
	 * @param vendorCode
	 *           code of vendor
	 * @return list of products belong to given vendor
	 */
	List<ProductModel> getAllProductByVendor(String vendorCode);
}
