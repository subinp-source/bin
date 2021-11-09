/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch;

import org.springframework.beans.BeanUtils;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;



/**
 * Header containing all relevant process information for batch processing. This includes:
 */
public class MarketplaceBatchHeader extends BatchHeader
{
	private String vendorCode;
	private String taxGroup;

	public MarketplaceBatchHeader(final BatchHeader header)
	{
		BeanUtils.copyProperties(header, this);
	}

	public String getVendorCode()
	{
		return vendorCode;
	}

	public void setVendorCode(final String vendorCode)
	{
		this.vendorCode = vendorCode;
	}

	public String getTaxGroup()
	{
		return taxGroup;
	}

	public void setTaxGroup(final String taxGroup)
	{
		this.taxGroup = taxGroup;
	}

}
