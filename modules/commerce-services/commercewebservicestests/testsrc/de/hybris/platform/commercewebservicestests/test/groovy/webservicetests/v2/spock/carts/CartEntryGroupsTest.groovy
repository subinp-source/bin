/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.carts

import de.hybris.bootstrap.annotations.ManualTest
import spock.lang.IgnoreIf
import spock.lang.Unroll

import static de.hybris.platform.commercewebservicestests.setup.TestSetupUtils.anyExtensionPresent
import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.*

@Unroll
@ManualTest
class CartEntryGroupsTest extends AbstractCartTest {

	private static final TYPE_STANDALONE = 'STANDALONE'

	def "Customer tries to add product to an entry group with wrong content type when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]

		when: "customer tries to add product to cart"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)

		then: "error is returned"
		with(response) {
			status == SC_UNSUPPORTED_MEDIA_TYPE
			data.errors[0].type == errorType
			data.errors[0].message == errorMessage
		}

		where:
		requestFormat | responseFormat | postBody                                                                                                                              | errorType                        | errorMessage
		XML           | JSON           | "<?xml version=\"1.0\" encoding=\"UTF-8\"?><orderEntry><product>${PRODUCT_FLEXI_TRIPOD}</product><quantity>1</quantity></orderEntry>" | 'HttpMediaTypeNotSupportedError' | "Content type 'application/xml' not supported"
	}

	def "Customer tries to add product to an entry group without required code parameter when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]

		when: "customer tries to add product to cart"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)

		then: "error is returned"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == errorType
			data.errors[0].message == errorMessage
			data.errors[0].subject == errorSubject
		}

		where:
		requestFormat | responseFormat | postBody             | errorType         | errorMessage              | errorSubject
		JSON          | JSON           | "{\"quantity\" : 1}" | 'ValidationError' | 'This field is required.' | 'product.code'
	}

	def "Customer tries to add product to an entry group with wrong code when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]

		when: "customer tries to add product to cart"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)

		then: "UnknownIdentifierError is returned"
		with(response) {
			status == SC_BAD_REQUEST
			println data;
			data.errors[0].type == 'UnknownIdentifierError'
			data.errors[0].message == "Product with code 'notExistingProduct' not found!"
		}

		where:
		requestFormat | responseFormat | postBody
		JSON          | JSON           | "{\"product\" : {\"code\" : \"notExistingProduct\"},\"quantity\" : 1}"
	}

	def "Customer tries to add product to an entry group with wrong quantity when request: #requestFormat and response: #responseFormat"() {
		given: "a registerd and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]

		when: "customer tries to add product to cart"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)

		then: "UnknownIdentifierError is returned"
		with(response) {
			status == SC_BAD_REQUEST
			println data;
			data.errors[0].type == errorType
			data.errors[0].message == errorMessage
		}

		where:
		requestFormat | responseFormat | postBody                                                                     | errorType         | errorMessage
		JSON          | JSON           | "{\"product\" : {\"code\" : \"${PRODUCT_FLEXI_TRIPOD}\"},\"quantity\" : -1}" | 'ValidationError' | 'This field must be greater than 0.'
	}

	def "Customer tries to add product to an entry group with wrong entry group number when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]

		when: "customer tries to add product to cart"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/' + entryGroupNumber,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)

		then: "UnknownIdentifierError is returned"
		with(response) {
			status == SC_BAD_REQUEST
			println data;
			data.errors[0].type == errorType
			data.errors[0].message.startsWith(errorMessage)
		}

		where:
		requestFormat | responseFormat | entryGroupNumber | postBody                                                                    | errorType         | errorMessage
		JSON          | JSON           | 0                | "{\"product\" : {\"code\" : \"${PRODUCT_FLEXI_TRIPOD}\"},\"quantity\" : 1}" | 'ValidationError' | "This field must be greater than 0."
		JSON          | JSON           | -1               | "{\"product\" : {\"code\" : \"${PRODUCT_FLEXI_TRIPOD}\"},\"quantity\" : 1}" | 'ValidationError' | "This field must be greater than 0."
	}

	/**
	 * This test is only useful when no custom bundle implementations are present,
	 * it only verifies the behaviour if addtoentrygroup is invoked for standalone products.
	 */
	@IgnoreIf({ anyExtensionPresent("commercewebservicestests.bundle.extensions") })
	def "Customer tries to add product to an existing entry group, without any bundle extensions, should create a new entry group when request: #requestFormat and response: #responseFormat"() {
		given: "a registerd and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, responseFormat)
		def customer = val[0]
		def cart = val[1]
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_FLEXI_TRIPOD, 1, requestFormat)

		when: "get entry groups and check entryGroupType to see whether it is a custom bundle or standalone implementation"
		def getCartWithEntryGroupResponse = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code,
				contentType: requestFormat,
				query: [
						'fields': 'entryGroups(entries,DEFAULT)'
				],
				requestContentType: URLENC
		)

		with(getCartWithEntryGroupResponse) {
			status == SC_OK
			data.entryGroups.size() == 1
			data.entryGroups[0].entries[0].product.code.toString() == PRODUCT_FLEXI_TRIPOD.toString()
			data.entryGroups[0].entryGroupNumber == 1
		}

		then: "customer trying to add another product to previous STANDALONE entry group should create new entry group"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat
		)
		with(response) {
			status == SC_OK
			println data;
		}

		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code,
				contentType: requestFormat,
				query: [
						'fields': 'entryGroups(entries,DEFAULT)'
				],
				requestContentType: URLENC
		)
		with(response2) {
			status == SC_OK
			data.entryGroups.size() == 2
			data.entryGroups[0].entries[0].product.code.toString() == PRODUCT_POWER_SHOT_A480.toString()
			data.entryGroups[0].entryGroupNumber == 2
			data.entryGroups[0].type == TYPE_STANDALONE
			data.entryGroups[1].entries[0].product.code.toString() == PRODUCT_FLEXI_TRIPOD.toString()
			data.entryGroups[1].entryGroupNumber == 1
			data.entryGroups[1].type == TYPE_STANDALONE
		}

		where:
		requestFormat | responseFormat | postBody
		JSON          | JSON           | "{\"product\" : {\"code\" : \"${PRODUCT_POWER_SHOT_A480}\"},\"quantity\" : 1}"
	}


	def "Registered Customer gets entryGroups for his cart: #format"() {
		given: "a registered and logged in customer with cart and two cart entries"
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_FLEXI_TRIPOD, 1, format)
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_POWER_SHOT_A480, 1, format)

		when: "customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
		def response = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code,
				contentType: format,
				query: [
						'fields': 'entryGroups(DEFAULT)'
				],
				requestContentType: URLENC
		)


		then: "cart entryGroups are returned and have values"
		with(response) {
			status == SC_OK
			data.entryGroups.size() == 2
			data.entryGroups[0].entries[0].product.code.toString() == PRODUCT_POWER_SHOT_A480.toString()
			data.entryGroups[0].entryGroupNumber == 2
			data.entryGroups[0].type == TYPE_STANDALONE
			data.entryGroups[1].entries[0].product.code.toString() == PRODUCT_FLEXI_TRIPOD.toString()
			data.entryGroups[1].entryGroupNumber == 1
			data.entryGroups[1].type == TYPE_STANDALONE
		}

		where:
		format << [XML, JSON]
	}


	def "Registered Customer removes entry from his cart should also remove the entrygroup: #format"() {
		given: "a registerd and logged in customer with cart and two cart entries"
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_FLEXI_TRIPOD, 1, format)
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_POWER_SHOT_A480, 1, format)

		when: "customer decides to change his cart entry"
		def response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entries/0',
				contentType: format,
				requestContentType: URLENC
		)

		and: "customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code,
				contentType: format,
				query: [
						'fields': 'entryGroups(entries,DEFAULT)'
				],
				requestContentType: URLENC
		)

		then: "cart entry is removed"
		with(response) { status == SC_OK }

		and: "only one entryGroup is returned"
		with(response2) {
			status == SC_OK
			data.entryGroups.size() == 1
			data.entryGroups[0].entries[0].product.code.toString() == PRODUCT_POWER_SHOT_A480.toString()
			data.entryGroups[0].entryGroupNumber == 1
			data.entryGroups[0].type == TYPE_STANDALONE
		}

		where:
		format << [XML, JSON]
	}

	def "Customer tries to delete a virtual entrygroup with STANDALONE product should fail: #format"() {
		given: "a registered and logged in customer with cart and two cart entries"
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_FLEXI_TRIPOD, 1, format)

		when: "customer tries to delete the entry group"
		def response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entrygroups/1',
				contentType: format,
				requestContentType: URLENC
		)

		then: "error is returned"
		with(response) {
			status == SC_BAD_REQUEST
			println data;
			data.errors[0].type == 'CartEntryGroupError'
			data.errors[0].message == "Entry group not found"
			data.errors[0].subject == "1"
			data.errors[0].subjectType == "entryGroup"
			data.errors[0].reason == "notFound"
		}

		where:
		format << [JSON]
	}
}
