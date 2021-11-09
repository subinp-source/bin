/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl.csrf

import com.google.common.collect.Maps
import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.outboundservices.decorator.CSRFTokenFetchingException
import de.hybris.platform.outboundservices.decorator.DecoratorContext
import de.hybris.platform.outboundservices.decorator.DecoratorExecution
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultCsrfOutboundRequestDecoratorUnitTest extends Specification {
	private static final String TEST_URL = "http://test.url"
	private static final String TEST_TOKEN = "asdf-1234"
	private static final String TEST_COOKIES = "cookieForTheTest=thisCookie"

	def csrfParametersCache = Mock(CsrfParametersCache)
	def restOperations = Mock(RestOperations)
	def decoratorExecution = Mock(DecoratorExecution)

	def decorator = new DefaultCsrfOutboundRequestDecorator().tap {
		integrationRestTemplateFactory = Stub(IntegrationRestTemplateFactory) {
			create(_ as ConsumedDestinationModel) >> restOperations
		}
		cache = csrfParametersCache
	}

	def parameters = Stub(CsrfParameters) {
		getCsrfToken() >> TEST_TOKEN
		getCsrfCookie() >> TEST_COOKIES
	}

	@Test
	def "sets csrf token in headers when additionalProperties contains csrf url and csrf token is found in cache"() {
		given: 'csrf parameters are cached'
		csrfParametersCache.get(_ as CsrfParametersCacheKey) >> parameters
		and: 'destination contains CSRF URL'
		def decoratorContext = Stub(DecoratorContext) {
			getDestinationModel() >> Stub(ConsumedDestinationModel)  {
				getAdditionalProperties() >> [csrfURL: TEST_URL]
			}
		}

		when:
		decorator.decorate(new HttpHeaders(), [:], decoratorContext, decoratorExecution)

		then:
		0 * restOperations.exchange(TEST_URL, HttpMethod.GET, _, Map)
		0 * csrfParametersCache.put(_, _)
		1 * decoratorExecution.createHttpEntity(_, _, decoratorContext) >> { args ->
			assert args[0]['X-CSRF-Token'][0] == TEST_TOKEN
			assert args[0]['Cookie'][0] == TEST_COOKIES
		}
	}

	@Test
	def "sets csrf token in headers when additionalProperties contains destination token url and csrf token is not in cache"() {
		given:
		def decoratorContext = Stub(DecoratorContext) {
			getDestinationModel() >> Stub(ConsumedDestinationModel) {
				getAdditionalProperties() >> [csrfURL: TEST_URL]
			}
		}

		when:
		decorator.decorate(new HttpHeaders(), [:], decoratorContext, decoratorExecution)

		then:
		1 * restOperations.exchange(TEST_URL, HttpMethod.GET, _, Map) >> responseWithCsrfTokenHeader([TEST_TOKEN], [TEST_COOKIES])
		1 * csrfParametersCache.put(_, _)
		1 * decoratorExecution.createHttpEntity(_, _, decoratorContext) >> { args ->
			assert args[0]['X-CSRF-Token'][0] == TEST_TOKEN
			assert args[0]['Cookie'][0] == TEST_COOKIES
		}
	}

	@Test
	def "csrf token is not set in the headers when additionalProperties does not contain destination token url"() {
		given:
		def decoratorContext = Stub(DecoratorContext) {
			getDestinationModel() >> Stub(ConsumedDestinationModel) {
				getAdditionalProperties() >> Collections.emptyMap()
			}
		}
		when:
		decorator.decorate(new HttpHeaders(), [:], decoratorContext, decoratorExecution)

		then:
		0 * restOperations.exchange(TEST_URL, HttpMethod.GET, _, Map)
		0 * csrfParametersCache._
		1 * decoratorExecution.createHttpEntity(_, _, decoratorContext) >> { args ->
			assert args[0].get('X-CSRF-Token') == null
		}
	}

	@Test
	@Unroll
	def "exception is thrown when csrf #parameter is empty"() {
		given:
		def decoratorContext = Stub(DecoratorContext) {
			getDestinationModel() >> Stub(ConsumedDestinationModel) {
				getAdditionalProperties() >> [csrfURL: TEST_URL]
			}
		}

		when:
		decorator.decorate(new HttpHeaders(), Maps.newHashMap(), decoratorContext, decoratorExecution)

		then:
		1 * restOperations.exchange(TEST_URL, HttpMethod.GET, _, Map) >> responseWithCsrfTokenHeader(tokens, cookies)

		then:
		thrown(CSRFTokenFetchingException)

		where:
		parameter | tokens       | cookies
		'token'   | []           | [TEST_COOKIES]
		'cookie'  | [TEST_TOKEN] | []
	}

	def responseWithCsrfTokenHeader(List<String> token, List<String> cookies) {
		Stub(ResponseEntity) {
			getHeaders() >> new HttpHeaders().tap {
				addAll 'X-CSRF-Token', token
				addAll 'Set-Cookie', cookies
			}
		}
	}
}
