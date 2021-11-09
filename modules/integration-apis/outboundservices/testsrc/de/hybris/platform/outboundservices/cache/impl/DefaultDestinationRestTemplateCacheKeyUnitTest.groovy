/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.outboundservices.cache.impl

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultDestinationRestTemplateCacheKeyUnitTest extends Specification {

	@Shared
	def id1 = Stub(DestinationOauthRestTemplateId)
	@Shared
	def id2 = Stub(DestinationOauthRestTemplateId)

	@Test
	def "cache key created"() {
		when:
		def key = DefaultDestinationRestTemplateCacheKey.from(id1)

		then:
		key.id == id1
	}

	@Test
	def "create cache key throws exception when id is null"() {
		when:
		DefaultDestinationRestTemplateCacheKey.from(null)

		then:
		thrown IllegalArgumentException
	}

	@Test
	@Unroll
	def "equals is #result when #condition"() {
		expect:
		(key1 == key2) == result

		where:
		condition 						| key1												| key2												| result
		"keys with same ids"			| DefaultDestinationRestTemplateCacheKey.from(id1)	| DefaultDestinationRestTemplateCacheKey.from(id1)	| true
		"keys with different ids"		| DefaultDestinationRestTemplateCacheKey.from(id1)	| DefaultDestinationRestTemplateCacheKey.from(id2)	| false
		"other key is null"				| DefaultDestinationRestTemplateCacheKey.from(id1)	| null												| false
		"other key is a different type"	| DefaultDestinationRestTemplateCacheKey.from(id1)	| new Object()										| false
	}

	@Test
	@Unroll
	def "hashcode is #result when #condition"() {
		expect:
		(key1.hashCode() == key2.hashCode()) == result

		where:
		condition 						| key1												| key2												| result
		"keys with same ids"			| DefaultDestinationRestTemplateCacheKey.from(id1)	| DefaultDestinationRestTemplateCacheKey.from(id1)	| true
		"keys with different ids"		| DefaultDestinationRestTemplateCacheKey.from(id1)	| DefaultDestinationRestTemplateCacheKey.from(id2)	| false
		"other key is a different type"	| DefaultDestinationRestTemplateCacheKey.from(id1)	| new Object()										| false
	}
}
