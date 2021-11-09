/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.validator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class RootExistsValidatorUnitTest extends Specification {

    def validator = new RootExistsValidator()

    @Test
    @Unroll
    def "no warning logged when integration object's items=#items and rootItem=#rootItem"() {
        given:
        def io = Mock(IntegrationObjectModel) {
            getRootItem() >> rootItem
            getItems() >> items
        }

        when:
        validator.onValidate io, Stub(InterceptorContext)

        then:
        0 * io.getCode()

        where:
        items                              | rootItem
        null                               | null
        []                                 | null
        [Stub(IntegrationObjectItemModel)] | Stub(IntegrationObjectItemModel)
    }

    @Test
    def 'log warning when integration object has no root item'() {
        given:
        def io = Mock(IntegrationObjectModel) {
            getRootItem() >> null
            getItems() >> [Stub(IntegrationObjectItemModel)]
            getCode() >> 'IO'
        }

        when:
        validator.onValidate io, Stub(InterceptorContext)

        then:
        1 * io.getCode()
    }

    @Test
    def "catch and log exception when integration object has multiple roots"() {
        given:
        def exception = Mock(SingleRootItemConstraintViolationException)

        and: 'An IO that has multiple roots'
        def io = Stub(IntegrationObjectModel) {
            getRootItem() >> { throw exception }
            getItems() >> [Stub(IntegrationObjectItemModel)]
        }

        when:
        validator.onValidate io, Stub(InterceptorContext)

        then:
        1 * exception.getMessage() >> 'Testing exception'
    }
}
