/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.service.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.dto.EventSourceData
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.util.TestOutboundFacade
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.tx.AfterSaveEvent
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource
import java.time.Duration

import static de.hybris.platform.integrationservices.util.EventualCondition.eventualCondition
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.condition
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.createCatalogWithId
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-4552')
class WebhookEventToItemSenderIntegrationTest extends ServicelayerSpockSpecification {
    private static final String TEST_NAME = 'WebhookEventToItemSender'
    private static final String CATALOG_IO = "${TEST_NAME}_CatalogIO"
    private static final String CATALOG_ID = "${TEST_NAME}_Catalog"
    private static final String DESTINATION_1 = "${TEST_NAME}_Destination_1"
    private static final String DESTINATION_2 = "${TEST_NAME}_Destination_2"
    private static final Duration REASONABLE_TIME = Duration.ofSeconds(7)

    @Resource
    private WebhookEventToItemSender webhookEventSender
    @Resource
    private OutboundServiceFacade outboundServiceFacade
    @Rule
    private TestOutboundFacade mockOutboundServiceFacade = new TestOutboundFacade().respondWithCreated()

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $CATALOG_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code) ; root',
                "                                   ; $CATALOG_IO                           ; Catalog            ; Catalog    ; true",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $integrationItem    ; $attributeName ; $attributeDescriptor',
                "                                            ; $CATALOG_IO:Catalog ; id             ; Catalog:id")
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == CATALOG_IO }
        IntegrationTestUtil.remove EventConfigurationModel, { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    def setup() {
        webhookEventSender.outboundServiceFacade = mockOutboundServiceFacade
    }

    def cleanup() {
        webhookEventSender.outboundServiceFacade = outboundServiceFacade
        IntegrationTestUtil.remove WebhookConfigurationModel, { it?.integrationObject?.code == CATALOG_IO}
    }

    @Test
    def 'Catalog is sent to webhook after it is saved'() {
        given: 'WebhookConfiguration created for a Catalog item'
        def consumedDestination = consumedDestinationBuilder()
                .withId(DESTINATION_1)
                .withUrl("https://path/to/webhooks")
                .withDestinationTarget('webhookServices') // created in essential data
                .build()
        webhookConfiguration()
                .withDestination(consumedDestination)
                .withIntegrationObject(CATALOG_IO)
                .build()

        when: 'Catalog created'
        def catalog = createCatalogWithId CATALOG_ID

        then:
        condition().eventually {
            assert mockOutboundServiceFacade.invocations() == 1
        }

        cleanup:
        IntegrationTestUtil.remove catalog
        IntegrationTestUtil.remove consumedDestination
    }

    @Test
    def 'No item is sent when the PK in the event is not found'() {
        given: 'WebhookConfiguration created for a Catalog item'
        def consumedDestination = consumedDestinationBuilder()
                .withId(DESTINATION_2)
                .withUrl("https://path/to/webhooks")
                .withDestinationTarget('webhookServices') // created in essential data
                .build()
        webhookConfiguration()
                .withDestination(consumedDestination)
                .withIntegrationObject(CATALOG_IO)
                .build()

        and: 'Event contains a Catalog PK that does not exist'
        final EventSourceData data = new EventSourceData()
        final int catalogTypeCode = 600
        final AfterSaveEvent afterSaveEvent = new AfterSaveEvent(PK.createCounterPK(catalogTypeCode), 4)
        final ItemSavedEvent webhookEvent = new ItemSavedEvent(afterSaveEvent)
        data.setEvent(webhookEvent)

        when:
        webhookEventSender.send(data)

        then: 'Catalog does not exist so the facade is not called'
        eventualCondition().within(REASONABLE_TIME).retains {
            assert mockOutboundServiceFacade.invocations() == 0
        }

        cleanup:
        IntegrationTestUtil.remove consumedDestination
    }
}
