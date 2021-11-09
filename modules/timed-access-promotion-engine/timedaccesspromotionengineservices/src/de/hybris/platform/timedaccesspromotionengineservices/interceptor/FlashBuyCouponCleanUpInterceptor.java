/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Deletes the cronjob after removing a flash buy coupon
 */
public class FlashBuyCouponCleanUpInterceptor implements RemoveInterceptor<FlashBuyCouponModel>
{

	private FlashBuyService flashBuyService;

	/**
	 * Deletes the cronjob after removing a flash buy coupon
	 *
	 * @param coupon
	 *           flash buy coupon
	 * @param ctx
	 *           the context
	 */
	@Override
	public void onRemove(final FlashBuyCouponModel coupon, final InterceptorContext ctx) throws InterceptorException
	{
		getFlashBuyService().deleteCronJobAndTrigger(coupon);
	}

	protected FlashBuyService getFlashBuyService()
	{
		return flashBuyService;
	}

	@Required
	public void setFlashBuyService(final FlashBuyService flashBuyService)
	{
		this.flashBuyService = flashBuyService;
	}

}
