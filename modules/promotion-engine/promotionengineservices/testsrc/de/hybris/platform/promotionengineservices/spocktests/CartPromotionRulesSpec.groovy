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
class CartPromotionRulesSpec extends AbstractPromotionEngineServicesSpockTest
{
	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it"()
	{
		given:
		// set the user here - blank == anonymous
		def user = ''
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		// set products and quantities here
		// ['sku':quantity], for example ['429':3] will add product 429 with quantity 3 to the cart

		// set to true to erase an existing cart for the user
		// WARNING: If the user is active, setting this to true may disrupt their session
		boolean clearCart = true

		prepareUser(user, forCurrency)

		if (clearCart)
		{
			CartModel newCart = getCartFactory().createCart()
			getCartService().setSessionCart(newCart)
		}
		productsToAddToCart.each { code, quantity ->
			println 'Adding ' + code + ' to the cart with quantity ' + quantity
			addProductToCart(code, quantity)
		}
		println(echoCart())

		CartModel cart = getCartService().getSessionCart()
		double cartTotalPriceWithoutPromo = cart.totalPrice
		evaluatePromotionForCart(cart.getCode())

		tearDownUser()

		then:
		cartTotalPriceWithoutPromo == baseAmount
		cart.totalPrice == promotedAmount


		where:
		testId | productsToAddToCart | promotionRules | promotedAmount | baseAmount | forCurrency


		// Order threshold got percentage discount to cart. Try to apply promotion to cart with total value less than threshold - discount will not be applied
		'Test_Apply_Promotion_To_Cart_With_Total_Less_Than_250' | ['1934793': 1] |['order_threshold_percentage_discount_cart']| 99.85  | 99.85 | 'USD'
		'Test_Apply_Promotion_To_Cart_With_Total_Less_Than_250' | ['1934793': 2] |['order_threshold_percentage_discount_cart']| 199.70 | 199.70 | 'USD'

		// Order threshold got percentage discount to cart. Try to apply promotion to cart with total value greater than threshold - discount will be applied
		// (# $29.96 is 10% off for cart price 3 * $99.85 ($299.55)
		'Test_Apply_Promotion_To_Cart_Total_Bigger_250_Three_Same_Products' | ['1934793': 3] |['order_threshold_percentage_discount_cart']| 269.59 | 299.55 | 'USD'
		'Test_Apply_Promotion_To_Cart_Total_Bigger_250_Three_Same_Products' | ['1934793': 4] |['order_threshold_percentage_discount_cart']| 359.46 | 399.40 | 'USD'

		// Apply 2 cart level promotions of non-exclusive rule groups at the same time. Get applied 10% off first as it priority is 40, then $20 off as it priority is 30
		'Test_Apply_Not_Exclusive_Promotions_To_Cart_' | ['1934793': 3] |['order_threshold_percentage_discount_cart', 'order_threshold_fixed_discount_cart']| 249.59 | 299.55 | 'USD'

		// Try to apply 2 cart level promotions of exclusive rule groups at the same time - only the 1st one triggers. Get applied 10% off only as it has the highest priority (40)
		'Test_Apply_Exclusive_Promotions_To_Cart_' | ['1934793': 3] |['order_threshold_percentage_discount_cart_exclusive', 'order_threshold_fixed_discount_cart_exclusive']| 269.59 | 299.55 | 'USD'

		// A percentage discount is applied to the cart when cart does not contain products from 576 category
		// 20% discount as product of NOT-category is not in cart
		'Test_Percentage_Discount_When_Product_From_Categories_NOT_in_cart' | ['1641905': 1] |['products_from_category_not_in_cart_percentage_discount']| 114.16 | 142.70 | 'USD'
		// no discount as NOT-category product is now in cart
		'Test_Percentage_Discount_When_Product_From_Categories_NOT_in_cart' | ['1641905': 1, '1934794': 1] |['products_from_category_not_in_cart_percentage_discount']| 242.55 | 242.55 | 'USD'

		// A percentage discount is applied to the cart when cart does not contain specific product (2934302)
		// 10% discount
		'Test_Percentage_Discount_When_Product_NOT_In_Cart' | ['1382080': 1] |['product_not_in_cart_percentage_discount']| 517.39 | 574.88 | 'USD'
		// no discount as product is now in cart
		'Test_Percentage_Discount_When_Product_NOT_In_Cart' | ['1382080': 1, '2934302': 2] |['product_not_in_cart_percentage_discount']| 1592.64 | 1592.64 | 'USD'

		// Test order threshold perfect partner promotion (discounted price is included when calculating total threshold). Threshold = $250, fixed price $100, '325414' is partner product
		// no discount
		'Test_Order_Threshold_Perfect_Partner_Included' | ['325414': 1, '493683': 1] |['order_threshold_perfect_partner_included']| 265.49 | 265.49 | 'USD'
		// Promotion should fire
		'Test_Order_Threshold_Perfect_Partner_Included' | ['325414': 1, '493683': 2] |['order_threshold_perfect_partner_included']| 336.90 | 383.94 | 'USD'
		// Promotion should fire for 1 partner product
		'Test_Order_Threshold_Perfect_Partner_Included' | ['325414': 2, '493683': 2] |['order_threshold_perfect_partner_included']| 483.94 | 530.98 | 'USD'
		// Partner products in cart, no other products. Promotion shouldn't fire
		'Test_Order_Threshold_Perfect_Partner_Included' | ['325414': 2] |['order_threshold_perfect_partner_included']| 294.08 | 294.08 | 'USD'
		// Partner products in cart, no other products. Promotion should fire for 1 unit
		'Test_Order_Threshold_Perfect_Partner_Included' | ['325414': 3] |['order_threshold_perfect_partner_included']| 394.08 | 441.12 | 'USD'

		// Test order threshold perfect partner promotion (discounted price is not included when calculating total threshold). Threshold = $250, fixed price $100
		// $118.45 + $147.04 = $265.49; Promotion should not fire yet as $118.45 (price of non-partner products in the cart) < $250 (threshold)
		'Test_Order_Threshold_Perfect_Partner_Not_Included' | ['325414': 1, '493683': 1] |['order_threshold_perfect_partner_not_included']| 265.49 | 265.49 | 'USD'
		// Promotion should not fire yet as $118.45 + $118.45 (price of non-partner products in the cart) < $250 (threshold)
		'Test_Order_Threshold_Perfect_Partner_Not_Included' | ['325414': 1, '493683': 2] |['order_threshold_perfect_partner_not_included']| 383.94 | 383.94 | 'USD'
		// Promotion should fire as $118.45 + $118.45 + $118.45 (price of non-partner products in the cart) > $250 (threshold)
		'Test_Order_Threshold_Perfect_Partner_Not_Included' | ['325414': 1, '493683': 3] |['order_threshold_perfect_partner_not_included']| 455.35 | 502.39 | 'USD'

		// Test order threshold perfect partner promotion (discounted price is included when calculating total threshold). Threshold = $250, fixed price $100 and the same product fixed discount $10 promotion with low priority
		// partner product in cart, fixed_discount promotion should fire
		'Test_Order_Threshold_Perfect_Partner_And_Product_Fixed_Discount' | ['325414': 1] |['order_threshold_perfect_partner_included', 'product_fixed_discount_325414']| 137.04 | 147.04 | 'USD'
		// partner product with quantity 2 in cart, fixed_discount promotion should fire
		'Test_Order_Threshold_Perfect_Partner_And_Product_Fixed_Discount' | ['325414': 2] |['order_threshold_perfect_partner_included', 'product_fixed_discount_325414']| 274.08 | 294.08 | 'USD'
		// partner product with quantity 2 and another product in cart, both promotions should fire
		'Test_Order_Threshold_Perfect_Partner_And_Product_Fixed_Discount' | ['325414': 2, '493683': 1] |['order_threshold_perfect_partner_included', 'product_fixed_discount_325414']| 355.49 | 412.53 | 'USD'

		// Apply special target price on selected product(s) or categories where cart contains products from different brand category(categoryCode: brand_10 and brand_5) but same category(category code : 576)
		// Buy any Canon digital compact camera (combination of categoryCode: 576 AND categoryCode: brand_10) for $60
		// '23210' - Sony compact, '1934793' - Canon compact, '1934794' - Canon compact
		'Test_Apply_Fixed_Price_For_Category_Promotion_TestCase_1' | ['23210': 1, '1934793': 1, '1934794': 1] |['product_fixed_price']| 230.00 | 309.70 | 'USD'
		'Test_Apply_Fixed_Price_For_Category_Promotion_TestCase_1' | ['23210': 1] |['product_fixed_price']| 110.00 | 110.00 | 'USD'

		// A percentage discount is applied on every item with qualifying category
		// 10% off on Canon digital compact camera (combination of categoryCode: 576 AND categoryCode: brand_10) but not another Canon or other brands
		// '1934793' - Canon compact, '1934794' - Canon compact, '1382080' - another Canon, '23210' - Sony compact
		'Test_Apply_Percentage_Discount_For_Category_Promotion' | ['1934793': 2, '1934794': 1, '1382080': 1, '23210': 1] |['product_category_percentage_discount_product']| 954.49 | 984.43 | 'USD'
		'Test_Apply_Percentage_Discount_For_Category_Promotion' | ['1934794': 1, '1382080': 1, '23210': 1] |['product_category_percentage_discount_product']| 774.75 | 784.73 | 'USD'

		// A percentage discount is applied on every item with qualifying category (JPY)
		// 10% off on Canon digital compact camera (combination of categoryCode: 576 AND categoryCode: brand_10) but not another Canon or other brands
		// '1934793' - Canon compact, '1934794' - Canon compact, '1382080' - another Canon, '23210' - Sony compact
		'Test_Apply_Percentage_Discount_For_Category_Promotion_JPY' | ['1934793': 2, '1934794': 1, '1382080': 1, '23210': 1] |['product_category_percentage_discount_product']| 81284 | 83834 | 'JPY'
		'Test_Apply_Percentage_Discount_For_Category_Promotion_JPY' | ['1934794': 1, '1382080': 1, '23210': 1] |['product_category_percentage_discount_product']| 65984 | 66834 | 'JPY'

		// Cart contain only excluded product. No promotions should be applied.
		'Test_No_Promotion_Applied_Only_Excluded_Product' | ['1934793': 3] |['order_threshold_percentage_discount_excluded_product']| 299.55 | 299.55 | 'USD'
		'Test_No_Promotion_Applied_Only_Excluded_Product' | ['1934793': 5] |['order_threshold_percentage_discount_excluded_product']| 499.25 | 499.25 | 'USD'

		// Apply threshold promotion only without excluded products. Cart contains only not excluded products.
		'Test_Apply_Percentage_Threshold_Promotion_Without_Excluded_Products' | ['1382080': 1] |['order_threshold_percentage_discount_excluded_product']| 517.4 | 574.88 | 'USD'
		'Test_Apply_Percentage_Threshold_Promotion_Without_Excluded_Products' | ['1382080': 1, '1432722': 2] |['order_threshold_percentage_discount_excluded_product']| 1231.8 | 1368.64 | 'USD'

		// Buy a certain product from within a defined set for a fixed price when the threshold order value is exceeded. The target price should apply to as many as possible partner products in the cart.
		// '482105', '805693', '824267', '824259' - partner products, '784343' - not a partner product
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['482105': 2] |['order_threshold_perfect_partner']| 299.38 | 299.38 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['482105': 3] |['order_threshold_perfect_partner']| 349.38 | 449.07 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['805693': 2] |['order_threshold_perfect_partner']| 273.00 | 273.00 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['805693': 3] |['order_threshold_perfect_partner']| 323.00 | 409.50 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['824267': 1] |['order_threshold_perfect_partner']| 283.85 | 283.85 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['824267': 2] |['order_threshold_perfect_partner']| 333.85 | 567.70 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['824259': 2] |['order_threshold_perfect_partner']| 369.16 | 369.16 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['824259': 3] |['order_threshold_perfect_partner']| 419.16 | 553.74 | 'USD'
		// no promotion for having only not-partner product
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['784343': 1] |['order_threshold_perfect_partner']| 818.33 | 818.33 | 'USD'
		// not qualifying + qualifying with multiple quantities
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['784343': 1, '805693': 1] |['order_threshold_perfect_partner']| 868.33 | 954.83 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['784343': 1, '805693': 1, '482105': 1] |['order_threshold_perfect_partner']| 1018.02 | 1104.52 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['784343': 1, '805693': 2, '482105': 1] |['order_threshold_perfect_partner']| 1154.52 | 1241.02 | 'USD'
		'Test_Order_Threshold_Perfect_Partner_Fixed_Price' | ['805693': 2, '482105': 1] |['order_threshold_perfect_partner']| 336.19 | 422.69 | 'USD'

		// A fixed price discount is applied on products whose base price exceeds the specified threshold value. If product price is over $1000 give fixed discount on products $100 off
		// Test only "cheap" product in cart (< $1000)
		'Test_Product_Price_Threshold_Fixed_Discount' | ['2934302': 1] |['product_price_threshold_fixed_discount']| 508.88 | 508.88 | 'USD'
		// Add another "cheap" product in cart so that in total > 1000$
		'Test_Product_Price_Threshold_Fixed_Discount' | ['2934302': 2] |['product_price_threshold_fixed_discount']| 1017.76 | 1017.76 | 'USD'
		// Test adding "expensive" product in cart
		'Test_Product_Price_Threshold_Fixed_Discount' | ['2934302': 2, '816780': 1] |['product_price_threshold_fixed_discount']| 2003.76 | 2103.76 | 'USD'
		// Test adding one more "expensive" product in cart
		'Test_Product_Price_Threshold_Fixed_Discount' | ['2934302': 2, '816780': 2] |['product_price_threshold_fixed_discount']| 2989.76 | 3189.76 | 'USD'

		// 5% discount on all Camera accessories and supplies - Minimum order value $150
		// check 5% discount is applied on products from 585 category ('301233', '137220') and not on '325414' (which is not of 585 category)
		'Test_Order_Threshold_Percentage_Discount_Products' | ['301233': 1, '137220': 1, '325414': 1] |['order_threshold_percentage_discount_products']| 175.13 | 176.6 | 'USD'
		// order total is below $150 and check 5% discount is not applied on products of 585 category
		'Test_Order_Threshold_Percentage_Discount_Products' | ['301233': 1, '137220': 1] |['order_threshold_percentage_discount_products']| 29.56 | 29.56 | 'USD'

		// Buy the Cannon EOS 500D with any 2 memory cards and any Cannon camera flash for a bundle price of $1000
		'Test_Product_Bundle_Fixed_Price' | ['4812254': 1, '479956': 2, '1099413': 1] |['product_bundle_fixed_price']| 1000.00 | 1493.49 | 'USD'
		// check promotion does not apply if flashMemory is absent
		'Test_Product_Bundle_Fixed_Price' | ['4812254': 1, '1099413': 1] |['product_bundle_fixed_price']| 1288.75 | 1288.75 | 'USD'

		// Buy any 3 Color Films get 2 Black & White Films free
		'Test_Product_Buy_X_Get_Y_Free' | ['779848': 3, '779866': 2] |['product_buy_x_get_y_free']| 25.50 | 40.50 | 'USD'
		'Test_Product_Buy_X_Get_Y_Free' | ['779848': 2, '779866': 2] |['product_buy_x_get_y_free']| 32.00 | 32.00 | 'USD'

		// Buy any 6 Color Films get 4 Black & White Films free
		'Test_Product_Buy_X_Get_Y_Free_Twice' | ['779848': 6, '779866': 4] |['product_buy_x_get_y_free']| 51.00 | 81.00 | 'USD'
		'Test_Product_Buy_X_Get_Y_Free_Twice' | ['779848': 5, '779866': 4] |['product_buy_x_get_y_free']| 57.50 | 72.5 | 'USD'

		// $15 off on all Hand held Camcorders - excluding Camileo S10 EU ('1776948') and Camileo H20 EU ('1776947')
		'Test_Product_Fixed_Discount' | ['1432722': 1, '1776948': 1, '1776947': 1] |['product_category_fixed_discount_excluded_products']| 772.64 | 787.64 | 'USD'
		'Test_Product_Fixed_Discount' | ['1432722': 1, '1776948': 1, '1776947': 1, '1934406': 1] |['product_category_fixed_discount_excluded_products']| 1210.52 | 1240.52 | 'USD'
		'Test_Product_Fixed_Discount' | ['1776948': 1, '1776947': 1] |['product_category_fixed_discount_excluded_products']| 390.76 | 390.76 | 'USD'

		// Buy any 3 film rolls for a combined price of $10
		'Test_Product_Multibuy_Fixed_Price' | ['779848': 2, '779866': 1] |['product_multibuy_fixed_price']| 10.00 | 24.50 | 'USD'
		'Test_Product_Multibuy_Fixed_Price' | ['779848': 4, '779866': 1] |['product_multibuy_fixed_price']| 27.00 | 41.50 | 'USD'
		'Test_Product_Multibuy_Fixed_Price' | ['779848': 4, '779866': 2] |['product_multibuy_fixed_price']| 20.00 | 49.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price' | ['779848': 2] |['product_multibuy_fixed_price']| 17.00 | 17.00 | 'USD'
		// Buy any 3 film rolls for a combined price of $10 - twice (6 film rolls for $20)
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 1, '779842': 1] |['product_multibuy_fixed_price']| 16.00 | 16.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 4, '779842': 2] |['product_multibuy_fixed_price']| 20.00 | 49.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 2, '779842': 4] |['product_multibuy_fixed_price']| 20.00 | 47.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 1, '779842': 5] |['product_multibuy_fixed_price']| 20.00 | 46.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 5, '779842': 1] |['product_multibuy_fixed_price']| 20.00 | 50.00 | 'USD'
		'Test_Product_Multibuy_Fixed_Price_Twice' | ['779848': 3, '779842': 3] |['product_multibuy_fixed_price']| 20.00 | 48.00 | 'USD'

		// Buy the camera DSLR-A100H and get InfoLITHIUM Battery NP-FM55H for $75
		'Test_Product_Perfect_Partner_Fixed_Price' | ['816780': 1, '482105': 1] |['product_perfect_partner_fixed_price']| 1161.00 | 1235.69 | 'USD'
		'Test_Product_Perfect_Partner_Fixed_Price' | ['816780': 2, '482105': 1] |['product_perfect_partner_fixed_price']| 2247.00 | 2321.69 | 'USD'
		'Test_Product_Perfect_Partner_Fixed_Price' | ['816780': 2, '482105': 2] |['product_perfect_partner_fixed_price']| 2322.00 | 2471.38 | 'USD'
		'Test_Product_Perfect_Partner_Fixed_Price' | ['482105': 1] |['product_perfect_partner_fixed_price']| 149.69 | 149.69 | 'USD'

		// Buy any Samsung PL60 camera and get a memory card for 25% less
		'Test_Product_Perfect_Partner_Percentage_Discount' | ['1981413': 1, '1641905': 1] |['product_perfect_partner_percentage_discount']| 291.72 | 327.39 | 'USD'
		'Test_Product_Perfect_Partner_Percentage_Discount' | ['1641905': 1] |['product_perfect_partner_percentage_discount']| 142.70 | 142.70 | 'USD'

		// Buy 2 any Samsung PL60 cameras and get 2 memory cards for 25% less for every one
		'Test_2_Product_Perfect_Partner_Percentage_Discounts' | ['1981412': 2, '2938458': 1, '479956': 1] |['product_perfect_partner_percentage_discount']| 416.72 | 445.93 | 'USD'
		// the cheapest memory card goes for discount rate
		'Test_2_Product_Perfect_Partner_Percentage_Discounts' | ['1981412': 1, '2938458': 1, '479956': 1] |['product_perfect_partner_percentage_discount']| 277.78 | 281.40 | 'USD'

		// Buy 1 Color Films get 1 Black & White Film for free & Buy camcodertape get 1 Black & White Film for free
		'Test_Product_Buy_CF_or_CT_Get_BWF_Free_Stackable' | ['779868': 1, '1291399': 1] |['product_buy_CF_get_BWF_free', 'product_buy_CT_get_BWF_free']| 7.50 | 20.00 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_Free_Stackable' | ['779868': 1, '1291399': 2] |['product_buy_CF_get_BWF_free', 'product_buy_CT_get_BWF_free']| 20.00 | 32.50 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_Free_Stackable' | ['779868': 1, '1291399': 2, '137220': 1] |['product_buy_CF_get_BWF_free', 'product_buy_CT_get_BWF_free']| 15.50 | 40.50 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_Free_Stackable' | ['779868': 1, '1291399': 1, '137220': 1] |['product_buy_CF_get_BWF_free', 'product_buy_CT_get_BWF_free']| 15.50 | 28.00 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_Free_Stackable' | ['779868': 1, '1291399': 3, '137220': 1] |['product_buy_CF_get_BWF_free', 'product_buy_CT_get_BWF_free']| 28.00 | 53.00 | 'USD'

		// Buy 1 Color Films get 1 Black & White Film for 50% & Buy camcodertape get 1 Black & White Film for 50%
		// check b&wfilm is 50% discounted after promotion
		'Test_Product_Buy_CF_or_CT_Get_BWF_50_percent_Stackable' | ['779868': 1, '1291399': 1] |['product_buy_CF_get_BWF_50_percent', 'product_buy_CT_get_BWF_50_percent']| 13.75 | 20.00 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_50_percent_Stackable' | ['779868': 1, '1291399': 2] |['product_buy_CF_get_BWF_50_percent', 'product_buy_CT_get_BWF_50_percent']| 26.25 | 32.50 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_50_percent_Stackable' | ['779868': 1, '1291399': 2, '137220': 1] |['product_buy_CF_get_BWF_50_percent', 'product_buy_CT_get_BWF_50_percent']| 28.00 | 40.50 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_50_percent_Stackable' | ['779868': 1, '1291399': 1, '137220': 1] |['product_buy_CF_get_BWF_50_percent', 'product_buy_CT_get_BWF_50_percent']| 21.75 | 28.00 | 'USD'
		'Test_Product_Buy_CF_or_CT_Get_BWF_50_percent_Stackable' | ['779868': 1, '1291399': 3, '137220': 1] |['product_buy_CF_get_BWF_50_percent', 'product_buy_CT_get_BWF_50_percent']| 40.50 | 53.00 | 'USD'

		// Buy 1 Color Films get 1 Black & White Film for 50%
		'Test_Product_Buy_CF_Get_BWF_50_percent' | ['779868': 1, '1291399': 1] |['product_buy_CF_get_BWF_50_percent']| 13.75 | 20.00 | 'USD'
		'Test_Product_Buy_CF_Get_BWF_50_percent' | ['779868': 1, '1291399': 2] |['product_buy_CF_get_BWF_50_percent']| 26.25 | 32.50 | 'USD'
		'Test_Product_Buy_CF_Get_BWF_50_percent' | ['779868': 2, '1291399': 2] |['product_buy_CF_get_BWF_50_percent']| 27.50 | 40.00 | 'USD'
		'Test_Product_Buy_CF_Get_BWF_50_percent' | ['779868': 2, '1291399': 5] |['product_buy_CF_get_BWF_50_percent']| 65.00 | 77.50 | 'USD'
		'Test_Product_Buy_CF_Get_BWF_50_percent' | ['779868': 5, '1291399': 5] |['product_buy_CF_get_BWF_50_percent']| 68.75 | 100.00 | 'USD'

		// Buy 3 Color Films get 1 Black & White Film for free. Add one more Black & White Film and get it for 50%
		// check cart is 50% discounted after promotion
		'Test_Product_Buy_X_CF_Get_Y_BWF_Plus_BWF_50_percent' | ['779868': 1, '1291399': 1] |['product_buy_3_CF_get_BWF_free', 'product_50_percent_discount']| 10.00 | 20.00 | 'USD'
		// colorFilm should not be discounted, but the B&W film goes for free
		'Test_Product_Buy_X_CF_Get_Y_BWF_Plus_BWF_50_percent' | ['779868': 3, '1291399': 1] |['product_buy_3_CF_get_BWF_free', 'product_50_percent_discount']| 22.50 | 35.00 | 'USD'
		// colorFilm should not be discounted, but the extra B&W film item is discounted by 50%
		'Test_Product_Buy_X_CF_Get_Y_BWF_Plus_BWF_50_percent' | ['779868': 3, '1291399': 2] |['product_buy_3_CF_get_BWF_free', 'product_50_percent_discount']| 28.75 | 47.50 | 'USD'
		// extra colorFilm item should be discounted by 50% as well
		'Test_Product_Buy_X_CF_Get_Y_BWF_Plus_BWF_50_percent' | ['779868': 4, '1291399': 2] |['product_buy_3_CF_get_BWF_free', 'product_50_percent_discount']| 32.50 | 55.00 | 'USD'
		// extraColorFilm item should be discounted by 50%
		'Test_Product_Buy_X_CF_Get_Y_BWF_Plus_BWF_50_percent' | ['779868': 4, '1291399': 2, '779848': 1] |['product_buy_3_CF_get_BWF_free', 'product_50_percent_discount']| 36.75 | 63.50 | 'USD'

		// Buy any 5 Memory Cards get 2 Memory Cards for free (10% discount prior to 5 cards in cart)
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 1] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 9.00 | 10.00 | 'USD'
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 5] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 45.00 | 50.00 | 'USD'
		// percentage discount should disappear, but 2 items for free promo is applied
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 7] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 50.00 | 70.00 | 'USD'
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 13] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 104.00 | 130.00 | 'USD'
		// 2 times the buy_x_get_y_free should be applied
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 14] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 100.00 | 140.00 | 'USD'
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 1, '872912': 6] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 50.00 | 70.00 | 'USD'
		'Test_Product_Buy_X_MC_Get_Y_MC_Free' | ['1366053': 1, '872912': 4, '1322041': 2] |['product_buy_x_902_get_y_902_free', 'product_902_percentage_discount']| 50.00 | 69.98 | 'USD'

		// Buy any variant of a base product with discount 10%
		'Test_Product_Variants_Percentage_Discount' | ['1978440_red': 1] |['base_product_percentage_discount']| 502.56 | 558.40 | 'USD'
		'Test_Product_Variants_Percentage_Discount' | ['1978440_red': 1, '1978440_green': 1] |['base_product_percentage_discount']| 1005.12 | 1116.80 | 'USD'

		// Get 10% off on cart if there is no variant of a base product
		'Test_Cart_Dicount_Without_Variants' | ['1981412': 1] |['cart_dicount_no_base_product']| 148.08 | 164.53 | 'USD'
		// $164.53 + $558.40 = $722,93
		'Test_Cart_Dicount_Without_Variants' | ['1981412': 1, '1978440_red': 1] |['cart_dicount_no_base_product']| 722.93 | 722.93 | 'USD'

		// Get 10% off on cart if there is no a particular variant of a base product
		'Test_Cart_Dicount_Without_Particular_Variant' | ['1981412': 1, '1978440_red': 1] |['cart_dicount_no_variant_product']| 650.64 | 722.93 | 'USD'
		// add a particular variant and lose the cart discount: $722.93 + $558.40 = $1281.33
		'Test_Cart_Dicount_Without_Particular_Variant' | ['1981412': 1, '1978440_red': 1, '1978440_green': 1] |['cart_dicount_no_variant_product']| 1281.33 | 1281.33 | 'USD'

		// Get 10% off on a product of qualifying category (Digital Compact) excluding a particular variant product
		'Test_Category_Dicount_Excluding_Particular_Variant' | ['1981412': 1, '1978440_green': 1] |['category_dicount_no_variant_product']| 650.64 | 722.93 | 'USD'
		// add a particular variant and w/o discount (1978440_red)
		// $650.64 + $558.40 = $1209.04
		'Test_Category_Dicount_Excluding_Particular_Variant' | ['1981412': 1, '1978440_green': 1, '1978440_red': 1] |['category_dicount_no_variant_product']| 1209.04 | 1281.33 | 'USD'

		// Get 10% off on a product of qualifying category (Digital Compact) excluding base product
		'Test_Category_Dicount_Excluding_Base_Product' | ['1981412': 1, '1978440_green': 1] |['category_dicount_no_base_product']| 706.48 | 722.93 | 'USD'
		'Test_Category_Dicount_Excluding_Base_Product' | ['1981412': 1, '1978440_green': 1, '1978440_red': 1] |['category_dicount_no_base_product']| 1264.88 | 1281.33 | 'USD'

		// Give 10% off for 2 products if all them are in cart
		'Test_All_Of_2_Products_Percentage_Discount' | ['872912': 1] |['all_of_2_products_percentage_discount']| 10.00 | 10.00 | 'USD'
		'Test_All_Of_2_Products_Percentage_Discount' | ['1382080': 1] |['all_of_2_products_percentage_discount']| 574.88 | 574.88 | 'USD'
		'Test_All_Of_2_Products_Percentage_Discount' | ['872912': 1, '1382080': 1] |['all_of_2_products_percentage_discount']| 526.4 | 584.88 | 'USD'
		'Test_All_Of_2_Products_Percentage_Discount' | ['872912': 1, '1382080': 1, '1934793': 1] |['all_of_2_products_percentage_discount']| 626.25 | 684.73 | 'USD'
		'Test_All_Of_2_Products_Percentage_Discount' | ['872912': 2, '1382080': 1, '1934793': 1] |['all_of_2_products_percentage_discount']| 635.25 | 694.73 | 'USD'
		'Test_All_Of_2_Products_Percentage_Discount' | ['872912': 2, '1382080': 2, '1934793': 1] |['all_of_2_products_percentage_discount']| 1152.65 | 1269.61 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7564
		// https://jira.hybris.com/browse/QA-7564
		// Add to the cart product from Color Film category - Promotion shouldn't trigger
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7564' | ['779848': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 8.50 | 8.50 | 'USD'
		// Add to the cart product from Black&White Film category - Promotion shouldn't be triggered
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7564' | ['779848': 1, '779866': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 16.00 | 16.00 | 'USD'
		// Increase quantity for product from Color Film category from 1 to 3 - Promotion shouldn't be triggered
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7564' | ['779848': 3, '779866': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 33.00 | 33.00 | 'USD'
		// Increase quantity for product from B&W Films category from 1 to 2 - Promotion should be triggered, two products B&W should be as free gift
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7564' | ['779848': 3, '779866': 2] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 25.50 | 40.50 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7567
		// https://jira.hybris.com/browse/QA-7567
		// Increase quantity for product from B&W Films category from 1 to 30 - Promotion should be triggered, 10% discount should be applied
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7567' | ['779848': 30, '779866': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 236.25 | 262.50 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7568
		// https://jira.hybris.com/browse/QA-7568
		// Increase quantity for product from B&W Films category from 1 to 30 - Promotion should be triggered, 10% discount should be applied
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7568' | ['779848': 30, '779866': 2] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 229.50 | 270.00 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7569
		// https://jira.hybris.com/browse/QA-7569
		// Add to the cart product from Color Film category - Promotion shouldn't trigger
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7569' | ['779848': 1] |['order_threshold_percentage_discount_cart_exclusive', 'product_buy_x_get_y_free_exclusive']| 8.50 | 8.50 | 'USD'
		// Add to the cart product from Black&White Film category - Promotion shouldn't be triggered
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7569' | ['779848': 1, '779866': 1] |['order_threshold_percentage_discount_cart_exclusive', 'product_buy_x_get_y_free_exclusive']| 16.00 | 16.00 | 'USD'
		// Increase quantity for product from Color Film category from 1 to 30 - Promotion should be triggered, 10% discount should be applied
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7569' | ['779848': 30, '779866': 1] |['order_threshold_percentage_discount_cart_exclusive', 'product_buy_x_get_y_free_exclusive']| 236.25 | 262.50 | 'USD'
		// Increase quantity for product from B&W Films category from 1 to 20
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7569' | ['779848': 30, '779866': 20] |['order_threshold_percentage_discount_cart_exclusive', 'product_buy_x_get_y_free_exclusive']| 255.00 | 405.00 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7570
		// https://jira.hybris.com/browse/QA-7570
		// Add to the cart two products from Color Film category - Promotion shouldn't trigger
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7570' | ['779848': 1, '834955': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 16.00 | 16.00 | 'USD'
		// Add to the cart two products from Black&White Film category - Promotion shouldn't be triggered
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7570' | ['779848': 1, '834955': 1, '779866': 1, '1291399': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 36.00 | 36.00 | 'USD'
		// Increase quantity for product productCF_Farbelt from Color Film category from 1 to 2 - Promotion product_buy_x_get_y_free should be triggered - All products from B&W Films category should be for free
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7570' | ['779848': 2, '834955': 1, '779866': 1, '1291399': 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 24.50 | 44.50 | 'USD'
		// Add to the cart new product from B&W Films category - Promotion product_buy_x_get_y_free should be triggered - Two products from B&W Films category should be for free - with lowest price
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7570' | ['779848': 2, '834955': 1, '779866': 1, '1291399': 1, '779864' : 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 37.00 | 52.45 | 'USD'

		// Check two nonstackable promotions - covered test case QA-7571
		// https://jira.hybris.com/browse/QA-7571
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7571' | ['779848': 20, '834955': 10, '779866': 1, '1291399': 1, '779864' : 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 231.75 | 272.95 | 'USD'
		'Test_Apply_Nonstackable_Promotions_Product_Order_TC_QA_7571' | ['779848': 20, '834955': 10, '779866': 1, '1291399': 2, '779864' : 1] |['order_threshold_percentage_discount_cart', 'product_buy_x_get_y_free']| 245.00 | 285.45 | 'USD'

	}

}
