/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.outboundservices.cache.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateCacheKey
import de.hybris.platform.regioncache.CacheValueLoader
import de.hybris.platform.regioncache.key.CacheKey
import de.hybris.platform.regioncache.region.CacheRegion
import org.junit.Test
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DestinationRestTemplateCacheUnitTest extends Specification {

	@Shared
	def restTemplate = Stub(RestTemplate)
	@Shared
	def key = Stub(DestinationRestTemplateCacheKey)

	def cacheRegion = Mock(CacheRegion)
	def cache = Spy(DestinationRestTemplateCache)

	def setup() {
		cache.cacheRegion = cacheRegion
		cache.toCacheKey(_ as DestinationRestTemplateCacheKey) >> Stub(CacheKey)
	}

	@Test
	@Unroll
	def "get returns #result when #condition"() {
		given:
		cacheRegion.get(_ as CacheKey) >> cacheRegionResult
		def getCache = { cache.get(cacheKey as DestinationRestTemplateCacheKey) }

		expect:
		getCache() == result

		where:
		condition								| cacheKey	| cacheRegionResult	| result
		"there is a cached item for the key"	| key		| restTemplate 		| restTemplate
		"there is not cached item for the key"	| key		| null 				| null
		"cached item is not a rest template"	| key		| [] 				| null
		"key is null"							| null		| null				| null
	}

	@Test
	@Unroll
	def "rest template is #condition"() {
		when:
		cache.put(cacheKey, restTemplate)

		then:
		times * cacheRegion.getWithLoader(_ as CacheKey, _ as CacheValueLoader)

		where:
		condition						| cacheKey	| times
		"cached when key is not null"	| key		| 1
		"not cached when key is null"	| null		| 0
	}

	@Test
	@Unroll
	def "remove returns #result when #condition"() {
		given:
		cacheRegion.invalidate(_ as CacheKey, false) >> cacheRegionResult
		def removeCache = { cache.remove(cacheKey as DestinationRestTemplateCacheKey) }

		expect:
		removeCache() == result

		where:
		condition								| cacheKey	| cacheRegionResult	| result
		"there is a cached item for the key"	| key		| restTemplate 		| restTemplate
		"there is not cached item for the key"	| key		| null 				| null
		"cached item is not a rest template"	| key		| [] 				| null
		"key is null"							| null		| null				| null
	}

	@Test
	@Unroll
	def "contains returns #result when the key #condition"() {
		given:
		cacheRegion.containsKey(_ as CacheKey) >> cacheRegionResult
		def containsKey = { cache.contains(cacheKey as DestinationRestTemplateCacheKey) }

		expect:
		containsKey() == result

		where:
		condition			| cacheKey	| cacheRegionResult	| result
		"exists"			| key		| true 				| true
		"does not exist"	| key		| false 			| false
		"is null"			| null		| true				| false
	}
}
