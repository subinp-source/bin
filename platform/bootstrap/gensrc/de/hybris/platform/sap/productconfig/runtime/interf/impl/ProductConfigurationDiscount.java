/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.impl;

import java.io.Serializable;
import java.math.BigDecimal;


import java.util.Objects;
public  class ProductConfigurationDiscount  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductConfigurationDiscount.csticName</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String csticName;

	/** <i>Generated property</i> for <code>ProductConfigurationDiscount.csticValueName</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String csticValueName;

	/** <i>Generated property</i> for <code>ProductConfigurationDiscount.discount</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private BigDecimal discount;
	
	public ProductConfigurationDiscount()
	{
		// default constructor
	}
	
	public void setCsticName(final String csticName)
	{
		this.csticName = csticName;
	}

	public String getCsticName() 
	{
		return csticName;
	}
	
	public void setCsticValueName(final String csticValueName)
	{
		this.csticValueName = csticValueName;
	}

	public String getCsticValueName() 
	{
		return csticValueName;
	}
	
	public void setDiscount(final BigDecimal discount)
	{
		this.discount = discount;
	}

	public BigDecimal getDiscount() 
	{
		return discount;
	}
	

}