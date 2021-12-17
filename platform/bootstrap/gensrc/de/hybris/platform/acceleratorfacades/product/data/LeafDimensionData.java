/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.product.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.PriceData;


import java.util.Objects;
public  class LeafDimensionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>LeafDimensionData.leafDimensionHeader</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String leafDimensionHeader;

	/** <i>Generated property</i> for <code>LeafDimensionData.leafDimensionValue</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String leafDimensionValue;

	/** <i>Generated property</i> for <code>LeafDimensionData.leafDimensionData</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String leafDimensionData;

	/** <i>Generated property</i> for <code>LeafDimensionData.sequence</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private int sequence;

	/** <i>Generated property</i> for <code>LeafDimensionData.price</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private PriceData price;
	
	public LeafDimensionData()
	{
		// default constructor
	}
	
	public void setLeafDimensionHeader(final String leafDimensionHeader)
	{
		this.leafDimensionHeader = leafDimensionHeader;
	}

	public String getLeafDimensionHeader() 
	{
		return leafDimensionHeader;
	}
	
	public void setLeafDimensionValue(final String leafDimensionValue)
	{
		this.leafDimensionValue = leafDimensionValue;
	}

	public String getLeafDimensionValue() 
	{
		return leafDimensionValue;
	}
	
	public void setLeafDimensionData(final String leafDimensionData)
	{
		this.leafDimensionData = leafDimensionData;
	}

	public String getLeafDimensionData() 
	{
		return leafDimensionData;
	}
	
	public void setSequence(final int sequence)
	{
		this.sequence = sequence;
	}

	public int getSequence() 
	{
		return sequence;
	}
	
	public void setPrice(final PriceData price)
	{
		this.price = price;
	}

	public PriceData getPrice() 
	{
		return price;
	}
	

}