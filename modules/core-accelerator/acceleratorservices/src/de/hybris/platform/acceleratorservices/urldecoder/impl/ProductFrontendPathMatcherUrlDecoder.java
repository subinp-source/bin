/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.urldecoder.impl;


import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class ProductFrontendPathMatcherUrlDecoder extends BaseFrontendPathMatcherUrlDecoder<ProductModel>
{

	private static final Logger LOG = Logger.getLogger(ProductFrontendPathMatcherUrlDecoder.class);
	private ProductService productService;

	@Override
	protected ProductModel translateId(final String id)
	{
		try
		{
			return getProductService().getProductForCode(id);
		}
		catch (ModelNotFoundException | UnknownIdentifierException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return null;
		}
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected ProductService getProductService()
	{
		return this.productService;
	}

}
