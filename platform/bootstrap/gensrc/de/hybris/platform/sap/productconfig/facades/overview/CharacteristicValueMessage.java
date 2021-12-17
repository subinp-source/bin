/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.overview;

import java.io.Serializable;


import java.util.Objects;
public  class CharacteristicValueMessage  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CharacteristicValueMessage.message</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>CharacteristicValueMessage.endDate</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String endDate;
	
	public CharacteristicValueMessage()
	{
		// default constructor
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate() 
	{
		return endDate;
	}
	

}