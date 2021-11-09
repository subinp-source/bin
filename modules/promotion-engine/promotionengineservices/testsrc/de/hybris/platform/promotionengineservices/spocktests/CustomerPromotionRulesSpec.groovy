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
package de.hybris.platform.promotionengineservices.spocktests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.order.CartModel

import org.junit.Test

import spock.lang.Unroll


@IntegrationTest
class CustomerPromotionRulesSpec extends AbstractPromotionEngineServicesSpockTest
{
	@Test
	@Unroll
	def "#testId : Adding #productsToAddToCart to Cart(#forCurrency) for #customerId of #customerGroup with #promotionRules promotions should amount to #promotedAmount with the promotions and #baseAmount without it"()
	{
		given:
		compileSourceRules(promotionRules, "promotions-module-junit")

		when:
		createUser(customerId, customerGroup)
		CartModel cart = createCart("cart", forCurrency)
		setCartToUser(customerId, cart.getCode())
		productsToAddToCart.each
		{ code, quantity ->
			addProductToCart(code, quantity, cart)
		}
		double cartTotalPriceWithoutPromo = cart.totalPrice
		evaluatePromotionForCart(cart.getCode())
		double cartTotalAfterPromo = cart.totalPrice

		then:
		cartTotalPriceWithoutPromo == baseAmount
		cartTotalAfterPromo == promotedAmount

		where:
		testId | productsToAddToCart | promotionRules | customerId | customerGroup | promotedAmount | baseAmount | forCurrency
		// A percentage discount is applied to the cart for the specified customer group.
		// Employees (groupName: employeeCustomerGroup) get 10% off on cart total
		'Test_Apply_Customer_Specific_Percentage_Discount' | ['1934793': 1, '1382080': 1] | ['target_user_group_percentage_discount_cart']| 'employee' | 'employeePromotionGroup' | 607.26 | 674.73 | 'USD'

		// A percentage discount is applied to the cart for the specified customer group ONLY
		// customers that are not Employees (groupName: employeeCustomerGroup) don't get 10% off on cart total
		'Test_Apply_Customer_Specific_Percentage_Discount_For_Not_Group_Member' | ['1934793': 1, '1382080': 1] | ['target_user_group_percentage_discount_cart']| 'not_employee' | 'anotherGroup' | 674.73 | 674.73 | 'USD'

		// A percentage discount is applied to the cart for the specified customer group
		// Employees (groupName: employeeCustomerGroup) get 10% off on cart total. Check for JPY currency.
		'Test_Apply_Customer_Specific_Percentage_Discount_JPY' | ['1934793': 1, '1382080': 1] | ['target_user_group_percentage_discount_cart']| 'employee' | 'employeePromotionGroup' | 51723 | 57470 | 'JPY'
	}
}
