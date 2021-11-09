/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.util.JsonErrorResponse
import de.hybris.platform.odata2webservices.odata.ODataFacade
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.order.events.SubmitOrderEvent
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.apache.http.HttpStatus
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.IntegrationObjectModelBuilder.integrationObject
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/IAPI-4452')
class WebhookServiceErrorsIntegrationTest extends ServicelayerSpockSpecification {
    private static final def TEST_NAME = 'WebhookServiceErrors'
    private static final def EXISTING_IO_CODE = "IO_$TEST_NAME"
    private static final String DESTINATION_ID = "destination_$TEST_NAME"

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = 'defaultODataFacade')
    private ODataFacade facade

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-webhookservice-api.impex', 'UTF-8'
        integrationObject().withCode(EXISTING_IO_CODE).build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove EventConfigurationModel, { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        ConsumedDestinationBuilder.cleanup()
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def 'cannot POST new webhook configuration without integration object'() {
        given: 'a payload that does not contain integration object reference'
        def payload = json()
                .withField('eventType', ItemSavedEvent.name)
                .withField('destination', destinationPayloadBuilder())

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'missing_key'
            it.message.contains 'integrationObject'
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
    }

    @Test
    def 'cannot POST new webhook configuration without consumed destination'() {
        given: 'a payload that does not contain a consumed destination reference'
        def payload = json()
                .withField('eventType', ItemSavedEvent.name)
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'missing_key'
            it.message.contains 'destination'
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
    }

    @Test
    def 'cannot POST webhook configuration with unsupported event type'() {
        given: 'a payload that refers any unsupported event type'
        def payload = json()
                .withField('eventType', SubmitOrderEvent.name)
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', destinationPayloadBuilder())

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'invalid_attribute_value'
            it.message.contains 'event type is not supported'
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
    }

    @Test
    def 'cannot POST webhook configuration with non WEBHOOKSERVICES destination target'() {
        given: 'a payload, in which consumed destination is associated with DEFAULT destination target'
        def payload = json()
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', destinationPayloadBuilder()
                        .withField('destinationTarget', targetPayloadBuilder(DestinationChannel.DEFAULT)))

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'invalid_attribute_value'
            it.message.contains DESTINATION_ID
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
    }

    @Test
    @Unroll
    def "cannot POST webhook configuration when destination target is #reason"() {
        given: 'a payload with destination target not associated with supported event'
        def payload = json()
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', destinationPayloadBuilder()
                        .withField('destinationTarget', targetPayloadBuilder(DestinationChannel.WEBHOOKSERVICES)
                                .withField('eventConfigurations', events)))

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'invalid_attribute_value'
            it.message.contains errorMessage
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty

        where:
        reason                              | events                                  | errorMessage
        'not associated with events'        | []                                      | 'not registered with the destination target'
        'associated with unsupported event' | [eventPayloadBuilder(SubmitOrderEvent)] | 'ItemSavedEvent is not registered'
    }

    @Test
    def 'cannot POST webhook configuration with invalid filter location'() {
        given: 'a payload with destination target with unsupported filter location'
        def payload = json()
                .withField('integrationObject', json().withCode(EXISTING_IO_CODE))
                .withField('destination', destinationPayloadBuilder())
                .withField('filterLocation', 'http://not.supported')

        when: 'the payload is posted'
        def response = facade.handleRequest post(payload)

        then: 'an error is reported'
        with(JsonErrorResponse.createFrom(response)) {
            it.statusCode == HttpStatus.SC_BAD_REQUEST
            it.errorCode == 'invalid_attribute_value'
            it.message.contains 'Filter location'
        }
        and: 'the webhook is not created'
        IntegrationTestUtil.findAll(WebhookConfigurationModel).empty
    }

    ODataContext post(JsonBuilder payload) {
        contextGenerator.generate ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(path().withEntitySet('WebhookConfigurations'))
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withBody(payload)
                .build()
    }

    private static PathInfoBuilder path() {
        PathInfoBuilder.pathInfo().withServiceName('WebhookService')
    }

    private static JsonBuilder destinationPayloadBuilder() {
        json().withId(DESTINATION_ID)
                .withField('url', 'http://does.not.matter')
                .withField('destinationTarget', targetPayloadBuilder(DestinationChannel.WEBHOOKSERVICES))
                .withField('endpoint', json()
                        .withId("endpoint_$TEST_NAME")
                        .withField('version', 'irrelevant - key value must be provided')
                        .withField('name', 'irrelevant - required and must be provided')
                        .withField('specUrl', 'http://does.not.matter'))
                .withField('credentialBasic', json()
                        .withId("credential_$TEST_NAME")
                        .withField('username', "user-$TEST_NAME")
                        .withField('password', 'secret'))
    }

    private static JsonBuilder targetPayloadBuilder(DestinationChannel channel) {
        json()
                .withId("target_$TEST_NAME")
                .withField('destinationChannel', json().withCode(channel.name()))
                .withFieldValues('eventConfigurations', eventPayloadBuilder(ItemSavedEvent))
    }

    private static JsonBuilder eventPayloadBuilder(Class eventType) {
        json().withField('eventClass', eventType.name)
                .withField('exportName', 'irrelevant - required and must be provided')
    }
}
