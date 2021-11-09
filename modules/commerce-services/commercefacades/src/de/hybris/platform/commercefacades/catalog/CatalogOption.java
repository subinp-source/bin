/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.catalog;


/**
 * Options for catalog facade. BASIC - only basic informations. CATEGORIES - also informations about categories.
 * PRODUCTS - works with CATEGORIES option to get informations about products as well.
 */
public enum CatalogOption
{
	BASIC("BASIC"), CATEGORIES("CATEGORIES"), PRODUCTS("PRODUCTS"), SUBCATEGORIES("SUBCATEGORIES");

	private final String code;

	private CatalogOption(final String code)
	{
		this.code = code;
	}

}
