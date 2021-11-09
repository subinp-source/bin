/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.util.builder.ProductModelBuilder;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

import java.util.Locale;


public class ProductModelMother extends AbstractModelMother<ProductModel>
{
	public static final String MOUSE = "mouse";
	public static final String CAR = "car";
	public static final String EN_MULTI_LANG_NAME = "snowboard name";
	public static final String DE_MULTI_LANG_NAME = "reparatur";
	public static final String MULTI_LANG_UID = "snow-board-uid";


	private ProductDao productDao;

	public ProductModel createMouseProduct(final CatalogVersionModel catalogVersion)
	{
		return createDefaultProduct(MOUSE, catalogVersion);
	}

	public ProductModel createCarProduct(final CatalogVersionModel catalogVersion)
	{
		return createDefaultProduct(CAR, catalogVersion);
	}

	public ProductModel createMultiLangProduct(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode(MULTI_LANG_UID),
				() -> ProductModelBuilder.aModel() //
						.withName(EN_MULTI_LANG_NAME, Locale.ENGLISH) //
						.withName(DE_MULTI_LANG_NAME, Locale.GERMAN)
						.withCatalogVersion(catalogVersion) //
						.withCode(MULTI_LANG_UID).build());
	}

	protected ProductModel createDefaultProduct(final String uid, final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode(uid),
				() -> ProductModelBuilder.aModel() //
				.withName(uid, Locale.ENGLISH) //
				.withCatalogVersion(catalogVersion) //
				.withCode(uid).build());
	}

	public ProductDao getProductDao()
	{
		return productDao;
	}

	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}
}

