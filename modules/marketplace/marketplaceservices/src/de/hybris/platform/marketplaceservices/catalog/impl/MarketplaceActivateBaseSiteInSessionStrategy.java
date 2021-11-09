/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.catalog.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.impl.DefaultActivateBaseSiteInSessionStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.marketplaceservices.vendor.VendorService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * Override implementation for marketplace related activating attributes in session in
 * {@link DefaultActivateBaseSiteInSessionStrategy}
 */
public class MarketplaceActivateBaseSiteInSessionStrategy<T extends BaseSiteModel> extends
		DefaultActivateBaseSiteInSessionStrategy<T>
{
	private VendorService vendorService;

	@Override
	protected Collection<CatalogVersionModel> collectCatalogVersions(final T site)
	{
		final Collection<CatalogVersionModel> catalogVersions = super.collectCatalogVersions(site);
		catalogVersions.addAll(getVendorService().getActiveProductCatalogVersions());
		return catalogVersions;
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
