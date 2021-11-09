/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.search.impl;

import de.hybris.platform.b2bacceleratorservices.product.B2BProductService;
import de.hybris.platform.b2bacceleratorservices.product.impl.DefaultB2BProductService;
import de.hybris.platform.b2bacceleratorservices.search.B2BProductSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated Since 6.0. Use {@link de.hybris.platform.b2bacceleratorservices.product.impl.DefaultB2BProductService}
 *             instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultB2BFlexibleSearchProductSearchService implements B2BProductSearchService<ProductModel>
{

	private B2BProductService b2BProductService;

	/**
	 * @deprecated Since 6.0. Use {@link DefaultB2BProductService#getProductsForSkus(Collection, PageableData)} instead.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	@Override
	public SearchPageData<ProductModel> findProductsBySkus(final Collection<String> skus, final PageableData pageableData)
	{
		return getB2BProductService().getProductsForSkus(skus, pageableData);
	}

	@Required
	public void setB2BProductService(final B2BProductService b2BProductService)
	{
		this.b2BProductService = b2BProductService;
	}

	protected B2BProductService getB2BProductService()
	{
		return b2BProductService;
	}

}
