/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.forms;


public class AddressForm
{
	private String titleCode;
	private String firstName;
	private String lastName;
	private String line1;
	private String line2;
	private String townCity;
	private String postcode;
	private String countryIso;

	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getLine1()
	{
		return line1;
	}

	public void setLine1(final String line1)
	{
		this.line1 = line1;
	}

	public String getLine2()
	{
		return line2;
	}

	public void setLine2(final String line2)
	{
		this.line2 = line2;
	}

	public String getTownCity()
	{
		return townCity;
	}

	public void setTownCity(final String townCity)
	{
		this.townCity = townCity;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(final String postcode)
	{
		this.postcode = postcode;
	}

	public String getCountryIso()
	{
		return countryIso;
	}

	public void setCountryIso(final String countryIso)
	{
		this.countryIso = countryIso;
	}
}
