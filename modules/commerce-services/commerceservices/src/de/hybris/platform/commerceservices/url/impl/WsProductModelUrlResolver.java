/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.url.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.site.BaseSiteService;


/**
 * URL resolver for ProductModel instances.
 * Generates a URL with the following pattern:
 *  /[Site Uid]/products/[Product Code]
 */
public class WsProductModelUrlResolver extends AbstractUrlResolver<ProductModel>
{
	BaseSiteService baseSiteService;

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver#resolveInternal(java.lang.Object)
	 */
	@Override
	protected String resolveInternal(final ProductModel source)
	{
		final String uid = baseSiteService.getCurrentBaseSite().getUid();
		return "/" + uid + "/products/" + source.getCode();
	}
}
