package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.misc

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.AbstractSpockTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.SC_OK

@ManualTest
@Unroll
class RequestPathSuffixMatchingTest extends AbstractSpockTest {

	protected static final String PRODUCT_CODE_WITH_DOTS = "11.12.22"
	protected static final String PRODUCT_CODE_WITHOUT_DOTS = "1422224"

	def "Get product details by code, that contains specified suffix, format : #format, productCode: #productCode, suffix: #suffix"() {

		when: "requests product details"
		def code = productCode + suffix
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + code,
				contentType: format)

		then: "the product details received"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.code == productCode
			response.getFirstHeader('Content-Type').getValue().startsWith(format.toString())
		}

		where:
		format | productCode               | suffix
		JSON   | PRODUCT_CODE_WITHOUT_DOTS | ""
		XML    | PRODUCT_CODE_WITHOUT_DOTS | ""
		JSON   | PRODUCT_CODE_WITHOUT_DOTS | ".xml"
		XML    | PRODUCT_CODE_WITHOUT_DOTS | ".xml"
		JSON   | PRODUCT_CODE_WITHOUT_DOTS | ".json"
		XML    | PRODUCT_CODE_WITHOUT_DOTS | ".json"
		JSON   | PRODUCT_CODE_WITHOUT_DOTS | "///"
		XML    | PRODUCT_CODE_WITHOUT_DOTS | "///"
		JSON   | PRODUCT_CODE_WITH_DOTS    | ""
		XML    | PRODUCT_CODE_WITH_DOTS    | ""
		JSON   | PRODUCT_CODE_WITH_DOTS    | ".xml"
		XML    | PRODUCT_CODE_WITH_DOTS    | ".xml"
		JSON   | PRODUCT_CODE_WITH_DOTS    | ".json"
		XML    | PRODUCT_CODE_WITH_DOTS    | ".json"
		JSON   | PRODUCT_CODE_WITH_DOTS    | "///"
		XML    | PRODUCT_CODE_WITH_DOTS    | "///"
	}

	def "Get product details by code and use response formatting by parameter, parameterFormat : #parameterFormat, headerFormat : #headerFormat, fileExtension : #fileExtension"() {

		when: "requests product details"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE_WITH_DOTS + fileExtension,
				query: ['format': parameterFormat],
				headers: ['Accept': headerFormat.toString()])

		then: "the product details received"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.code == PRODUCT_CODE_WITH_DOTS
			response.getFirstHeader('Content-Type').getValue().contains(parameterFormat)
		}

		where:
		parameterFormat | headerFormat | fileExtension
		"json"          | XML          | ".xml"
		"xml"           | JSON         | ".json"
		"xml"           | ANY          | ".json"
	}

	def "Get product details by code and use response formatting by header, headerFormat : #headerFormat, fileExtension : #fileExtension"() {

		when: "requests product details"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE_WITH_DOTS + fileExtension,
				contentType: headerFormat)

		then: "the product details received"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.code == PRODUCT_CODE_WITH_DOTS
			response.getFirstHeader('Content-Type').getValue().startsWith(headerFormat.toString())
		}

		where:
		headerFormat | fileExtension
		XML          | ".json"
		JSON         | ".xml"
	}
}
