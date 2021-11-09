/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.queues;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.queues.OrderStatusUpdateElementWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of an Order Status Update Element List
 */
@ApiModel(value="OrderStatusUpdateElementList", description="Representation of an Order Status Update Element List")
public  class OrderStatusUpdateElementListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of order status update elements<br/><br/><i>Generated property</i> for <code>OrderStatusUpdateElementListWsDTO.orderStatusUpdateElements</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="orderStatusUpdateElements", value="List of order status update elements") 	
	private List<OrderStatusUpdateElementWsDTO> orderStatusUpdateElements;
	
	public OrderStatusUpdateElementListWsDTO()
	{
		// default constructor
	}
	
	public void setOrderStatusUpdateElements(final List<OrderStatusUpdateElementWsDTO> orderStatusUpdateElements)
	{
		this.orderStatusUpdateElements = orderStatusUpdateElements;
	}

	public List<OrderStatusUpdateElementWsDTO> getOrderStatusUpdateElements() 
	{
		return orderStatusUpdateElements;
	}
	

}