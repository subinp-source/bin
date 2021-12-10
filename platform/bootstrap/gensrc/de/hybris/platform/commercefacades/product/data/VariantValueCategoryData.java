/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.VariantCategoryData;
import java.util.Collection;


import java.util.Objects;
public  class VariantValueCategoryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantValueCategoryData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariantValueCategoryData.sequence</code> property defined at extension <code>commercefacades</code>. */
		
	private int sequence;

	/** <i>Generated property</i> for <code>VariantValueCategoryData.superCategories</code> property defined at extension <code>commercefacades</code>. */
		
	private Collection<VariantCategoryData> superCategories;
	
	public VariantValueCategoryData()
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
	
	public void setSequence(final int sequence)
	{
		this.sequence = sequence;
	}

	public int getSequence() 
	{
		return sequence;
	}
	
	public void setSuperCategories(final Collection<VariantCategoryData> superCategories)
	{
		this.superCategories = superCategories;
	}

	public Collection<VariantCategoryData> getSuperCategories() 
	{
		return superCategories;
	}
	

}