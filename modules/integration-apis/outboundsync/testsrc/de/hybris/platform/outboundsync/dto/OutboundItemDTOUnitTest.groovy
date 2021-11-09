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
import de.hybris.platform.core.PK
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class OutboundItemDTOUnitTest extends Specification {

    @Shared
    def channelConfigurationPk = 2L
    @Shared
    def integrationObjPk = 1L
    @Shared
    def rootItemPk = 3L
    @Shared
    def cronJobPk = PK.fromLong(4L)
    private static final anotherCronJobPk = PK.fromLong(5L)
    @Shared
    def outboundItem = Stub(OutboundItemChange)

    @Unroll
    @Test
    def "create outbound item DTO with null #part throws exception"() {
        when:
        OutboundItemDTO.Builder.item()
                .withItem(item)
                .withChannelConfigurationPK(channelConfig)
                .withIntegrationObjectPK(intObj)
                .withCronJobPK(cronjobPk)
                .build()

        then:
        thrown(IllegalArgumentException)

        where:
        part                     | item         | channelConfig          | intObj           | cronjobPk
        'item'                   | null         | channelConfigurationPk | integrationObjPk | cronJobPk
        'channelConfigurationPk' | outboundItem | null                   | integrationObjPk | cronJobPk
        'integrationObjectPk'    | outboundItem | channelConfigurationPk | null             | cronJobPk
        'cronJobPk'              | outboundItem | channelConfigurationPk | integrationObjPk | null
    }

    @Test
    def "create an outbound item"() {
        given:
        def itemDTO = item()

        expect:
        with(itemDTO) {
            getItem() == outboundItem
            getChannelConfigurationPk() == channelConfigurationPk
            getIntegrationObjPk() == integrationObjPk
            getRootItemPk() == rootItemPk
            getCronJobPk() == cronJobPk
        }
    }

    @Test
    def "create an outbound item from another outbound item"() {
        when:
        def itemDTO = OutboundItemDTO.Builder.from(item()).build()

        then:
        with(itemDTO) {
            getItem() == outboundItem
            getChannelConfigurationPk() == channelConfigurationPk
            getIntegrationObjPk() == integrationObjPk
            getRootItemPk() == rootItemPk
            getCronJobPk() == cronJobPk
        }
    }

    @Test
    def "two outbound items are equal"() {
        given:
        def item1 = item()
        def item2 = item()

        expect:
        item1 == item2
    }

    @Test
    @Unroll
    def "two outbound items are not equal when #condition"() {
        given:
        def item1 = item(outboundItem, channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)

        expect:
        item1 != item2

        where:
        condition                 | item2
        "item is different"       | item(Stub(OutboundItemChange), channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)
        "channel pk is different" | item(outboundItem, -channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)
        "intObj pk is different"  | item(outboundItem, channelConfigurationPk, -integrationObjPk, rootItemPk, cronJobPk)
        "root pk is different"    | item(outboundItem, channelConfigurationPk, integrationObjPk, -rootItemPk, cronJobPk)
        "cronJob pk is different" | item(outboundItem, channelConfigurationPk, integrationObjPk, rootItemPk, anotherCronJobPk)
        "type is different"       | 1
        "item2 is null"           | null
    }

    @Test
    def "two outbound items have the same hashcode"() {
        given:
        def item1 = item()
        def item2 = item()

        expect:
        item1.hashCode() == item2.hashCode()
    }

    @Test
    @Unroll
    def "two outbound items have different hashcode when #condition"() {
        given:
        def item1 = item(outboundItem, channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)

        expect:
        item1.hashCode() != item2.hashCode()

        where:
        condition                 | item2
        "item is different"       | item(Stub(OutboundItemChange), channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)
        "channel pk is different" | item(outboundItem, -channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)
        "intObj pk is different"  | item(outboundItem, channelConfigurationPk, -integrationObjPk, rootItemPk, cronJobPk)
        "root pk is different"    | item(outboundItem, channelConfigurationPk, integrationObjPk, -rootItemPk, cronJobPk)
        "cronJob pk is different" | item(outboundItem, channelConfigurationPk, integrationObjPk, rootItemPk, anotherCronJobPk)
        "type is different"       | 1
    }

    def item() {
        item(outboundItem, channelConfigurationPk, integrationObjPk, rootItemPk, cronJobPk)
    }

    def item(OutboundItemChange item, Long channel, Long intObj, Long root, PK cronJobPk) {
        OutboundItemDTO.Builder.item()
                .withItem(item)
                .withChannelConfigurationPK(channel)
                .withIntegrationObjectPK(intObj)
                .withRootItemPK(root)
                .withCronJobPK(cronJobPk)
                .build()
    }
}
