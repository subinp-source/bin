/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;


import java.util.Objects;
/**
 * Representation of an Order History
 */
@ApiModel(value="OrderHistory", description="Representation of an Order History")
public  class OrderHistoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of Order History<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of Order History") 	
	private String code;

	/** Status of Order History<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="status", value="Status of Order History") 	
	private String status;

	/** Status display<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.statusDisplay</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="statusDisplay", value="Status display") 	
	private String statusDisplay;

	/** Date of placing order<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.placed</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="placed", value="Date of placing order") 	
	private Date placed;

	/** Guest user identifier<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.guid</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="guid", value="Guest user identifier") 	
	private String guid;

	/** Total price<br/><br/><i>Generated property</i> for <code>OrderHistoryWsDTO.total</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="total", value="Total price") 	
	private PriceWsDTO total;
	
	public OrderHistoryWsDTO()
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
	
	public void setStatusDisplay(final String statusDisplay)
	{
		this.statusDisplay = statusDisplay;
	}

	public String getStatusDisplay() 
	{
		return statusDisplay;
	}
	
	public void setPlaced(final Date placed)
	{
		this.placed = placed;
	}

	public Date getPlaced() 
	{
		return placed;
	}
	
	public void setGuid(final String guid)
	{
		this.guid = guid;
	}

	public String getGuid() 
	{
		return guid;
	}
	
	public void setTotal(final PriceWsDTO total)
	{
		this.total = total;
	}

	public PriceWsDTO getTotal() 
	{
		return total;
	}
	

}