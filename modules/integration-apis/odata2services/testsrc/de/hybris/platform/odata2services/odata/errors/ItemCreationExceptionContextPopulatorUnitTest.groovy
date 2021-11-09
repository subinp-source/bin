/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.populator.ItemCreationException
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ItemCreationExceptionContextPopulatorUnitTest extends Specification {
    def populator = new ItemCreationExceptionContextPopulator()

    @Test
    def 'does not populate context values if context does not contain ItemCreationException'() {
        given:
        def context = new ODataErrorContext(exception: new InvalidAttributeValueException('value', Stub(TypeAttributeDescriptor)))

        when:
        populator.populate context

        then:
        with(context) {
            !httpStatus
            !message
            !locale
            !errorCode
        }
    }

    @Test
    def 'populates context values'() {
        given:
        def itemType = 'String2StringMapType'
        def type = Stub(TypeDescriptor) {
            getTypeCode() >> itemType
        }
        def ex = new ItemCreationException(Stub(Throwable), type)
        def context = new ODataErrorContext(exception: ex)

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.INTERNAL_SERVER_ERROR
            locale == Locale.ENGLISH
            errorCode == "internal_error"
            message == "There was an error creating the new [String2StringMapType] model instance in the type system"
        }
    }
}
