/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.enricher

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ImmutableItemSearchRequest
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ScriptServiceItemSearchRequestEnricherUnitTest extends Specification {

    def enricher = new ScriptServiceItemSearchRequestEnricher()

    @Test
    def 'can only enrich request when the integration object is ScriptService and item is a Script'() {
        given:
        def itemType = Stub(TypeDescriptor) {
            getIntegrationObjectCode() >> 'ScriptService'
            getTypeCode() >> 'Script'
        }

        def request = ImmutableItemSearchRequest.itemSearchRequestBuilder().withItemType(itemType).build()

        when:
        def enrichedRequest = enricher.enrich(request)

        then:
        !enrichedRequest.is(request)
        with(enrichedRequest.getTypeDescriptor()) {
            getIntegrationObjectCode() == 'ScriptService'
            getTypeCode() == 'Script'
        }
        with(enrichedRequest.getFilter().conditions) {
            size() == 1
            get(0) == SimplePropertyWhereClauseCondition.eq('active', 1)
        }
    }

    @Test
    @Unroll
    def "cannot enrich request when integration object is #io and item is of type #type"() {
        def itemType = Stub(TypeDescriptor) {
            getIntegrationObjectCode() >> io
            getTypeCode() >> type
        }

        def request = ImmutableItemSearchRequest.itemSearchRequestBuilder().withItemType(itemType).build()

        when:
        def enrichedRequest = enricher.enrich(request)

        then:
        enrichedRequest.is request

        where:
        io              | type
        ''              | 'Script'
        null            | 'Script'
        'SomeService'   | 'Script'
        'ScriptService' | ''
        'ScriptService' | null
        'ScriptService' | 'SomeOtherType'
    }
}
