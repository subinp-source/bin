/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.queues;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of an Order Status Update Element
 */
@ApiModel(value="OrderStatusUpdateElement", description="Representation of an Order Status Update Element")
public  class OrderStatusUpdateElementWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of update element of order status<br/><br/><i>Generated property</i> for <code>OrderStatusUpdateElementWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of update element of order status") 	
	private String code;

	/** Status of update element<br/><br/><i>Generated property</i> for <code>OrderStatusUpdateElementWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="status", value="Status of update element") 	
	private String status;

	/** BaseSite identifier<br/><br/><i>Generated property</i> for <code>OrderStatusUpdateElementWsDTO.baseSiteId</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="baseSiteId", value="BaseSite identifier") 	
	private String baseSiteId;
	
	public OrderStatusUpdateElementWsDTO()
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