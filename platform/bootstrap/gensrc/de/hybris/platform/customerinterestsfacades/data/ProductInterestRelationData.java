/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestEntryData;
import java.util.List;


import java.util.Objects;
public  class ProductInterestRelationData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductInterestRelationData.product</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private ProductData product;

	/** <i>Generated property</i> for <code>ProductInterestRelationData.productInterestEntry</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private List<ProductInterestEntryData> productInterestEntry;
	
	public ProductInterestRelationData()
	{
		// default constructor
	}
	
	public void setProduct(final ProductData product)
	{
		this.product = product;
	}

	public ProductData getProduct() 
	{
		return product;
	}
	
	public void setProductInterestEntry(final List<ProductInterestEntryData> productInterestEntry)
	{
		this.productInterestEntry = productInterestEntry;
	}

	public List<ProductInterestEntryData> getProductInterestEntry() 
	{
		return productInterestEntry;
	}
	

}