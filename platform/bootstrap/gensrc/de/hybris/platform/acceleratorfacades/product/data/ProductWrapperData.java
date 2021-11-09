/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;


import java.util.Objects;
public  class ProductWrapperData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductWrapperData.productData</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private ProductData productData;

	/** <i>Generated property</i> for <code>ProductWrapperData.errorMsg</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String errorMsg;
	
	public ProductWrapperData()
	{
		// default constructor
	}
	
	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}

	public ProductData getProductData() 
	{
		return productData;
	}
	
	public void setErrorMsg(final String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() 
	{
		return errorMsg;
	}
	

}