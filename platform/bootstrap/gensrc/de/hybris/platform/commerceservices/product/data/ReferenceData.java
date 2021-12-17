/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class ReferenceData<TYPE, TARGET>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.referenceType</code> property defined at extension <code>commerceservices</code>. */
		
	private TYPE referenceType;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.description</code> property defined at extension <code>commerceservices</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.quantity</code> property defined at extension <code>commerceservices</code>. */
		
	private Integer quantity;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.target</code> property defined at extension <code>commerceservices</code>. */
		
	private TARGET target;
	
	public ReferenceData()
	{
		// default constructor
	}
	
	public void setReferenceType(final TYPE referenceType)
	{
		this.referenceType = referenceType;
	}

	public TYPE getReferenceType() 
	{
		return referenceType;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getQuantity() 
	{
		return quantity;
	}
	
	public void setTarget(final TARGET target)
	{
		this.target = target;
	}

	public TARGET getTarget() 
	{
		return target;
	}
	

}