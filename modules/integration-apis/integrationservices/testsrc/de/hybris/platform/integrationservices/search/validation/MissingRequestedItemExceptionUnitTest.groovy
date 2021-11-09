/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class MissingRequestedItemExceptionUnitTest extends Specification {
    @Test
    def 'instantiates exception with request'() {
        given:
        def request = Stub ItemSearchRequest

        when:
        def e = new MissingRequestedItemException(request)

        then:
        e.message == "Key is not specified for a unique item search: $request"
        e.rejectedRequest == request
    }
}
