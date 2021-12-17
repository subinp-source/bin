/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Cart list
 */
@ApiModel(value="CartList", description="Representation of a Cart list")
public  class CartListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of carts<br/><br/><i>Generated property</i> for <code>CartListWsDTO.carts</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="carts", value="List of carts") 	
	private List<CartWsDTO> carts;
	
	public CartListWsDTO()
	{
		// default constructor
	}
	
	public void setCarts(final List<CartWsDTO> carts)
	{
		this.carts = carts;
	}

	public List<CartWsDTO> getCarts() 
	{
		return carts;
	}
	

}