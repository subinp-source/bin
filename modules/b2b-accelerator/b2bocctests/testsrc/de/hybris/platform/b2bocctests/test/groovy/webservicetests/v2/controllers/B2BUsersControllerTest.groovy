/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

@ManualTest
@Unroll
class B2BUsersControllerTest extends AbstractUserTest {
	static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
	static final String B2BADMIN_PASSWORD = "1234"

	static final String USERS_PATH = "/users"
	static final String COMPATIBLE_USERS_PATH = "/orgUsers"
	static final String OCC_OVERLAPPING_PATHS_FLAG = "occ.rewrite.overlapping.paths.enabled"

	static final ENABLED_USERS_PATH = Config.getBoolean(OCC_OVERLAPPING_PATHS_FLAG, false) ? COMPATIBLE_USERS_PATH : USERS_PATH

	def "B2B Admin gets information about himself with compatible users path: #descriptor"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get information about himself"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + ENABLED_USERS_PATH + "/current",
				query: [
						'fields': FIELD_SET_LEVEL_FULL
				],
				contentType: JSON,
				requestContentType: JSON)

		then: "the request is successful"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_OK
		}
		where:
		descriptor << [ENABLED_USERS_PATH]
	}

	def "B2B Admin tries to get information about himself when a B2C base-site is used"() {
		given: "a registered and logged in customer with B2B Admin role"
		authorizeTrustedClient(restClient)
		def b2bAdminCustomer = [
				'id'      : B2BADMIN_USERNAME,
				'password': B2BADMIN_PASSWORD
		]
		authorizeCustomer(restClient, b2bAdminCustomer)

		when: "he requests to get information about himself"
		HttpResponseDecorator response = restClient.get(
				path: getBasePath() + "/wsTestB2C" + ENABLED_USERS_PATH + "/current",
				query: [
						'fields': FIELD_SET_LEVEL_FULL
				],
				contentType: JSON,
				requestContentType: JSON)

		then: "request fails because a restricted B2B API endpoint is used from a B2C base-site"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) {
				println(data)
			}
			status == SC_UNAUTHORIZED
		}
	}
}

