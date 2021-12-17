/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import java.util.Date;


import java.util.Objects;
public  class Wishlist2EntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Wishlist2EntryData.product</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private ProductData product;

	/** <i>Generated property</i> for <code>Wishlist2EntryData.addedDate</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private Date addedDate;

	/** <i>Generated property</i> for <code>Wishlist2EntryData.quantity</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private Integer quantity;
	
	public Wishlist2EntryData()
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
	
	public void setAddedDate(final Date addedDate)
	{
		this.addedDate = addedDate;
	}

	public Date getAddedDate() 
	{
		return addedDate;
	}
	
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getQuantity() 
	{
		return quantity;
	}
	

}