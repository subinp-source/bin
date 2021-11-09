/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.catalog.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchSolrQueryPopulator;
import de.hybris.platform.marketplaceservices.vendor.VendorService;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Required;


/**
 * Override implementation of get session product catalogVersions in {@link SearchSolrQueryPopulator}
 */
public class MarketplaceSearchSolrQueryPopulator extends SearchSolrQueryPopulator
{
	private VendorService vendorService;

	@Override
	protected Collection<CatalogVersionModel> getSessionProductCatalogVersions()
	{
		final Collection<CatalogVersionModel> sessionProductCatalogVersions = new HashSet<>();
		sessionProductCatalogVersions.addAll(super.getSessionProductCatalogVersions());
		sessionProductCatalogVersions.addAll(getVendorService().getActiveProductCatalogVersions());
		return sessionProductCatalogVersions;
	}

	protected VendorService getVendorService()
	{
		return vendorService;
	}

	@Required
	public void setVendorService(final VendorService vendorService)
	{
		this.vendorService = vendorService;
	}
}
