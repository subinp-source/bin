/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.listeners;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.processor.ProductDirectoryProcessor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

/**
 * ProductDirectoryRemoveInterceptor is an {@link RemoveInterceptor} for capturing deletion of
 * {@link MerchProductDirectoryConfigModel} items for synchronisation to CDS.
 *
 */
public class ProductDirectoryRemoveInterceptor implements RemoveInterceptor<MerchProductDirectoryConfigModel>
{
	private ProductDirectoryProcessor productDirectoryProcessor;

	@Override
	public void onRemove(final MerchProductDirectoryConfigModel model, final InterceptorContext ctx) throws InterceptorException
	{
		productDirectoryProcessor.delete(model);
	}

	public void setProductDirectoryProcessor(final ProductDirectoryProcessor productDirectoryProcessor)
	{
		this.productDirectoryProcessor = productDirectoryProcessor;
	}
}