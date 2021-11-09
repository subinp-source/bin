/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.converters.data;


public class TestProductData implements java.io.Serializable
{

	private String code;
	private String description;
	private String name;

	public TestProductData()
	{
		// default constructor
	}


	public void setCode(final String code)
	{
		this.code = code;
	}


	public String getCode()
	{
		return code;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}


	public String getDescription()
	{
		return description;
	}

	public void setName(final String name)
	{
		this.name = name;
	}


	public String getName()
	{
		return name;
	}

}
