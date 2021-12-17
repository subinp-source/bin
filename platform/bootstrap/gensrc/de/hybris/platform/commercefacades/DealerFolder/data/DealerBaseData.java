/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.DealerFolder.data;

import java.io.Serializable;


import java.util.Objects;
public  class DealerBaseData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DealerBaseData.localizeName</code> property defined at extension <code>trainingfacades</code>. */
		
	private String localizeName;

	/** <i>Generated property</i> for <code>DealerBaseData.uniqueId</code> property defined at extension <code>trainingfacades</code>. */
		
	private String uniqueId;

	/** <i>Generated property</i> for <code>DealerBaseData.address</code> property defined at extension <code>trainingfacades</code>. */
		
	private String address;
	
	public DealerBaseData()
	{
		// default constructor
	}
	
	public void setLocalizeName(final String localizeName)
	{
		this.localizeName = localizeName;
	}

	public String getLocalizeName() 
	{
		return localizeName;
	}
	
	public void setUniqueId(final String uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	public String getUniqueId() 
	{
		return uniqueId;
	}
	
	public void setAddress(final String address)
	{
		this.address = address;
	}

	public String getAddress() 
	{
		return address;
	}
	

}