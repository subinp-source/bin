/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.paymentmodes

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.SC_OK

@ManualTest
@Unroll
class PaymentModesTest extends AbstractUserTest {

	def "client requests payment modes: #format"() {
		given: "authenticated client"
		authorizeClient(restClient)

		when: "client retrieves paymentmodes"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + "/paymentmodes",
				contentType: format,
				query: ['fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC)

		then: "he receives payment modes"
		with(response) {
			status == SC_OK
			data.paymentModes.size() >= 2

			assert data.paymentModes.any { paymentMode ->
				paymentMode.code == "wstestpay" &&
						paymentMode.name == "WsTestPay" &&
						paymentMode.description == "Sample payment mode for WsTest"
			}
			assert data.paymentModes.any { paymentMode ->
				paymentMode.code == "wsmockpay" &&
						paymentMode.name == "WsMockPay" &&
						paymentMode.description == "Sample payment mode for WsTest"
			}
		}

		where:
		format << [XML, JSON]
	}
}
