/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.constants;

/**
 * Constants used for Merchandising Addons
 *
 */
public class MerchandisingConstants
{
	public static final String PAGE_CONTEXT_CATEGORY = "ItemCategory";
	public static final String PAGE_CONTEXT_FACETS = "ContextFacets";
	public static final String PAGE_CONTEXT_BREADCRUMBS = "ContextBreadcrumbs";
	public static final String JS_ADDONS_CONTEXT_VARIABLES = "jsAddOnsVariables";
	public static final String SITE_ID = "siteId";
	public static final String PAGE_CONTEXT_TENANT = "hybrisTenant";
	public static final String CONTEXT_STORE_KEY = "hybrisMerchandisingContextStore";
	public static final String LANGUAGE = "language";
	public static final String PRODUCT = "product";

	private MerchandisingConstants()
	{
		throw new IllegalStateException("Merchandising Constants class shouldn't be instantiated");
	}

}
