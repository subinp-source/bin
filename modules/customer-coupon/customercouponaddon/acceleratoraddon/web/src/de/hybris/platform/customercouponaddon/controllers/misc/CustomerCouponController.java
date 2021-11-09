/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponaddon.controllers.misc;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;

/**
 * Controller for my coupons.
 */
@Controller
@RequestMapping("/cart")
public class CustomerCouponController 
{
	@Resource(name = "customerCouponFacade")
	private CustomerCouponFacade customerCouponFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@ResponseBody
	@RequestMapping(value = "/effectivecoupons", method = RequestMethod.GET)
	public List<CustomerCouponData> effectiveCoupons()
	{
		final CartData cartData = getCartFacade().getSessionCart();
		List<CustomerCouponData> couponList = getCustomerCouponFacade().getCouponsData();
		final List<String> couponId = cartData.getAppliedVouchers();
		if (couponId != null)
		{
			couponList = couponList.stream().filter(coupon -> !couponId.contains(coupon.getCouponId()))
					.collect(Collectors.toList());
		}
		
		return couponList;
	}
	
	protected CustomerCouponFacade getCustomerCouponFacade()
	{
		return customerCouponFacade;
	}
	
	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

}
