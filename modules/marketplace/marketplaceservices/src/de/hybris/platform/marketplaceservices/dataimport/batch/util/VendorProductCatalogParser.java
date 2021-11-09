/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch.util;

import de.hybris.platform.marketplaceservices.vendor.daos.VendorDao;
import de.hybris.platform.ordersplitting.model.VendorModel;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Strategy to extract the catalog of a vendor from a file name.
 */
public class VendorProductCatalogParser
{
	private VendorDao vendorDao;

	public String getVendorCatalog(final File file)
	{
		final String vendorCode = DataIntegrationUtils.resolveVendorCode(file);
		final Optional<VendorModel> option = vendorDao.findVendorByCode(vendorCode);
		return option.map(x -> x.getCatalog().getId())
				.orElseThrow(() -> new IllegalArgumentException("Cannot find vendor in " + file.getPath()));
	}

	@Required
	public void setVendorDao(VendorDao vendorDao)
	{
		this.vendorDao = vendorDao;
	}

	protected VendorDao getVendorDao()
	{
		return vendorDao;
	}

}
