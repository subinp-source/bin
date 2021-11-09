/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Locale;


public class CategoryModelBuilder
{

	private final CategoryModel model;

	private CategoryModelBuilder()
	{
		model = new CategoryModel();
	}

	protected CategoryModel getModel()
	{
		return this.model;
	}

	public static CategoryModelBuilder aModel()
	{
		return new CategoryModelBuilder();
	}

	public static CategoryModelBuilder fromModel(final CategoryModel model)
	{
		return new CategoryModelBuilder();
	}


	public CategoryModel build()
	{
		return getModel();
	}

	public CategoryModelBuilder withCatalogVersion(final CatalogVersionModel catalogue)
	{
		getModel().setCatalogVersion(catalogue);
		return this;
	}

	public CategoryModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public CategoryModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

}
