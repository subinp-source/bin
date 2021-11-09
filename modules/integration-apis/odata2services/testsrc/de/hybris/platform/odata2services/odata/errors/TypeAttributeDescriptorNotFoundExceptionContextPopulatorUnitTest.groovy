/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class TypeAttributeDescriptorNotFoundExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new TypeAttributeDescriptorNotFoundExceptionContextPopulator()

    @Test
    def "provides error context values for TypeAttributeDescriptorNotFoundException"() {
        given:
        def attributeName = "catalog"
        def itemCode = "SomeCatalog"
        def objCode = "IO"
        def itemDescriptor = Stub(TypeDescriptor) {
            getItemCode() >> itemCode
            getIntegrationObjectCode() >> objCode
        }
        def ex = new TypeAttributeDescriptorNotFoundException(itemDescriptor, attributeName)
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == "missing_attribute"
            message == "Attribute [$attributeName] is not defined for Item [$itemCode] in IntegrationObject [$objCode]."
        }
    }

    @Test
    def "does not populate context when context exception is not an instanceof TypeAttributeDescriptorNotFoundException"() {
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
    def 'handles TypeAttributeDescriptorNotFoundException'() {
        expect:
        contextPopulator.exceptionClass == TypeAttributeDescriptorNotFoundException
    }
}
