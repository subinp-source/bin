/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.SC_NOT_FOUND
import static org.apache.http.HttpStatus.SC_OK

@ManualTest
@Unroll
class B2BOrdersControllerReplenishmentTest extends AbstractUserTest {

	static final B2BCUSTOMER_USERNAME = "arthur.smith@rustic-hw.com"
	static final String B2BCUSTOMER_PASSWORD = "1234"

	static final B2BCUSTOMER2_USERNAME = "peter.wiley@rustic-hw.com"
	static final String B2BCUSTOMER2_PASSWORD = "1234"

	private static final int NUMBER_OF_REPLENISHMENT_ORDERS = 2
	private static final int PAGE_SIZE = 20

	static final String CONTROLLER_PATH = "/users/"

	static final REPLENISHMENT_ORDER_PATH = "/replenishmentOrders"

	def setup() {
		authorizeTrustedClient(restClient)
	}

	def "B2B Customer get replenishment orders: #format"() {
		given: "a registered and logged in B2B customer"
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get replenishment orders"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + CONTROLLER_PATH + b2bCustomer.id + REPLENISHMENT_ORDER_PATH,
				contentType: format,
				requestContentType: URLENC)

		then: "he/she gets replenishment orders"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.replenishmentOrders.size() == NUMBER_OF_REPLENISHMENT_ORDERS
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() == NUMBER_OF_REPLENISHMENT_ORDERS
		}

		where:
		format << [JSON]
	}

	def "B2B Customer gets replenishment order: #replenishmentOrderCode"(String replenishmentOrderCode, boolean active) {
		given: "a registered and logged in B2B customer"
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)
		when: "he requests to get replenishment order"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + CONTROLLER_PATH + b2bCustomer.id + REPLENISHMENT_ORDER_PATH + '/' + replenishmentOrderCode,
				contentType: JSON,
				requestContentType: URLENC)
		then: "he gets replenishment order"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.replenishmentOrderCode == replenishmentOrderCode
			data.active == active
		}
		where:
		replenishmentOrderCode | active
		"000000RS"             | true
		"000000RT"             | false
	}

	def "B2B Customer disables a specific replenishment order: #replenishmentOrderCode"() {
		given: "a registered and logged in B2B Customer"
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to disable a specific replenishment order by updating the replenishment order"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + CONTROLLER_PATH + b2bCustomer.id + REPLENISHMENT_ORDER_PATH + "/" + replenishmentOrderCode,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getReplenishmentOrder(restClient, b2bCustomer, replenishmentOrderCode)

		then: "deactivation is successfully applied"
		with(response) {
			compareReplenishmentOrders(data, replenishmentOrderCode, active)
		}

		and: "deactivation is persistent"
		with(updatedInfo) {
			compareReplenishmentOrders(data, replenishmentOrderCode, active)
		}

		where:
		replenishmentOrderCode | active
		"000000RS"             | false
		"000000RT"             | false
	}

	def "B2B Customer tries to disable a non-existing replenishment order: #replenishmentOrderCode"() {
		given: "a registered and logged in B2B Customer"
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to disable a non-existing replenishment order by updating the replenishment order"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + CONTROLLER_PATH + b2bCustomer.id + REPLENISHMENT_ORDER_PATH + "/" + replenishmentOrderCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets not found response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		replenishmentOrderCode << ["Non_Existing_Budget"]
	}

	def "B2B Customer tries to disable a specific replenishment order: #replenishmentOrderCode from another customer"() {
		given: "a registered and logged in B2B Customer"
		def otherCustomerUser = [
				'id'      : B2BCUSTOMER2_USERNAME,
				'password': B2BCUSTOMER2_PASSWORD
		]
		authorizeCustomer(restClient, otherCustomerUser)

		when: "he requests to disable a specific replenishment order from another customer by updating the replenishment order"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + CONTROLLER_PATH + otherCustomerUser.id + REPLENISHMENT_ORDER_PATH + "/" + replenishmentOrderCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets not found error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
		}

		where:
		replenishmentOrderCode | active
		"000000RS"             | false
	}

	protected void compareReplenishmentOrders(responseData, replenishmentOrderCode, active) {
		assert responseData.replenishmentOrderCode == replenishmentOrderCode
		assert responseData.active == active
	}

	protected getReplenishmentOrder(RESTClient client, customer, replenishmentOrderCode) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + CONTROLLER_PATH + customer.id + REPLENISHMENT_ORDER_PATH + '/' + replenishmentOrderCode,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response
	}

}
