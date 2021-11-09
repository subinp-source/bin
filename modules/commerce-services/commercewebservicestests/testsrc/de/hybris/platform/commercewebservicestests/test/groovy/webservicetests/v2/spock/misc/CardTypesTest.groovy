/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 *
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.misc

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_OK

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

@ManualTest
@Unroll
/**
 *
 *
 */
class CardTypesTest extends AbstractSpockFlowTest {

	private codes = [
			'maestro',
			'switch',
			'mastercard_eurocard',
			'amex',
			'diners',
			'visa',
			'master'
	]

	def "Client retrieves card types: #format"() {

		when:
		HttpResponseDecorator response = restClient.get(path: getBasePathWithSite() + '/cardtypes', contentType: format)

		then:
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			isNotEmpty(data.cardTypes)
			data.cardTypes.collect { it.code.toString() }.containsAll(codes)
		}

		where:
		format << [JSON, XML]
	}
}
