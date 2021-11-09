/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.data;

import java.io.Serializable;


import java.util.Objects;
public  class VariantSearchResult  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Product code of variant result item<br/><br/><i>Generated property</i> for <code>VariantSearchResult.productCode</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private String productCode;
	
	public VariantSearchResult()
	{
		// default constructor
	}
	
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	

}