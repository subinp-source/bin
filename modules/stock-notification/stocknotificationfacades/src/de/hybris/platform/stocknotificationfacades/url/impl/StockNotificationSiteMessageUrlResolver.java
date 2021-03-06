/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationfacades.url.impl;

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.notificationfacades.url.SiteMessageUrlResolver;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link UrlResolver} to resolve stock notification site message link.
 */
public class StockNotificationSiteMessageUrlResolver extends SiteMessageUrlResolver<ProductModel>
{

	private UrlResolver<ProductModel> productModelUrlResolver;

	@Override
	public String resolve(final ProductModel source)
	{
		return source == null ? getDefaultUrl() : getProductModelUrlResolver().resolve(source);
	}

	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	@Required
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

}
