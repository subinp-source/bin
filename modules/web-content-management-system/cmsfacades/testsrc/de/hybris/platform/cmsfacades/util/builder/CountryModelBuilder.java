/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.core.model.c2l.CountryModel;

import java.util.Locale;

public class CountryModelBuilder 
{
	private final CountryModel model;

	private CountryModelBuilder() 
	{
		model = new CountryModel();
	}

	protected CountryModel getModel() 
	{
		return this.model;
	}

	public static CountryModelBuilder aModel() 
	{
		return new CountryModelBuilder();
	}
	
	public static CountryModelBuilder fromModel(final CountryModel model)
	{
		return new CountryModelBuilder();
	}


	public CountryModel build() 
	{
		return getModel();
	}

	public CountryModelBuilder withIsoCode(final String isoCode) 
	{
		getModel().setIsocode(isoCode);
		return this;
	}

	public CountryModelBuilder withName(final String name, final Locale locale) 
	{
		getModel().setName(name, locale);
		return this;
	}

	public CountryModelBuilder withActive(final Boolean active) 
	{
		getModel().setActive(active);
		return this;
	}
}