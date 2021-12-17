/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;


import java.util.Objects;
public  class CommerceSaveCartParameterData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CommerceSaveCartParameterData.cartId</code> property defined at extension <code>commercefacades</code>. */
		
	private String cartId;

	/** <i>Generated property</i> for <code>CommerceSaveCartParameterData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CommerceSaveCartParameterData.description</code> property defined at extension <code>commercefacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CommerceSaveCartParameterData.enableHooks</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean enableHooks;
	
	public CommerceSaveCartParameterData()
	{
		// default constructor
	}
	
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

	public String getCartId() 
	{
		return cartId;
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