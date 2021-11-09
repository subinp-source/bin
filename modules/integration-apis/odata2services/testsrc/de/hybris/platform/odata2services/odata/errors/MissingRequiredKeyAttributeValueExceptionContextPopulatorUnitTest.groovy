/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.AttributePersistenceException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.validation.MissingRequiredKeyAttributeValueException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class MissingRequiredKeyAttributeValueExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new MissingRequiredKeyAttributeValueExceptionContextPopulator()

    @Test
    def 'does not populates context values if context does not contain MissingRequiredKeyAttributeValueException'() {
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
    @Unroll
    def "provides error context values for MissingRequiredKeyAttributeValueException"() {
        given:
        def itemKey = 'integration-key'
        def attributeName = 'attribute'
        def itemCode = 'Item'
        def attributeDesc = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> itemCode
                isPrimitive() >> primitive
            }
        }
        def request = Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.of(integrationItem(itemKey))
        }


        def ex = new MissingRequiredKeyAttributeValueException(request, attributeDesc)
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == 'missing_key'
            message == "Key $propType [$attributeName] is required for EntityType [$itemCode]."
            innerError == itemKey
        }

        where:
        primitive | propType
        true      | "Property"
        false     | "NavigationProperty"
    }

    IntegrationItem integrationItem(String key) {
        Stub(IntegrationItem) {
            getIntegrationKey() >> key
        }
    }
}
