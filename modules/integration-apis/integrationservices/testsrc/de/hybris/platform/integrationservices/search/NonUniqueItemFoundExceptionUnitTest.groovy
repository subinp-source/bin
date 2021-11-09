/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class NonUniqueItemFoundExceptionUnitTest extends Specification {
    @Test
    def 'can be created with custom message and no context'() {
        given:
        def e = new NonUniqueItemFoundException('custom message')

        expect:
        with(e) {
            message == 'custom message'
            request == null
        }
    }

    @Test
    def 'can be created with context provided'() {
        given:
        def ioCode = 'IntegrationObject'
        def ioItemCode = 'Item'
        def integrationKey = 'abc-123'
        and:
        def request = Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.ofNullable(integrationItem(integrationKey))
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getIntegrationObjectCode() >> ioCode
                getItemCode() >> ioItemCode
            }
        }

        when:
        def e = new NonUniqueItemFoundException(request)

        then:
        e.message == "Multiple items found when unique item of type '$ioItemCode' in integration object '$ioCode' is searched by its key '$integrationKey'"
        e.request == request
    }

    IntegrationItem integrationItem(String key) {
        Stub(IntegrationItem) {
            getIntegrationKey() >> key
        }
    }
}
