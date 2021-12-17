/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;


import java.util.Objects;
public  class AsSortData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsSortData.code</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>AsSortData.name</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AsSortData.applyPromotedItems</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean applyPromotedItems;

	/** <i>Generated property</i> for <code>AsSortData.highlightPromotedItems</code> property defined at extension <code>adaptivesearch</code>. */
		
	private boolean highlightPromotedItems;
	
	public AsSortData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "20.11", forRemoval = true)
	public void setApplyPromotedItems(final boolean applyPromotedItems)
	{
		this.applyPromotedItems = applyPromotedItems;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "20.11", forRemoval = true)
	public boolean isApplyPromotedItems() 
	{
		return applyPromotedItems;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "20.11", forRemoval = true)
	public void setHighlightPromotedItems(final boolean highlightPromotedItems)
	{
		this.highlightPromotedItems = highlightPromotedItems;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "20.11", forRemoval = true)
	public boolean isHighlightPromotedItems() 
	{
		return highlightPromotedItems;
	}
	

}