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
class SettableForNewItemAttributeHandlerUnitTest extends Specification {
    //Test model definitions
    def modelService = Stub(ModelService)
    def handler = new SettableForNewItemAttributeHandler(modelService)

    //Testing isApplicable for new ItemModels
    @Test
    @Unroll
    def "isApplicableNew=#isApplicableNew and result=#result"() {
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
        true            | true
        false           | false
    }

    //Testing isSettable for new
    @Test
    @Unroll
    def "isInitial=#isInitial and isWritable=#isWritable and result=#result"() {
        given:
        def attributeDescriptor = Mock(AttributeDescriptorModel) {
            getInitial() >> isInitial
            getWritable() >> isWritable
        }


        expect:
        handler.isSettable(attributeDescriptor) == result

        where:
        isInitial | isWritable | result
        true      | true       | true
        null      | true       | true
        true      | null       | true
        true      | false      | true
        false     | true       | true
        false     | null       | false
        false     | false      | false
        null      | false      | false
        null      | null       | false
    }

}
