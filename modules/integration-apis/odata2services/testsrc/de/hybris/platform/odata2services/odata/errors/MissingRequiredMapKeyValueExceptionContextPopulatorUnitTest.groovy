/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.AttributePersistenceException
import de.hybris.platform.inboundservices.persistence.populator.MissingRequiredMapKeyValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class MissingRequiredMapKeyValueExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new MissingRequiredMapKeyValueExceptionContextPopulator()

    @Test
    def 'does not populates context values if context does not contain MissingRequiredMapKeyValueException'() {
        given:
        def context = new ODataErrorContext(exception: new AttributePersistenceException('message [%s %s]', Stub(TypeAttributeDescriptor), Stub(PersistenceContext)))

        when:
        contextPopulator.populate context

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
    def "provides error context values for MissingRequiredMapKeyValueException"() {
        given:
        def itemKey = 'item-key'
        def attributeName = 'specialTreatmentClasses'
        def itemCode = 'Product'
        def itemType = 'String2StringMapType'
        def attributeDesc = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> itemCode
            }
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> itemType
            }
        }
        def persistenceContext = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getIntegrationKey() >> itemKey
            }
        }


        def ex = new MissingRequiredMapKeyValueException(attributeDesc, persistenceContext)
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        def suffix = '.key'
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == 'missing_property'
            message == "Property [$attributeName$suffix] of type $itemType is required for EntityType [$itemCode]."
            innerError == itemKey
        }
    }
}
