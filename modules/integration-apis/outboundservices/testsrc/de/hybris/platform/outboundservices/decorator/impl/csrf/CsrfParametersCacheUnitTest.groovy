/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl.csrf

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.regioncache.CacheValueLoader
import de.hybris.platform.regioncache.key.CacheKey
import de.hybris.platform.regioncache.region.CacheRegion
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CsrfParametersCacheUnitTest extends Specification {
    @Shared
    def parameters = Stub(CsrfParameters)
    @Shared
    def key = Stub(CsrfParametersCacheKey)

    def cacheRegion = Mock(CacheRegion)
    def cache = new CsrfParametersCache(cacheRegion: cacheRegion)

    @Test
    def 'get() returns a value when the value is present in the cache region'() {
        given:
        def key = CsrfParametersCacheKey.from Stub(ConsumedDestinationModel)
        def value = Stub(CsrfParameters)
        cacheRegion.get(key) >> value

        expect:
        cache.get(key) == value
    }

    @Test
    @Unroll
    def "get() returns null when #condition"() {
        given:
        cacheRegion.get(_ as CacheKey) >> cachedValue

        expect:
        cache.get(cacheKey) == null

        where:
        condition                              | cacheKey | cachedValue
        "there is no cached value for the key" | key      | null
        "cached value is not a CsrfParameters" | key      | new Object()
        "key is null"                          | null     | null
    }

    @Test
    @Unroll
    def "put() #res the CsrfParameters when the provided key is #condition"() {
        when:
        cache.put(cacheKey, parameters)

        then:
        times * cacheRegion.getWithLoader(cacheKey, _ as CacheValueLoader)

        where:
        res              | condition  | cacheKey | times
        'caches'         | 'not null' | key      | 1
        'does not cache' | 'is null'  | null     | 0
    }

    @Test
    @Unroll
    def "remove() returns #result when #condition"() {
        given:
        cacheRegion.invalidate(cacheKey, false) >> evictedValue

        expect:
        cache.remove(cacheKey) == result

        where:
        condition                              | cacheKey | evictedValue | result
        'there is a cached value for the key'  | key      | parameters   | parameters
        'there is no cached value for the key' | key      | null         | null
        'cached value is not a CsrfParameters' | key      | new Object() | null
        'key is null'                          | null     | null         | null
    }

    @Test
    @Unroll
    def "contains() returns #result when the key #condition"() {
        given:
        cacheRegion.containsKey(cacheKey) >> cacheRegionResult

        expect:
        cache.contains(cacheKey) == result

        where:
        condition        | cacheKey | cacheRegionResult | result
        "exists"         | key      | true              | true
        "does not exist" | key      | false             | false
        "is null"        | null     | true              | false
    }
}
