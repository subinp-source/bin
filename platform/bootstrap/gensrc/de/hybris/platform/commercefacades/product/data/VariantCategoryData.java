/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class VariantCategoryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantCategoryData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariantCategoryData.hasImage</code> property defined at extension <code>commercefacades</code>. */
		
	private Boolean hasImage;

	/** <i>Generated property</i> for <code>VariantCategoryData.priority</code> property defined at extension <code>commercefacades</code>. */
		
	private int priority;
	
	public VariantCategoryData()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setHasImage(final Boolean hasImage)
	{
		this.hasImage = hasImage;
	}

	public Boolean getHasImage() 
	{
		return hasImage;
	}
	
	public void setPriority(final int priority)
	{
		this.priority = priority;
	}

	public int getPriority() 
	{
		return priority;
	}
	

}