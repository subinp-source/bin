/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.errors

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_BAD_REQUEST

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.AbstractSpockTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

@ManualTest
@Unroll
class ErrorTest extends AbstractSpockTest {

	def "Missing identifier : #format"() {
		given: "an authorized trusted client"
		authorizeTrustedClient(restClient)

		when:
		HttpResponseDecorator response = restClient.get(path: getBasePathWithSite() + '/stores/WRONG_ID', contentType: format)

		then:
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'UnknownIdentifierError'
		}

		where:
		format << [JSON, XML]
	}

	def "Missing required parameter : #format"() {
		given: "an authorized trusted client"
		authorizeTrustedClient(restClient)

		when:
		HttpResponseDecorator response = restClient.get(path: getBasePathWithSite() + '/promotions', contentType: format)

		then:
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'MissingServletRequestParameterError'
		}

		where:
		format << [JSON, XML]
	}

	def "Invalid base site : #format"() {

		when: "attempt to request incorrect base site is made"
		HttpResponseDecorator response = restClient.get(path: getBasePath() + '/wrongBaseSite', contentType: format)

		then:
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'InvalidResourceError'
			data.errors[0].message == 'Base site wrongBaseSite doesn\'t exist'
		}

		where:
		format << [JSON, XML]
	}

	def "Missing request body"() {
		given: "an authorized trusted client"
		authorizeTrustedClient(restClient)

		when:
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users',
				contentType: format,
				body: postBody,
				requestContentType: format)

		then:
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'HttpMessageNotReadableError'
			data.errors[0].message == 'Request body is invalid or missing'
		}

		where:
		postBody              | format
		''                    | JSON
		'{"orderEntryNumber}' | JSON
	}
}
