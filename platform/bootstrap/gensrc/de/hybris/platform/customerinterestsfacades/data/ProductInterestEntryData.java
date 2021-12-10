/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class ProductInterestEntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductInterestEntryData.interestType</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private String interestType;

	/** <i>Generated property</i> for <code>ProductInterestEntryData.dateAdded</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private Date dateAdded;

	/** <i>Generated property</i> for <code>ProductInterestEntryData.expirationDate</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private Date expirationDate;
	
	public ProductInterestEntryData()
	{
		// default constructor
	}
	
	public void setInterestType(final String interestType)
	{
		this.interestType = interestType;
	}

	public String getInterestType() 
	{
		return interestType;
	}
	
	public void setDateAdded(final Date dateAdded)
	{
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded() 
	{
		return dateAdded;
	}
	
	public void setExpirationDate(final Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	public Date getExpirationDate() 
	{
		return expirationDate;
	}
	

}