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
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Delivery mode
 */
@ApiModel(value="DeliveryMode", description="Representation of a Delivery mode")
public  class DeliveryModeWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the delivery mode<br/><br/><i>Generated property</i> for <code>DeliveryModeWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of the delivery mode") 	
	private String code;

	/** Name of the delivery mode<br/><br/><i>Generated property</i> for <code>DeliveryModeWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the delivery mode") 	
	private String name;

	/** Description of the delivery mode<br/><br/><i>Generated property</i> for <code>DeliveryModeWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Description of the delivery mode") 	
	private String description;

	/** Cost of the delivery<br/><br/><i>Generated property</i> for <code>DeliveryModeWsDTO.deliveryCost</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="deliveryCost", value="Cost of the delivery") 	
	private PriceWsDTO deliveryCost;
	
	public DeliveryModeWsDTO()
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
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setDeliveryCost(final PriceWsDTO deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}

	public PriceWsDTO getDeliveryCost() 
	{
		return deliveryCost;
	}
	

}