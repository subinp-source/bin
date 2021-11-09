/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.FlexibleSearchQueryBuilder
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification

@UnitTest
class DefaultFlexSearchQueryGeneratorUnitTest extends Specification {

    private static final def QUERY = new FlexibleSearchQuery('')
    @Shared
    private def REQUEST = Stub ItemSearchRequest

    def customBuilderFactory = Stub(FlexSearchQueryBuilderFactory) {
        createQueryBuilder(REQUEST) >> Stub(FlexibleSearchQueryBuilder) {
            build() >> QUERY
        }
    }
    def generator = new DefaultFlexSearchQueryGenerator(queryBuilderFactory: customBuilderFactory)

    @Test
    def 'generates flexible search query by delegating to the builder produced by the builder factory'() {
        expect:
        generator.generate(REQUEST) == QUERY
    }
}
