/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users

import de.hybris.bootstrap.annotations.ManualTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Ignore
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.*

/**
 *
 * This class contains tests for user account related to password change and user profile.S
 *
 */
@ManualTest
@Unroll
class UserAccountTest extends AbstractUserTest {
	protected static final DATE_HEADER = "Date"

	def "User gets his profile : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user requests his profile"
		def response = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id,
				contentType: format,
				requestContentType: URLENC)

		then: "he gets profile details"
		with(response) {
			status == SC_OK
			data.uid == customer.id
			data.firstName == CUSTOMER_FIRST_NAME
			data.lastName == CUSTOMER_LAST_NAME
			data.titleCode == CUSTOMER_TITLE_CODE
		}

		where:
		format << [XML, JSON]
	}

	def "User tries to get his profile from a B2B base site: #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user requests his profile"
		def response = restClient.get(
				path: getBasePathWithB2BSite() + '/users/' + customer.id,
				contentType: format,
				requestContentType: URLENC)

		then: "request fails"
		with(response) {
			status == SC_UNAUTHORIZED
		}

		where:
		format << [XML, JSON]
	}

	def "User updates his profile when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, JSON)

		when: "user attempts to update his profile"
		def response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + customer.id,
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat)
		and: "retrieves his updated profile"
		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: responseFormat,
				requestContentType: URLENC)
		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}
		and: "updates are visible"
		with(response2) {
			data.uid == customer.id
			data.name == "Udo Hubertus"
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == CUSTOMER_TITLE_CODE
			data.language.isocode == "zh"
			data.currency.isocode == "USD" // default
		}
		where:
		requestFormat | responseFormat | postBody
		URLENC        | JSON           | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		URLENC        | XML            | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		JSON          | JSON           | '{"firstName" : "Udo","lastName" : "Hubertus", "language" : {"isocode" : "zh"}}'
		XML           | XML            | "<user><firstName>Udo</firstName><lastName>Hubertus</lastName><language><isocode>zh</isocode></language></user>"
	}

	def "User replaces his profile when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, JSON)

		when: "user attempts to replace his profile"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id,
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat)

		and: "retrieves his updated profile"
		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: responseFormat,
				requestContentType: URLENC)

		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}

		and: "stored in the system; default value for language is set (normally would be null, but platform provides default values for language and currency)"
		with(response2) {
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == "mr"
			data.language.isocode == "en" // default language (cannot be null)
			data.currency.isocode == "JPY"
		}
		where:
		requestFormat | responseFormat | postBody
		URLENC        | JSON           | ['firstName': 'Udo', 'lastName': 'Hubertus', 'titleCode': 'mr', 'currency': 'JPY']
		URLENC        | XML            | ['firstName': 'Udo', 'lastName': 'Hubertus', 'titleCode': 'mr', 'currency': 'JPY']
		JSON          | JSON           | '{"firstName" : "Udo","lastName" : "Hubertus", "titleCode" : "mr", "currency" : { "isocode" : "JPY" }}'
		XML           | XML            | "<user><firstName>Udo</firstName><lastName>Hubertus</lastName><titleCode>mr</titleCode><currency><isocode>JPY</isocode></currency></user>"
	}

	def "Profile replacement (put) with missing required fields fails when request: #requestFormat and response: #responseFormat"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, JSON)

		when: "user attempts to replace his profile without providing required fields"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id,
				contentType: responseFormat,
				body: postBody,
				requestContentType: requestFormat)

		then: "request fails"
		with(response) {
			status == SC_BAD_REQUEST
			println data
			data.errors[0].type == errorType
		}
		where:
		requestFormat | responseFormat | postBody                                  | errorType
		URLENC        | JSON           | ['firstName': 'Udo']                      | 'MissingServletRequestParameterError'
		URLENC        | XML            | ['firstName': 'Udo']                      | 'MissingServletRequestParameterError'
		JSON          | JSON           | '{"firstName" : "Udo"}'                   | 'ValidationError'
		XML           | XML            | "<user><firstName>Udo</firstName></user>" | 'ValidationError'
	}

	def "Change user password as the user : #format and useCustomerId: #useCustomerId"() {
		//This is happy path
		given: "a registered and logged in customer"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		when: "user attempts to change their password"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [
						'old': CUSTOMER_PASSWORD_STRONG,
						'new': "NEWpass1234!",
				],
				contentType: format,
				requestContentType: URLENC)

		then: "the password is updated"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_ACCEPTED
		}

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true
	}

	def "Verify password strength for user : #format and useCustomerId: #useCustomerId"() {
		given: "a registered and logged in customer"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		when: "user attempts to change password"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [
						'old': CUSTOMER_PASSWORD_STRONG,
						'new': newPass,
				],
				contentType: format,
				requestContentType: URLENC)

		then: "the new password is not valid"
		with(response) { status == SC_BAD_REQUEST }

		where:
		format | newPass         | useCustomerId
		XML    | "Password1234"  | false
		JSON   | "Password1234"  | false
		XML    | "password1234!" | true
		XML    | "PAssword!"     | true
		XML    | "PAs2!"         | true


	}

	def "Old password should be required when user wants to change his own password : #format and useCustomerId: #useCustomerId"() {
		given: "a registered and logged in customer"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		when: "user attempts to change their password without providing old one"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [ //do not provide old password
						'new': "NEWpass1234!",
				],
				contentType: format,
				requestContentType: URLENC)

		then: "the attempt is rejected"
		// returns
		with(response) { status == SC_BAD_REQUEST }

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true

	}

	def "Client should not be allowed to change customer password : #format and useCustomerId: #useCustomerId"() {
		//client : a device; customer : a user
		given: "a registered and logged in customer"
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		authorizeClient(restClient)

		when: "user client attempts to change customer password"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [
						'new': "new password",
						'old': CUSTOMER_PASSWORD
				],
				contentType: format,
				requestContentType: URLENC)

		then: "the password is updated"
		with(response) { status == SC_FORBIDDEN }

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true
	}

	def "Force user profile update as customer manager when request: #requestFormat and response: #responseFormat"() {
		given: "a registered user and customer manager"
		def customer = registerCustomerWithTrustedClient(restClient, JSON)
		authorizeCustomerManager(restClient)

		when: "a customer manager attempts to update customer's profile"
		def response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + customer.id,
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat)
		and: "retrieves his updated profile"
		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: responseFormat,
				requestContentType: URLENC)
		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}
		and: "updates are visible"
		with(response2) {
			data.uid == customer.id
			data.name == "Udo Hubertus"
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == CUSTOMER_TITLE_CODE
			data.language.isocode == "zh"
			data.currency.isocode == "USD" // default
		}
		where:
		requestFormat | responseFormat | postBody
		URLENC        | JSON           | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		URLENC        | XML            | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		JSON          | JSON           | '{"firstName" : "Udo","lastName" : "Hubertus", "language" : {"isocode" : "zh"}}'
		XML           | XML            | "<user><firstName>Udo</firstName><lastName>Hubertus</lastName><language><isocode>zh</isocode></language></user>"
	}

	def "Force user profile update as trusted client when request: #requestFormat and response: #responseFormat"() {
		// happy path for customer manager
		given: "a registered user and trusted client"
		def customer = registerCustomerWithTrustedClient(restClient, JSON)
		authorizeTrustedClient(restClient);

		when: "a trusted client attempts to update customer's profile"
		def response = restClient.patch(
				path: getBasePathWithSite() + '/users/' + customer.id,
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat)
		and: "retrieves his updated profile"
		def response2 = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id,
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: responseFormat,
				requestContentType: URLENC)
		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}
		and: "updates are visible"
		with(response2) {
			data.uid == customer.id
			data.name == "Udo Hubertus"
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == CUSTOMER_TITLE_CODE
			data.language.isocode == "zh"
			data.currency.isocode == "USD" // default
		}
		where:
		requestFormat | responseFormat | postBody
		URLENC        | JSON           | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		URLENC        | XML            | ['firstName': 'Udo', 'lastName': 'Hubertus', 'language': 'zh']
		JSON          | JSON           | '{"firstName" : "Udo","lastName" : "Hubertus", "language" : {"isocode" : "zh"}}'
		XML           | XML            | "<user><firstName>Udo</firstName><lastName>Hubertus</lastName><language><isocode>zh</isocode></language></user>"
	}

	def "Force user password change as customer manager : #format and useCustomerId: #useCustomerId"() {
		// happy path for customer manager
		given: "a registered user and customer manager"
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		authorizeCustomerManager(restClient)

		when: "a customer manager attempts to change user password without providing current value"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [ // do not provide old password
						'new': "Newpassword1234!",
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is allowed to change it"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_ACCEPTED
		}

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true
	}

	def "Force user password change as trusted client : #format and useCustomerId: #useCustomerId"() {
		// happy path for trusted client
		given: "a registered user and trusted client"
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		when: "a trusted client attempts to change user password without providing current value"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + userId + "/password",
				body: [ // do not provide old password
						'new': "Newpassword1234!",
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is allowed to change it"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_ACCEPTED
		}

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true
	}

	def "Force non-existing user password change : #format"() {
		given: "a trusted client and logged in customer manager"
		authorizeCustomerManager(restClient)

		when: "Customer manager wants to update password of non-existing customer"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + 'nonexisting@hybris.com' + "/password",
				body: [
						'new': "new password",
				],
				contentType: format,
				requestContentType: URLENC)

		then: "Update fails"
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].message == "Cannot find user with propertyValue 'nonexisting@hybris.com'"
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login : #format"() {
		//This is happy path
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def oldUid = customer.id
		def newUid = "AbC" + System.currentTimeMillis() + "@hybris.com"

		when: "user attempts to change his login to a unique one"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': newUid,
						'password': customer.password
				],
				contentType: format,
				requestContentType: URLENC)

		and: "later attempts to authorize using old login"
		def response2 = restClient.post(
				uri: getOAuth2TokenUri(),
				path: getOAuth2TokenPath(),
				body: [
						'grant_type'   : 'password',
						'client_id'    : getClientId(),
						'client_secret': getClientSecret(),
						'username'     : oldUid,
						'password'     : customer.password
				],
				contentType: JSON,
				requestContentType: URLENC)
		then: "he is allowed to change login"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}
		and: "authorization with old credentials fails"
		with(response2) { status == SC_BAD_REQUEST }

		where:
		format << [XML, JSON]
	}

	@Ignore
	def "Force user login change : #format"() {

		given: "an existing user, a trusted client and logged in customer manager"
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def newUid = "AbC" + System.currentTimeMillis() + "@hybris.com"
		authorizeCustomerManager(restClient)

		when: "customer manager attempts to change user's login to a unique one without providing user's password"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': newUid,
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is allowed to do so"
		with(response) {
			if (isNotEmpty(data)) println data
			status == SC_OK
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login providing wrong password : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def newUid = "AbC" + System.currentTimeMillis() + "@hybris.com"

		when: "user attempts to change his login to a unique one"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': newUid,
						'password': 'wrongPassword'
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is not allowed to do so"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'PasswordMismatchError'
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login providing empty password : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)
		def newUid = "AbC" + System.currentTimeMillis() + "@hybris.com"

		when: "user attempts to change his login to a unique one"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': newUid,
						'password': ''
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is not allowed to do so"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'IllegalArgumentError'
			data.errors[0].message == 'The field [currentPassword] cannot be empty'
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login to duplicate ID : #format"() {
		given: "two users, one logged in"
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def customer2 = registerAndAuthorizeCustomer(restClient, format)

		when: "user attempts to change his login to a login already held by another user"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer2.id + "/login",
				body: [
						'newLogin': customer.id,
						'password': customer2.password
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is not allowed to do so"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'DuplicateUidError'
			data.errors[0].message == "User with email " + customer.id + " already exists."
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login to invalid email : #format"() {
		given: "two users, one logged in"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user attempts to change his login to an invalid login "
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': 'invalidEmailAddress',
						'password': customer.password
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is not allowed to do so"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'ValidationError'
			data.errors[0].subject == 'newLogin'
			data.errors[0].subjectType == 'parameter'
			data.errors[0].reason == 'invalid'
			data.errors[0].message == 'Login [invalidEmailAddress] is not a valid e-mail address!'
		}

		where:
		format << [XML, JSON]
	}

	def "User changes his login to empty email : #format"() {
		given: "two users, one logged in"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user attempts to change his login to an empty login"
		def response = restClient.put(
				path: getBasePathWithSite() + '/users/' + customer.id + "/login",
				body: [
						'newLogin': '',
						'password': customer.password
				],
				contentType: format,
				requestContentType: URLENC)

		then: "he is not allowed to do so"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'ValidationError'
			data.errors[0].subject == 'newLogin'
			data.errors[0].subjectType == 'parameter'
			data.errors[0].reason == 'invalid'
			data.errors[0].message == 'Login [] is not a valid e-mail address!'
		}

		where:
		format << [XML, JSON]
	}

	def "User requests a password change : #format and useCustomerId: #useCustomerId"() {

		given: "a user who has forgotten his password"
		//we are not logging in as the customer, just registering one
		def customer = registerCustomerWithTrustedClient(restClient, format)
		def userId = getUserId(useCustomerId, customer)
		when: "user requests password change"
		def response = restClient.post(
				path: getBasePathWithSite() + "/forgottenpasswordtokens",
				body: [
						'userId': userId
				],
				contentType: format,
				requestContentType: URLENC)

		then: "his request is accepted"
		with(response) {
			status == SC_ACCEPTED
			isEmpty(data)
		}

		where:
		format | useCustomerId
		XML    | false
		JSON   | false
		XML    | true
		JSON   | true
	}

	def "User requests a password change for non-existing account : #format"() {

		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "user requests password change for non-existing account"
		def response = restClient.post(
				path: getBasePathWithSite() + "/forgottenpasswordtokens",
				body: [
						'userId': 'badid'
				],
				contentType: format,
				requestContentType: URLENC)

		then: "his request is rejected"
		with(response) {
			status == SC_ACCEPTED
			isEmpty(data)
		}

		where:
		format << [XML, JSON]
	}

	def "User requests a password change for existing and non-existing account : #format"() {

		given: "a trusted client"
		def customer = registerCustomerWithTrustedClient(restClient, format)

		when: "user requests password change"
		def existingUserResponse = restClient.post(
				path: getBasePathWithSite() + "/forgottenpasswordtokens",
				body: [
						'userId': customer.id
				],
				contentType: format,
				requestContentType: URLENC)

		and: "user requests password change for non-existing account"
		def nonExistingUserResponse = restClient.post(
				path: getBasePathWithSite() + "/forgottenpasswordtokens",
				body: [
						'userId': 'badid'
				],
				contentType: format,
				requestContentType: URLENC)

		then: "response headers are equal"
		def existingUserHeaderMap = existingUserResponse.getAllHeaders()
				.findAll { !it.name.contains(DATE_HEADER) }
				.collectEntries { [it.name, it.value] }
		def nonExistingUserHeaderMap = nonExistingUserResponse.getAllHeaders()
				.findAll { !it.name.contains(DATE_HEADER) }
				.collectEntries { [it.name, it.value] }

		existingUserHeaderMap == nonExistingUserHeaderMap

		where:
		format << [XML, JSON]
	}

	def "Check if reset password endpoint returns expected error: #responseMessage, for request: #requestFormat and response: #responseFormat"() {

		given: "a trusted client"
		def customer = registerCustomerWithTrustedClient(restClient, responseFormat)

		when: "user requests password change for non-existing account"
		def response = restClient.post(
				path: getBasePathWithSite() + "/resetpassword",
				body: postBody,
				contentType: responseFormat,
				requestContentType: requestFormat)

		then: "his request is rejected"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == responseType
			data.errors[0].message == responseMessage
		}

		where:
		requestFormat | responseFormat | postBody                                                                                                                                                                                                                                    | responseType           | responseMessage
		XML           | XML            | '<?xml version="1.0"?><ResetPassword><newPassword>A@12345</newPassword><token>too-short</token></ResetPassword>'                                                                                                                            | "IllegalArgumentError" | "Encrypted data too short"
		XML           | XML            | '<?xml version="1.0"?><ResetPassword><newPassword>A@12345</newPassword><token>rw9dI17Loh55Ma+NvPfzEm9EV7LR8BBBqbXKslwiXgL9qkf7zjsYo8RYKOca4wrAMjNfRCmaC0L54Y79wXH8AJmVHSe64tgZaKbqxFgY+UYptTTH7dkhrEbbHPdEP3aIYQ==</token></ResetPassword>' | "IllegalArgumentError" | "Invalid token"
		JSON          | JSON           | '{"newPassword": "A%40123456","token": "too-short"}'                                                                                                                                                                                        | "IllegalArgumentError" | "Encrypted data too short"
		JSON          | JSON           | '{"newPassword": "A%40123456","token": "rw9dI17Loh55Ma+NvPfzEm9EV7LR8BBBqbXKslwiXgL9qkf7zjsYo8RYKOca4wrAMjNfRCmaC0L54Y79wXH8AJmVHSe64tgZaKbqxFgY+UYptTTH7dkhrEbbHPdEP3aIYQ=="}'                                                             | "IllegalArgumentError" | "Invalid token"
	}

	def "Current user replaces his profile : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user attempts to replace his profile"
		HttpResponseDecorator response = restClient.put(
				path: getBasePathWithSite() + '/users/current',
				body: [
						"firstName": "Udo",
						"lastName" : "Hubertus",
						"titleCode": "mr",
				],
				contentType: format,
				requestContentType: URLENC)

		and: "retrieves his updated profile"
		HttpResponseDecorator response2 = restClient.get(
				path: getBasePathWithSite() + '/users/current',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC)

		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}

		and: "stored in the system; default value for language is set (normally would be null, but platform provides default values for language and currency)"
		with(response2) {
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == "mr"
			data.language.isocode == "en"
		}
		where:
		format << [XML, JSON]
	}

	def "Current user gets his profile : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user requests his profile"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/users/current',
				contentType: format,
				requestContentType: URLENC)

		then: "he gets profile details"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.uid == customer.id
			data.firstName == CUSTOMER_FIRST_NAME
			data.lastName == CUSTOMER_LAST_NAME
			data.titleCode == CUSTOMER_TITLE_CODE
		}

		where:
		format << [XML, JSON]
	}

	def "Current user updates his profile : #format"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, format)

		when: "user attempts to update his profile"
		HttpResponseDecorator response = restClient.patch(
				path: getBasePathWithSite() + '/users/current',
				body: [
						"name"     : "Udo Hubertus",
						"firstName": "Udo",
						"lastName" : "Hubertus",
						"language" : "zh",
				],
				contentType: format,
				requestContentType: URLENC)
		and: "retrieves his updated profile"
		HttpResponseDecorator response2 = restClient.get(
				path: getBasePathWithSite() + '/users/current',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC)
		then: "his data is updated"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}
		and: "updates are visible"
		with(response2) {
			data.uid == customer.id
			data.name == "Udo Hubertus"
			data.lastName == "Hubertus"
			data.firstName == "Udo"
			data.titleCode == "dr"
			data.language.isocode == "zh"
		}
		where:
		format << [XML, JSON]
	}

}
