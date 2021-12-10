/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.ReturnRequestEntryInputWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a return request entry input list for an order
 */
@ApiModel(value="ReturnRequestEntryInputList", description="Representation of a return request entry input list for an order")
public  class ReturnRequestEntryInputListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the order which return request is related to<br/><br/><i>Generated property</i> for <code>ReturnRequestEntryInputListWsDTO.orderCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="orderCode", value="Code of the order which return request is related to", required=true, example="00000001") 	
	private String orderCode;

	/** Return request entry inputs which contain information about the order entries which are requested to be returned<br/><br/><i>Generated property</i> for <code>ReturnRequestEntryInputListWsDTO.returnRequestEntryInputs</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="returnRequestEntryInputs", value="Return request entry inputs which contain information about the order entries which are requested to be returned", required=true) 	
	private List<ReturnRequestEntryInputWsDTO> returnRequestEntryInputs;
	
	public ReturnRequestEntryInputListWsDTO()
	{
		// default constructor
	}
	
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getOrderCode() 
	{
		return orderCode;
	}
	
	public void setReturnRequestEntryInputs(final List<ReturnRequestEntryInputWsDTO> returnRequestEntryInputs)
	{
		this.returnRequestEntryInputs = returnRequestEntryInputs;
	}

	public List<ReturnRequestEntryInputWsDTO> getReturnRequestEntryInputs() 
	{
		return returnRequestEntryInputs;
	}
	

}