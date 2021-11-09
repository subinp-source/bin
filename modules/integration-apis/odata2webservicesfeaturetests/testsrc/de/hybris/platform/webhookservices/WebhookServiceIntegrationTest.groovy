/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.ItemTracker
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.ODataFacade
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource
import java.util.function.Predicate

import static de.hybris.platform.integrationservices.IntegrationObjectItemModelBuilder.integrationObjectItem
import static de.hybris.platform.integrationservices.IntegrationObjectModelBuilder.integrationObject
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/IAPI-4452')
class WebhookServiceIntegrationTest extends ServicelayerSpockSpecification {
    private static final def ESSENTIAL_DATA_TARGET_ID = 'webhookServices'
    private static final def TEST_NAME = 'WebhookService'
    private static final def EXISTING_DESTINATION_ID = "existingDestination_$TEST_NAME"
    private static final def EXISTING_IO_CODE = "IO_$TEST_NAME"
    private static final def DESTINATION_URL = 'https://does.not.matter'

    @Rule
    ItemTracker itemTracker = ItemTracker.track(WebhookConfigurationModel, ConsumedDestinationModel,
            EndpointModel, BasicCredentialModel, EventConfigurationModel, DestinationTargetModel)

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = 'defaultODataFacade')
    private ODataFacade facade

    ConsumedDestinationModel existingDestination

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservice-api.impex', 'UTF-8'
        integrationObjectItem()
                .withIntegrationObject(integrationObject()
                        .withCode(EXISTING_IO_CODE)
                        .build())
                .withCode('Catalog')
                .asRoot()
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove WebhookConfigurationModel, matchWebhook(EXISTING_IO_CODE, EXISTING_DESTINATION_ID)
        IntegrationTestUtil.remove EventConfigurationModel, { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        ConsumedDestinationBuilder.cleanup()
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def setup() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        existingDestination = ConsumedDestinationBuilder.consumedDestinationBuilder()
                .withId(EXISTING_DESTINATION_ID)
                .withUrl(DESTINATION_URL)
                .withDestinationTarget(ESSENTIAL_DATA_TARGET_ID)
                .build()
    }

    def cleanup() {
        IntegrationTestUtil.remove existingDestination
    }

    @Test
    def 'can retrieve WebhookService EDMX'() {
        when:
        def response = facade.handleRequest getMetadata()

        then:
        response.status == HttpStatusCodes.OK
    }

    @Test
    def 'POSTs new webhook configuration with existing consumed destination'() {
        given: 'a payload referencing an existing consumed destination and an existing IO'
        def payload = json()
                .withField('eventType', ItemSavedEvent.name)
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', json()
                        .withId(EXISTING_DESTINATION_ID)
                        .withField('destinationTarget', json().withId(ESSENTIAL_DATA_TARGET_ID)))

        when: 'the payload is posted'
        def response = facade.handleRequest post('WebhookConfigurations', payload)

        then: 'the webhook is created'
        response.status == HttpStatusCodes.CREATED
        and: 'exists in the platform'
        itemTracker.isCreated WebhookConfigurationModel, matchWebhook(EXISTING_IO_CODE, EXISTING_DESTINATION_ID)
    }

    @Test
    def 'POSTs new webhook configuration with implicit event type'() {
        given: 'a payload that does not specify event type for the webhook configuration'
        def payload = json()
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', json()
                        .withId(EXISTING_DESTINATION_ID)
                        .withField('destinationTarget', json().withId(ESSENTIAL_DATA_TARGET_ID)))

        when: 'the payload is posted'
        def response = facade.handleRequest post('WebhookConfigurations', payload)

        then: 'the webhook is created'
        response.status == HttpStatusCodes.CREATED
        and: 'the created webhook has default event type'
        def webhook = itemTracker.getCreatedItems(WebhookConfigurationModel).find {
            matchWebhook(EXISTING_IO_CODE, EXISTING_DESTINATION_ID)
        }
        webhook.eventType == ItemSavedEvent.name
    }

    @Test
    def 'POST auto-creates non-existing items related to consumed destination'() {
        given: 'a payload referencing non-existent consumed destination items'
        def destinationId = "destination-$TEST_NAME"
        def targetId = "target-$TEST_NAME"
        def endpointId = "endpoint-$TEST_NAME"
        def credentialId = "credential-$TEST_NAME"
        def payload = json()
                .withField('eventType', ItemSavedEvent.name)
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', json()
                        .withId(destinationId)
                        .withField('url', 'http://does.not.matter')
                        .withField('destinationTarget', json()
                                .withId(targetId)
                                .withField('destinationChannel', json().withCode(DestinationChannel.WEBHOOKSERVICES.name()))
                                .withFieldValues('eventConfigurations', json()
                                        .withField('eventClass', ItemSavedEvent.name)
                                        .withField('exportName', 'irrelevant - required and must be provided')))
                        .withField('endpoint', json()
                                .withId(endpointId)
                                .withField('version', 'irrelevant - key value must be provided')
                                .withField('name', 'irrelevant - required and must be provided')
                                .withField('specUrl', 'http://does.not.matter'))
                        .withField('credentialBasic', json()
                                .withId(credentialId)
                                .withField('username', "user-$TEST_NAME")
                                .withField('password', 'secret')))

        when: 'the payload is posted'
        def response = facade.handleRequest post('WebhookConfigurations', payload)

        then: 'the webhook is created'
        response.status == HttpStatusCodes.CREATED
        and: 'all relevant items created in the platform'
        itemTracker.isCreated(WebhookConfigurationModel, matchWebhook(EXISTING_IO_CODE, destinationId))
        itemTracker.isCreated(ConsumedDestinationModel) { it.id == destinationId }
        itemTracker.isCreated(DestinationTargetModel) { it.id == targetId }
        itemTracker.isCreated(EndpointModel) { it.id == endpointId }
        itemTracker.isCreated(BasicCredentialModel) { it.id == credentialId }
    }

    @Test
    def 'POST can create non-root items'() {
        given: 'a payload for non-existent consumed destination'
        def destinationId = "destination-$TEST_NAME"
        def endpointId = "endpoint-$TEST_NAME"
        def credentialId = "credential-$TEST_NAME"
        def payload = json()
                .withId(destinationId)
                .withField('url', 'http://does.not.matter')
                .withField('destinationTarget', json().withId(ESSENTIAL_DATA_TARGET_ID))
                .withField('endpoint', json()
                        .withId(endpointId)
                        .withField('version', 'irrelevant - key value must be provided')
                        .withField('name', 'irrelevant - required and must be provided')
                        .withField('specUrl', 'http://does.not.matter'))
                .withField('credentialBasic', json()
                        .withId(credentialId)
                        .withField('username', "user-$TEST_NAME")
                        .withField('password', 'secret'))

        when: 'the payload is posted'
        def response = facade.handleRequest post('ConsumedDestinations', payload)

        then: 'the destination is created'
        response.status == HttpStatusCodes.CREATED
        and: 'exists in the platform'
        itemTracker.isCreated(ConsumedDestinationModel) { it.id == destinationId }
    }

    @Test
    def 'GET retrieves existing webhook configurations'() {
        given: 'a webhook configuration exists'
        webhookConfiguration()
                .withDestination(existingDestination)
                .withIntegrationObject(EXISTING_IO_CODE)
                .build()

        when: 'GET is sent'
        def response = facade.handleRequest get('WebhookConfigurations')

        then: 'webhooks are retrieved'
        response.status == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        !json.getCollection('d.results').empty
    }

    @Test
    def 'GET retrieves existing webhook configuration by its key'() {
        given: 'a webhook configuration exists'
        webhookConfiguration()
                .withDestination(existingDestination)
                .withIntegrationObject(EXISTING_IO_CODE)
                .build()
        and: 'integration key for the webhook configuration'
        def integrationKey = webhookKey()

        when: 'GET is sent'
        def response = facade.handleRequest get('WebhookConfigurations', integrationKey)

        then: 'the webhook is retrieved'
        response.status == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('d.integrationKey') == integrationKey
    }

    @Test
    def 'PATCH updates webhook filter'() {
        given: 'a webhook configuration exists'
        webhookConfiguration()
                .withDestination(existingDestination)
                .withIntegrationObject(EXISTING_IO_CODE)
                .build()
        and: 'a payload specifying filter for the webhook'
        def filterLoc = 'model://someScript'
        def payload = json().withField('filterLocation', filterLoc)

        when: 'the payload is sent'
        def response = facade.handleRequest patch('WebhookConfigurations', webhookKey(), payload)

        then: 'the webhook is updated'
        response.status == HttpStatusCodes.OK
        and: 'the filter value is changed in the platform'
        IntegrationTestUtil.findAny(WebhookConfigurationModel, matchWebhook(EXISTING_IO_CODE, EXISTING_DESTINATION_ID))
                .map({ it.filterLocation })
                .orElse('') == filterLoc
    }

    @Test
    def 'DELETE removes a webhook configuration'() {
        given: 'a webhook configuration exists'
        webhookConfiguration()
                .withDestination(existingDestination)
                .withIntegrationObject(EXISTING_IO_CODE)
                .build()

        when: 'delete is sent'
        def response = facade.handleRequest delete('WebhookConfigurations', webhookKey())

        then: 'the webhook is deleted'
        response.status == HttpStatusCodes.OK
        and: 'the webhook does not exist in the platform'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
        and: 'items related to the webhook still exist'
        IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == EXISTING_IO_CODE }).present
        IntegrationTestUtil.findAny(ConsumedDestinationModel, { it.id == EXISTING_DESTINATION_ID }).present
    }

    String webhookKey() {
        "$EXISTING_DESTINATION_ID|$ESSENTIAL_DATA_TARGET_ID|$EXISTING_IO_CODE|${ItemSavedEvent.name}"
    }

    ODataContext getMetadata() {
        contextGenerator.generate ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(path().withRequestPath('$metadata'))
                .build()
    }

    ODataContext get(String entitySetName, String key = null) {
        contextGenerator.generate ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(path().withEntitySet(entitySetName).withEntityKeys(key))
                .build()
    }

    ODataContext post(String entitySet, JsonBuilder payload) {
        contextGenerator.generate ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(path().withEntitySet(entitySet))
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withBody(payload)
                .build()
    }

    ODataContext patch(String entitySet, String key, JsonBuilder payload) {
        contextGenerator.generate ODataRequestBuilder.oDataPatchRequest()
                .withPathInfo(path().withEntitySet(entitySet).withEntityKeys(key))
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withBody(payload)
                .build()
    }

    ODataContext delete(String entitySet, String key) {
        contextGenerator.generate ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(path().withEntitySet(entitySet).withEntityKeys(key))
                .withAccepts(APPLICATION_JSON_VALUE)
                .build()
    }

    private static PathInfoBuilder path() {
        PathInfoBuilder.pathInfo().withServiceName('WebhookService')
    }

    private static Predicate<WebhookConfigurationModel> matchWebhook(String io, String destination) {
        { it -> it.integrationObject.code = io && it.destination.id == destination }
    }
}
