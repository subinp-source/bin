/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dao;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


/**
 * Dao to find order related data
 */
public interface MarketplaceProductDao extends Dao
{
	/**
	 * Find all product by vendor
	 *
	 * @param VendorModel vendor
	 * @return list of products belong to given vendor
	 */
	List<ProductModel> findAllProductByVendor(String vendorCode);
}
