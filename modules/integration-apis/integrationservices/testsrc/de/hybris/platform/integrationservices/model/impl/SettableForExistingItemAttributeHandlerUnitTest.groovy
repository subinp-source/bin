/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SettableForExistingItemAttributeHandlerUnitTest extends Specification {
    //Test model definitions
    def modelService = Stub(ModelService)
    def handler = new SettableForExistingItemAttributeHandler(modelService)

    //Testing isApplicable for Existing ItemModels
    @Test
    @Unroll
    def "isApplicableExisting=#isApplicableExisting and result=#result"() {
        given:
        def itemType = 'String'
        def item = Stub(ItemModel) {
            getItemtype() >> itemType
        }

        modelService.isNew(_) >> isApplicableNew


        expect:
        handler.isApplicable(item) == result

        where:
        isApplicableNew | result
        true            | false
        false           | true
    }

    //Testing isSettable for Existing
    @Test
    @Unroll
    def "isInitial=#isInitial and isWritable=#isWritable and result=#result"() {
        given:
        def attributeDescriptor = Stub(AttributeDescriptorModel) {
            getWritable() >> isWritable
        }

        expect:
        handler.isSettable(attributeDescriptor) == result

        where:
        isWritable | result
        true       | true
        false      | false
        null       | false
    }
}
