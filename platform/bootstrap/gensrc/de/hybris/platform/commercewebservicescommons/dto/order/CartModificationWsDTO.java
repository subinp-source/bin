/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Cart modification
 */
@ApiModel(value="CartModification", description="Representation of a Cart modification")
public  class CartModificationWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Status code of cart modification<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.statusCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="statusCode", value="Status code of cart modification") 	
	private String statusCode;

	/** Quantity added with cart modification<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.quantityAdded</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="quantityAdded", value="Quantity added with cart modification") 	
	private Long quantityAdded;

	/** Final quantity after cart modification<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="quantity", value="Final quantity after cart modification") 	
	private Long quantity;

	/** Order entry<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.entry</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="entry", value="Order entry") 	
	private OrderEntryWsDTO entry;

	/** Delivery mode changed<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.deliveryModeChanged</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="deliveryModeChanged", value="Delivery mode changed") 	
	private Boolean deliveryModeChanged;

	/** Status message<br/><br/><i>Generated property</i> for <code>CartModificationWsDTO.statusMessage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="statusMessage", value="Status message") 	
	private String statusMessage;
	
	public CartModificationWsDTO()
	{
		// default constructor
	}
	
	public void setStatusCode(final String statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getStatusCode() 
	{
		return statusCode;
	}
	
	public void setQuantityAdded(final Long quantityAdded)
	{
		this.quantityAdded = quantityAdded;
	}

	public Long getQuantityAdded() 
	{
		return quantityAdded;
	}
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Long getQuantity() 
	{
		return quantity;
	}
	
	public void setEntry(final OrderEntryWsDTO entry)
	{
		this.entry = entry;
	}

	public OrderEntryWsDTO getEntry() 
	{
		return entry;
	}
	
	public void setDeliveryModeChanged(final Boolean deliveryModeChanged)
	{
		this.deliveryModeChanged = deliveryModeChanged;
	}

	public Boolean getDeliveryModeChanged() 
	{
		return deliveryModeChanged;
	}
	
	public void setStatusMessage(final String statusMessage)
	{
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() 
	{
		return statusMessage;
	}
	

}