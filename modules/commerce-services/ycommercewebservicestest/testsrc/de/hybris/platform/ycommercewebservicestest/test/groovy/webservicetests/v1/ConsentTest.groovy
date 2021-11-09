/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v1
import de.hybris.bootstrap.annotations.ManualTest
import org.junit.Test
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

@ManualTest
class ConsentTest extends BaseWSTest {

	static final X_ANONYMOUS_CONSENTS = 'X-Anonymous-Consents'

	@Test
	void testAnonymousConsent() {
		def con = testUtil.getSecureConnection("/products", 'GET', 'JSON', HttpURLConnection.HTTP_OK)
		def anonConsentHeader = con.getHeaderField(X_ANONYMOUS_CONSENTS)
		println "anonConsentHeader: " + anonConsentHeader
		assert anonConsentHeader != null
		assert anonConsentHeader.length() > 0
	}
	@Test
	void testAnonymousConsentGiven() {
		def templateCode = 'CONSENT_TEMPLATE_2'
		def templateVersion = 2
		def consentState = 'GIVEN'
		def consentTemplateElement = JsonOutput.toJson([[templateCode: templateCode, templateVersion: templateVersion, consentState: consentState]])
		def consentItemEncoded = URLEncoder.encode(consentTemplateElement, "UTF-8")
		def con = testUtil.getSecureConnection("/products", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, null,
				"application/x-www-form-urlencoded", consentItemEncoded)
		def anonConsentHeader = con.getHeaderField(X_ANONYMOUS_CONSENTS)
		def anonymousHeaderDecodedObject = new JsonSlurper().parseText(URLDecoder.decode(anonConsentHeader, "UTF-8"))
		assert anonymousHeaderDecodedObject.any{ consent ->
			consent.templateCode == templateCode &&
			consent.templateVersion.toInteger() == templateVersion &&
			consent.consentState == consentState}
	}

	@Test
	void testAnonymousConsentWithdraw() {
		def templateCode = 'CONSENT_TEMPLATE_2'
		def templateVersion = 2
		def consentState = 'WITHDRAW'
		def consentTemplateElement = JsonOutput.toJson([[templateCode: templateCode, templateVersion: templateVersion, consentState: consentState]])
		def consentItemEncoded = URLEncoder.encode(consentTemplateElement, "UTF-8")
		def con = testUtil.getSecureConnection("/products", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, null,
				"application/x-www-form-urlencoded", consentItemEncoded)
		def anonConsentHeader = con.getHeaderField(X_ANONYMOUS_CONSENTS)
		def anonymousHeaderDecodedObject = new JsonSlurper().parseText(URLDecoder.decode(anonConsentHeader, "UTF-8"))
		assert anonymousHeaderDecodedObject.any{ consent ->
			consent.templateCode == templateCode &&
			consent.templateVersion.toInteger() == templateVersion &&
			consent.consentState == consentState}
	}

	@Test
	void testAnonymousConsentChangedConsent() {
		def templateCode = 'CONSENT_TEMPLATE_3'
		def templateVersion = 0
		def consentState = 'GIVEN'
		def consentTemplateElement = JsonOutput.toJson([[templateCode: templateCode, templateVersion: templateVersion, consentState: consentState]])
		def consentItemEncoded = URLEncoder.encode(consentTemplateElement, "UTF-8")
		def con = testUtil.getSecureConnection("/products", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, null,
				"application/x-www-form-urlencoded", consentItemEncoded)
		def anonConsentHeader = con.getHeaderField(X_ANONYMOUS_CONSENTS)
		def anonymousHeaderDecodedObject = new JsonSlurper().parseText(URLDecoder.decode(anonConsentHeader, "UTF-8"))
		assert anonymousHeaderDecodedObject.any{ consent ->
			consent.templateCode == templateCode &&
			consent.templateVersion.toInteger() == templateVersion &&
			consent.consentState == consentState}
	}

	@Test
	void testAnonymousConsentShouldNotBeDisplayedInHeaderForLogedUser() {
		def access_token = testUtil.getAccessToken("orderhistoryuser@test.com", "1234")
		def con = testUtil.getSecureConnection("/cart", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		assert !con.getHeaderFields().any{it.key == X_ANONYMOUS_CONSENTS}
	}
}
