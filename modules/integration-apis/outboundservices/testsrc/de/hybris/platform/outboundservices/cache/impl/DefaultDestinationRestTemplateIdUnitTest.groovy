/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.outboundservices.cache.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import org.junit.Test
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultDestinationRestTemplateIdUnitTest extends Specification {

	@Shared
	def destination1 = Stub(ConsumedDestinationModel)
	@Shared
	def destination2 = Stub(ConsumedDestinationModel)

	@Test
	def "id created"() {
		given:
		def id = DefaultDestinationRestTemplateId.from(destination1)

		expect:
		with(id) {
			destination == destination1
			restTemplateType == RestTemplate
		}
	}

	@Test
	def "create id with null destination"() {
		when:
		DefaultDestinationRestTemplateId.from(null)

		then:
		thrown IllegalArgumentException
	}

	@Test
	@Unroll
	def "equals is #result when #condition"() {
		expect:
		(id1 == id2) == result

		where:
		condition 							| id1													| id2													| result
		"ids with same destination"			| DefaultDestinationRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination1)	| true
		"ids with different destinations"	| DefaultDestinationRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination2)	| false
		"other id is null"					| DefaultDestinationRestTemplateId.from(destination1)	| null													| false
		"other id is a different type"		| DefaultDestinationRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination1)		| false
	}

	@Test
	@Unroll
	def "hashcode is #result when #condition"() {
		expect:
		(id1 == id2) == result

		where:
		condition 							| id1													| id2													| result
		"ids with same destination"			| DefaultDestinationRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination1)	| true
		"ids with different destinations"	| DefaultDestinationRestTemplateId.from(destination1)	| DefaultDestinationRestTemplateId.from(destination2)	| false
		"other id is a different type"		| DefaultDestinationRestTemplateId.from(destination1)	| DestinationOauthRestTemplateId.from(destination1)		| false
	}
}
