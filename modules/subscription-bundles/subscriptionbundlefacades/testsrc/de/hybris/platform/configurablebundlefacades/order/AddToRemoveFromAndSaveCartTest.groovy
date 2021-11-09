/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order


import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.configurablebundlefacades.AbstractSubscriptionBundleFacadesSpockTest
import de.hybris.platform.order.CartService
import de.hybris.platform.util.AppendSpringConfiguration

import javax.annotation.Resource

import org.junit.Test

/**
 * Port of old ATDD-framework tests to Spock framework
 */
@IntegrationTest
@AppendSpringConfiguration("classpath:subscriptionfacades-spring-test-context.xml")
class AddToRemoveFromAndSaveCartTest extends AbstractSubscriptionBundleFacadesSpockTest {

	// test data values
	def CUSTOMER_ID = "testuser1@saved-carts.com"

	@Resource
	private BundleCartFacade bundleCartFacade;

	@Resource
	private CartService cartService;

	// is run before every test
	def setup() {
		importCsv("/subscriptionbundlefacades/test/testBundleCartFacade.csv", "utf-8")
		prepareSession("testSite")
	}

	// Save Cart Tests

	@Test
	def "Save a cart that has a product bundle"() {

		given: "a session with a logged in user"
		def sessionCart = getCurrentSessionCart()
		createCustomer(CUSTOMER_ID);
		login(CUSTOMER_ID)

		when: "we add a product for a bundle component to the cart"
		bundleCartFacade.startBundle("ProductComponent1", "PRODUCT01", 1)

		then: "the cart total is as per sample data"
		verifyCartTotal(600.00)

		when: "we save the cart"
		def savedCart = saveCartWithNameAndDescription("TestCart", "Test")

		then: "the saved cart has the same code as the session cart"
		savedCart.code == sessionCart.code

		when: "we clone the cart"
		def clonedCart = cloneSavedCart(savedCart.code)

		then: "the clone does not have the same code as the original saved cart, is identical to the original saved cart, and last modified entries are empty"
		clonedCart.code != savedCart.code
		verifyCartClone(savedCart.code, clonedCart.code)
		verifyLastModifiedEntriesIsEmpty(clonedCart.code)

	}

}