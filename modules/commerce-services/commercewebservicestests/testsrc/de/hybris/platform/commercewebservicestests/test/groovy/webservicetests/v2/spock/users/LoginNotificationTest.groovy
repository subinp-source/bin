/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users

import de.hybris.bootstrap.annotations.ManualTest
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.SC_ACCEPTED
import static org.apache.http.HttpStatus.SC_FORBIDDEN

/**
 *
 * This class contains tests for login notification
 *
 */
@ManualTest
@Unroll
class LoginNotificationTest extends AbstractUserTest {

	def "Call Login Notification endpoint with current registered and authorized customer should be accepted"() {
		given: "a registered and logged in user"
		def customer = registerAndAuthorizeCustomer(restClient, JSON)

		when: "calls login notification endpoint"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/current/loginnotification/',
				contentType: JSON,
				requestContentType: URLENC)

		then: "backend accepts the notification"
		with(response) {
			status == SC_ACCEPTED
		}
	}

	def "Call Login Notification endpoint without authorized customer should return unauthorized"() {
		given: "authenticated client"
		authorizeClient(restClient)

		when: "calls login notification endpoint"
		def response = restClient.post(
				path: getBasePathWithSite() + '/users/current/loginnotification/',
				contentType: JSON,
				requestContentType: URLENC)

		then: "backend rejects not authorized customer"
		with(response) {
			status == SC_FORBIDDEN
		}
	}
}
