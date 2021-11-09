/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Locale;


public class ProductCategoryModelBuilder
{
	private final CategoryModel model;

	private ProductCategoryModelBuilder(final CategoryModel model)
	{
		this.model = model;
	}

	private ProductCategoryModelBuilder()
	{
		this.model = new CategoryModel();
	}

	public CategoryModel getModel()
	{
		return model;
	}

	public static ProductCategoryModelBuilder aModel()
	{
		return new ProductCategoryModelBuilder();
	}

	public static ProductCategoryModelBuilder fromModel(final CategoryModel model)
	{
		return new ProductCategoryModelBuilder(model);
	}

	public ProductCategoryModelBuilder withName(final String name)
	{
		getModel().setName(name);
		return this;
	}

	public ProductCategoryModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public ProductCategoryModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}
	
	public ProductCategoryModelBuilder withCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		getModel().setCatalogVersion(catalogVersion);
		return this;
	}

	
	public CategoryModel build()
	{
		return this.getModel();
	}
}
