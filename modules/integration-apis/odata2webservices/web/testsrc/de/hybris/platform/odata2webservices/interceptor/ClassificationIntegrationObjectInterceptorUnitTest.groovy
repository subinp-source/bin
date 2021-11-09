/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@UnitTest
class ClassificationIntegrationObjectInterceptorUnitTest extends Specification {

	def integrationObjectService = Stub(IntegrationObjectService)
	def interceptor = new ClassificationIntegrationObjectInterceptor(integrationObjectService: integrationObjectService)

	@Test
	@Unroll
	def "interceptor ignores #method request"() {
		given:
		def request = request(method)

		expect:
		interceptor.preHandle request, Stub(HttpServletResponse), new Object()

		where:
		method << ['get', 'delete', 'post', null]
	}

	@Test
	def "interceptor does not throw exception for patch request when integration object does not contain classification attributes"() {
		given:
		def io = 'serviceIO'
		def request = request('patch')
		request.getPathInfo() >> "context/$io"

		and: 'integration object does not contain classification attributes'
		integrationObjectService.findIntegrationObject(io) >> Stub(IntegrationObjectModel) {
			getClassificationAttributesPresent() >> false
		}

		expect:
		interceptor.preHandle request, Stub(HttpServletResponse), new Object()
	}

	@Test
	def "interceptor does not throw exception for patch request when integration object is not found"() {
		given:
		def io = 'serviceIO'
		def request = request('patch')
		request.getPathInfo() >> "context/$io"

		and: 'integration object is not found'
		integrationObjectService.findIntegrationObject(io) >> { throw new ModelNotFoundException('IGNORE - Test Exception') }

		expect:
		interceptor.preHandle request, Stub(HttpServletResponse), new Object()
	}

	@Test
	def "interceptor throws exception for patch request when integration object contains classification attributes"() {
		given:
		def io = 'serviceIO'
		def request = request('patch')
		request.getPathInfo() >> "context/$io"

		and: 'integration object does not contain classification attributes'
		integrationObjectService.findIntegrationObject(io) >> Stub(IntegrationObjectModel) {
			getClassificationAttributesPresent() >> true
		}

		when:
		interceptor.preHandle request, Stub(HttpServletResponse), new Object()

		then:
		def exception = thrown UpsertIntegrationObjectWithClassificationAttributeException
		exception.message == "Executing a PATCH request on an Integration Object configured with classification attributes is not currently supported."
	}

	def request(def method) {
		Stub(HttpServletRequest) {
			getMethod() >> method
		}
	}
}
