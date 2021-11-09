/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.service.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.util.TestOutboundFacade
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue
import spock.lang.Shared

import javax.annotation.Resource
import java.time.Duration

import static de.hybris.platform.integrationservices.util.EventualCondition.eventualCondition
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.createCatalogWithId
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration

@Ignore('will be un-ignored after https://cxjira.sap.com/browse/IAPI-4650 is resolved')
@IntegrationTest
@Issue('https://cxjira.sap.com/browse/IAPI-4453')
class WebhookImmediateRetryIntegrationTest extends ServicelayerSpockSpecification {

    private static final String CATALOG_IO = 'WebhookImmediateRetryIntegrationTestIOf'
    private static final Duration REASONABLE_TIME = Duration.ofSeconds(6)

    @Resource
    private WebhookImmediateRetryOutboundServiceFacade webhookImmediateRetryOutboundServiceFacade
    @Resource
    private OutboundServiceFacade outboundServiceFacade
    @Rule
    private TestOutboundFacade mockOutboundServiceFacade = new TestOutboundFacade()
    @Shared
    private ConsumedDestinationModel consumedDestination

    def setup() {
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
                "                                            ; $CATALOG_IO:Catalog ; id             ; Catalog:id"
        )
        consumedDestination = consumedDestination()
        webhookForCatalogItem(consumedDestination)
        webhookImmediateRetryOutboundServiceFacade.outboundServiceFacade = mockOutboundServiceFacade
    }

    def cleanup() {
        IntegrationTestUtil.remove consumedDestination
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == CATALOG_IO }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == CATALOG_IO }
        IntegrationTestUtil.remove(EventConfigurationModel) { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove(DestinationTargetModel) { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        IntegrationTestUtil.removeAll WebhookConfigurationModel
        webhookImmediateRetryOutboundServiceFacade.outboundServiceFacade = outboundServiceFacade
    }

    @Test
    def 'webhook does not retry after a 400 error'() {
        given: 'Outbound service facade responds with a 500 error'
        mockOutboundServiceFacade.respondWithBadRequest().respondWithCreated()

        when: 'Catalog created'
        def catalog = createCatalogWithId('catalogFor400')

        then:
        eventualCondition().within(REASONABLE_TIME).retains {
            assert mockOutboundServiceFacade.invocations() == 1
        }

        cleanup:
        IntegrationTestUtil.remove catalog
    }

    @Test
    def 'webhook is only retried once'() {
        given: 'Outbound service facade responds with a 500 error'
        mockOutboundServiceFacade.respondWithServerError().respondWithServerError().respondWithServerError()

        when: 'Catalog created'
        def catalog = createCatalogWithId('anotherCatalog')

        then:
        eventualCondition().within(REASONABLE_TIME).retains {
            assert mockOutboundServiceFacade.invocations() == 2
        }

        cleanup:
        IntegrationTestUtil.remove catalog
    }

    def webhookForCatalogItem(ConsumedDestinationModel consumedDestination) {
        webhookConfiguration()
                .withDestination(consumedDestination)
                .withIntegrationObject(CATALOG_IO)
                .build()
    }

    def consumedDestination() {
        def consumedDestination = consumedDestinationBuilder()
                .withId('webhookDestination')
                .withUrl("https://path/to/webhooks")
                .withDestinationTarget('webhookServices') // created in essential data
                .build()
        consumedDestination
    }
}
