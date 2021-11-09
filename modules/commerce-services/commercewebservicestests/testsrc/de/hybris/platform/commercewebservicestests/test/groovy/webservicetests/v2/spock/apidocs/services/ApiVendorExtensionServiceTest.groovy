/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.apidocs.services

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.AbstractSpockTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static org.apache.http.HttpStatus.SC_OK

@ManualTest
@Unroll
class ApiVendorExtensionServiceTest extends AbstractSpockTest {

	def "Customer gets API documentation that contains vendor extensions values"() {
		when: "user attempts to get API documentation"
		HttpResponseDecorator response = restClient.get(
				path: getBasePath() + "/api-docs")
		then: "exist vendor extensions in the response"
		with(response) {
			status == SC_OK
			isNotEmpty(data."x-servers")
			isNotEmpty(data."x-sap-api-type")
			isNotEmpty(data."x-sap-shortText")
			isNotEmpty(data."x-sap-stateInfo")
			isNotEmpty(data."x-sap-stateInfo".state)
			isNotEmpty(data."x-servers")
			data."x-servers".each { server ->
				assert isNotEmpty(server.url)
				assert isNotEmpty(server.description)
				assert isNotEmpty(server.templates)
				assert isNotEmpty(server.templates.url)
				assert isNotEmpty(server.templates.url.description)
			}
			isNotEmpty(data."security")
		}
	}
}
