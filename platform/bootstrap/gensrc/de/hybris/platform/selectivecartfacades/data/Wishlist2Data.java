/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.selectivecartfacades.data.Wishlist2EntryData;
import java.util.List;


import java.util.Objects;
public  class Wishlist2Data  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Wishlist2Data.name</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>Wishlist2Data.user</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private PrincipalData user;

	/** <i>Generated property</i> for <code>Wishlist2Data.entries</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private List<Wishlist2EntryData> entries;
	
	public Wishlist2Data()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setUser(final PrincipalData user)
	{
		this.user = user;
	}

	public PrincipalData getUser() 
	{
		return user;
	}
	
	public void setEntries(final List<Wishlist2EntryData> entries)
	{
		this.entries = entries;
	}

	public List<Wishlist2EntryData> getEntries() 
	{
		return entries;
	}
	

}