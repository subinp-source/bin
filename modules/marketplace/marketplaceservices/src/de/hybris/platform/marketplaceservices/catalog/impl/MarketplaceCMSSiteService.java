/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.catalog.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSSiteService;
import de.hybris.platform.marketplaceservices.vendor.VendorService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Override implementation of get all catalogs in {@link DefaultCMSSiteService}
 */
public class MarketplaceCMSSiteService extends DefaultCMSSiteService
{
	private transient VendorService vendorService;

	@Override
	public Collection<CatalogModel> getAllCatalogs(final CMSSiteModel site)
	{
		final Set<CatalogModel> ret = new HashSet<>();
		if (site == null)
		{
			throw new IllegalArgumentException("No site specified.");
		}

		ret.addAll(super.getAllCatalogs(site));
		ret.addAll(getVendorService().getActiveCatalogs());

		return ret;
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
