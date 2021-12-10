/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.service.data;

import java.io.Serializable;
import de.hybris.platform.core.model.order.CartModel;


import java.util.Objects;
public  class CommerceSaveCartParameter  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** The CartModel to be saved<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.cart</code> property defined at extension <code>commerceservices</code>. */
		
	private CartModel cart;

	/** The name of the saved cart provided by user or auto-generated<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** The description of the saved cart provided by user or auto-generated<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.description</code> property defined at extension <code>commerceservices</code>. */
		
	private String description;

	/** Should the method hooks be executed<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.enableHooks</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean enableHooks;
	
	public CommerceSaveCartParameter()
	{
		// default constructor
	}
	
	public void setCart(final CartModel cart)
	{
		this.cart = cart;
	}

	public CartModel getCart() 
	{
		return cart;
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
	
	public void setEnableHooks(final boolean enableHooks)
	{
		this.enableHooks = enableHooks;
	}

	public boolean isEnableHooks() 
	{
		return enableHooks;
	}
	

}