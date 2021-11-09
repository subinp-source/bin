/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.exception.MissingKeyReferencedAttributeValueException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class MissingKeyReferencedAttributeValueExceptionContextPopulatorUnitTest extends Specification {
    def populator = new MissingKeyReferencedAttributeValueExceptionContextPopulator()

    @Test
    def 'does not populate context values if context does not contain MissingKeyReferencedAttributeValueException'() {
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
        def attribute = Stub(IntegrationObjectItemAttributeModel)
        def context = new ODataErrorContext(exception: new MissingKeyReferencedAttributeValueException(attribute))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'missing_key'
            message == 'Key NavigationProperty [] is required for EntityType [null].'
            locale == Locale.ENGLISH
        }
    }

    @Test
    def 'handles MissingKeyReferencedAttributeValueException'() {
        expect:
        populator.exceptionClass == MissingKeyReferencedAttributeValueException
    }
}
