/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend;

import java.io.Serializable;


import java.util.Objects;
public  class FilterData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FilterData.key</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String key;

	/** <i>Generated property</i> for <code>FilterData.description</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>FilterData.selected</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean selected;
	
	public FilterData()
	{
		// default constructor
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey() 
	{
		return key;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setSelected(final boolean selected)
	{
		this.selected = selected;
	}

	public boolean isSelected() 
	{
		return selected;
	}
	

}