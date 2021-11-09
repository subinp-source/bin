/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ImageData;


import java.util.Objects;
public  class VariantOptionQualifierData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantOptionQualifierData.qualifier</code> property defined at extension <code>commercefacades</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>VariantOptionQualifierData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariantOptionQualifierData.value</code> property defined at extension <code>commercefacades</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>VariantOptionQualifierData.image</code> property defined at extension <code>commercefacades</code>. */
		
	private ImageData image;
	
	public VariantOptionQualifierData()
	{
		// default constructor
	}
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getQualifier() 
	{
		return qualifier;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	
	public void setImage(final ImageData image)
	{
		this.image = image;
	}

	public ImageData getImage() 
	{
		return image;
	}
	

}