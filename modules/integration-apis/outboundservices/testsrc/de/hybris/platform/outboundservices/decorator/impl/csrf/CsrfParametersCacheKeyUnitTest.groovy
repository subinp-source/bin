/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl.csrf

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.PK
import de.hybris.platform.regioncache.key.CacheUnitValueType
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CsrfParametersCacheKeyUnitTest extends Specification {
    @Shared
    def destination = Stub(ConsumedDestinationModel) {
        getPk() >> PK.fromLong(1L)
        getTenantId() >> 'test-tenant'
        getItemtype() >> 'ConsumedDestination'
    }

    @Test
    def 'throws exception if attempted to be created with a null destination'() {
        when:
        CsrfParametersCacheKey.from null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def 'provides access to the consumed destination attributes'() {
        given:
        def key = CsrfParametersCacheKey.from destination

        expect:
        key.id == destination.getPk()
        key.tenantId == destination.getTenantId()
        key.typeCode == destination.getItemtype()
        key.cacheValueType == CacheUnitValueType.NON_SERIALIZABLE
    }

    @Test
    @Unroll
    def "not equal when the other #condition"() {
        given:
        def sample = CsrfParametersCacheKey.from destination

        expect:
        sample != other

        where:
        condition                   | other
        'is null'                   | null
        'has non-equal destination' | CsrfParametersCacheKey.from(Stub(ConsumedDestinationModel))
        'has different class'       | csrfParametersCacheSubclassKey(destination)
    }

    @Test
    @Unroll
    def "equal when the other #condition"() {
        expect:
        sample == other

        where:
        condition               | sample                                   | other
        'is same'               | CsrfParametersCacheKey.from(destination) | sample
        'has equal destination' | CsrfParametersCacheKey.from(destination) | CsrfParametersCacheKey.from(destination)
    }

    @Test
    @Unroll
    def "hashCodes not equal when the other #condition"() {
        given:
        def sample = CsrfParametersCacheKey.from destination

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition                   | other
        'has non-equal destination' | CsrfParametersCacheKey.from(Stub(ConsumedDestinationModel))
        'has different class'       | csrfParametersCacheSubclassKey(destination)
    }

    @Test
    def 'hashCodes are equal when the other is equal'() {
        given:
        def sample = CsrfParametersCacheKey.from destination
        def other = CsrfParametersCacheKey.from destination

        expect:
        sample.hashCode() == other.hashCode()
    }

    private CsrfParametersCacheKey csrfParametersCacheSubclassKey(ConsumedDestinationModel dest) {
        Stub(CsrfParametersCacheKey) {
            getId() >> dest.getPk()
            getTenantId() >> dest.getTenantId()
            getTypeCode() >> dest.getItemtype()
            getCacheValueType() >> CacheUnitValueType.NON_SERIALIZABLE
        }
    }
}
