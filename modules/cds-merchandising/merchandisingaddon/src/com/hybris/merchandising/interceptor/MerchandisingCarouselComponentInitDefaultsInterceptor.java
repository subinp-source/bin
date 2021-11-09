/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.interceptor;

import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import com.hybris.merchandising.addon.model.MerchandisingCarouselComponentModel;

public class MerchandisingCarouselComponentInitDefaultsInterceptor
		implements InitDefaultsInterceptor<MerchandisingCarouselComponentModel>
{
	@Override
	public void onInitDefaults(final MerchandisingCarouselComponentModel model, final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model.getNumberToDisplay() <= 0)
		{
			model.setNumberToDisplay(5);
		}
		if (model.getViewportPercentage() <= 0)
		{
			model.setViewportPercentage(80);
		}
	}
}
