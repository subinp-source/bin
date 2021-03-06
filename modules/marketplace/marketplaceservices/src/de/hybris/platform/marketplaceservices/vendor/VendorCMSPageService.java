/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.vendor;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.marketplaceservices.model.VendorPageModel;
import de.hybris.platform.ordersplitting.model.VendorModel;

import java.util.Optional;


/**
 * Extend CMSPageService to support VendorPage
 */
public interface VendorCMSPageService extends CMSPageService
{

	/**
	 * Get vendor landing page for a given vendor code
	 *
	 * @param vendorModel
	 *           The target vendor
	 * @return VendorPageModel if it exists otherwise return empty option
	 *
	 */
	Optional<VendorPageModel> getPageForVendor(VendorModel vendorModel);

	/**
	 * Get vendor landing page for a given vendor code
	 *
	 * @param vendorModel
	 *           The target vendor
	 * @param catalogVersionModel
	 *           specific ContentCatalog
	 * @return VendorPageModel if it exists otherwise return empty option
	 * @throws CMSItemNotFoundException
	 */
	Optional<VendorPageModel> getPageForVendor(VendorModel vendorModel, CatalogVersionModel catalogVersionModel);

}
