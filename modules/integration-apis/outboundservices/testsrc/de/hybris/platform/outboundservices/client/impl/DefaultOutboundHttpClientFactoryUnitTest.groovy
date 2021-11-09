/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultOutboundHttpClientFactoryUnitTest extends Specification
{
	def outboundHttpClientFactory = Spy(DefaultOutboundHttpClientFactory)

	def setup() {
		outboundHttpClientFactory.setMaxConnections(4)
	}

	@Test
	def "factory creates http client"()
	{
		given:
		def httpClient = Stub(CloseableHttpClient)
		def httpClientBuilder = Stub(HttpClientBuilder) {
			build() >> httpClient
		}
		outboundHttpClientFactory.getHttpClientBuilder() >> httpClientBuilder

		when:
		def actualClient = outboundHttpClientFactory.create()

		then:
		httpClient == actualClient
	}

	@Test
	def "maximum connections is set"()
	{
		expect:
		4 == outboundHttpClientFactory.getMaxConnections()
	}

	@Test
	def "factory creates client with connection manager"() {
		
		when:
		def client = outboundHttpClientFactory.create()

		then:
		client.connectionManager != null
	}
}
