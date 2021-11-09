/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.orders

import de.hybris.bootstrap.annotations.ManualTest
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Requires
import spock.lang.Unroll

import static de.hybris.platform.ycommercewebservicestest.setup.TestSetupUtils.isExtensionPresent
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class OrderReturnsTest extends AbstractOrderTest {

	static final String USERNAME_WITH_RETURNS = "orderreturnsuser@test.com"
	static final String USERNAME_WITH_NO_RETURNS = "orderreturnsuser2@test.com"
	static final String PASSWORD = "1234"

	static final CUSTOMER_WITH_RETURNS = ["id": USERNAME_WITH_RETURNS, "password": PASSWORD]
	static final CUSTOMER_WITH_NO_RETURNS = ["id": USERNAME_WITH_NO_RETURNS, "password": PASSWORD]

	static final PAGE_SIZE = 20

	def "Customer retrieves a returnable order: #orderCode"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "customer retrieves order by code"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orders/" + orderCode,
				contentType: JSON,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "order is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.code == orderCode
			data.returnable == returnable
		}

		where:
		orderCode     | returnable
		"testOrder14" | true
		"testOrder15" | true
	}

	def "Customer tries to create a new return request with bad parameters: #descriptor"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "customer creates a return request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns",
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
		descriptor        | message                                     | type                   | postBody
		"IllegalArgument" | "Item is not returnable for this quantity." | "IllegalArgumentError" | '{"orderCode":"testOrder16", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":10}, {"orderEntryNumber":1, "quantity":10}]}'
	}

	def "Customer tries to create a new return request with not valid parameters: #descriptor"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "customer creates a return request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns",
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
		descriptor                                   | message                   | subject                                        | type              | postBody
		"FieldRequiredOrderCodeNull"                 | "This field is required." | "orderCode"                                    | "ValidationError" | '{"returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":10}]}'
		"FieldRequiredOrderCodeEmpty"                | "This field is required." | "orderCode"                                    | "ValidationError" | '{"orderCode":"", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":10}, {"orderEntryNumber":1, "quantity":10}]}'
		"FieldRequiredReturnRequestEntryInputsNull"  | "This field is required." | "returnRequestEntryInputs"                     | "ValidationError" | '{"orderCode":"testOrder16"}'
		"FieldRequiredReturnRequestEntryInputsEmpty" | "This field is required." | "returnRequestEntryInputs"                     | "ValidationError" | '{"orderCode":"testOrder16", "returnRequestEntryInputs": []}'
		"FieldRequiredOrderEntryNumber"              | "This field is required." | "returnRequestEntryInputs[0].orderEntryNumber" | "ValidationError" | '{"orderCode":"testOrder16", "returnRequestEntryInputs": [{"quantity":10}]}'
		"FieldRequiredQuantity"                      | "This field is required." | "returnRequestEntryInputs[0].quantity"         | "ValidationError" | '{"orderCode":"testOrder16", "returnRequestEntryInputs": [{"orderEntryNumber":0}]}'
	}

	def "Customer creates a new return request"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "customer creates a return request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "newly created return request is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_CREATED

			data.returnEntries.any {
				it.orderEntry.entryNumber == firstReturnEntryOrderEntryNumber && it.expectedQuantity == firstReturnEntryQuantity
			}

			data.returnEntries.any {
				it.orderEntry.entryNumber == secondReturnEntryOrderEntryNumber && it.expectedQuantity == secondReturnEntryQuantity
			}
		}

		where:
		firstReturnEntryOrderEntryNumber | firstReturnEntryQuantity | secondReturnEntryOrderEntryNumber | secondReturnEntryQuantity | postBody
		0                                | 3                        | 1                                 | 3                         | '{"orderCode":"testOrder16", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}

	def "Customer tries to create a new return request from a non-existing order"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_NO_RETURNS)

		when: "customer creates a return request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_NO_RETURNS.id + "/orderReturns",
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
		message              | type            | postBody
		"Resource not found" | "NotFoundError" | '{"orderCode":"testOrderNotExisting", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}

	def "Customer tries to create a new return request from an order that he didn't place"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_NO_RETURNS)

		when: "customer creates a return request"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_NO_RETURNS.id + "/orderReturns",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON)

		then: "newly created return request is retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
			data.errors.any { it.message == message && it.type == type }
		}

		where:
		message              | type            | postBody
		"Resource not found" | "NotFoundError" | '{"orderCode":"testOrder16", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":3}, {"orderEntryNumber":1, "quantity":3}]}'
	}

	def "Customer retrieves order return requests"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "customer retrieves order return requests"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/",
				contentType: JSON,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "order return requests are retrieved"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.returnRequests.size() > 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() > 0
		}
	}

	def "Customer retrieves a specific order return request: #returnRequestCode"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "he requests to get a specific order return request"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequestCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets a specific order return request"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.code == returnRequestCode
			data.rma == rma
			data.status == returnRequestStatus
			data.order.code == orderCode
			data.cancellable == true
			data.returnEntries.any {
				it.orderEntry.entryNumber == returnEntryOrderEntryNumber && it.orderEntry.quantity == returnEntryOrderEntryQuantity && it.expectedQuantity == returnEntryExpectedQuantity
			}
		}

		where:
		returnRequestCode     | rma       | returnRequestStatus | orderCode     | cancellable | returnEntryOrderEntryNumber | returnEntryOrderEntryQuantity | returnEntryExpectedQuantity
		"testReturnRequest01" | "0000001" | "WAIT"              | "testOrder15" | true        | 0                           | 4                             | 3
	}

	def "Customer tries to retrieve a non-existing order return request: #returnRequestCode"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "he requests to get a specific order return request"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequestCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		returnRequestCode << ["testReturnRequestNotExisting"]
	}

	def "Customer tries to cancel an order return request which doesn't belong to him: #returnRequestCode"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_NO_RETURNS)

		when: "he requests to cancel a specific order return request"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_NO_RETURNS.id + "/orderReturns/" + returnRequestCode,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		returnRequestCode     | patchBody
		"testReturnRequest03" | '{"status":"CANCELLING"}'
	}

	def "Customer tries to cancel a non-existing order return request: #returnRequestCode"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		when: "he requests to cancel a non-existing  order return request"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequestCode,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		returnRequestCode            | patchBody
		"testReturnRequestNotExists" | '{"status":"CANCELLING"}'
	}

	def "Customer sends an empty body for update request: #returnRequestCode"() {
		given: "authorized customer and a return request"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		def existingReturnRequestResponse = getReturnRequest(restClient, CUSTOMER_WITH_RETURNS, returnRequestCode)
		existingReturnRequestResponse.status == SC_OK
		existingReturnRequestResponse.data.status == "WAIT"

		when: "he requests to cancel a specific order return request"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequestCode,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedReturnRequestResponse = getReturnRequest(restClient, CUSTOMER_WITH_RETURNS, returnRequestCode)

		then: "request is successful"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
		}
		and: "there are no changes to the return request"
		with(updatedReturnRequestResponse) {
			status == SC_OK
			data == existingReturnRequestResponse.data
		}

		where:
		returnRequestCode     | patchBody
		"testReturnRequest03" | '{}'
	}

	/**
	 * This test works properly when OMS extensions are installed. The yacceleratorfulfilment does not support return request process and test fails.
	 */
	@Requires({ isExtensionPresent("warehousing") })
	def "Customer cancels a specific order return request"() {
		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		and: "creates a new return request"
		def returnRequest = createReturnRequest(restClient, CUSTOMER_WITH_RETURNS, postBody);

		and: "requests to cancel a specific order return request"
		with(restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequest.code,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
		}

		when: "gets canceled return request"
		def canceledReturnRequest = getReturnRequest(restClient, CUSTOMER_WITH_RETURNS, returnRequest.code)

		then: "request is cancelled"
		with(canceledReturnRequest) {
			status == SC_OK
			data.status ==~ /^CANCEL(ED|LING)$/
		}

		where:
		patchBody                 | postBody
		'{"status":"CANCELLING"}' | '{"orderCode":"testOrder14", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":1}, {"orderEntryNumber":1, "quantity":1}]}'
	}

	/**
	 * This test works properly when OMS extensions are installed. The yacceleratorfulfilment does not support return request process and test fails.
	 */
	@Requires({ isExtensionPresent("warehousing") })
	def "Customer tries to cancel a cancelled order return request"() {

		given: "authorized customer"
		authorizeCustomer(restClient, CUSTOMER_WITH_RETURNS)

		and: "creates a new return request"
		def returnRequest = createReturnRequest(restClient, CUSTOMER_WITH_RETURNS, postBody);

		and: "cancels a return request"
		with(restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequest.code,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}

		and: "gets cancelled return request"
		with(getReturnRequest(restClient, CUSTOMER_WITH_RETURNS, returnRequest.code)) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.status ==~ /^CANCEL(ED|LING)$/
		}

		when: "requests to cancel a cancelled order return request"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + "/users/" + CUSTOMER_WITH_RETURNS.id + "/orderReturns/" + returnRequest.code,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.any {
				it.message ==~ regex && it.type == type
			}
		}

		where:
		regex                                                                | type                   | patchBody                 | postBody
		/^Return request with status CANCEL(ED|LING) cannot be cancelled\.$/ | "IllegalArgumentError" | '{"status":"CANCELLING"}' | '{"orderCode":"testOrder14", "returnRequestEntryInputs": [{"orderEntryNumber":0, "quantity":1}, {"orderEntryNumber":1, "quantity":1}]}'

	}

	/**
	 * This method retrieves a return request details based on code
	 * @param client REST client to be used
	 * @param customer customer for whom the order is placed
	 * @param returnRequestCode code of return request
	 * @param format format to be used
	 * @return return request
	 */
	protected getReturnRequest(RESTClient client, customer, returnRequestCode) {

		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + "/users/" + customer.id + "/orderReturns/" + returnRequestCode,
				contentType: JSON,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		return response
	}

	/**
	 * This methid creates a return request with given postBody
	 * @param client REST client to be used
	 * @param customer customer for whom the order is placed
	 * @param postBody request data
	 * @param basePathWithSite base path with site
	 * @return created return request
	 */
	protected createReturnRequest(RESTClient client, customer, postBody, basePathWithSite = getBasePathWithSite()) {
		return returningWith(client.post(
				path: basePathWithSite + "/users/" + customer.id + "/orderReturns",
				contentType: JSON,
				body: postBody,
				requestContentType: JSON), {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_CREATED
		}).data
	}

}
