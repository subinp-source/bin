/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;


import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.AbstractMultidimensionalProductFieldValueProvider;
import de.hybris.platform.core.model.product.ProductModel;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;


/**
 * Retrieves the multidimensional flag for a product model.
 */
public class MultidimentionalProductFlagValueProvider extends AbstractMultidimensionalProductFieldValueProvider implements Serializable
{

	@Override
	public Object getFieldValue(final ProductModel product)
	{
		Boolean isMultidimentional = null;

		final Object variants = modelService.getAttributeValue(product, "variants");
		isMultidimentional = Boolean.valueOf(CollectionUtils.isNotEmpty((Collection) variants));

		return isMultidimentional;
	}

}
