/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class MissingRequiredKeyAttributeValueExceptionUnitTest extends Specification {
    @Test
    def 'context can be retrieved from the exception'() {
        given:
        def request = Stub ItemSearchRequest
        def attribute = Stub TypeAttributeDescriptor

        expect:
        with(new MissingRequiredKeyAttributeValueException(request, attribute)) {
            rejectedRequest == request
            violatedAttribute == attribute
        }
    }

    @Test
    def 'message includes violated attribute information'() {
        given:
        def request = Stub ItemSearchRequest
        def attribute = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> 'someAttribute'
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> 'SomeItem'
            }
        }
        def exception = new MissingRequiredKeyAttributeValueException(request, attribute)

        expect:
        exception.message == "Required key attribute 'someAttribute' in item of 'SomeItem' type has no value"
    }
}
