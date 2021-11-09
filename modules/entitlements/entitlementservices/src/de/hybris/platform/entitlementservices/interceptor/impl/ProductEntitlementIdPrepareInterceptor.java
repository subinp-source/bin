/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.interceptor.impl;

import de.hybris.platform.entitlementservices.model.ProductEntitlementModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Create a unique id for new {@link de.hybris.platform.entitlementservices.model.ProductEntitlementModel}s if not yet set
 */
public class ProductEntitlementIdPrepareInterceptor implements PrepareInterceptor
{
	private KeyGenerator productEntitlementIDGenerator;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{

		if (model instanceof ProductEntitlementModel)
		{
			final ProductEntitlementModel productEntitlement = (ProductEntitlementModel) model;
			final String id = productEntitlement.getId();
			if (StringUtils.isEmpty(id))
			{
				productEntitlement.setId((String) this.productEntitlementIDGenerator.generate());
			}
		}
	}

	@Required
	public void setProductEntitlementIDGenerator(final KeyGenerator productEntitlementIDGenerator)
	{
		this.productEntitlementIDGenerator = productEntitlementIDGenerator;
	}

}
