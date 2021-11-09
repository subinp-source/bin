/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.outboundservices.cache.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import org.junit.Test
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DestinationOauthRestTemplateIdUnitTest extends Specification {

	@Shared
	def destination1 = Stub(ConsumedDestinationModel)
	@Shared
	def destination2 = Stub(ConsumedDestinationModel)

	@Test
	def "id created"() {
		given:
		def id = DestinationOauthRestTemplateId.from(destination1)

		expect:
		with(id) {
			destination == destination1
			restTemplateType == OAuth2RestTemplate
		}
	}

	@Test
	def "create id with null destination"() {
		when:
		DestinationOauthRestTemplateId.from(null)

		then:
		thrown IllegalArgumentException
	}

	@Test
	@Unroll
	def "equals is #result when #condition"() {
		expect:
		(id1 == id2) == result

		where:
		condition 							| id1												| id2													| result
		"ids with same destination"			| DestinationOauthRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination1)		| true
		"ids with different destinations"	| DestinationOauthRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination2)		| false
		"other id is null"					| DestinationOauthRestTemplateId.from(destination1)	| null													| false
		"other id is a different type"		| DestinationOauthRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination1)	| false
	}

	@Test
	@Unroll
	def "hashcode is #result when #condition"() {
		expect:
		(id1 == id2) == result

		where:
		condition 							| id1												| id2													| result
		"ids with same destination"			| DestinationOauthRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination1)		| true
		"ids with different destinations"	| DestinationOauthRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination2)		| false
		"other id is a different type"		| DestinationOauthRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination1)	| false
	}
}
