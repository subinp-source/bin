/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultItemModelFactoryUnitTest extends Specification {
    def modelService = Stub ModelService
    def factory = new DefaultItemModelFactory(modelService: modelService)

    @Test
    def 'delegates item creation to ModelService'()
    {
        given: 'a context for type "TestItem"'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getItemType() >> Stub(TypeDescriptor) {
                    getTypeCode() >> 'TestItem'
                }
            }
        }
        and: 'the model service creates an item when "TestItem" is passed'
        def itemModel = Stub ItemModel
        modelService.create('TestItem') >> itemModel

        expect:
        factory.createItem(context).is itemModel
    }
}
