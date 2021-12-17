/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import java.io.Serializable;
import java.util.List;


import java.util.Objects;
public  class OrderLineItem  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderLineItem.ref</code> property defined at extension <code>profileservices</code>. */
		
	private String ref;

	/** <i>Generated property</i> for <code>OrderLineItem.type</code> property defined at extension <code>profileservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>OrderLineItem.unit</code> property defined at extension <code>profileservices</code>. */
		
	private String unit;

	/** <i>Generated property</i> for <code>OrderLineItem.price_list</code> property defined at extension <code>profileservices</code>. */
		
	private Double price_list;

	/** <i>Generated property</i> for <code>OrderLineItem.price_effective</code> property defined at extension <code>profileservices</code>. */
		
	private Double price_effective;

	/** <i>Generated property</i> for <code>OrderLineItem.currency</code> property defined at extension <code>profileservices</code>. */
		
	private String currency;

	/** <i>Generated property</i> for <code>OrderLineItem.taxAmount</code> property defined at extension <code>profileservices</code>. */
		
	private Double taxAmount;

	/** <i>Generated property</i> for <code>OrderLineItem.status</code> property defined at extension <code>profileservices</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>OrderLineItem.pos</code> property defined at extension <code>profileservices</code>. */
		
	private Integer pos;

	/** <i>Generated property</i> for <code>OrderLineItem.quantity</code> property defined at extension <code>profileservices</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>OrderLineItem.reason</code> property defined at extension <code>profileservices</code>. */
		
	private String reason;

	/** <i>Generated property</i> for <code>OrderLineItem.categories</code> property defined at extension <code>profileservices</code>. */
		
	private List<Category> categories;
	
	public OrderLineItem()
	{
		// default constructor
	}
	
	public void setRef(final String ref)
	{
		this.ref = ref;
	}

	public String getRef() 
	{
		return ref;
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	public String getUnit() 
	{
		return unit;
	}
	
	public void setPrice_list(final Double price_list)
	{
		this.price_list = price_list;
	}

	public Double getPrice_list() 
	{
		return price_list;
	}
	
	public void setPrice_effective(final Double price_effective)
	{
		this.price_effective = price_effective;
	}

	public Double getPrice_effective() 
	{
		return price_effective;
	}
	
	public void setCurrency(final String currency)
	{
		this.currency = currency;
	}

	public String getCurrency() 
	{
		return currency;
	}
	
	public void setTaxAmount(final Double taxAmount)
	{
		this.taxAmount = taxAmount;
	}

	public Double getTaxAmount() 
	{
		return taxAmount;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setPos(final Integer pos)
	{
		this.pos = pos;
	}

	public Integer getPos() 
	{
		return pos;
	}
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Long getQuantity() 
	{
		return quantity;
	}
	
	public void setReason(final String reason)
	{
		this.reason = reason;
	}

	public String getReason() 
	{
		return reason;
	}
	
	public void setCategories(final List<Category> categories)
	{
		this.categories = categories;
	}

	public List<Category> getCategories() 
	{
		return categories;
	}
	

}