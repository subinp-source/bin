/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.dto

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import org.junit.Test
import spock.lang.Specification

@UnitTest
class OutboundItemUnitTest extends Specification {
    private static String ROOT_ITEM_TYPE = 'Product'
    private static String ROOT_ITEM_SUBTYPE = 'CustomProduct'
    private static String REFERENCED_ITEM_TYPE = 'Category'
    private static String REFERENCED_ITEM_SUBTYPE = 'CustomCategory'
    private static String NON_REFERENCED_ITEM_TYPE = 'Address'

    private ItemModel CHANGED_ITEM = Stub(ItemModel)

    @Test
    def 'retrieves type descriptor for item present in the IO model'() {
        given:
        CHANGED_ITEM = itemModel(REFERENCED_ITEM_TYPE)

        expect:
        outboundItem().typeDescriptor.present
    }

    @Test
    def 'does not retrieve type descriptor for item not present in the IO model'() {
        given:
        CHANGED_ITEM = itemModel(NON_REFERENCED_ITEM_TYPE)

        expect:
        !outboundItem().typeDescriptor.present
    }

    private ItemModel itemModel(String type) {
        Stub(ItemModel) {
            getItemtype() >> type
        }
    }

    private OutboundItem outboundItem() {
        OutboundItem.outboundItem()
                .withChangedItemModel(CHANGED_ITEM)
                .withIntegrationObject(integrationObject())
                .withChannelConfiguration(Stub(OutboundChannelConfigurationModel))
                .build()
    }

    private IntegrationObjectModel integrationObject() {
        Stub(IntegrationObjectModel) {
            getRootItem() >> integrationObjectItem(ROOT_ITEM_TYPE)
            getItems() >> [integrationObjectItem(ROOT_ITEM_TYPE), integrationObjectItem(REFERENCED_ITEM_TYPE)]
        }
    }

    private IntegrationObjectItemModel integrationObjectItem(String type) {
        Stub(IntegrationObjectItemModel) {
            getType() >> composedType(type)
            getCode() >> type
        }
    }

    private ComposedTypeModel composedType(String type) {
        Stub(ComposedTypeModel) {
            getCode() >> type
            getAllSubTypes() >> {
                switch (type) {
                    case ROOT_ITEM_TYPE:
                        return [composedType(ROOT_ITEM_SUBTYPE)]
                    case REFERENCED_ITEM_TYPE:
                        return [composedType(REFERENCED_ITEM_SUBTYPE)]
                    default: []
                }
            }
        }
    }
}
