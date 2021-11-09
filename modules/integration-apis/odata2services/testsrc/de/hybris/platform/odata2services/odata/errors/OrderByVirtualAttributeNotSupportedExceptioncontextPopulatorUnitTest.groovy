/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.OrderByVirtualAttributeNotSupportedException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class OrderByVirtualAttributeNotSupportedExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new OrderByVirtualAttributeNotSupportedExceptionContextPopulator()

    @Test
    def "provides error context values for OrderByVirtualAttributeNotSupportedException"() {
        given:
        def attribute = Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> 'attrName'
        }
        and:
        def ex = new OrderByVirtualAttributeNotSupportedException(attribute)
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == 'order_by_virtual_attribute_not_supported'
            message == ex.message
        }
    }

    @Test
    def "does not populate context when context exception is not an instanceof OrderByVirtualAttributeNotSupportedException"() {
        given:
        def ex = new Exception('IGNORE - test exception')
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            ! httpStatus
            ! locale
            ! errorCode
            ! message
        }
    }

    @Test
    def 'handles OrderByVirtualAttributeNotSupportedException'() {
        expect:
        contextPopulator.exceptionClass == OrderByVirtualAttributeNotSupportedException
    }
}