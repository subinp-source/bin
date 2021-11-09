/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundsync.model.*
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.exceptions.ModelSavingException
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.session.SessionService
import org.junit.Test
import spock.lang.Shared

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil.*
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.*

@IntegrationTest
class ChannelConfigurationIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String TEST_NAME = "ChannelConfigurationIntegrationTest"
    private static final String INTEGRATION_OBJECT_CODE = "${TEST_NAME}_IO"
    private static final String CHANNEL_CODE = "${TEST_NAME}_Channel"
    private static final String CONTAINER_CODE = "${CHANNEL_CODE}Container"
    public static final String STREAM_ID_SUFFIX = "Stream"
    private static final String DESTINATION_ID = "${TEST_NAME}_ConsumedDestination"
    private static def originalLocale

    @Shared
    ConsumedDestinationModel consumedDestination

    @Resource
    ModelService modelService
    @Resource
    SessionService sessionService;

    def setup() {
        consumedDestination = ConsumedDestinationBuilder.consumedDestinationBuilder().withId(DESTINATION_ID).build()
        channelAndRelatedConfigDoNotExist()
        originalLocale = sessionService.getAttribute('locale')
        sessionService.setAttribute('locale', Locale.ENGLISH)
    }

    def cleanup() {
        IntegrationObjectTestUtil.cleanup()
        ConsumedDestinationBuilder.cleanup()
        removeAll OutboundChannelConfigurationModel
        removeAll OutboundSyncStreamConfigurationContainerModel
        originalLocale = sessionService.getAttribute('locale')
    }

    @Test
    def 'channel config created but related configurations are not created when auto generate is false'() {
        given:
        def integrationObject = createIntegrationObject(INTEGRATION_OBJECT_CODE)
        def integrationObjectItem = createIntegrationObjectItem(integrationObject, 'Category', true)

        and:
        def channelConfig = new OutboundChannelConfigurationModel()
        channelConfig.setCode(CHANNEL_CODE)
        channelConfig.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig.setDestination(consumedDestination)
        channelConfig.setAutoGenerate(false)

        when:
        modelService.save(channelConfig)

        then:
        channelConfigExists(CHANNEL_CODE)

        and:
        !streamContainerExists(CONTAINER_CODE)
        !streamExists("${CHANNEL_CODE}_Category_${STREAM_ID_SUFFIX}")
        !jobExistsForStreamContainer(CONTAINER_CODE)
    }

    @Test
    def 'channel config created with one stream when io does not contain parent child relationship'() {
        given:
        def integrationObject = createIntegrationObject(INTEGRATION_OBJECT_CODE)
        def integrationObjectItem = createIntegrationObjectRootItem(integrationObject, 'Category')
        def streamId = "${CHANNEL_CODE}_Category_${STREAM_ID_SUFFIX}"

        and:
        def channelConfig = new OutboundChannelConfigurationModel()
        channelConfig.setCode(CHANNEL_CODE)
        channelConfig.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig.setDestination(consumedDestination)
        channelConfig.setAutoGenerate(true)

        when:
        modelService.save(channelConfig)

        then:
        channelConfigExists(CHANNEL_CODE)
        streamContainerExists(CONTAINER_CODE)
        streamExists(streamId)
        jobExistsForStreamContainer(CONTAINER_CODE)
    }

    @Test
    def 'channel config created with multiple streams when io contains parent child relationship'() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $INTEGRATION_OBJECT_CODE",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code) ; root[default = false]',
                "                                   ; $INTEGRATION_OBJECT_CODE              ; Order              ; Order      ; true",
                "                                   ; $INTEGRATION_OBJECT_CODE              ; OrderEntry         ; OrderEntry ;     ",
                "                                   ; $INTEGRATION_OBJECT_CODE              ; Product            ; Product    ;     ",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $INTEGRATION_OBJECT_CODE:Order                                     ; code                        ; Order:code                                         ;                                                           ; true",
                "                                            ; $INTEGRATION_OBJECT_CODE:Order                                     ; entries                     ; Order:entries                                      ; $INTEGRATION_OBJECT_CODE:OrderEntry                       ;     ",
                "                                            ; $INTEGRATION_OBJECT_CODE:OrderEntry                                ; product                     ; OrderEntry:product                                 ; $INTEGRATION_OBJECT_CODE:Product                          ;     ",
                "                                            ; $INTEGRATION_OBJECT_CODE:OrderEntry                                ; order                       ; OrderEntry:order                                   ; $INTEGRATION_OBJECT_CODE:Order                            ;     "
        )

        def integrationObject = findIntegrationObjectByCode(INTEGRATION_OBJECT_CODE)
        def orderStreamId = "${CHANNEL_CODE}_Order_${STREAM_ID_SUFFIX}"
        def orderEntryStreamId = "${CHANNEL_CODE}_OrderEntry_${STREAM_ID_SUFFIX}"

        and:
        def channelConfig = new OutboundChannelConfigurationModel()
        channelConfig.setCode(CHANNEL_CODE)
        channelConfig.setIntegrationObject(integrationObject)
        channelConfig.setDestination(consumedDestination)
        channelConfig.setAutoGenerate(true)

        when:
        modelService.save(channelConfig)

        then:
        channelConfigExists(CHANNEL_CODE)
        streamContainerExists(CONTAINER_CODE)
        streamExists(orderStreamId)
        streamExists(orderEntryStreamId)
        jobExistsForStreamContainer(CONTAINER_CODE)

        and:
        !streamExists("${CHANNEL_CODE}_Product_${STREAM_ID_SUFFIX}")
    }

    @Test
    def 'channel config created with multiple streams when io contains no root items'() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $INTEGRATION_OBJECT_CODE",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code) ; root[default = false]',
                "                                   ; $INTEGRATION_OBJECT_CODE              ; MyOrder1           ; Order      ;     ",
                "                                   ; $INTEGRATION_OBJECT_CODE              ; MyOrderEntry1      ; OrderEntry ;     ",
                "                                   ; $INTEGRATION_OBJECT_CODE              ; MyProduct1         ; Product    ;     "
        )

        def integrationObject = findIntegrationObjectByCode(INTEGRATION_OBJECT_CODE)
        def orderStreamId = "${CHANNEL_CODE}_Order_${STREAM_ID_SUFFIX}"
        def orderEntryStreamId = "${CHANNEL_CODE}_OrderEntry_${STREAM_ID_SUFFIX}"
        def productStreamId = "${CHANNEL_CODE}_Product_${STREAM_ID_SUFFIX}"

        and:
        def channelConfig = new OutboundChannelConfigurationModel()
        channelConfig.setCode(CHANNEL_CODE)
        channelConfig.setIntegrationObject(integrationObject)
        channelConfig.setDestination(consumedDestination)
        channelConfig.setAutoGenerate(true)

        when:
        modelService.save(channelConfig)

        then:
        channelConfigExists(CHANNEL_CODE)
        streamContainerExists(CONTAINER_CODE)
        streamExists(orderStreamId)
        streamExists(orderEntryStreamId)
        streamExists(productStreamId)
        jobExistsForStreamContainer(CONTAINER_CODE)
    }

    @Test
    def 'cannot create the same channel configuration more than once'() {
        given:
        def integrationObject = createIntegrationObject(INTEGRATION_OBJECT_CODE)
        def integrationObjectItem = createIntegrationObjectRootItem(integrationObject, 'Category')

        and:
        def channelConfig1 = new OutboundChannelConfigurationModel()
        channelConfig1.setCode(CHANNEL_CODE)
        channelConfig1.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig1.setDestination(consumedDestination)
        channelConfig1.setAutoGenerate(true)

        def channelConfig2 = new OutboundChannelConfigurationModel()
        channelConfig2.setCode(CHANNEL_CODE)
        channelConfig2.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig2.setDestination(consumedDestination)
        channelConfig2.setAutoGenerate(true)

        when:
        modelService.save(channelConfig1)
        modelService.save(channelConfig2)

        then:
        channelConfigExists(CHANNEL_CODE)
        thrown(ModelSavingException)
    }

    @Test
    def 'two channel configs with different code having the same io and destination are created'() {
        given:
        def integrationObject = createIntegrationObject(INTEGRATION_OBJECT_CODE)
        def integrationObjectItem = createIntegrationObjectRootItem(integrationObject, 'Category')

        and:
        def channelConfig1 = new OutboundChannelConfigurationModel()
        channelConfig1.setCode(CHANNEL_CODE)
        channelConfig1.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig1.setDestination(consumedDestination)
        channelConfig1.setAutoGenerate(true)

        def channelConfig2 = new OutboundChannelConfigurationModel()
        def channelConfig2Code = "${CHANNEL_CODE}2"
        channelConfig2.setCode(channelConfig2Code)
        channelConfig2.setIntegrationObject(integrationObjectItem.integrationObject)
        channelConfig2.setDestination(consumedDestination)
        channelConfig2.setAutoGenerate(true)

        when:
        modelService.save(channelConfig1)
        modelService.save(channelConfig2)

        then:
        channelConfigExists(CHANNEL_CODE)
        channelConfigExists(channelConfig2Code)

        and:
        findAll(OutboundSyncStreamConfigurationContainerModel).size() == 2
        findAll(OutboundSyncStreamConfigurationModel).size() == 2
        findAll(OutboundSyncJobModel).size() == 2
        findAll(OutboundSyncCronJobModel).size() == 2
    }

    def channelConfigExists(String code) {
        findAny(OutboundChannelConfigurationModel, { it.code.contains(code) }).isPresent()
    }

    def streamContainerExists(String id) {
        findAny(OutboundSyncStreamConfigurationContainerModel, { it.id.contains(id) }).isPresent()
    }

    def streamExists(String id) {
        findAny(OutboundSyncStreamConfigurationModel, { it.streamId.contains(id) }).isPresent()
    }

    def jobExistsForStreamContainer(String code) {
        findAny(OutboundSyncCronJobModel, { it.job?.streamConfigurationContainer?.id?.contains(code) }).isPresent()
    }

    def channelAndRelatedConfigDoNotExist() {
        findAll(OutboundChannelConfigurationModel).size() == 0
        findAll(OutboundSyncStreamConfigurationContainerModel).size() == 0
        findAll(OutboundSyncStreamConfigurationModel).size() == 0
        findAll(OutboundSyncJobModel).size() == 0
        findAll(OutboundSyncCronJobModel).size() == 0
    }
}
