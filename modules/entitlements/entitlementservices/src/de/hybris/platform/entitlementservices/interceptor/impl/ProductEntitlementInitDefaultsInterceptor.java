/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.interceptor.impl;

import de.hybris.platform.entitlementservices.enums.EntitlementTimeUnit;
import de.hybris.platform.entitlementservices.model.ProductEntitlementModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

/**
 * Set default values to {@link ProductEntitlementModel}
 */
public class ProductEntitlementInitDefaultsInterceptor implements InitDefaultsInterceptor
{
	@Override
	public void onInitDefaults(final Object o, final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (o instanceof ProductEntitlementModel)
		{
			final ProductEntitlementModel productEntitlement = (ProductEntitlementModel) o;
			productEntitlement.setTimeUnitStart(1);
			productEntitlement.setTimeUnit(EntitlementTimeUnit.MONTH);
		}
	}
}
