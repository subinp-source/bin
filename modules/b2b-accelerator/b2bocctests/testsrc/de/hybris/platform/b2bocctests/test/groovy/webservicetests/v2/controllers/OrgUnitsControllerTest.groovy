/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import static groovyx.net.http.ContentType.JSON
import static org.apache.http.HttpStatus.*
import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Unroll

@ManualTest
@Unroll
class OrgUnitsControllerTest extends AbstractUserTest {
	static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
	static final String B2BADMIN_PASSWORD = "1234"

	static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
	static final String B2BCUSTOMER_PASSWORD = "1234"

	static final ORGANIZATIONAL_UNITS_PATH = "/orgUnits"

	static final PAGE_SIZE = 20

	def "B2B Admin gets the root organizational unit node"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get the root organizational unit node of his organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgUnitsRootNodeTree',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the root organizational unit node"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.id == orgUnitId
			data.name == name
			data.active == active
			data.children.find {
				it.id == childrenId && it.name == childrenName && it.active == childrenActive && it.parent == orgUnitId
			}
		}
		where:
		orgUnitId | name     | active | childrenId      | childrenName    | childrenActive
		"Rustic"  | "Rustic" | true   | "Rustic_Retail" | "Rustic Retail" | true
	}

	def "B2B Customer tries to get the root organizational unit node"() {
		given: "a registered and logged in B2B customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to the root organizational unit node"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + '/orgUnitsRootNodeTree',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_UNAUTHORIZED
		}
	}

	def "B2B Admin gets a specific organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specific organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId,
				query: [
						'fields': FIELD_SET_LEVEL_FULL
				],
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets a specific organizational unit"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			compareUnitDetails(data, orgUnitId, name, parent, active, approvalProcessCode)
			compareUnitAddress(data, firstName, lastName, line1, town, postalCode)
			compareUnitRoles(data, approversUid, managersUid, administratorsUid, customersUid)
			if ( orgUnitId == "Rustic" ) {
				data.costCenters != null
			}
		}

		where:
		orgUnitId       | name            | parent   | active | firstName | lastName  | line1                    | town       | postalCode | approversUid                  | managersUid                | administratorsUid          | customersUid                | approvalProcessCode
		"Rustic"        | "Rustic"        | null     | true   | "Hanna"   | "Schmidt" | "999 South Wacker Drive" | "Chicago"  | "60606"    | "hanna.schmidt@rustic-hw.com" | null                       | "linda.wolf@rustic-hw.com" | "mark.rivers@rustic-hw.com" | "accApproval"
		"Rustic_Retail" | "Rustic Retail" | "Rustic" | true   | "James"   | "Bell"    | "700 E 50th Street"      | "New York" | "10022"    | "james.bell@rustic-hw.com"    | "anil.gupta@rustic-hw.com" | null                       | null                        | "accApproval"
	}

	def "B2B Admin tries to get a non-existing organizational unit"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a non-existing organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		orgUnitId << [
				"Non_Existing_Organizational_Unit"
		]
	}

	def "B2B Admin tries to get a specific organizational unit from another organizational unit"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specific organizational unit from another organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_NOT_FOUND
		}

		where:
		orgUnitId << ["Pronto"]
	}

	def "B2B Customer tries to get a specific organizational unit"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get a specific organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId << ["Rustic"]
	}


	def "B2B Admin edits a specific organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a specific organizational unit"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitIdInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "his changes are successfully saved"
		with(response) { status == SC_NO_CONTENT }

		and: "new values are visible"
		with(updatedInfo) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			compareUnitDetails(data, orgUnitId, name, parent, active, approvalProcessCode)
		}

		where:
		orgUnitIdInRequest | orgUnitId              | name              | parent   | active | approvalProcessCode | patchBody
		"Rustic"           | "Rustic"               | "Rustic2"         | null     | true   | "accApproval"       | '{"name": "Rustic2"}'
		"Rustic_Custom"    | "Rustic_Custom_Edited" | "Rustic Custom"   | "Rustic" | true   | "accApproval"       | '{"uid": "Rustic_Custom_Edited"}'
		"Rustic_Custom_2"  | "Rustic_Custom_2"      | "Rustic Custom 2" | "Rustic" | true   | "accApproval"       | '{"parentOrgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin gets a list of available parent units for the given unit based on id"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get available parent unit nodes"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + "/availableParents",
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets available parent unit nodes"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.unitNodes.find {
				it.id == availableParentId && it.parent == parentName
			}
		}
		where:
		orgUnitId          | availableParentId | parentName
		"Rustic_Custom_3"  | "Rustic_Custom_4" | "Rustic"
	}

	def "B2B Admin tries to get available parent units for a non-existing unit"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get available parent unit nodes"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + "/availableParents",
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
		}


		where:
		orgUnitId << ["Non_Existing_Unit"]
	}

	def "B2B Admin tries to edit a non-existing organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a non-existing organizational unit"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				body: patchBody,
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
		orgUnitId           | patchBody
		"Non_Existing_Unit" | '{"uid": "Non_Existing_Unit_2"}'
	}

	def "B2B Customer tries to edit a specific organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit:"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId | patchBody
		"Rustic"  | '{"uid": "Rustic_2"}'
	}

	def "B2B Admin tries to edit a specific organizational unit with non-valid attributes: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit with non-valid attributes"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
		}

		where:
		orgUnitId | descriptor   | patchBody
		"Rustic"  | "Short_Id"   | '{"uid": ""}'
		"Rustic"  | "Short_Name" | '{"name": ""}'
	}

	def "B2B Admin tries to edit a specific organizational unit with non-valid or empty patch body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit with non-valid patch body"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message}
		}

		where:
		orgUnitId |	type               				| message 					| patchBody
		"Rustic"  | "HttpMessageNotReadableError" 	| "Invalid request body"	| '{"uid":}'
		"Rustic"  | "HttpMessageNotReadableError" 	| "Invalid request body"	| ''
	}

	def "B2B Admin enables/disables a specific organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to enable/disable a specific organizational unit by editing the organizational unit"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "activation/deactivation is successfully applied"
		with(response) { status == SC_NO_CONTENT }

		and: "activation/deactivation is visible"
		with(updatedInfo) {
			compareUnitDetails(data, orgUnitId, name, parent, active, approvalProcessCode)
		}

		where:
		orgUnitIdInRequest | orgUnitId         | name              | parent   | active | approvalProcessCode | patchBody
		"Rustic_Custom_3"  | "Rustic_Custom_3" | "Rustic Custom 3" | "Rustic" | true   | "accApproval"       | '{"active": "true"}'
		"Rustic_Custom_4"  | "Rustic_Custom_4" | "Rustic Custom 4" | "Rustic" | false  | "accApproval"       | '{"active": "false"}'
	}

	def "B2B Admin creates a new organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the newly created specific organizational unit"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_CREATED

			compareUnitDetails(data, orgUnitId, name, parent, active, approvalProcessCode)
		}

		where:
		orgUnitId         | name              | parent          | active | approvalProcessCode | postBody
		"Rustic_Custom_5" | "Rustic Custom 5" | "Rustic_Retail" | true   | "accApproval"       | '{"approvalProcess": {"code": "accApproval"}, "uid": "Rustic_Custom_5", "name": "Rustic Custom 5", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
	}

	def "B2B Admin tries to create a new organizational unit with an already existing unit id: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit with an already existing unit id"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH,
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
		orgUnitId       | postBody
		"Rustic_Retail" | '{"approvalProcess": {"code": "accApproval"}, "uid": "Rustic_Retail", "name": "Rustic Retail", "parentOrgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin tries to create a new organizational unit with non-valid attributes: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit with non-valid attributes"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
		}

		where:
		orgUnitId           | message                                                   | reason    | subject | subjectType | type              | postBody
		"Rustic_No_Id"      | "Please select a unique identifier for the business unit" | "missing" | "uid"   | "parameter" | "ValidationError" | '{"approvalProcess": {"code": "accApproval"}, "name": "Rustic No Id", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
		"Rustic_Short_Id"   | "Please select a unique identifier for the business unit" | "invalid" | "uid"   | "parameter" | "ValidationError" | '{"approvalProcess": {"code": "accApproval"}, "uid": "", "name": "Rustic Short Id", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
		"Rustic_No_Name"    | "Please select a name for the business unit"              | "missing" | "name"  | "parameter" | "ValidationError" | '{"approvalProcess": {"code": "accApproval"}, "uid": "Rustic_No_Name", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
		"Rustic_Short_Name" | "Please select a name for the business unit"              | "invalid" | "name"  | "parameter" | "ValidationError" | '{"approvalProcess": {"code": "accApproval"}, "uid": "Rustic_Short_Name", "name": "", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
	}

	def "B2B Admin tries to create a new organizational unit with non-valid or empty post body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit with non-valid post body"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message}
		}

		where:
		orgUnitId       |  type               				| message					| postBody
		"Rustic_Retail" | "HttpMessageNotReadableError" 	| "Invalid request body"	| '{"approvalProcess": {"code" accApproval"}, "uid": "Rustic_Retail", "name": "Rustic Retail", "parentOrgUnit": {"uid": "Rustic}}'
		"Rustic_Retail" | "HttpMessageNotReadableError" 	| "Invalid request body"	| ''
	}

	def "B2B Customer tries to create a new order approval permission: #orgUnitId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to create a new organizational unit"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId         | postBody
		"Rustic_Retail_5" | '{"approvalProcess": {"code": "accApproval"}, "uid": "Rustic_Retail_5", "name": "Rustic Retail", "parentOrgUnit": {"uid": "Rustic_Retail"}}'
	}

	def "B2B Admin gets available approval processes"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get the root organizational unit node of his organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgUnitsAvailableApprovalProcesses',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets available approval processes"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.approvalProcesses.find { it.code == approvalProcessCode && it.name == approvalProcessName }
		}
		where:
		approvalProcessCode | approvalProcessName
		"accApproval"       | "Escalation Approval with Merchant Check"
	}

	def "B2B Customer tries to get available approval processes"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get the root organizational unit node of his organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + '/orgUnitsAvailableApprovalProcesses',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}
	}

	def "B2B Admin gets available organizational unit nodes"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get available organizational unit nodes"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/availableOrgUnitNodes',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets available organizational unit nodes"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.unitNodes.find {
				it.id == childrenId && it.name == childrenName && it.active == childrenActive && it.parent == orgUnitId
			}
		}
		where:
		orgUnitId | name     | active | childrenId      | childrenName    | childrenActive
		"Rustic"  | "Rustic" | true   | "Rustic_Retail" | "Rustic Retail" | true
	}

	def "B2B Customer tries to get available organizational unit nodes"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get available organizational unit nodes"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + '/availableOrgUnitNodes',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}
	}

	def "B2B Customer tries to get an organizational unit's addresses: #orgUnitId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get available organizational unit's addresses"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/addresses',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			status == SC_UNAUTHORIZED
		}
		where:
		orgUnitId << ["Rustic"]
	}

	def "B2B Admin gets an organizational unit's addresses: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get organizational unit addresses"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/addresses',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the addresss"
		with(response) {
			status == SC_OK
			data.addresses.find {
				it.firstName == firstName && it.lastName == lastName && it.formattedAddress == formattedAddress && it.defaultAddress == defaultAddress && it.postalCode == postalCode && it.town == town
			}
		}
		where:
		orgUnitId | firstName | lastName 		| formattedAddress     										| defaultAddress | postalCode | town
		"Rustic" 	| "Hanna"  	| "Schmidt"	| "999 South Wacker Drive, Chicago, 60606"  | false				   | "60606" 		| "Chicago"
	}

	def "B2B Admin creates a new address for the organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new address"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/addresses',
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the newly created specific organizational unit"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_CREATED

			compareAddress(data, firstName, lastName, line1, town, postalCode)
		}

		where:
		orgUnitId | firstName | lastName | line1          | town    | postalCode | postBody
		"Rustic"  | "Carla"   | "Torres" | "Bagby Street" | "Texas" | "1000"     | '{"titleCode": "ms", "firstName":"Carla", "lastName":"Torres", "line1": "Bagby Street", "town":"Texas", "postalCode": "1000", "country": {"isocode": "US"}}'
	}

	def "B2B Customer tries to create a new address for the organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to create a new address"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/addresses',
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId | postBody
		"Rustic"  | '{"titleCode": "ms", "firstName":"Carla", "lastName":"Torres", "line1": "Bagby Street", "town":"Texas", "postalCode": "1000", "country": {"isocode": "US"}}'
	}

	def "B2B Admin tries to create a new address for the organizational unit with non-valid post body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new address"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/addresses',
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message}
		}

		where:
		orgUnitId | type               				| message 						|	postBody
		"Rustic"  | "HttpMessageNotReadableError" 	| "Invalid request body"		| 	'{"titleCode: , "firstName":"Carla", "lastName":"Torres", "line1": "Bagby Street", "town":"Texas", "postalCode": "1000", "country": {"isocode": "US"}}'
		"Rustic"  | "HttpMessageNotReadableError" 	| "Invalid request body"		|	''
	}


	def "B2B Admin edits a specific address from an organizational unit: #line1"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId).data

		def addressId = currentUnit.addresses.find { it.line1 == originalLine1 }.id

		when: "he requests to edit a specific address"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/addresses/' + addressId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "his changes are successfully saved"
		with(response) { status == SC_NO_CONTENT }

		and: "new values are visible"
		with(updatedInfo) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			compareUnitAddress(data, firstName, lastName, line1, town, postalCode)
		}

		where:
		orgUnitId | firstName | lastName | originalLine1         | line1                 | town            | postalCode | patchBody
		"Rustic"  | "Carla"   | "Torres" | "Bagby Street"        | "Bagby Street 2"      | "California"    | "10200"    | '{"titleCode": "ms", "firstName":"Carla", "lastName":"Torres", "line1": "Bagby Street 2", "town":"California", "postalCode": "10200", "country": {"isocode": "US"}}'
		"Rustic"  | "Carla"   | "Torres" | "3000 Lombard Street" | "3000 Lombard Street" | "San Francisco" | "94123"    | '{"firstName":"Carla", "lastName":"Torres"}}'
	}

	def "B2B Customer tries to edit a specific address from an organizational unit: #line1"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific address"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/addresses/' + addressId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId | addressId  | patchBody
		"Rustic"  | "randomId" | '{"titleCode": "ms", "firstName":"Carla", "lastName":"Torres", "line1": "Bagby Street 2", "town":"California", "postalCode": "10200", "country": {"isocode": "US"}}'
	}

	def "B2B Customer tries to edit a specific address from an organizational unit with non-valid patch body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a specific address"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/addresses/' + addressId,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_BAD_REQUEST
			data.errors.find { it.type == type && it.message == message}
		}

		where:
		orgUnitId |  type               			|	addressId  | message 					| patchBody
		"Rustic"  |  "HttpMessageNotReadableError" 	|	"randomId" | "Invalid request body"		| '{"titleCode":, "firstName":"Carla, "lastName":"Torres", "line1": "Bagby Street 2", "town":"California", "postalCode": "10200", "country": {"isocode": "US"}}'
		"Rustic"  |  "HttpMessageNotReadableError" 	|	"randomId" | "Invalid request body"		| ''
	}

	def "B2B Admin removes a specific address from an organizational unit: #line1"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId).data

		def addressId = currentUnit.addresses.find { it.line1 == line1 }.id

		when: "he requests to remove an address from an organizational unit"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/addresses/' + addressId,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "the address is successfully removed"
		with(response) { status == SC_OK }

		and: "the address is removed"
		with(updatedInfo) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.addresses.find { it.line1 == line1 } == null
		}

		where:
		orgUnitId | line1
		"Rustic"  | "999 South Wacker Drive"
	}

	def "B2B Customer tries to remove a specific address from an organizational unit: #orgUnitId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/addresses/' + addressId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}
		where:
		orgUnitId | addressId
		"Rustic"  | "randomId"
	}

	def "B2B Admin gets users related to an organizational unit based on role: #orgUnitId #roleId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get users related to an organizational unit based on role"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/availableUsers/' + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets users"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK

			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0

			data.users.find { it.uid == selectedCustomerId && it.selected == true }
			data.users.find { it.uid == nonSelectedCustomerId && it.selected == false }

			if (nonAvailableCustomerId != null) {
				assert data.users.find { it.uid == nonAvailableCustomerId } == null
			}
		}
		where:
		orgUnitId | selectedCustomerId             | nonSelectedCustomerId         | nonAvailableCustomerId     | roleId
		"Rustic"  | "hanna.schmidt@rustic-hw.com"  | "james.bell@rustic-hw.com"    | null                       | "b2bapprovergroup"
		"Rustic"  | "linda.wolf@rustic-hw.com"     | "hanna.schmidt@rustic-hw.com" | "james.bell@rustic-hw.com" | "b2badmingroup"
		"Rustic"  | "akiro.nakamura@rustic-hw.com" | "linda.wolf@rustic-hw.com"    | "james.bell@rustic-hw.com" | "b2bmanagergroup"
		"Rustic"  | "mark.rivers@rustic-hw.com"    | "hanna.schmidt@rustic-hw.com" | "james.bell@rustic-hw.com" | "b2bcustomergroup"
	}

	def "B2B Customer gets users related to an organizational unit based on role"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get users related to an organizational unit based on role"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/availableUsers/' + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}
		where:
		orgUnitId | roleId
		"Rustic"  | "b2bapprovergroup"
	}

	def "B2B Admin tries to get users related to an organizational unit based on role with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get users related to an organizational unit based on role"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/availableUsers/' + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request"
		with(response) {
			status == SC_BAD_REQUEST
		}
		where:
		descriptor           | orgUnitId           | roleId
		"Non_Existing_Unit"  | "Non_Existing_Unit" | "b2bmanagergroup"
		"Non_Existing_Group" | "Rustic"            | "Non_Existing_Group"
	}

	def "B2B Admin removes a role from a specific user: #orgUnitId #userId #roleId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)

		def updatedUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "the operation is successful"
		with(response) { status == SC_OK }

		and: "the user was assigned to the role"
		with(currentUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId }
		}
		and: "the role is removed from the user"
		with(updatedUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId } == null
		}

		where:
		orgUnitId       | userId                             | customerId                             | roleId             | roleKey
		"Rustic_Retail" | "linda.wolf@rustic-retail-hw.com"  | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a20" | "b2badmingroup"    | "administrators"
		"Rustic_Retail" | "anil.gupta@rustic-hw.com"         | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "b2bmanagergroup"  | "managers"
		"Rustic_Retail" | "mark.rivers@rustic-retail-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a26" | "b2bcustomergroup" | "customers"
	}

	def "B2B Customer tries to remove a role from a specific user"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId       | customerId                             | roleId
		"Rustic_Retail" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bcustomergroup"
	}

	def "B2B Admin tries to remove a specific user from a role with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) { status == SC_BAD_REQUEST }

		where:
		descriptor           | customerId                             | roleId
		"Non_Existing_Group" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "Non_Existing_Group"
		"Non_Existing_User"  | "non-existing-user@rustic-hw.com"      | "b2bmanagergroup"
	}

	def "B2B Admin adds a role to a user: #orgUnitId #userId #roleId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)

		def updatedUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "the operation is successful"
		with(response) {
			status == SC_CREATED
			compareB2BSelectionData(data, userId, true)
		}

		and: "the role was not added to the user"
		with(currentUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId } == null
		}

		then: "the role is added to the user"
		with(updatedUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK

			data[roleKey].find { it.uid == userId }
		}

		where:
		orgUnitId       | userId                     | customerId                             | roleId             | roleKey
		"Rustic_Retail" | "anil.gupta@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "b2badmingroup"    | "administrators"
		"Rustic_Retail" | "james.bell@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bmanagergroup"  | "managers"
		"Rustic_Retail" | "james.bell@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bcustomergroup" | "customers"
	}

	def "B2B Customer tries to add a role to a user"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)


		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId       | userId                     | customerId                             | roleId
		"Rustic_Retail" | "anil.gupta@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "b2bcustomergroup"
	}

	def "B2B Admin tries to add a role to a user with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) { status == SC_BAD_REQUEST }

		where:
		descriptor           | customerId                             | roleId
		"Non_Existing_Group" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "Non_Existing_Group"
		"Non_Existing_User"  | "non-existing-user@rustic-hw.com"      | "b2bapprovergroup"
	}

	def "B2B Admin removes a specific user from an organizational unit related role: #orgUnitId #userId #roleId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)

		def updatedUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "the operation is successful"
		with(response) { status == SC_OK }

		and: "the user was assigned to the role"
		with(currentUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId }
		}
		and: "the role is removed from the user"
		with(updatedUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId } == null
		}

		where:
		orgUnitId       | userId                     | customerId                             | roleId             | roleKey
		"Rustic_Retail" | "james.bell@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bapprovergroup" | "approvers"
	}

	def "B2B Customer tries to remove a specific user from a  organizational unit dependent role"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId       | customerId                             | roleId
		"Rustic_Retail" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bapprovergroup"
	}

	def "B2B Admin tries to remove a specific user from an organizational unit dependent role with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + "/" + orgUnitId + '/orgCustomers/' + customerId + "/roles/" + roleId,
				contentType: JSON,
				requestContentType: JSON)

		then: "the operation is unsuccessful"
		with(response) { status == SC_BAD_REQUEST }

		where:
		descriptor           | orgUnitId           | customerId                             | roleId
		"Non_Existing_Unit"  | "Non_Existing_Unit" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bapprovergroup"
		"Non_Existing_Group" | "Rustic"            | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "Non_Existing_Group"
		"Non_Existing_User"  | "Rustic_Retail"     | "non-existing-user@rustic-hw.com"      | "b2bapprovergroup"
	}

	def "B2B Admin adds an organizational unit dependent role to a user: #orgUnitId #userId #roleId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		def currentUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)

		def updatedUnit = getOrganizationalUnit(restClient, b2bAdminCustomer, orgUnitId)

		then: "the operation is successful"
		with(response) { status == SC_CREATED }

		and: "the role was not added to the user"
		with(currentUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data[roleKey].find { it.uid == userId } == null
		}

		then: "the role is added to the user"
		with(updatedUnit) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK

			data[roleKey].find { it.uid == userId }
		}

		where:
		orgUnitId       | userId                     | customerId                             | roleId             | roleKey
		"Rustic_Retail" | "anil.gupta@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "b2bapprovergroup" | "approvers"
	}

	def "B2B Customer tries to add an organizational unit dependent role role to a user"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)


		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}

		where:
		orgUnitId       | userId                     | customerId                             | roleId
		"Rustic_Retail" | "anil.gupta@rustic-hw.com" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a24" | "b2bapprovergroup"
	}

	def "B2B Admin tries to add an organizational unit dependent role to a user with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a role to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId + '/orgCustomers/' + customerId + "/roles",
				query: ["roleId": roleId],
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) { status == SC_BAD_REQUEST }

		where:
		descriptor           | orgUnitId           | customerId                             | roleId
		"Non_Existing_Unit"  | "Non_Existing_Unit" | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "b2bapprovergroup"
		"Non_Existing_Group" | "Rustic"            | "1a2a41a3-c54c-4ce8-a2d2-0324e1c32a22" | "Non_Existing_Group"
		"Non_Existing_User"  | "Rustic_Retail"     | "non-existing-user@rustic-hw.com"      | "b2bapprovergroup"
	}

	protected void compareUnitDetails(responseData, id, name, parent, active, approvalProcessCode) {
		assert responseData.uid == id
		assert responseData.name == name
		assert responseData.approvalProcess.code == approvalProcessCode
		if (parent != null) {
			assert responseData.parentOrgUnit.uid == parent
		}
		assert responseData.active == active
	}

	protected void compareUnitAddress(responseData, firstName, lastName, line1, town, postalCode) {
		assert responseData.addresses.find {
			it.firstName == firstName && it.lastName == lastName && it.line1 == line1 && it.town == town && it.postalCode == postalCode
		}
	}

	protected void compareAddress(responseData, firstName, lastName, line1, town, postalCode) {
		assert responseData.firstName == firstName
		assert responseData.lastName == lastName
		assert responseData.line1 == line1
		assert responseData.town == town
		assert responseData.postalCode == postalCode
	}

	protected void compareUnitRoles(responseData, approversUid, managersUid, administratorsUid, customersUid) {
		if (approversUid != null) {
			assert responseData.approvers.find { it.uid == approversUid }
		}
		if (managersUid != null) {
			assert responseData.managers.find { it.uid == managersUid }
		}
		if (administratorsUid != null) {
			assert responseData.administrators.find { it.uid == administratorsUid }
		}
		if (customersUid != null) {
			assert responseData.customers.find { it.uid == customersUid }
		}
	}

	protected getOrganizationalUnit(RESTClient client, customer, orgUnitId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORGANIZATIONAL_UNITS_PATH + '/' + orgUnitId,
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

