/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cmsfacades.util.builder.ProductCategoryModelBuilder;

import java.util.Locale;


public class ProductCategoryModelMother extends AbstractModelMother<CategoryModel>
{
	public static final String ELECTRONICS = "electronics";
	public static final String CARS = "cars";
	public static final String EN_MULTI_LANG_NAME = "snowboards";
	public static final String DE_MULTI_LANG_NAME = "reparatur";
	public static final String MULTI_LANG_CODE = "snowboard-category-code";

	private CategoryDao categoryDao;

	protected CategoryModel getCategoryByCode(final String categoryCode, final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn(() -> getCategoryDao().findCategoriesByCode(categoryCode),
				() -> ProductCategoryModelBuilder.aModel() //
						.withName(categoryCode, Locale.ENGLISH) //
						.withCatalogVersion(catalogVersion) //
						.withCode(categoryCode)
						.build());
	}

	public CategoryModel createMultiLangCategory(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn(() -> getCategoryDao().findCategoriesByCode(MULTI_LANG_CODE),
				() -> ProductCategoryModelBuilder.aModel() //
						.withName(EN_MULTI_LANG_NAME, Locale.ENGLISH) //
						.withName(DE_MULTI_LANG_NAME, Locale.GERMAN) //
						.withCatalogVersion(catalogVersion) //
						.withCode(MULTI_LANG_CODE)
						.build());
	}

	public CategoryModel createDefaultCategory(final CatalogVersionModel catalogVersion)
	{
		return getCategoryByCode(ELECTRONICS, catalogVersion);
	}

	public CategoryModel createCarsCategory(final CatalogVersionModel catalogVersion)
	{
		return getCategoryByCode(CARS, catalogVersion);
	}

	public CategoryDao getCategoryDao()
	{
		return categoryDao;
	}

	public void setCategoryDao(final CategoryDao categoryDao)
	{
		this.categoryDao = categoryDao;
	}
}
