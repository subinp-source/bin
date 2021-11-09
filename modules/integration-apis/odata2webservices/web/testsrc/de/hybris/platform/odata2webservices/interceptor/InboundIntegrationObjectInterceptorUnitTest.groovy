/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.odata2webservices.enums.IntegrationType
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import org.apache.http.HttpStatus
import org.junit.Test
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

@UnitTest
class InboundIntegrationObjectInterceptorUnitTest extends Specification {
	private static final String IO = 'IntegrationObj'

	def response = new MockHttpServletResponse()
	def service = Mock(IntegrationObjectService)

	def interceptor = new InboundIntegrationObjectInterceptor(integrationObjectService: service)

	@Test
	@Unroll
	def "preHandle() result is false when request URI is invalid: #uri"() {
		given:
		service.findIntegrationObject('') >> { throw new IllegalArgumentException() }

		expect:
		!interceptor.preHandle(request(uri), response, null)

		where:
		uri << [null, '', '/', '//', '//Products']
	}

	@Test
	def 'preHandle() result is false when integration object is not found for the request URI'() {
		given:
		service.findIntegrationObject(IO) >> { throw new ModelNotFoundException('') }

		expect:
		!interceptor.preHandle(request(), response, null)
		response.getStatus() == HttpStatus.SC_NOT_FOUND
	}

	@Test
	@Unroll
	def "preHandle() result is true if #uri references existing INBOUND integration object"() {
		given:
		service.findIntegrationObject(IO) >> Stub(IntegrationObjectModel) {
			getIntegrationType() >> IntegrationType.INBOUND
		}

		expect:
		interceptor.preHandle(request(), response, null)

		where:
		uri << ["/$IO", "/$IO/", "/$IO/Products"]
	}

	@Test
	def 'postHandle() does nothing'()
	{
		when:
		interceptor.postHandle(null, null, null, null)

		then:
		0 * service._
	}

	@Test
	def 'afterCompletion() does nothing'() {
		when:
		interceptor.afterCompletion(null, null, null, null)

		then:
		0 * service._
	}

	private HttpServletRequest request(String uri="/$IO") {
		Stub(HttpServletRequest) {
			getPathInfo() >> uri
		}
	}
}
