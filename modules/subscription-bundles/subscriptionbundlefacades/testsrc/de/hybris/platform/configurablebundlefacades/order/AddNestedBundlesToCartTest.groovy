/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order


import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.commerceservices.order.CommerceCartModificationException
import de.hybris.platform.configurablebundlefacades.AbstractSubscriptionBundleFacadesSpockTest
import de.hybris.platform.order.CartService
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.util.AppendSpringConfiguration

import javax.annotation.Resource

import org.junit.Test

/**
 * Port of old ATDD-framework tests to Spock framework
 */
@IntegrationTest
@AppendSpringConfiguration("classpath:subscriptionfacades-spring-test-context.xml")
class AddNestedBundlesToCartTest extends AbstractSubscriptionBundleFacadesSpockTest {

	// test data values
	def CUSTOMER_ID = "testuser1@saved-carts.com"

	@Resource
	private BundleCartFacade bundleCartFacade;

	@Resource
	private CartService cartService;

	// is run before every test
	def setup() {
		importCsv("/configurablebundleservices/test/nestedBundleTemplates.impex", "utf-8")
		prepareSession("testSite")
	}


	// Add nested bundle to cart tests

	@Test
	def "Add a single product to a new bundle"() {
		// Test_Add_To_Cart_SingleBundleNew_SingleProduct

		given:
		verifyCartTotal(0.0)

		when: "we add a product for a bundle component to the cart"
		bundleCartFacade.startBundle("ProductComponent1", "PRODUCT01", 1)

		then: "number of child carts and cart totals are correct "
		verifyCartTotal(600.00)
		verifyCartTotalForBillingEvent("paynow", 600.00)
		verifyNumberOfChildCarts(0)

	}
	@Test
	def "Standalone product can not be added with a bundle"() {
		// Test_AddAStandaloneProductAsABundled

		when: "we add a standalone product to a bundle component in the cart"
		addProductToNewBundle("STANDALONE01", "ProductComponent1")

		then: "exception is expected"
		AssertionError e = thrown()
		e.message == "Product 'STANDALONE01' is not in the product list of component (bundle template) #ProductComponent1"
	}

	@Test
	def "Product from another bundle can not be added with the bundle"() {
		// Test_AddAProductOfAnotherBundle

		when: "we add a standalone product to a bundle component in the cart"
		addProductToNewBundle("PRODUCT01", "PremiumComponent2")

		then: "exception is expected"
		AssertionError e = thrown()
		e.message == "Product 'PRODUCT01' is not in the product list of component (bundle template) #PremiumComponent2"
	}

	@Test
	def "Products with soldIndividually true can be sold out of bundle"() {
		// Test_IndividualProductCanBeSoldSeparately

		given:
		verifyCartTotal(0.0)

		when: "we add a product with soldIndividually true to the cart"
		addProductToCartOnce("PRODUCT05")

		then: "cart totals are correct "
		verifyCartTotal(650.00)
	}

	@Test
	def "Not individual products can not be sold separately"() {
		// Test_NotIndividualProductCanNotBeSoldSeparately

		when: "we add a standalone product to a bundle component in the cart"
		addProductToCartOnce("PRODUCT06")

		then: "exception is expected"
		AssertionError e = thrown()
		e.message == "The given product 'PRODUCT06' can not be sold individually."
	}

	@Test
	def "Not individual products can be sold as a part of bundle"() {
		// Test_NotIndividualProductCanBeSoldInABundle

		given:
		verifyCartTotal(0.0)

		when: "we add a product for a bundle component to the cart"
		bundleCartFacade.startBundle("ProductComponent1", "PRODUCT06", 1)

		then: "cart totals are correct "
		verifyCartTotal(600.00)
	}
	
	@Test
	def "Adding the same product to different bundles is allowed"() {
		// Test_Add_To_Cart_TheSameProduct_to_DifferentBundles

		given:
		verifyCartTotal(0)

		when:
		addProductToNewBundle("SHARED01", "ProductComponent1")

		then:
		verifyCartTotal(99)

		when:
		def cart = getCartDTO()
		def entryGroup = findEntryGroupByRefInOrder(cart, "PremiumComponent2")
		addProductToExistingBundle("SHARED01", entryGroup.getGroupNumber())

		then:
		verifyCartTotal(198)
	}
	
	@Test
	def "Adding different product to the same component is allowed"() {
		// Test_Add_To_Cart_TheSameProduct_to_DifferentBundles

		given:
		verifyCartTotal(0)

		when:
		addProductToNewBundle("PRODUCT01", "ProductComponent1")

		then:
		verifyCartTotal(600)

		when:
		def cart = getCartDTO()
		def entryGroup = findEntryGroupByRefInOrder(cart, "ProductComponent1")
		addProductToExistingBundle("PRODUCT02", entryGroup.getGroupNumber())

		then:
		verifyCartTotal(1250)
	}

	@Test
	def "Add additional quantity of the product to the bundle"() {

		when:
		addProductToNewBundle("PRODUCT01", "ProductComponent1")

		then:
		verifyCartTotal(600)

		when:
		def cart = getCartDTO()
		def entryGroup = findEntryGroupByRefInOrder(cart, "ProductComponent1")
		addQuantityOfProductsToExistingBundle(2, "PRODUCT01", entryGroup.getGroupNumber())

		then:
		verifyCartTotal(1800)
	}

	@Test
	def "Try to add product giving non existing entry number"() {

		when:
		addProductToNewBundle("PRODUCT01", "ProductComponent1")

		then:
		verifyCartTotal(600)

		when:
		addProductToExistingBundle("PRODUCT02", -2)

		then: "exception is expected"
		IllegalArgumentException e = thrown()
		e.message.startsWith("No group with number '-2' in the order with code")
	}

}