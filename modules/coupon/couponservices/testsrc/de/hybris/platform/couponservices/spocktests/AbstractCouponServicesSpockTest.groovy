/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.couponservices.spocktests

import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.couponservices.services.CouponService
import de.hybris.platform.promotionengineservices.spocktests.AbstractPromotionEngineServicesSpockTest

import javax.annotation.Resource

import org.junit.Ignore


@Ignore
class AbstractCouponServicesSpockTest extends AbstractPromotionEngineServicesSpockTest
{

	@Resource
	CouponService couponService

	def setup()
	{
		importCsv("/couponservices/test/couponsAndSourceRules.impex", "utf-8")
	}

	// from legacy atdd keyword
	protected void redeemCouponWithCodeForCart(final String couponCode, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)
		getCouponService().redeemCoupon(couponCode, cart)
	}
	// from legacy atdd keyword
	protected void releaseCouponWithCodeForCart(final String couponCode, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)
		getCouponService().releaseCouponCode(couponCode, cart)
	}

	//	ImpersonationContext buildImpersonationContext(String user, String forCurrency)
	//	{
	//	  ImpersonationContext impersonationContext = new ImpersonationContext()
	//	  UserModel userModel = user.isEmpty() ? getUserService().getAnonymousUser() : getUserService().getUserForUID(user)
	//
	//	  impersonationContext.catalogVersions = [
	//				 getCatalogVersionService().getCatalogVersion('testCatalog', 'Online')
	//	  ]
	//	  impersonationContext.currency = getCommonI18NService().getCurrency(forCurrency)
	//	  getCommonI18NService().setCurrentCurrency(impersonationContext.currency)
	//	  impersonationContext.language = getCommonI18NService().getLanguage('en')
	//	  impersonationContext.site = getBaseSiteService().getBaseSiteForUID('testSite')
	//	  impersonationContext.user = userModel
	//	  return impersonationContext
	//	}

	protected CouponService getCouponService()
	{
		return couponService
	}
}
