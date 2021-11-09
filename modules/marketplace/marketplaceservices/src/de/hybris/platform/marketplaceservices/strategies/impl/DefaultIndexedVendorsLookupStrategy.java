/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.marketplaceservices.strategies.IndexedVendorsLookupStrategy;
import de.hybris.platform.marketplaceservices.vendor.daos.VendorDao;
import de.hybris.platform.ordersplitting.model.VendorModel;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultIndexedVendorsLookupStrategy implements IndexedVendorsLookupStrategy
{

	private VendorDao vendorDao;

	@Override
	public SearchPageData<VendorModel> getIndexVendors(final PageableData pageableData)
	{
		return getVendorDao().findPagedActiveVendors(pageableData);
	}

	protected VendorDao getVendorDao()
	{
		return vendorDao;
	}

	@Required
	public void setVendorDao(final VendorDao vendorDao)
	{
		this.vendorDao = vendorDao;
	}

}
