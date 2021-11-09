/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultExceptionTranslatorUnitTest extends Specification
{
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH
	private static final String ERROR_CODE = 'misconfigured_attribute'

	def defaultExceptionTranslator = new DefaultExceptionTranslator()

	@Test
	def "translate IntegrationAttributeException"() {
		given:
		final String originalExceptionMessage = "original message"
		final IntegrationAttributeException originalException = Stub(IntegrationAttributeException)
		originalException.getMessage() >> originalExceptionMessage

		when:
		final ODataRuntimeApplicationException e = defaultExceptionTranslator.translate(originalException)

		then:
		e.getMessage() == originalExceptionMessage
		e.getLocale() == DEFAULT_LOCALE
		e.getHttpStatus() == HttpStatusCodes.BAD_REQUEST
	}

	@Test
	def 'populates error context containing IntegrationAttributeException'() {
		given:
		def exception = Stub(IntegrationAttributeException) {
			getMessage() >> 'error message'
		}
		def errorContext = new ODataErrorContext(exception: exception)

		when:
		defaultExceptionTranslator.populate errorContext

        then:
		with errorContext, {
			message == exception.message
			httpStatus == HttpStatusCodes.BAD_REQUEST
			locale == DEFAULT_LOCALE
			errorCode == ERROR_CODE
		}
	}

	@Test
	def 'does not populate context containing exception other than IntegrationAttributeException'() {
		given:
		def exception = new IllegalArgumentException('error message')
		def errorContext = new ODataErrorContext(exception: exception)

		when:
		defaultExceptionTranslator.populate errorContext

		then:
		with errorContext, {
			! errorCode
			! message
			! httpStatus
			! locale
		}
	}

	@Test
	def 'handles IntegrationAttributeException'() {
		expect:
		defaultExceptionTranslator.exceptionClass.is IntegrationAttributeException
	}
}
