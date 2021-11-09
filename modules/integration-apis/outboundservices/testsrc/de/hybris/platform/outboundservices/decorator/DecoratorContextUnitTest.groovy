/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.outboundservices.enums.OutboundSource
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DecoratorContextUnitTest extends Specification {
    def ITEM = Stub(ItemModel)
    def IO_CODE = 'TestObject'

    @Test
    def 'derives integration object code'() {
        when:
        def context = contextBuilder().withIntegrationObject(integrationObject(IO_CODE)).build()

        then:
        context.integrationObjectCode == IO_CODE
    }

    @Test
    def 'derives context integration object item when a matching item exists in the integration object'() {
        given: 'integration object contains an item matching the item model type'
        def itemCode = 'SomeItem'
        def item = Stub(TypeDescriptor) {
            getItemCode() >> itemCode
        }
        def io = Stub(IntegrationObjectDescriptor) {
            getItemTypeDescriptor(ITEM) >> Optional.of(item)
        }
        when:
        def context = contextBuilder()
                .withItemModel(ITEM)
                .withIntegrationObject(io)
                .build()

        then:
        context.integrationObjectItem == Optional.of(item)
        context.integrationObjectItemCode == itemCode
    }

    @Test
    def 'does not derive a context integration object item when a matching item not found in the integration object'() {
        given: 'integration object has no matching item for the item model type'
        def io = Stub(IntegrationObjectDescriptor) {
            getItemTypeDescriptor(ITEM) >> Optional.empty()
        }
        when:
        def context = contextBuilder()
                .withItemModel(ITEM)
                .withIntegrationObject(io)
                .build()

        then:
        context.integrationObjectItem.empty
        context.integrationObjectItemCode == ''
    }

    DecoratorContext.DecoratorContextBuilder contextBuilder() {
        DecoratorContext.decoratorContextBuilder()
                .withItemModel(ITEM)
                .withDestinationModel(new ConsumedDestinationModel())
                .withOutboundSource(OutboundSource.OUTBOUNDSYNC)
    }

    IntegrationObjectDescriptor integrationObject(String ioCode = IO_CODE) {
        Stub(IntegrationObjectDescriptor) {
            getCode() >> ioCode
        }
    }
}
