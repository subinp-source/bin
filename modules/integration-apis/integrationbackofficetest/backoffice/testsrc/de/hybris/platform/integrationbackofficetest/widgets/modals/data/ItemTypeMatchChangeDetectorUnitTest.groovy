/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.data

import de.hybris.platform.integrationbackoffice.widgets.modals.data.ItemTypeMatchModal.ItemTypeMatchChangeDetector
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

class ItemTypeMatchChangeDetectorUnitTest extends Specification {

    @Unroll
    @Test
    def "When IntegrationObjectItemModel itemTypeMatchEnum is #itemTypeMatchIOI and the new value is #selectedItemTypeMatchCode then it is dirty #isDirtyResult"() {
        given:
        def integrationObjectItemModel = Stub(IntegrationObjectItemModel) {
            getItemTypeMatch() >> itemTypeMatchIOI
        }
        ItemTypeMatchChangeDetector itemTypeMatchChangeDetector = new ItemTypeMatchChangeDetector(integrationObjectItemModel, selectedItemTypeMatchCode)

        when:
        boolean isDirty = itemTypeMatchChangeDetector.isDirty()

        then:
        isDirty == isDirtyResult

        where:
        itemTypeMatchIOI                        | selectedItemTypeMatchCode                    | isDirtyResult
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE.code | false
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE | ItemTypeMatchEnum.ALL_SUBTYPES.code          | true
        null                                    | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE.code | true
    }
}
