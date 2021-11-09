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
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class OrgUnitUserGroupsControllerTest extends AbstractUserTest {
	static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
	static final String B2BADMIN_PASSWORD = "1234"

	static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
	static final String B2BCUSTOMER_PASSWORD = "1234"

	static final ORGANIZATIONAL_USER_GROUPS_PATH = "/orgUnitUserGroups"

	static final PAGE_SIZE = 20

	def "B2B Admin gets organizational unit user groups"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get organizational unit user groups available to his organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets organizational unit user groups"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.orgUnitUserGroups.size() >= 0
			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0
			data.pagination.totalResults.toInteger() >= 0
		}
	}

	def "B2B Customer tries to get organizational unit user groups"() {
		given: "a registered and logged in B2B customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get organizational unit user groups"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets unauthorized error"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_UNAUTHORIZED
		}
	}

	def "B2B Admin gets a specific organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specific organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId,
				query: [
						'fields': FIELD_SET_LEVEL_FULL
				],
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets a specific organizational unit user group"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.uid == orgUnitUserGroupId
			data.name == name
			data.orgUnit.uid == orgUnitId
			data.permissions.find { it.code == permissionCode }
			data.members.find { it.uid == membersUid }
		}

		where:
		orgUnitUserGroupId    | name                   | orgUnitId | permissionCode        | membersUid
		"limitedPermissions"  | "Limited Permissions"  | "Rustic"  | "Rustic_0K_USD_ORDER" | "anthony.lombardi@rustic-hw.com"
		"standardPermissions" | "Standard Permissions" | "Rustic"  | "Rustic_5K_USD_ORDER" | "william.hunter@rustic-hw.com"

	}

	def "B2B Admin tries to get a non-existing organizational unit user group"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a non-existing organizational unit organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId,
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
		orgUnitUserGroupId << ["Non_Existing_Organizational_Unit_User_Group"]
	}

	def "B2B Admin tries to get a specific organizational unit user group from another organizational unit"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get a specificorganizational unit user group from another organizational unit"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId,
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
		orgUnitUserGroupId << ["enhancedPermissions"]
	}

	def "B2B Customer tries to get a specific organizational unit user group"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get a specific organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId,
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
		orgUnitUserGroupId << ["enhancedPermissions"]
	}

	def "B2B Admin edits a specific organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to edit a specific organizational unit user group"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupIdInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		def updatedInfo = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		then: "his changes are successfully saved"
		with(response) { status == SC_NO_CONTENT }

		and: "new values are visible"
		with(updatedInfo) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			data.uid == orgUnitUserGroupId
			data.name == name
			data.orgUnit.uid == parentOrgUnitId
		}

		where:
		orgUnitUserGroupIdInRequest | orgUnitUserGroupId          | name                    | parentOrgUnitId | patchBody
		"premiumPermissions"        | "premiumPermissions_Edited" | "Premium Permissions 2" | "Rustic_Retail" | '{"uid": "premiumPermissions_Edited", "name": "Premium Permissions 2", "orgUnit": {"uid": "Rustic_Retail"}}'
	}

	def "B2B Customer tries to edit a specific organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit user group:"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupIdInRequest,
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
		orgUnitUserGroupIdInRequest | patchBody
		"standardPermissions"       | '{"uid": "standardPermissions_2"}'
	}

	def "B2B Admin tries to edit a specific organizational unit user group with non-valid attributes: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit user group with non-valid attributes"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupIdInRequest,
				body: patchBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == statusCode
			data.errors.find { it.message == message && it.type == type }
		}

		where:

		orgUnitUserGroupIdInRequest | descriptor    | statusCode     | message                                                | type                 | patchBody
		"standardPermissions"       | "Short_Id"    | SC_BAD_REQUEST | "Please select a unique identifier for the user group" | "ValidationError"    | '{"uid": ""}'
		"standardPermissions"       | "Short_Name"  | SC_BAD_REQUEST | "Please select a name for the user group"              | "ValidationError"    | '{"name": ""}'
		"standardPermissions"       | "Existing_Id" | SC_CONFLICT    | "Member Permission with the same id already exists"    | "AlreadyExistsError" | '{"uid": "limitedPermissions"}'
	}

	def "B2B Admin tries to edit a specific organizational unit user group with non-valid or empty patch body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to edit a specific organizational unit user group with non-valid patch body"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupIdInRequest,
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

		orgUnitUserGroupIdInRequest |	type                 				| message 					| patchBody
		"standardPermissions"       | "HttpMessageNotReadableError" 		| "Invalid request body"	| '{"uid": "}'
		"standardPermissions"       | "HttpMessageNotReadableError" 		| "Invalid request body"	| ''
	}

	def "B2B Admin removes all members of a specific organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to remove all members of a specific organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/members",
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		then: "request succeds"
		with(response) { status == SC_OK }

		and: "the user group had members"
		with(currentOrgUnitUserGroup) {
			status == SC_OK
			data.members.size() > 0
		}
		and: "the user group doesn't have members anymore"
		with(updatedOrgUnitUserGroup) {
			status == SC_OK
			data.members == null
		}

		where:
		orgUnitUserGroupId << ["goldPremiumPermissions"]
	}

	def "B2B Admin tries to remove all members of a non-existing organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove all members of a non-existing organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/members",
				contentType: JSON,
				requestContentType: JSON)


		then: "request fails"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}


		where:
		orgUnitUserGroupId   | message                                                   | type
		"Non_Existing_Group" | "UserGroupModel with uid 'Non_Existing_Group' not found!" | "UnknownIdentifierError"
	}

	def "B2B Customer tries to remove all members of a specific organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to remove all members of a specific organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/members",
				contentType: JSON,
				requestContentType: JSON)


		then: "request fails"
		with(response) { status == SC_UNAUTHORIZED }

		where:
		orgUnitUserGroupId << ["premiumPermissions"]
	}

	def "B2B Admin deletes an existing organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to delete an existing organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		then: "the operation is successful"
		with(response) {
			status == SC_OK
		}
		and: "the user group existed"
		with(currentOrgUnitUserGroup) {
			status == SC_OK
		}
		and: "the user group does not exist anymore"
		with(updatedOrgUnitUserGroup) {
			status == SC_NOT_FOUND
		}

		where:
		orgUnitUserGroupId << ["goldPremiumPermissions"]
	}

	def "B2B Admin tries deletes a non-existing organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to delete an existing organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		orgUnitUserGroupId        | message                                                        | type
		"not_existing_user_group" | "UserGroupModel with uid 'not_existing_user_group' not found!" | "UnknownIdentifierError"
	}

	def "B2B Customer tries to delete an existing organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to delete an existing organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId,
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails"
		with(response) {
			status == SC_UNAUTHORIZED
		}
		where:
		orgUnitUserGroupId << ["premiumPermissions"]
	}

	def "B2B Admin creates a new organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit user group"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets the newly created specific organizational unit user group"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_CREATED

			data.uid == orgUnitUserGroupId
			data.name == name
			data.orgUnit.uid == orgUnitId
		}

		where:
		orgUnitUserGroupId | name               | orgUnitId | postBody
		"testPermissions"  | "Test Permissions" | "Rustic"  | '{"uid": "testPermissions", "name": "Test Permissions","orgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin tries to create a new organizational unit user group with an already existing organizational unit user group id: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit user group with an already existing organizational unit user group id"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_CONFLICT
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		orgUnitUserGroupId    | message                                             | type                 | postBody
		"standardPermissions" | "Member Permission with the same id already exists" | "AlreadyExistsError" | '{"uid": "standardPermissions", "name": "Standard Permissions","orgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin tries to create a new organizational unit user group with non-valid attributes: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit user group with non-valid attributes"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
				body: postBody,
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request response"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == statusCode
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		descriptor                 | statusCode     | message                                                | type                 | postBody
		"UserGroup_No_Id"          | SC_BAD_REQUEST | "Please select a unique identifier for the user group" | "ValidationError"    | '{"name": "Standard Permissions","orgUnit": {"uid": "Rustic"}}'
		"UserGroup_Short_Id"       | SC_BAD_REQUEST | "Please select a unique identifier for the user group" | "ValidationError"    | '{"uid": "", "name": "Standard Permissions","orgUnit": {"uid": "Rustic"}}'
		"UserGroup_No_Name"        | SC_BAD_REQUEST | "Please select a name for the user group"              | "ValidationError"    | '{"uid": "standardPermissions","orgUnit": {"uid": "Rustic"}}'
		"UserGroup_Short_Name"     | SC_BAD_REQUEST | "Please select a name for the user group"              | "ValidationError"    | '{"uid": "standardPermissions", "name": "","orgUnit": {"uid": "Rustic"}}'
		"UserGroup_No_Parent_Unit" | SC_BAD_REQUEST | "Please select a Parent Business unit"                 | "ValidationError"    | '{"uid": "standardPermissions", "name": "Standard Permissions"}'
		"UserGroup_Already_Exists" | SC_CONFLICT    | "Member Permission with the same id already exists"    | "AlreadyExistsError" | '{"uid": "standardPermissions", "name": "Standard Permissions","orgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin tries to create a new organizational unit user group with non-valid or empty post body"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to create a new organizational unit user group with invalid post body"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
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
		type                 			| message 					| postBody
		"HttpMessageNotReadableError"  	| "Invalid request body"	| '{"name:  Permissions","orgUnit" {"uid": "Rustic"}}'
		"HttpMessageNotReadableError"  	| "Invalid request body"	| ''
	}

	def "B2B Customer tries to create a new organizational unit user group: #descriptor"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to create a new organizational unit user group"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH,
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
		descriptor             | postBody
		"standardPermissions2" | '{"uid": "standardPermissions2", "name": "Standard Permissions 2","orgUnit": {"uid": "Rustic"}}'
	}

	def "B2B Admin gets users related to the organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get users related to an organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrgCustomers',
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

		}
		where:
		orgUnitUserGroupId    | selectedCustomerId                | nonSelectedCustomerId
		"limitedPermissions"  | "anthony.lombardi@rustic-hw.com"  | "alejandro.navarro@rustic-hw.com"
		"standardPermissions" | "alejandro.navarro@rustic-hw.com" | "anthony.lombardi@rustic-hw.com"
	}

	def "B2B Customer gets users related to the organizational unit user group"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get users related to the organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrgCustomers',
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
		orgUnitUserGroupId << ["limitedPermissions"]
	}

	def "B2B Admin tries to get users related to an organizational unit user group with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get users related to an organizational unit based on role"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrgCustomers',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request"
		with(response) {
			status == SC_BAD_REQUEST
		}
		where:
		descriptor                         | orgUnitUserGroupId
		"Non_Existing_Org_Unit_User_Group" | "Non_Existing_Org_Unit_User_Group"
	}

	def "B2B Admin adds an organizational customer to organizational unit user group members: #orgUnitUserGroupId #userId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to add an organizational customer to organizational unit user group members"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members',
				query: ["orgCustomerId": customerID],
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)


		then: "the operation is successful"
		with(response) { status == SC_CREATED }

		and: "the user was not added to user group members"
		with(currentOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.members.find { it.uid == userId } == null
		}

		then: "the user is added to user group members"
		with(updatedOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK

			data.members.find { it.uid == userId }
		}

		where:
		orgUnitUserGroupId   | userId                      | customerID
		"limitedPermissions" | "axel.krause@rustic-hw.com" | "9a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Customer tries to add an organizational customer to organizational unit user group members"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to add an organizational customer to organizational unit user group members"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members',
				query: ["orgCustomerId": customerID],
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
		orgUnitUserGroupId    | customerID
		"standardPermissions" | "7a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Admin tries to add an organizational customer to organizational unit user group members with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add an organizational customer to organizational unit user group members"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members',
				query: ["orgCustomerId": customerID],
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		descriptor           | orgUnitUserGroupId   | customerID                             | message                                                   | type
		"Non_Existing_Group" | "Non_Existing_Group" | "9a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "UserGroupModel with uid 'Non_Existing_Group' not found!" | "UnknownIdentifierError"
		"Non_Existing_User"  | "limitedPermissions" | "non-existing-user"                    | "Cannot find user with propertyValue 'non-existing-user'" | "UnknownIdentifierError"
	}

	def "B2B Admin removes a specific organizational unit user group from a user: #orgUnitUserGroupId #userId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to remove an organizational customer from organizational user group members"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members/' + customerID,
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		then: "the operation is successful"
		with(response) { status == SC_OK }

		and: "user group members had the user"
		with(currentOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.members.find { it.uid == userId }
		}
		and: "user is removed from user group members"
		with(updatedOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.members.find { it.uid == userId } == null
		}

		where:
		orgUnitUserGroupId   | userId                         | customerID
		"limitedPermissions" | "william.hunter@rustic-hw.com" | "8a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Customer tries to remove an organizational customer from organizational user group members"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to remove an organizational customer from organizational user group members"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members/' + customerID,
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
		orgUnitUserGroupId   | customerID
		"limitedPermissions" | "7a2a41a3-c54c-4ce8-a2d2-0324e1c32a21"
	}

	def "B2B Admin tries to remove an organizational customer from organizational user group members with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove an organizational customer from organizational user group members"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId + '/members/' + customerID,
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		descriptor           | orgUnitUserGroupId   | customerID                             | message                                                   | type
		"Non_Existing_Group" | "Non_Existing_Group" | "9a2a41a3-c54c-4ce8-a2d2-0324e1c32a21" | "UserGroupModel with uid 'Non_Existing_Group' not found!" | "UnknownIdentifierError"
		"Non_Existing_User"  | "limitedPermissions" | "non-existing-user"                    | "Cannot find user with propertyValue 'non-existing-user'" | "UnknownIdentifierError"
	}

	def "B2B Admin adds a permission to an organizational unit user group: #orgUnitUserGroupId #permissionCode"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to add a user group to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions",
				query: ["orderApprovalPermissionCode": permissionCode],
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)


		then: "the operation is successful"
		with(response) {
			status == SC_CREATED
			compareB2BSelectionData(data, permissionCode, true)
		}

		and: "the permission was not added to the user group"
		with(currentOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.permissions.find { it.code == permissionCode } == null
		}

		then: "the permission is added to the user group"
		with(updatedOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
			data.permissions.find { it.code == permissionCode }
		}

		where:
		orgUnitUserGroupId   | permissionCode
		"limitedPermissions" | "Rustic_7K_USD_ORDER"
	}

	def "B2B Customer tries to add a permission to an organizational unit user group"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to add an organizational unit user group to a user"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions",
				query: ["orderApprovalPermissionCode": permissionCode],
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
		orgUnitUserGroupId    | permissionCode
		"standardPermissions" | "Rustic_7K_USD_ORDER"
	}

	def "B2B Admin tries to add a permission to an organizational unit user group with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to add a permission to a user group"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions",
				query: ["orderApprovalPermissionCode": permissionCode],
				contentType: JSON,
				requestContentType: JSON)


		then: "the operation is unsuccessful"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		descriptor                | orgUnitUserGroupId   | permissionCode            | message                                                   | type
		"Non_Existing_Group"      | "Non_Existing_Group" | "Rustic_5K_USD_ORDER"     | "UserGroupModel with uid 'Non_Existing_Group' not found!" | "UnknownIdentifierError"
		"Non_Existing_Permission" | "limitedPermissions" | "Non_Existing_Permission" | "Illegal argument error."                                 | "IllegalArgumentError"
	}


	def "B2B Admin gets permissions related to the organizational unit user group: #orgUnitUserGroupId"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get permissions related to an organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrderApprovalPermissions',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets permissions"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK

			data.sorts.size() > 0
			data.pagination.pageSize == PAGE_SIZE
			data.pagination.currentPage == 0

			data.orderApprovalPermissions.find { it.code == selectedPermissionCode && it.selected == true }
			data.orderApprovalPermissions.find { it.code == nonSelectedPermissionCode && it.selected == false }

		}
		where:
		orgUnitUserGroupId    | selectedPermissionCode | nonSelectedPermissionCode
		"limitedPermissions"  | "Rustic_0K_USD_ORDER"  | "Rustic_5K_USD_ORDER"
		"standardPermissions" | "Rustic_5K_USD_ORDER"  | "Rustic_0K_USD_ORDER"
	}

	def "B2B Customer gets permissions related to the organizational unit user group"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to get permissions related to the organizational unit user group"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrderApprovalPermissions',
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
		orgUnitUserGroupId << ["limitedPermissions"]
	}

	def "B2B Admin tries to get permissions related to an organizational unit user group with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get permissions related to an organizational unit based on role"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + '/availableOrderApprovalPermissions',
				contentType: JSON,
				requestContentType: JSON)

		then: "he gets bad request"
		with(response) {
			status == SC_BAD_REQUEST
		}
		where:
		descriptor                         | orgUnitUserGroupId
		"Non_Existing_Org_Unit_User_Group" | "Non_Existing_Org_Unit_User_Group"
	}

	def "B2B Admin removes a specific permission from a organizational user group: #orgUnitUserGroupId #permissionCode"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)
		def currentOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		when: "he requests to remove a permission from an organizational user group "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions/" + permissionCode,
				contentType: JSON,
				requestContentType: JSON)

		def updatedOrgUnitUserGroup = getOrganizationalUnitUserGroup(restClient, b2bAdminCustomer, orgUnitUserGroupId)

		then: "the operation is successful"
		with(response) {
			status == SC_OK
			compareB2BSelectionData(data, permissionCode, false)
		}

		and: "the user group had the permission"
		with(currentOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.permissions.find { it.code == permissionCode }
		}
		and: "the permission is removed from the user group"
		with(updatedOrgUnitUserGroup) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}

			data.permissions.find { it.code == permissionCode } == null
		}

		where:
		orgUnitUserGroupId   | permissionCode
		"limitedPermissions" | "Rustic_0K_USD_ORDER"
	}

	def "B2B Customer tries to remove a specific permission from an organizational unit user group"() {
		given: "a registered and logged in customer without B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bCustomer = [
				'id'      : B2BCUSTOMER_USERNAME,
				'password': B2BCUSTOMER_PASSWORD
		]
		authorizeCustomer(restClient, b2bCustomer)

		when: "he requests to remove a permission from an organizational unit user group"
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions/" + permissionCode,
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
		orgUnitUserGroupId   | permissionCode
		"limitedPermissions" | "Rustic_0K_USD_ORDER"
	}

	def "B2B Admin tries to remove a specific permission from an organizational unit user group with invalid parameters: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to remove a role from a user "
		HttpResponseDecorator response = restClient.delete(
				path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + ORGANIZATIONAL_USER_GROUPS_PATH + "/" + orgUnitUserGroupId + "/orderApprovalPermissions/" + permissionCode,
				contentType: JSON,
				requestContentType: JSON)

		then: "the operation is unsuccessful"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors.find { it.message == message && it.type == type }
		}

		where:
		descriptor                | orgUnitUserGroupId   | permissionCode            | message                                                   | type
		"Non_Existing_Group"      | "Non_Existing_Group" | "Rustic_5K_USD_ORDER"     | "UserGroupModel with uid 'Non_Existing_Group' not found!" | "UnknownIdentifierError"
		"Non_Existing_Permission" | "limitedPermissions" | "Non_Existing_Permission" | "Illegal argument error."                                 | "IllegalArgumentError"
	}

	protected getOrganizationalUnitUserGroup(RESTClient client, customer, orgUnitUserGroupId) {
		HttpResponseDecorator response = client.get(
				path: getBasePathWithSite() + '/users/' + customer.id + ORGANIZATIONAL_USER_GROUPS_PATH + '/' + orgUnitUserGroupId,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: JSON,
				requestContentType: JSON)
		return response;
	}

	protected void compareB2BSelectionData(Object selectionData, String code, boolean selected) {
		assert selectionData.id == code
		assert selectionData.selected == selected
	}
}

