/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.promotionengineservices.spocktests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.order.CartModel

import org.junit.Test

import spock.lang.Unroll


@IntegrationTest
class PromotionRulesSpec extends AbstractPromotionEngineServicesSpockTest
{
	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) with #promotionRules promotions should give free gifts #gifts and amount to #promotedAmount with the promotions and #baseAmount without it"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		CartModel cart = createCart("cart", forCurrency)
		productsToAddToCart.each
		{ code, quantity ->
			addProductToCart(code, quantity, cart)
		}
		double cartTotalPriceWithoutPromo = cart.totalPrice
		evaluatePromotionForCart(cart.getCode())
		double cartTotalAfterPromo = cart.totalPrice

		boolean giftsPresentAsExpected = gifts.every
		{ code, quantity ->
			checkProductQuantity(code, cart, quantity)
		}
		then:
		cartTotalPriceWithoutPromo == baseAmount
		cartTotalAfterPromo == promotedAmount
		giftsPresentAsExpected == true

		where:
		testId | productsToAddToCart | promotionRules | gifts | promotedAmount | baseAmount | forCurrency
		// Apply a free gift to the cart for a complex bundle
		// Buy any digital compact camera (categoryCode: 576) OR any DSLR camera from the list (product codes: 2934302, 2934303, 1438465)
		// AND buy any camera lense (categoryCode: 588)
		'Test_Apply_Free_Gift_For_Complex_Bundle_USD' | ['2934302': 1] | ['product_bundle_free_gift']| ['1641905': 0] | 508.88 | 508.88 | 'USD'
		'Test_Apply_Free_Gift_For_Complex_Bundle_USD' | ['2934302': 1, '493683': 1] | ['product_bundle_free_gift']| ['1641905': 1] | 627.33 | 627.33 | 'USD'
		'Test_Apply_Free_Gift_For_Complex_Bundle_USD' | ['493683': 1] | ['product_bundle_free_gift']| ['1641905': 0] | 118.45 | 118.45 | 'USD'
		'Test_Apply_Free_Gift_For_Complex_Bundle_USD' | ['493683': 1, '325414': 1] | ['product_bundle_free_gift']| ['1641905': 1] | 265.49 | 265.49 | 'USD'
		'Test_Apply_Free_Gift_For_Complex_Bundle_USD' | ['493683': 2, '325414': 2] | ['product_bundle_free_gift']| ['1641905': 1] | 530.98 | 530.98 | 'USD'

		// Apply a free gift to the cart for a complex bundle
		// Buy any digital compact camera (categoryCode: 576) OR any DSLR camera from the list (product codes: 2934302, 2934303, 1438465)
		// AND buy any camera lense (categoryCode: 588)
		'Test_Apply_Free_Gift_For_Complex_Bundle_JPY' | ['2934302': 1] | ['product_bundle_free_gift']| ['1641905': 0] | 43350 | 43350 | 'JPY'
		'Test_Apply_Free_Gift_For_Complex_Bundle_JPY' | ['2934302': 1, '493683': 1] | ['product_bundle_free_gift']| ['1641905': 1] | 53440 | 53440 | 'JPY'
		'Test_Apply_Free_Gift_For_Complex_Bundle_JPY' | ['493683': 1] | ['product_bundle_free_gift']| ['1641905': 0] | 10090 | 10090 | 'JPY'
		'Test_Apply_Free_Gift_For_Complex_Bundle_JPY' | ['493683': 1, '325414': 1] | ['product_bundle_free_gift']| ['1641905': 1] | 22610 | 22610 | 'JPY'
		'Test_Apply_Free_Gift_For_Complex_Bundle_JPY' | ['493683': 2, '325414': 2] | ['product_bundle_free_gift']| ['1641905': 1] | 45220 | 45220 | 'JPY'

		// Apply special target price on selected product
		// Buy Canon PowershotA480 Digital compact camera for $60
		'Test_Apply_Fixed_Price_For_Product_Promotion' | ['1934793': 2, '1934794': 1, '23210': 1] | ['fixedPriceForPowershotA480-src']| []| 329.85 | 409.55 | 'USD'
		'Test_Apply_Fixed_Price_For_Product_Promotion' | ['1934794': 1, '23210': 1] | ['fixedPriceForPowershotA480-src']| []| 209.85 | 209.85 | 'USD'

		// A percentage discount is applied on every qualifying item from a list of products
		// 10% off on EOS 450D (productCode: 1382080) but not another Canon products or other brands
		'Test_Apply_Percentage_Discount_For_Product_Promotion' | ['1382080': 2, '1934794': 1, '23210': 1] | ['percentageDiscountForEOS450D-src']| []| 1244.65 | 1359.61 | 'USD'
		'Test_Apply_Percentage_Discount_For_Product_Promotion' | ['1934794': 1, '23210': 1] | ['percentageDiscountForEOS450D-src']| []| 209.85 | 209.85 | 'USD'

		// Order total over 500 USD
		'Test_Order_Threshold_Free_Gift_Promo_USD' | ['1934406': 1] | ['order_threshold_free_gift']| ['1687508': 0] | 452.88 | 452.88 | 'USD'
		'Test_Order_Threshold_Free_Gift_Promo_USD' | ['1934406': 2] | ['order_threshold_free_gift']| ['1687508': 1] | 905.76 | 905.76 | 'USD'

		// Get total 10% off all orders over $250
		'Test_Order_Threshold_Percentage_Discount_Cart' | ['1934793': 3] | ['order_threshold_percentage_discount_cart']| []| 269.59 | 299.55 | 'USD'

		// Order total over 500 USD
		'Test_Order_Threshold_Free_Gift_Promos_Not_Exclusive_Rule_Groups' | ['1934406': 1] | ['order_threshold_free_gift', 'order_threshold_free_gift2']| ['1687508': 0, '2938457': 1] | 452.88 | 452.88 | 'USD'
		'Test_Order_Threshold_Free_Gift_Promos_Not_Exclusive_Rule_Groups' | ['1934406': 2] | ['order_threshold_free_gift', 'order_threshold_free_gift2']| ['1687508': 1, '2938457': 1] | 905.76 | 905.76 | 'USD'

		// Order total over 500 USD
		'Test_Order_Threshold_Free_Gift_Promos_Exclusive_Rule_Groups' | ['1934406': 1] | ['order_threshold_free_gift11', 'order_threshold_free_gift12']| ['1687508': 0, '2938457': 1] | 452.88 | 452.88 | 'USD'
		'Test_Order_Threshold_Free_Gift_Promos_Exclusive_Rule_Groups' | ['1934406': 2] | ['order_threshold_free_gift11', 'order_threshold_free_gift12']| ['1687508': 1, '2938457': 0] | 905.76 | 905.76 | 'USD'

		// Get rules evaluation not stopped w/o halt action in a rule
		// only threshold of order_threshold_fixed_discount_cart ($200) is reached and the only rule triggers
		'Test_No_Halt' | ['493683': 2] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart']| []| 216.90 | 236.90 | 'USD'
		// threshold of order_threshold_percentage_discount_cart ($250) is reached and the rule triggers first as it has the highest priority of the 2 rules.
		// then the order_threshold_percentage_discount_cart triggers as no halt in the 1st rule
		'Test_No_Halt' | ['493683': 3] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart']| []| 299.81 | 355.35 | 'USD'

		// Get following rules evaluation stop after halt action in a rule
		// only threshold of order_threshold_fixed_discount_cart ($200) is reached and the only rule triggers
		'Test_Halt' | ['493683': 2] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart_then_halt']| []| 216.90 | 236.90 | 'USD'
		// threshold of order_threshold_percentage_discount_cart_then_halt ($250) is reached and the rule triggers first as it has the highest priority of the 2 rules
		// following evaluations stopped as the rule calls drools.halt(). That's why the 2nd promotion doesn't trigger
		'Test_Halt' | ['493683': 3] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart_then_halt']| []| 319.81 | 355.35 | 'USD'

		// Get following rules evaluation stop after halt action in a rule (the HALT action is on the first place of 2 rule actions)
		// only threshold of order_threshold_fixed_discount_cart ($200) is reached and the only rule triggers
		'Test_Halt_First' | ['493683': 2] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart_then_halt_first']| []| 216.90 | 236.90 | 'USD'
		// threshold of order_threshold_percentage_discount_cart_then_halt ($250) is reached and the rule triggers first as it has the highest priority of the 2 rules
		// following evaluations stopped as the rule calls drools.halt(). That's why the 2nd promotion doesn't trigger
		'Test_Halt_First' | ['493683': 3] | ['order_threshold_fixed_discount_cart', 'order_threshold_percentage_discount_cart_then_halt_first']| []| 319.81 | 355.35 | 'USD'

	}


	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) with #promotionRules promotions should amount to #promotedAmount with the promotions and #baseAmount without it when consumption mode is #consumption"()
	{
		given:
			def originalConsumptionValue = switchConsumption(consumption)
			compileSourceRules(promotionRules, "promotions-module-junit")

		when:
			CartModel cart = createCart("cart", forCurrency)
			productsToAddToCart.each
							{ code, quantity ->
								addProductToCart(code, quantity, cart)
							}
			double cartTotalPriceWithoutPromo = cart.totalPrice
			evaluatePromotionForCart(cart.getCode())
			double cartTotalAfterPromo = cart.totalPrice

			switchConsumption(originalConsumptionValue)
		then:
			cartTotalPriceWithoutPromo == baseAmount
			cartTotalAfterPromo == promotedAmount

		where:
			testId                      | productsToAddToCart                      | promotionRules                                                                                      | promotedAmount | baseAmount | forCurrency | consumption
			'Test_Consumption_Disabled' | ['4812254': 1, '1981412': 1]             | ['consumption_disabled_bundle_fixed_price', 'consumption_disabled_partner_products_fixed_discount'] | 1000           | 1092.42    | 'USD'       | false
			'Test_Consumption_Disabled' | ['4812254': 1, '23355': 1]               | ['consumption_disabled_bundle_fixed_price', 'consumption_disabled_partner_products_fixed_discount'] | 1308.27        | 1508.27    | 'USD'       | false
			'Test_Consumption_Disabled' | ['4812254': 1, '1981412': 1, '23355': 1] | ['consumption_disabled_bundle_fixed_price', 'consumption_disabled_partner_products_fixed_discount'] | 1380.38        | 1672.80    | 'USD'       | false
			'Test_Consumption_Disabled' | ['4812254': 3, '1981412': 3, '23355': 3] | ['consumption_disabled_bundle_fixed_price', 'consumption_disabled_partner_products_fixed_discount'] | 4141.14        | 5018.40    | 'USD'       | false
	}
}
