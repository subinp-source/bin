/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.product.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.marketplaceservices.dao.MarketplaceProductDao;
import de.hybris.platform.marketplaceservices.product.MarketplaceProductService;
import de.hybris.platform.marketplaceservices.vendor.VendorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link VendorService}.
 */
public class DefaultMarketplaceProductService implements MarketplaceProductService
{

	private MarketplaceProductDao marketplaceProductDao;


	@Override
	public List<ProductModel> getAllProductByVendor(String vendorCode)
	{
		return marketplaceProductDao.findAllProductByVendor(vendorCode);
	}

	protected MarketplaceProductDao getMarketplaceProductDao()
	{
		return marketplaceProductDao;
	}

	@Required
	public void setMarketplaceProductDao(final MarketplaceProductDao marketplaceProductDao)
	{
		this.marketplaceProductDao = marketplaceProductDao;
	}

}
