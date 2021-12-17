/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.user.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.user.data.CountryData;
import java.util.List;


import java.util.Objects;
public  class CountryDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CountryDataList.countries</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<CountryData> countries;
	
	public CountryDataList()
	{
		// default constructor
	}
	
	public void setCountries(final List<CountryData> countries)
	{
		this.countries = countries;
	}

	public List<CountryData> getCountries() 
	{
		return countries;
	}
	

}