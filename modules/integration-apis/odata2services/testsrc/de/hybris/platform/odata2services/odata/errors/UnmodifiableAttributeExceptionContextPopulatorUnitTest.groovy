/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.UnmodifiableAttributeException
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class UnmodifiableAttributeExceptionContextPopulatorUnitTest extends Specification {
    def populator = new UnmodifiableAttributeExceptionContextPopulator()

    @Test
    def 'does not populates context values if context does not contain UnmodifiableAttributeException'() {
        given:
        def context = new ODataErrorContext(exception: new InvalidAttributeValueException('value', Stub(TypeAttributeDescriptor)))

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
        def persistenceContext = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getIntegrationKey() >> 'item-key'
            }
        }
        def context = new ODataErrorContext(exception: new UnmodifiableAttributeException(attributeDesc, persistenceContext, null))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'unmodifiable_attribute'
            message == exception.message
            innerError == 'item-key'
            locale == Locale.ENGLISH
        }
    }

    @Test
    def 'handles UnmodifiableAttributeException'() {
        expect:
        populator.exceptionClass == UnmodifiableAttributeException
    }
}
