/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.router.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.ReferencePath
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.outboundsync.dto.OutboundItem
import de.hybris.platform.outboundsync.dto.OutboundItemChange
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.job.OutboundItemFactory
import org.junit.Test
import spock.lang.Specification

@UnitTest
class RootItemPKPopulatorUnitTest extends Specification {
    private static final Long CHANGED_ITEM_PK = 1L
    private static final Long ROOT_ITEM_PK = 3L
    private static final Long ROOT_ITEM_PK2 = 5L

    // If you're getting the 'Update to non-static final' error when running this test in Intellij
    // remove the 'final' keyword for the time being. The test runs fine in the terminal, just not in Intellij.
    // See this issue https://github.com/spockframework/spock/issues/1011.
    private OutboundItemDTO DTO = OutboundItemDTO.Builder.item()
            .withItem(Stub(OutboundItemChange) { getPK() >> CHANGED_ITEM_PK })
            .withIntegrationObjectPK(100)
            .withChannelConfigurationPK(200)
            .withCronJobPK(PK.fromLong(300))
            .build()

    RootItemPKPopulator populator = new RootItemPKPopulator()
    TypeDescriptor rootItem = Stub(TypeDescriptor)
    OutboundItem outboundItem

    def setup() {
        outboundItem = Stub(OutboundItem) {
            getChangedItemModel() >> Optional.of(itemModel(CHANGED_ITEM_PK))
            getIntegrationObject() >> Stub(IntegrationObjectDescriptor) {
                getRootItemType() >> { Optional.ofNullable(rootItem) }
            }
        }
        populator.itemFactory = Stub(OutboundItemFactory) {
            createItem(DTO) >> outboundItem
        }
    }

    @Test
    def "item that is not the root and has a reference to the root"() {
        given:
        def item = itemModel(ROOT_ITEM_PK)
        outboundItem.getTypeDescriptor() >> typeDescriptor(referencePath(item))

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == ROOT_ITEM_PK
    }

    @Test
    def "item that is not the root and has references to multiple roots via collection"() {
        given:
        outboundItem.getTypeDescriptor() >> typeDescriptor(referencePath([itemModel(ROOT_ITEM_PK), itemModel(ROOT_ITEM_PK2)]))

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 2
        dtos.any { it.rootItemPK == ROOT_ITEM_PK }
        dtos.any { it.rootItemPK == ROOT_ITEM_PK2 }
    }

    @Test
    def "item that is not the root and has multiple attribute references roots via different properties"() {
        given:
        outboundItem.getTypeDescriptor() >> typeDescriptor([referencePath(itemModel(ROOT_ITEM_PK)), referencePath(itemModel(ROOT_ITEM_PK2))])

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 2
        dtos.any { it.rootItemPK == ROOT_ITEM_PK }
        dtos.any { it.rootItemPK == ROOT_ITEM_PK2 }
    }

    @Test
    def "does not populate root PK for item that doesn't exist anymore"() {
        given:
        outboundItem.getChangedItemModel() >> Optional.empty()

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == null
    }

    @Test
    def "does not populate root PK when item that does not have a reference to the root"() {
        given:
        outboundItem.getTypeDescriptor() >> typeDescriptor([])

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == null
    }

    @Test
    def "root item PK should be own item PK when IO does not have root item type"() {
        given:
        rootItem = null

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == CHANGED_ITEM_PK
    }

    @Test
    def "root item PK should be own item PK when root item is null"() {
        given:
        outboundItem.getTypeDescriptor() >> typeDescriptor(referencePath([null]))

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == CHANGED_ITEM_PK
    }

    @Test
    def "does not populate root PK when item is not in the IO definition"() {
        given:
        outboundItem.getTypeDescriptor() >> Optional.empty()

        when:
        def dtos = populator.populatePK(DTO)

        then:
        dtos.size() == 1
        dtos[0].rootItemPK == null
    }

    private Optional<TypeDescriptor> typeDescriptor(ReferencePath path) {
        typeDescriptor([path])
    }

    private Optional<TypeDescriptor> typeDescriptor(List<ReferencePath> rootPaths) {
        Optional.of Stub(TypeDescriptor) {
            getPathsToRoot() >> rootPaths
        }
    }

    private ItemModel itemModel(Long pk) {
        Stub(ItemModel) {
            getPk() >> PK.fromLong(pk)
        }
    }

    private ReferencePath referencePath(ItemModel itemModel) {
        referencePath([itemModel])
    }

    private ReferencePath referencePath(List<ItemModel> items) {
        Stub(ReferencePath) {
            execute(_) >> items
        }
    }
}
