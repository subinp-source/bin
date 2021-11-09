/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.orders

import de.hybris.bootstrap.annotations.ManualTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class OrdersTest extends AbstractOrderTest {

	static final String USERNAME_WITH_CANCELLATIONS = "ordercancellationsuser@test.com"
	static final String USERNAME_WITH_NO_CANCELLATIONS = "ordercancellationsuser2@test.com"
	static final String PASSWORD = "1234"

	static final CUSTOMER_WITH_CANCELLATIONS = ["id": USERNAME_WITH_CANCELLATIONS, "password": PASSWORD]
	static final CUSTOMER_WITH_NO_CANCELLATIONS = ["id": USERNAME_WITH_NO_CANCELLATIONS, "password": PASSWORD]

	def "Trusted client requests order by code: #format"() {
		given: "trusted client"
		authorizeTrustedClient(restClient)

		when: "trusted client retrieves order by code"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/orders/" + ORDER_CODE,
				contentType: format,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "he receives requested order"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.store == "wsTest"
			data.net == false
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client requests order by wrong id: #format"() {
		given: "trusted client"
		authorizeTrustedClient(restClient)

		when: "trusted client retrieves order by code"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/orders/wrongOrderGuidOrCode",
				contentType: format,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "he receives requested order"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == "UnknownIdentifierError"
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client retrieves order by guid: #format"() {
		given: "an order with guid and trusted client"
		//this preparation steps are necessary because sample data does not contain orders with guid
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]
		def address = createAddress(restClient, customer)
		setDeliveryAddressForCart(restClient, customer, cart.code, address.id, format)
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_POWER_SHOT_A480)
		setDeliveryModeForCart(restClient, customer, cart.code, DELIVERY_STANDARD, format)
		createPaymentInfo(restClient, customer, cart.code)
		def order = placeOrder(restClient, customer, cart.code, "123", format)

		authorizeTrustedClient(restClient)

		when: "trusted client retrieves order by GUID"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/orders/" + order.guid,
				contentType: format,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "order is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.guid == order.guid
			data.code == order.code
		}

		where:
		format << [XML, JSON]
	}

	def "Customer retrieves a cancellable order: #code"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_CANCELLATIONS)

		when: "customer retrieves order by code"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_CANCELLATIONS.id + "/orders/" + code,
				contentType: JSON,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "order is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.code == code
			data.cancellable == cancellable
		}

		where:
		code          | cancellable
		"testOrder17" | true
	}

	def "Customer tries to create a new cancellation request with bad parameters: #descriptor"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.any { it.message == message && it.type == type }
		}

		where:
		descriptor     | code          | message                                  | type                | postBody
		"IllegalState" | "testOrder17" | "Given cancelled quantity is not valid." | "IllegalStateError" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":10}, {"orderEntryNumber":1, "quantity":10}]}'

	}

	def "Customer tries to create a new cancellation request with non-validated parameters: #descriptor"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.any { it.message == message && it.subject == subject && it.type == type }
		}

		where:
		descriptor                                         | code          | message                   | subject                                              | type              | postBody
		"FieldRequiredCancellationRequestEntryInputsNull"  | "testOrder17" | "This field is required." | "cancellationRequestEntryInputs"                     | "ValidationError" | '{}'
		"FieldRequiredCancellationRequestEntryInputsEmpty" | "testOrder17" | "This field is required." | "cancellationRequestEntryInputs"                     | "ValidationError" | '{"cancellationRequestEntryInputs": []}'
		"FieldRequiredOrderEntryNumber"                    | "testOrder17" | "This field is required." | "cancellationRequestEntryInputs[0].orderEntryNumber" | "ValidationError" | '{"cancellationRequestEntryInputs": [{"quantity":10}]}'
		"FieldRequiredQuantity"                            | "testOrder17" | "This field is required." | "cancellationRequestEntryInputs[0].quantity"         | "ValidationError" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0}]}'
	}

	def "Customer creates a new cancellation request: #code"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "cancellation request response is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}

		where:
		code          | postBody
		"testOrder17" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
		"testOrder18" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":4}, {"orderEntryNumber":1, "quantity":3}]}'

	}

	def "Customer tries to create a new cancellations request from a non-existing order: #code"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_NO_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_NO_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
			data.errors.any { it.message == message && it.type == type }
		}

		where:
		code                   | message              | type            | postBody
		"testOrderNotExisting" | "Resource not found" | "NotFoundError" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}

	def "Customer tries to create a new cancellation request from an order that he didn't place: #code"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_NO_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_NO_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
			data.errors.any { it.message == message && it.type == type }
		}

		where:
		code          | message              | type            | postBody
		"testOrder17" | "Resource not found" | "NotFoundError" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}

	def "Customer tries to cancel an order with non-cancellable status #code"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_CANCELLATIONS)

		when: "customer creates a cancellation request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_CANCELLATIONS.id + "/orders/" + code + "/cancellation",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.any { it.message == message && it.type == type }
		}

		where:
		code          | message                                        | type                | postBody
		"testOrder18" | "The Order is not cancellable at this moment." | "IllegalStateError" | '{"cancellationRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}
}
