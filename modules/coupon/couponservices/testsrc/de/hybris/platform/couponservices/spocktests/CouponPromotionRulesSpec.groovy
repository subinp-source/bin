/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.couponservices.spocktests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.order.CartModel

import org.junit.Test

import spock.lang.Unroll


@IntegrationTest
class CouponPromotionRulesSpec extends AbstractCouponServicesSpockTest
{
	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) and redemption of #couponCodes should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it. The coupon should be used - #couponShouldBeUsed"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		CartModel cart1 = createCart("cart1", forCurrency)
		productsToAddToCart.each
		{ code, quantity ->
			addProductToCart(code, quantity, cart1)
		}
		double cart1TotalPriceWithoutPromo = cart1.totalPrice
		couponCodes.each
		{ couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceWithRedeemedCoupons = cart1.totalPrice
		boolean couponIsUsed = couponCodes.empty ? false : couponCodes.every { couponCode -> orderUsesCoupon(cart1.getCode(), couponCode) }

		couponCodes.each
		{ couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceAfterReleasedCoupons = cart1.totalPrice

		then:
		cart1TotalPriceWithoutPromo == baseAmount
		cart1TotalPriceWithRedeemedCoupons == promotedAmount
		cart1TotalPriceAfterReleasedCoupons == baseAmount
		couponIsUsed == couponShouldBeUsed

		where:
		testId | productsToAddToCart | promotionRules | couponCodes | couponShouldBeUsed | promotedAmount | baseAmount | forCurrency

		// Apply fixed discount on carts using one coupon code
		'Test_Coupon_Code_Fixed_Discount_On_Carts' | ['1382080': 1] | ['coupon_code_fixed_discount']| ['SUMMER69']| true | 564.88  | 574.88 | 'USD'
		'Test_Coupon_Code_Fixed_Discount_On_Carts' | ['1382080': 1] | ['coupon_code_fixed_discount']| []| false | 574.88  | 574.88 | 'USD'

		// Apply fixed discount on cart using one coupon code. Discount from coupon is over cart total
		'Test_Coupon_Code_Fixed_Discount_On_Carts_Discount_Over_Cart_Total' | ['137220': 1] | ['coupon_code_fixed_discount']| ['SUMMER69']| false | 8.00  | 8.00 | 'USD'

		// Apply fixed discount on carts using one coupon code more than once (MultiCodeCoupon)
		'Test_Coupon_Code_Fixed_Discount_On_Carts_One_MultiCodeCoupon' | ['301233': 1] | ['coupon_code_fixed_discount']| ['SUMMER69']| true | 11.56  | 21.56 | 'USD'

		// TODO this one is not right
		// Apply fixed percentage discount on carts using one coupon code
		'Test_Coupon_Code_Percentage_Discount_On_Carts' | ['301233': 1] | ['coupon_code_fixed_discount']| ['SUMMER69']| true | 11.56  | 21.56 | 'USD'

		// Apply coupon code then over cart total threshold
		// below threshold
		'Test_Coupon_Code_And_Fixed_Threshold' | ['23210': 1] | ['coupon_cart_threshold']| ['SUMMER69']| false | 110.00  | 110.00 | 'USD'
		// over threshold
		'Test_Coupon_Code_And_Fixed_Threshold' | ['23210': 1, '300938': 1] | ['coupon_cart_threshold']| ['SUMMER69']| true | 214.12  | 224.12 | 'USD'

		// Apply coupon code then conditional product
		// redeem discount - promotion shouldn't trigger yet because cart total below threshold
		'Test_Coupon_Code_And_Product' | ['23210': 1] | ['coupon_PowershotA480']| ['SUMMER69']| false | 110.00  | 110.00 | 'USD'
		'Test_Coupon_Code_And_Product' | ['23210': 1, '1934794': 1] | ['coupon_PowershotA480']| ['SUMMER69']| true | 199.85  | 209.85 | 'USD'
	}

	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to 2 Carts(#forCurrency) and redemption of #couponCodes for both with #promotionRules promotion should amount to #promotedAmount1 for cart1, #promotedAmount2 for cart2 and #baseAmount1, #baseAmount2 without it"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		CartModel cart1 = createCart("cart1", forCurrency)
		CartModel cart2 = createCart("cart2", forCurrency)
		productsToAddToCart.each
		{ code, quantity ->
			addProductToCart(code, quantity, cart1)
			addProductToCart(code, quantity, cart2)
		}
		// cart1 redemption
		double cart1TotalPriceWithoutPromo = cart1.totalPrice
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceWithRedeemedCoupons = cart1.totalPrice
		// cart2 redemption
		double cart2TotalPriceWithoutPromo = cart2.totalPrice
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart2.getCode())
		}
		evaluatePromotionForCart(cart2.getCode())
		double cart2TotalPriceWithRedeemedCoupons = cart2.totalPrice
		// cart1 coupon release
		couponCodes.each { couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceAfterReleasedCoupons = cart1.totalPrice
		// cart2 coupon release
		couponCodes.each { couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart2.getCode())
		}
		evaluatePromotionForCart(cart2.getCode())
		double cart2TotalPriceAfterReleasedCoupons = cart2.totalPrice
		then:
		cart1TotalPriceWithoutPromo == baseAmount1
		cart1TotalPriceWithRedeemedCoupons == promotedAmount1
		cart1TotalPriceAfterReleasedCoupons == baseAmount1
		cart2TotalPriceWithoutPromo == baseAmount2
		cart2TotalPriceWithRedeemedCoupons == promotedAmount2
		cart2TotalPriceAfterReleasedCoupons == baseAmount2

		where:
		testId | productsToAddToCart | promotionRules | couponCodes | promotedAmount1 | baseAmount1 | promotedAmount2 | baseAmount2 | forCurrency

		// Apply fixed percentage discount on carts using one coupon code
		'Test_Coupon_Code_Percentage_Discount_On_Carts' | ['1382080': 1] | ['coupon_code_percentage_discount']| ['WINTER16']| 517.39  | 574.88 | 517.39  | 574.88 | 'USD'
	}

	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to 2 Carts(#forCurrency) and redemption of #couponCodes for both with #promotionRules promotion should give free gifts #gifts1 for cart1, #gifts2 for cart2 and have the same prices #baseAmount1 for cart1, #baseAmount2 for cart2"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		CartModel cart1 = createCart("cart1", forCurrency)
		CartModel cart2 = createCart("cart2", forCurrency)
		productsToAddToCart.each { code, quantity ->
			addProductToCart(code, quantity, cart1)
			addProductToCart(code, quantity, cart2)
		}
		// cart1 redemption
		double cart1TotalPriceWithoutPromo = cart1.totalPrice
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceWithRedeemedCoupons = cart1.totalPrice
		// cart2 redemption
		double cart2TotalPriceWithoutPromo = cart2.totalPrice
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart2.getCode())
		}
		evaluatePromotionForCart(cart2.getCode())
		double cart2TotalPriceWithRedeemedCoupons = cart2.totalPrice


		boolean gifts1PresentAsExpected = gifts1.every { code, quantity ->
			checkProductQuantity(code, cart1, quantity)
		}
		boolean gifts2PresentAsExpected = gifts2.every { code, quantity ->
			checkProductQuantity(code, cart2, quantity)
		}

		// cart1 coupon release
		couponCodes.each { couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceAfterReleasedCoupons = cart1.totalPrice
		// cart2 coupon release
		couponCodes.each { couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart2.getCode())
		}
		evaluatePromotionForCart(cart2.getCode())
		double cart2TotalPriceAfterReleasedCoupons = cart2.totalPrice
		then:
		cart1TotalPriceWithoutPromo == baseAmount1
		cart1TotalPriceWithRedeemedCoupons == baseAmount1
		cart1TotalPriceAfterReleasedCoupons == baseAmount1
		cart2TotalPriceWithoutPromo == baseAmount2
		cart2TotalPriceWithRedeemedCoupons == baseAmount2
		cart2TotalPriceAfterReleasedCoupons == baseAmount2
		gifts1PresentAsExpected == true
		gifts2PresentAsExpected == true

		where:
		testId | productsToAddToCart | promotionRules | couponCodes | gifts1 | baseAmount1 | gifts2 | baseAmount2 | forCurrency

		// Apply Free Gift on carts using one coupon code
		'Test_Coupon_Code_Free_Gift_Order_Threshold_On_Carts' | ['1382080': 1] | ['coupon_code_free_gift_order_threshold']| ['CHRISTMAS16']| ['443175': 1] | 574.88 | ['443175': 1] | 574.88 | 'USD'
		// Apply Free Gift on carts using one coupon code - Checking order threshold for coupon promotion
		// product price below threshold (< $200)
		'Test_Coupon_Code_Free_Gift_Order_Threshold_On_Carts_Threshold_Test' | ['493683': 1] | ['coupon_code_free_gift_order_threshold']| ['CHRISTMAS16']| ['443175': 0] | 118.45 | ['443175': 0] | 118.45 | 'USD'
		// product price above threshold (< $200)
		'Test_Coupon_Code_Free_Gift_Order_Threshold_On_Carts_Threshold_Test' | ['493683': 1, '1382080': 1] | ['coupon_code_free_gift_order_threshold']| ['CHRISTMAS16']| ['443175': 1] | 693.33 | ['443175': 1] | 693.33 | 'USD'
	}

	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) and try to redeem of #couponCodes twice should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		CartModel cart1 = createCart("cart1", forCurrency)
		productsToAddToCart.each { code, quantity ->
			addProductToCart(code, quantity, cart1)
		}
		double cart1TotalPriceWithoutPromo = cart1.totalPrice
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceWithRedeemedCoupons = cart1.totalPrice

		// try to redeem for the 2nd time
		couponCodes.each { couponCode ->
			redeemCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceWithRedeemedCoupons2 = cart1.totalPrice

		couponCodes.each { couponCode ->
			releaseCouponWithCodeForCart(couponCode, cart1.getCode())
		}
		evaluatePromotionForCart(cart1.getCode())
		double cart1TotalPriceAfterReleasedCoupons = cart1.totalPrice

		then:
		cart1TotalPriceWithoutPromo == baseAmount
		cart1TotalPriceWithRedeemedCoupons == promotedAmount
		cart1TotalPriceAfterReleasedCoupons == baseAmount
		cart1TotalPriceWithRedeemedCoupons2 == promotedAmount

		where:
		testId | productsToAddToCart | promotionRules | couponCodes | promotedAmount | baseAmount | forCurrency

		// Apply fixed discount on carts using the same coupon code twice on the same cart
		'Test_Coupon_Code_Fixed_Discount_On_Carts_Coupon_Applied_Twice' | ['1382080': 1] | ['coupon_code_fixed_discount']| ['SUMMER69']| 564.88  | 574.88 | 'USD'
	}
}
