/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.FilterByClassificationAttributeNotSupportedException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class FilterByClassificationAttributeNotSupportedExceptionContextPopulatorUnitTest extends Specification {
	def contextPopulator = new FilterByClassificationAttributeNotSupportedExceptionContextPopulator()

	@Test
	def "provides error context values for FilterByClassificationAttributeNotSupportedException"() {
		given:
		def attrName = "a"
		def classificationAttribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
			getAttributeName() >> attrName
		}

		def ex = new FilterByClassificationAttributeNotSupportedException(classificationAttribute)
		def context = new ODataErrorContext(exception: ex)

		when:
		contextPopulator.populate context

		then:
		with(context) {
			httpStatus == HttpStatusCodes.NOT_IMPLEMENTED
			locale == Locale.ENGLISH
			errorCode == 'filter_not_supported'
			message == "Filtering by classification attribute ${attrName} is not supported."
		}
	}

	@Test
	def "does not populate context when context exception is not an instanceof FilterByClassificationAttributeNotSupportedException"() {
		given:
		def ex = new Exception('IGNORE - test exception')
		def context = new ODataErrorContext(exception: ex)

		when:
		contextPopulator.populate context

		then:
		with(context) {
			! httpStatus
			! locale
			! errorCode
			! message
		}
	}

	@Test
	def 'handles FilterByClassificationAttributeNotSupportedException'() {
		expect:
		contextPopulator.exceptionClass == FilterByClassificationAttributeNotSupportedException
	}
}
