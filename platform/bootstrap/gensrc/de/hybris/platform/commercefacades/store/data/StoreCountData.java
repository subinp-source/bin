/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.store.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.store.data.StoreCountData;
import java.util.List;


import java.util.Objects;
public  class StoreCountData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>StoreCountData.type</code> property defined at extension <code>commercefacades</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>StoreCountData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>StoreCountData.isoCode</code> property defined at extension <code>commercefacades</code>. */
		
	private String isoCode;

	/** <i>Generated property</i> for <code>StoreCountData.count</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer count;

	/** <i>Generated property</i> for <code>StoreCountData.storeCountDataList</code> property defined at extension <code>commercefacades</code>. */
		
	private List<StoreCountData> storeCountDataList;
	
	public StoreCountData()
	{
		// default constructor
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setIsoCode(final String isoCode)
	{
		this.isoCode = isoCode;
	}

	public String getIsoCode() 
	{
		return isoCode;
	}
	
	public void setCount(final Integer count)
	{
		this.count = count;
	}

	public Integer getCount() 
	{
		return count;
	}
	
	public void setStoreCountDataList(final List<StoreCountData> storeCountDataList)
	{
		this.storeCountDataList = storeCountDataList;
	}

	public List<StoreCountData> getStoreCountDataList() 
	{
		return storeCountDataList;
	}
	

}