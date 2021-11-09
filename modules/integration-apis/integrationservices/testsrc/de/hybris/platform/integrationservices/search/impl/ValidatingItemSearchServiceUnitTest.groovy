/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.ItemSearchResult
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.search.enricher.ItemSearchRequestEnricher
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidationException
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidator
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ValidatingItemSearchServiceUnitTest extends Specification {
    @Shared
    def request = Stub ItemSearchRequest
    @Shared
    def enrichedRequest = Stub ItemSearchRequest

    def delegate = Mock ItemSearchService
    def validatingService = new ValidatingItemSearchService(delegate)

    @Test
    def 'delegate is required to instantiate the service'() {
        when:
        new ValidatingItemSearchService(null)

        then:
        def e = thrown IllegalArgumentException
        e.message == 'ItemSearchService to delegate search to is required'
    }

    @Test
    @Unroll
    def "delegates #method"() {
        given:
        delegate."$method"(request) >> result

        expect:
        validatingService."$method"(request) == result

        where:
        method           | result
        'findUniqueItem' | Optional.of(Stub(ItemModel))
        'findItems'      | ItemSearchResult.createWith([])
        'countItems'     | 100
    }

    @Test
    @Unroll
    def "rejects #method search if validation fails"() {
        given: 'a validator that invalidates a search request'
        validatingService."$validators" = [failingValidator()]

        when:
        validatingService."$method"(request)

        then:
        def e = thrown ItemSearchRequestValidationException
        e.rejectedRequest == request
        and:
        0 * delegate._

        where:
        method           | validators
        'findUniqueItem' | 'uniqueItemSearchValidators'
        'findItems'      | 'itemsSearchValidators'
        'countItems'     | 'countSearchValidators'
    }

    @Test
    @Unroll
    def "request is not enriched for #method when enrichers are not configured"() {
        when:
        validatingService."$method"(request)

        then:
        1 * delegate."$method"(request)

        where:
        method << ['findUniqueItem', 'findItems', 'countItems']
    }

    @Test
    @Unroll
    def "request is enriched for #method when enrichers are configured"() {
        given:
        validatingService."$enrichers" = [requestEnricher()]


        when:
        validatingService."$method"(request)

        then:
        1 * delegate."$method"(enrichedRequest)

        where:
        method           | enrichers
        'findUniqueItem' | 'uniqueItemSearchEnrichers'
        'findItems'      | 'itemsSearchEnrichers'
        'countItems'     | 'countSearchEnrichers'
    }

    @Test
    def "validators are called with the enriched request"() {
        given:
        validatingService.uniqueItemSearchEnrichers = [requestEnricher()]
        def validator = Mock(ItemSearchRequestValidator)
        validatingService.uniqueItemSearchValidators = [validator]

        when:
        validatingService.findUniqueItem(request)

        then:
        1 * validator.validate(enrichedRequest)
    }

    ItemSearchRequestValidator failingValidator(ItemSearchRequest req = request) {
        Stub(ItemSearchRequestValidator) {
            validate(req) >> { throw new ItemSearchRequestValidationException(req) }
        }
    }

    ItemSearchRequestEnricher requestEnricher(ItemSearchRequest req = request) {
        Stub(ItemSearchRequestEnricher) {
            enrich(req) >> enrichedRequest
        }
    }
}
