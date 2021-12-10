/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.queues.data;

import java.io.Serializable;


import java.util.Objects;
public  class OrderStatusUpdateElementData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderStatusUpdateElementData.code</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>OrderStatusUpdateElementData.status</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>OrderStatusUpdateElementData.baseSiteId</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String baseSiteId;
	
	public OrderStatusUpdateElementData()
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
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setBaseSiteId(final String baseSiteId)
	{
		this.baseSiteId = baseSiteId;
	}

	public String getBaseSiteId() 
	{
		return baseSiteId;
	}
	

}