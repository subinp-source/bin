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
public  class RemoveEntryGroupParameter  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** The CartModel<br/><br/><i>Generated property</i> for <code>RemoveEntryGroupParameter.cart</code> property defined at extension <code>commerceservices</code>. */
		
	private CartModel cart;

	/** Should the method hooks be executed<br/><br/><i>Generated property</i> for <code>RemoveEntryGroupParameter.enableHooks</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean enableHooks;

	/** Entry group number to be removed<br/><br/><i>Generated property</i> for <code>RemoveEntryGroupParameter.entryGroupNumber</code> property defined at extension <code>commerceservices</code>. */
		
	private Integer entryGroupNumber;
	
	public RemoveEntryGroupParameter()
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
	
	public void setEnableHooks(final boolean enableHooks)
	{
		this.enableHooks = enableHooks;
	}

	public boolean isEnableHooks() 
	{
		return enableHooks;
	}
	
	public void setEntryGroupNumber(final Integer entryGroupNumber)
	{
		this.entryGroupNumber = entryGroupNumber;
	}

	public Integer getEntryGroupNumber() 
	{
		return entryGroupNumber;
	}
	

}