/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl

import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundsync.dto.OutboundItemChange
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException
import de.hybris.platform.servicelayer.model.ModelService
import spock.lang.Specification

class DefaultOutboundItemFactoryUnitTest extends Specification {
    private static Long itemPk = 1L
    private static Long ioPk = 2L
    private static Long channelPk = 3L
    private final def dto = OutboundItemDTO.Builder.item()
            .withItem(Stub(OutboundItemChange) { getPK() >> itemPk })
            .withIntegrationObjectPK(ioPk)
            .withChannelConfigurationPK(channelPk)
            .build()

    private DefaultOutboundItemFactory itemFactory = new DefaultOutboundItemFactory()

    def "creates outbound item"() {
        given:
        itemFactory.modelService = Stub(ModelService) {
            get(PK.fromLong(itemPk)) >> Stub(ItemModel)
            get(PK.fromLong(ioPk)) >> Stub(IntegrationObjectModel)
            get(PK.fromLong(channelPk)) >> Stub(OutboundChannelConfigurationModel)
        }

        when:
        def item = itemFactory.createItem(dto)

        then:
        item.changedItemModel.present
        item.integrationObject
        item.channelConfiguration
    }

    def "creates outbound item for a deleted item model"() {
        given:
        itemFactory.modelService = Stub(ModelService) {
            get(PK.fromLong(itemPk)) >> { throw new ModelLoadingException("") }
            get(PK.fromLong(ioPk)) >> Stub(IntegrationObjectModel)
            get(PK.fromLong(channelPk)) >> Stub(OutboundChannelConfigurationModel)
        }

        when:
        def item = itemFactory.createItem(dto)

        then:
        ! item.changedItemModel.present
        item.integrationObject
        item.channelConfiguration
    }

    def "does not create outbound item for a deleted integration object model"() {
        given:
        itemFactory.modelService = Stub(ModelService) {
            get(PK.fromLong(itemPk)) >> Stub(ItemModel)
            get(PK.fromLong(ioPk)) >> { throw new ModelLoadingException("") }
            get(PK.fromLong(channelPk)) >> Stub(OutboundChannelConfigurationModel)
        }

        when:
        itemFactory.createItem(dto)

        then:
        thrown IllegalArgumentException
    }

    def "does not create outbound item for a deleted channel configuration"() {
        given:
        itemFactory.modelService = Stub(ModelService) {
            get(PK.fromLong(itemPk)) >> Stub(ItemModel)
            get(PK.fromLong(ioPk)) >> Stub(IntegrationObjectModel)
            get(PK.fromLong(channelPk)) >> { throw new ModelLoadingException("") }
        }

        when:
        itemFactory.createItem(dto)

        then:
        thrown IllegalArgumentException
    }
}
