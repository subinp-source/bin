/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.platform.merchandising.yaas;

import java.io.Serializable;


import java.util.Objects;
public  class ProductBatchResp  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductBatchResp.id</code> property defined at extension <code>merchandisingservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>ProductBatchResp.status</code> property defined at extension <code>merchandisingservices</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>ProductBatchResp.location</code> property defined at extension <code>merchandisingservices</code>. */
		
	private String location;
	
	public ProductBatchResp()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setLocation(final String location)
	{
		this.location = location;
	}

	public String getLocation() 
	{
		return location;
	}
	

}