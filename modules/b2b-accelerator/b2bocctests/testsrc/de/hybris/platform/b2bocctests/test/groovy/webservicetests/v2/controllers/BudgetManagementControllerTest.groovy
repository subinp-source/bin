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
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class BudgetManagementControllerTest extends AbstractUserTest {
	private final static String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
	private final static String B2BADMIN_PASSWORD = "1234"

	static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
	static final String B2BCUSTOMER_PASSWORD = "1234"

	private static final int NUMBER_OF_BUDGETS = 2
	private static final int PAGE_SIZE = 20

	static final BUDGETS_PATH = "/budgets"

	def "B2B Admin get budgets: #format"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get budgets"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH,
				contentType: format,
				requestContentType: URLENC)

		then: "he/she gets budgets"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.budgets.size() == NUMBER_OF_BUDGETS
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() == NUMBER_OF_BUDGETS
		}

		where:
		format << [JSON]
	}

	def "B2B Customer tries to get budgets"() {
		given: "a registered and logged in B2B customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get order approval permissions"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + BUDGETS_PATH,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_UNAUTHORIZED
		}
	}

	def "B2B Admin gets a specific budget: #budgetCode"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specific order approval permission"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets a specific budget"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.code == budgetCode
			data.budget.compareTo(budget) == 0

			data.orgUnit.uid == unitUid
			data.currency.isocode == currencyIso
		}

		where:
		budgetCode        | budget  | unitUid  | currencyIso
		"Monthly_50K_USD" | 50000.0 | "Rustic" | "USD"
		"Monthly_20K_USD" | 20000.0 | "Rustic" | "USD"
	}

	def "B2B Admin tries to get a non-existing budget"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a non-existing budget"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCode,
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
		budgetCode << ["Non_Existing_Budget"]
	}

	def "B2B Admin edits a specific budget: #budgetCodeInRequest"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a specific budget"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCodeInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getBudget(restClient, b2bAdminCustomer, budgetCode)

		then: "his changes are successfully saved"
		with(response) { status == SC_OK }

		and: "new values are visible"
		with(updatedInfo) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			compareBudgets(data, active, budgetCode, budget, unitUid, currencyIso, name)
		}

		where:
		budgetCodeInRequest | active | budgetCode          | budget  | unitUid  | currencyIso | name              | patchBody
		"Monthly_50K_USD"   | true   | "Monthly_50K_USD_2" | 50000.0 | "Rustic" | "USD"       | "Monthly 50K USD" | '{"code": "Monthly_50K_USD_2"}'
		"Monthly_20K_USD"   | true   | "Monthly_20K_USD"   | 15000.0 | "Rustic" | "EUR"       | "Monthly 20K USD" | '{"currency": {"isocode": "EUR"}, "budget": 15000}'
	}

	def "B2B Admin enables/disables a specific budget: #budgetCodeInRequest"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to enable/disable a specific budget by editing the budget"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCodeInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getBudget(restClient, b2bAdminCustomer, budgetCode)

		then: "activation/deactivation is successfully applied"
		with(response) { status == SC_OK }

		and: "activation/deactivation is visible"
		with(updatedInfo) {
			compareBudgets(data, active, budgetCode, budget, unitUid, currencyIso, name)
		}

		where:
		budgetCodeInRequest | active | budgetCode        | budget  | unitUid  | currencyIso | name              | patchBody
		"Monthly_50K_USD_2" | false  | "Monthly_50K_USD" | 50000.0 | "Rustic" | "USD"       | "Monthly 50K USD" | '{"active": false, "code": "Monthly_50K_USD"}'
	}

	def "B2B Admin tries to edit a budget [#budgetCodeInRequest] with non-valid attributes: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a budget with non-valid attributes"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCodeInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.any { it.message == message && it.subject == subject && it.type == type }
		}

		where:
		budgetCodeInRequest | descriptor   | message                   | subject | type              | patchBody
		"Monthly_50K_USD"   | "Short_Code" | "This field is required." | "code"  | "ValidationError" | '{"code":"","name":"Monthly 50K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Monthly_50K_USD"   | "Long_Code"  | "This field is required." | "code"  | "ValidationError" | '{"code":"' + createTestString(256) + '","name":"Monthly 50K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Monthly_50K_USD"   | "Short_Name" | "This field is required." | "name"  | "ValidationError" | '{"code":"Monthly_50K_EUR","name":"","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Monthly_50K_USD"   | "Long_Name"  | "This field is required." | "name"  | "ValidationError" | '{"code":"Monthly_50K_EUR","name":"' + createTestString(256) + '","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
	}
	
	def "B2B Admin tries to edit a budget [#budgetCodeInRequest] with non-valid patch body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a budget with non-valid patch body"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH + "/" + budgetCodeInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		budgetCodeInRequest | message 						| type              				| patchBody
		"Monthly_50K_USD"   | "Invalid request body"		| "HttpMessageNotReadableError" 	| '{"active": wrong_value, "code": Monthly_50K_USD"}'
		"Monthly_50K_USD"   | "Invalid request body" 		|"HttpMessageNotReadableError" 		| ''
	}

	def "B2B Admin creates a new budget: #budgetCode"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new budget"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the newly created specific budget"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_CREATED

			compareBudgets(data, active, budgetCode, budget, unitUid, currencyIso, name)
		}

		where:
		budgetCode        | active | budget  | unitUid         | currencyIso | name              | postBody
		"Monthly_10K_EUR" | true   | 10000.0 | "Rustic"        | "EUR"       | "Monthly 10K EUR" | '{"active":true,"budget":10000,"code":"Monthly_10K_EUR","name":"Monthly 10K EUR","costCenters":[{"code":"Rustic_Global"}],"currency":{"active":true,"isocode":"EUR","name":"Euro","symbol":"E"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Monthly_20K_EUR" | false  | 20000.0 | "Rustic_Retail" | "EUR"       | "Monthly 20K EUR" | '{"active":false,"budget":20000,"code":"Monthly_20K_EUR","name":"Monthly 20K EUR","costCenters":[{"code":"Rustic_Global"}],"currency":{"active":true,"isocode":"EUR","name":"Euro","symbol":"E"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic_Retail"},"startDate":"2020-01-01T09:00:00+0000"}'

	}

    def "B2B Admin tries to create a new budget with an already existing budget code: #budgetCode"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to create a new budget with an already existing budget code"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets conflict response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_CONFLICT
        }

        where:
        budgetCode        | budget  | unitUid  | currencyIso | name              | postBody
        "Monthly_20K_USD" | 10000.0 | "Rustic" | "USD"       | "Monthly 20K USD" | '{"active":true,"budget":10000,"code":"Monthly_20K_USD","name":"Monthly 20K USD","costCenters":[{"active":"true","code":"Rustic_Global","name":"Rustic Global","unit":{}}],"currency":{"active":true,"isocode":"EUR","name":"Euro","symbol":"E"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"name":"Rustic","uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
    }

	def "B2B Admin tries to create a new budget with non-valid attributes: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new budget with non-valid attributes"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.any { it.message == message && it.subject == subject && it.type == type }
		}

		where:
		descriptor              | message                   | subject            | type              | postBody
		"Null_Code"             | "This field is required." | "code"             | "ValidationError" | '{"code":null,"name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Short_Code"            | "This field is required." | "code"             | "ValidationError" | '{"code":"","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Long_Code"             | "This field is required." | "code"             | "ValidationError" | '{"code":"' + createTestString(256) + '","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Name"             | "This field is required." | "name"             | "ValidationError" | '{"code":"Monthly_100K_EUR","name":null,"currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Short_Name"            | "This field is required." | "name"             | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Long_Name"             | "This field is required." | "name"             | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"' + createTestString(256) + '","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Org_Unit"         | "This field is required." | "orgUnit"          | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":null,"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Org_Unit_Uid"     | "This field is required." | "orgUnit.uid"      | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":null},"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Currency"         | "This field is required." | "currency"         | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":null,"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Currency_Isocode" | "This field is required." | "currency.isocode" | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":{"isocode":null},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"Null_Start_Date"       | "This field is required." | "startDate"        | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":null}'
		"Null_End_Date"         | "This field is required." | "endDate"          | "ValidationError" | '{"code":"Monthly_100K_EUR","name":"Monthly 100K USD","currency":{"isocode":"EUR"},"endDate":null,"orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
	}
	
	def "B2B Admin tries to create a new budget with non-valid post body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new budget with non-valid post body"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + BUDGETS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		type              					| message                   | postBody
		"HttpMessageNotReadableError" 		| "Invalid request body"	| '{"code":null,"name":"Monthly 100K U,"currency" {"isocode":"EUR"},"endDate":"2020-12-31T09:00:00+0000","orgUnit":{"uid":"Rustic"},"startDate":"2020-01-01T09:00:00+0000"}'
		"HttpMessageNotReadableError" 		| "Invalid request body"	| ''
	}

	protected String createTestString(length) {
		StringBuilder stringBuilder = new StringBuilder("")
		for (int i = 0; i < length; i++) {
			stringBuilder.append("1")
		}
		return stringBuilder.toString()
	}

	protected void compareBudgets(responseData, active, budgetCode, budget, unitUid, currencyIso, name) {
		assert responseData.active == active
		assert responseData.code == budgetCode
		assert responseData.budget.compareTo(budget) == 0
		assert responseData.orgUnit.uid == unitUid
		assert responseData.currency.isocode == currencyIso
		assert responseData.name == name
	}

	protected getBudget(RESTClient client, customer, budgetCode) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + BUDGETS_PATH + '/' + budgetCode,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response;
	}


}
