/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

/**
 * Category name value provider. Value provider that generates field values for localized category names.
 */
public class CategoryNameValueProvider extends CategoryCodeValueProvider
{
	@Override
	protected Object getPropertyValue(final Object model)
	{
		return getPropertyValue(model, "name");
	}
}
