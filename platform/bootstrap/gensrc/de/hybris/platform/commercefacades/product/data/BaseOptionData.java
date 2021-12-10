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
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import java.util.List;


import java.util.Objects;
public  class BaseOptionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>BaseOptionData.variantType</code> property defined at extension <code>commercefacades</code>. */
		
	private String variantType;

	/** <i>Generated property</i> for <code>BaseOptionData.options</code> property defined at extension <code>commercefacades</code>. */
		
	private List<VariantOptionData> options;

	/** <i>Generated property</i> for <code>BaseOptionData.selected</code> property defined at extension <code>commercefacades</code>. */
		
	private VariantOptionData selected;
	
	public BaseOptionData()
	{
		// default constructor
	}
	
	public void setVariantType(final String variantType)
	{
		this.variantType = variantType;
	}

	public String getVariantType() 
	{
		return variantType;
	}
	
	public void setOptions(final List<VariantOptionData> options)
	{
		this.options = options;
	}

	public List<VariantOptionData> getOptions() 
	{
		return options;
	}
	
	public void setSelected(final VariantOptionData selected)
	{
		this.selected = selected;
	}

	public VariantOptionData getSelected() 
	{
		return selected;
	}
	

}