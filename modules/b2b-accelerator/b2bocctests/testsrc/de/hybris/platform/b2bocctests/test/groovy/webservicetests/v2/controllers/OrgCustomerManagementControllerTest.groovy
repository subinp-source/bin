/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.*

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Unroll

@ManualTest
@Unroll
class OrgCustomerManagementControllerTest extends AbstractUserTest {
	private final static String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
	private final static String B2BADMIN_PASSWORD = "1234"

	private static final int PAGE_SIZE = 20

	static final ORG_CUSTOMERS_PATH = "/orgCustomers"
	static final APPROVERS_PATH = "/approvers"
	static final ORG_USER_GROUPS_PATH = "/orgUserGroups"
	static final PERMISSIONS_PATH = "/permissions"

	def "B2B Admin gets orgCustomers: #format"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get orgCustomers"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				contentType: format,
				requestContentType: URLENC)

		then: "he/she gets orgCustomers"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.users.size() > 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() > 0
		}

		where:
		format << [JSON]
	}

	def "B2B Admin gets a specific org customer: #orgCustomerId which is #orgCustomerUID"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specific org customer"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets a specific org customer"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.uid == orgCustomerUID
			data.firstName == firstName
			data.lastName == lastName
		}

		where:
		orgCustomerId                          | orgCustomerUID                    | firstName  | lastName
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "oc.akiro.nakamura@rustic-hw.com" | "OC Akiro" | "Nakamura"
		"3a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "oc.hanna.schmidt@rustic-hw.com"  | "OC Hanna" | "Schmidt"
	}

	def "B2B Admin updates an organizational customer #orgCustomerId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update customer"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)
		def updatedCustomer = getOrgCustomer(restClient, b2bAdminCustomer, orgCustomerId)
		then: "the request is successful"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NO_CONTENT
		}

		with(updatedCustomer) {
			status == SC_OK
			data.customerId == orgCustomerId
			data.uid == email
			data.titleCode == title
			data.firstName == firstName
			data.lastName == lastName
			data.orgUnit.uid == orgUnitUid
			data.active == active
			data.roles.find { it == role }
		}

		where:
		orgCustomerId                          | title | firstName | lastName  | email                          | orgUnitUid      | active | role              | patchBody
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "ms"  | "Aiden"   | "Long"    | "aiden.long@rustic-hw.com"     | "Rustic_Retail" | false  | "b2badmingroup"   | '{"titleCode": "ms", "firstName": "Aiden", "lastName": "Long", "email": "aiden.long@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "active": false, "roles":["b2badmingroup"]}'
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "mr"  | "Russell" | "Sanchez" | "fabian.sanchez@rustic-hw.com" | "Rustic"        | true   | "b2bmanagergroup" | '{"firstName": "Russell"}'
	}

	def "B2B Admin updates a non-existing organizational customer #orgCustomerId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update a non-existing customer"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | patchBody
		"non-existing-id" | '{}'
	}

	def "B2B Admin updates organizational customer with invalid organizational unit #orgCustomerId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update customer with invalid organizational unit"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type }
		}

		where:
		orgCustomerId                          | type               | patchBody
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "ModelSavingError" | '{"orgUnit":{"uid":"invalid-id"}}'
	}

	def "B2B Admin updates organizational customer with invalid or empty request body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update customer with invalid request body"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message}
		}

		where:
		orgCustomerId                          | type               					| message 					| patchBody
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "HttpMessageNotReadableError" 			| "Invalid request body"	| '{"orgUnit":{"uid}}'
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "HttpMessageNotReadableError" 			| "Invalid request body"	| ''
	}

	def "B2B Admin updates a non-unique organizational customer #orgCustomerId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update a non-unique customer"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_CONFLICT
		}

		where:
		orgCustomerId                          | patchBody
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | '{"email":"oc.linda.wolf@rustic-hw.com"}'
	}

	def "B2B Admin updates an organizational customer with non-valid parameter #descriptior"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update a non-unique customer"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message && it.subject == subject }
		}

		where:
		descriptior          | orgCustomerId                          | subject     | message                     | type              | patchBody
		"invalid email"      | "0a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "email"     | "profile.email.invalid"     | "ValidationError" | '{"email": "invalid-email"}'
		"invalid title"      | "0a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "titleCode" | "profile.title.invalid"     | "ValidationError" | '{"titleCode": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only"}'
		"invalid first name" | "0a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "firstName" | "profile.firstName.invalid" | "ValidationError" | '{"firstName": ""}'
		"invalid last name"  | "0a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "lastName"  | "profile.lastName.invalid"  | "ValidationError" | '{"lastName": ""}'
		"invalid password"   | "0a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "password"  | "updatePwd.pwd.invalid"     | "ValidationError" | '{"password": "13245"}'
	}

	def "B2B Admin changes organizational customer password #orgCustomerId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to update customer"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def b2bCustomer = [
				'id'      : email,
				'password': password
		]

		authorizeCustomer(restClient, b2bCustomer) //authorization is successful

		then: "the request is successful"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NO_CONTENT
		}

		where:
		orgCustomerId                          | email                      | password | patchBody
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "aiden.long@rustic-hw.com" | "123456" | '{"password": "123456"}'
	}

	def "B2B Admin tries to create an organizational customer with existing e-mail"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create customer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_CONFLICT
			data.errors.find { it.type == type && it.message == message }
		}

		where:
		message               | type                 | postBody
		"User already exists" | "AlreadyExistsError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "oc.linda.wolf@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
	}

	def "B2B Admin tries to create organizational customer with invalid organizational unit"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create customer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type }
		}

		where:
		type               | postBody
		"ModelSavingError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen@rustic-hw.com", "orgUnit": { "uid": "invalid-org-unit" }, "roles":["b2badmingroup"]}'
	}

	def "B2B Admin tries to create organizational customer with invalid or empty post body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create customer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message }
		}

		where:
		type               				| message 					| postBody
		"HttpMessageNotReadableError" 	| "Invalid request body"	| '{"titleCode": "m, "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
		"HttpMessageNotReadableError" 	| "Invalid request body"	| ''
	}

	def "B2B Admin creates an organizational customer"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create customer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)
		then: "the request is successful"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_CREATED
			data.uid == email
			data.titleCode == titleCode
			data.firstName == firstName
			data.lastName == lastName
			data.orgUnit.uid == orgUnitUid
			data.roles.find { it == role }
		}

		where:
		titleCode | firstName | lastName | email                          | orgUnitUid      | role            | postBody
		"ms"      | "Lillian" | "Nguyen" | "lillian.nguyen@rustic-hw.com" | "Rustic_Retail" | "b2badmingroup" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
	}

	def "B2B Admin tries to create an organizational customer with invalid parameters: #descriptior"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create customer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)
		then: "the request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message && it.subject == subject }
		}

		where:
		descriptior          | subject     | message                     | type              | postBody
		"invalid email"      | "email"     | "profile.email.invalid"     | "ValidationError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "invalid-email", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
		"invalid title"      | "titleCode" | "profile.title.invalid"     | "ValidationError" | '{"titleCode": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only", "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen.2@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
		"invalid first name" | "firstName" | "profile.firstName.invalid" | "ValidationError" | '{"titleCode": "ms", "firstName": "", "lastName": "Nguyen", "email": "lillian.nguyen.2@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
		"invalid last name"  | "lastName"  | "profile.lastName.invalid"  | "ValidationError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "", "email": "lillian.nguyen.2@rustic-hw.com", "orgUnit": { "uid": "Rustic_Retail" }, "roles":["b2badmingroup"]}'
		"missing org unit"   | "orgUnit"   | "profile.orgUnit.invalid"   | "ValidationError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen.2@rustic-hw.com", "roles":["b2badmingroup"]}'
		"invalid org unit"   | "orgUnit"   | "profile.orgUnit.invalid"   | "ValidationError" | '{"titleCode": "ms", "firstName": "Lillian", "lastName": "Nguyen", "email": "lillian.nguyen.2@rustic-hw.com", "orgUnit": {}, "roles":["b2badmingroup"]}'

	}

	def "B2B Admin gets orgCustomer approvers"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get approvers"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "he/she gets approvers"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.users.size() > 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() > 0
		}

		where:
		orgCustomerId << ["6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"]
	}

	def "B2B Admin gets non-existing orgCustomer approvers"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get approvers"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND

		}

		where:
		orgCustomerId << ["non-existing-id"]
	}

	def "B2B Admin adds an approver to a orgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)
		def approversResult = getOrgCustomerApprovers(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request succeeds"
		with(response) {
			status == SC_OK
			compareB2BSelectionData(response.data, approverUid, true)
		}

		and: "he gets approvers"
		with(approversResult) {
			data.users.find { it.name == approverName && it.selected == true }
		}

		where:
		orgCustomerId                          | approverId                             | approverName	  | approverUid
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "OC Linda Wolf" | "oc.linda.wolf@rustic-hw.com"
	}

	def "B2B Admin adds an approver to a non-existing orgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | approverId
		"non-existing-id" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Admin adds a non-existing approver to an orgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId                          | approverId
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "non-existing-id"
	}

	def "B2B Admin removes an approver to a orgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)
		def approversResult = getOrgCustomerApprovers(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request succeeds"
		with(response) {
			status == SC_OK
			compareB2BSelectionData(response.data, approverUid, false)
		}

		and: "he gets approvers"
		with(approversResult) {
			data.users.find { it.name == approverName && it.selected == false }
		}

		where:
		orgCustomerId                          | approverId                             | approverName	  | approverUid
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "OC Linda Wolf" | "oc.linda.wolf@rustic-hw.com"
	}

	def "B2B Admin removes an approver from a non-existing orgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | approverId
		"non-existing-id" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Admin removes a non-existing approver from anorgCustomer: #orgCustomerId #approverId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH + "/" + approverId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId                          | approverId
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "non-existing-id"
	}

	def "B2B Admin gets orgCustomer orgUserGroups"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get orgUserGroups"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "he/she gets orgUserGroups"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.orgUnitUserGroups.size() > 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() > 0
		}

		where:
		orgCustomerId << ["6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"]
	}

	def "B2B Admin gets non-existing orgCustomer orgUserGroups"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get orgUserGroups"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId << ["not-existing-id"]
	}

	def "B2B Admin adds a orgUserGroup to a orgCustomer: #orgCustomerId #orgUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a orgUserGroup to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH + "/" + orgUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		def orgUserGroupsResult = getOrgCustomerOrgUserGroups(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request succeeds"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			compareB2BSelectionData(data, orgUserGroupId, true)
		}

		and: "new values are visible"
		with(orgUserGroupsResult) {
			status == SC_OK
			data.orgUnitUserGroups.find { it.name == orgUserGroupName && it.selected == true }
		}

		where:
		orgCustomerId                          | orgUserGroupId                     | orgUserGroupName
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "premiumPermissionsUserManagement" | "Premium Permissions"
	}

	def "B2B Admin adds a non-existing orgUserGroup to a orgCustomer: #orgCustomerId #orgUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a orgUserGroup to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH + "/" + orgUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId                          | orgUserGroupId
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "not-existing-group-id"
	}

	def "B2B Admin adds a orgUserGroup to a non-existing orgCustomer: #orgCustomerId #orgUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a orgUserGroup to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH + "/" + orgUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId  | orgUserGroupId
		"non-existing" | "premiumPermissionsUserManagement"
	}

	def "B2B Admin removes a orgUserGroup from a orgCustomer: #orgCustomerId #orgUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a orgUserGroup from an orgCustomer"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH + "/" + orgUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		def orgUserGroupsResult = getOrgCustomerOrgUserGroups(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request is successful"
		with(response) {
			status == SC_OK
		}

		and: "user group is removed"
		with(orgUserGroupsResult) {
			status == SC_OK
			data.orgUnitUserGroups.find { it.name == orgUserGroupName && it.selected == false }
		}

		where:
		orgCustomerId                          | orgUserGroupId                     | orgUserGroupName
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "premiumPermissionsUserManagement" | "Premium Permissions"
	}

	def "B2B Admin removes a orgUserGroup from a non-existing orgCustomer: #orgCustomerId #orgUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a orgUserGroup from an orgCustomer"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH + "/" + orgUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | orgUserGroupId
		"non-existing-id" | "premiumPermissionsUserManagement"
	}

	def "B2B Admin gets orgCustomer permissions"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get permissions"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "he/she gets permissions"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.orderApprovalPermissions.size() > 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() > 0
		}

		where:
		orgCustomerId << ["6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"]
	}

	def "B2B Admin gets non-existing orgCustomer permissions"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get permissions"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH,
				contentType: JSON,
				requestContentType: URLENC)

		then: "request fails"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId << ["not-existing-id"]
	}

	def "B2B Admin adds a permission to a orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a permission to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionCode,
				contentType: JSON,
				requestContentType: JSON)

		def permissionsResult = getOrgCustomerPermissions(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request succeeds"
		with(response) {
			status == SC_OK
			compareB2BSelectionData(data, permissionCode, true)
		}

		and: "he gets permissions result"
		with(permissionsResult) {
			status == SC_OK
			data.orderApprovalPermissions.find { it.code == permissionCode && it.selected == true }
		}

		where:
		orgCustomerId                          | permissionCode
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "Rustic_0K_USD_ORDER"
	}

	def "B2B Admin adds a non-existing permission to a orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a permission to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId                          | permissionId
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "not-existing-permission"
	}

	def "B2B Admin adds a permission to a non-existing orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "user requests to add a permission to a orgCustomer"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionId,
				contentType: JSON,
				requestContentType: JSON)

		then: "user gets permissions"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | permissionId
		"non-existing-id" | "Rustic 0K USD ORDER"
	}

	def "B2B Admin removes a permission from a orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a permission from an orgCustomer"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionCode,
				contentType: JSON,
				requestContentType: JSON)

		def permissionsResult = getOrgCustomerPermissions(restClient, b2bAdminCustomer, orgCustomerId)

		then: "request is successful"
		with(response) {
			status == SC_OK
			compareB2BSelectionData(data, permissionCode, false)
		}

		and: "permission is removed"
		with(permissionsResult) {
			status == SC_OK
			data.orderApprovalPermissions.find { it.code == permissionCode && it.selected == false }
		}

		where:
		orgCustomerId                          | permissionCode
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "Rustic_0K_USD_ORDER"
	}

	def "B2B Admin removes a non-existing permission from a orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a permission from an orgCustomer"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId                          | permissionId
		"6a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "not-existing-permission"
	}

	def "B2B Admin removes a permission from a non-existing orgCustomer: #orgCustomerId #permissionId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a permission from an orgCustomer"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH + "/" + permissionId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_NOT_FOUND
		}

		where:
		orgCustomerId     | permissionId
		"not-existing-id" | "Rustic 0K USD ORDER"
	}

	protected getOrgCustomer(RESTClient client, customer, orgCustomerId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response;
	}

	protected getOrgCustomerApprovers(RESTClient client, customer, orgCustomerId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + APPROVERS_PATH,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response;
	}

	protected getOrgCustomerOrgUserGroups(RESTClient client, customer, orgCustomerId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + ORG_USER_GROUPS_PATH,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response;
	}

	protected getOrgCustomerPermissions(RESTClient client, customer, orgCustomerId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORG_CUSTOMERS_PATH + "/" + orgCustomerId + PERMISSIONS_PATH,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		return response;
	}

	protected void compareB2BSelectionData(Object selectionData, String code, boolean selected) {
		assert selectionData.id == code
		assert selectionData.selected == selected
	}
}
