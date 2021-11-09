/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class CannotCreateReferenceItemExceptionContextPopulatorUnitTest extends Specification {
    def populator = new CannotCreateReferenceItemExceptionContextPopulator()

    @Test
    def 'does not populates context values if context does not contain CannotCreateReferenceItemException'() {
        given:
        def attributeDesc = Stub(TypeAttributeDescriptor)
        def context = new ODataErrorContext(exception: new InvalidAttributeValueException('value', attributeDesc))

        when:
        populator.populate context

        then:
        with(context) {
            !httpStatus
            !errorCode
            !message
            !locale
            !innerError
        }
    }

    @Test
    def 'populates error context values'() {
        given:
        def attributeDesc = Stub(TypeAttributeDescriptor)
        def integrationKey = 'integration|key'
        def persistenceContext = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getIntegrationKey() >> integrationKey
            }
        }
        def exception = new CannotCreateReferencedItemException(attributeDesc, persistenceContext)
        def context = new ODataErrorContext(exception: exception)

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'missing_nav_property'
            message == exception.message
            locale == Locale.ENGLISH
            innerError == integrationKey
        }
    }

    @Test
    def 'handles CannotCreateReferenceItemException'() {
        expect:
        populator.exceptionClass == CannotCreateReferencedItemException
    }
}
