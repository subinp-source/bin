/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.ItemTracker
import de.hybris.platform.order.events.SubmitOrderEvent
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Rule
import org.junit.Test

import static de.hybris.platform.integrationservices.IntegrationObjectModelBuilder.integrationObject
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.outboundservices.DestinationTargetBuilder.destinationTarget
import static de.hybris.platform.webhookservices.EventConfigurationBuilder.eventConfiguration

@IntegrationTest
class WebhookConfigurationModelingIntegrationTest extends ServicelayerSpockSpecification {
    private static final String TEST_NAME = 'WebhookConfigurationModeling'
    private static final String IO = "${TEST_NAME}_IO"
    private static final String DESTINATION_ID = "${TEST_NAME}_Destination"
    private static final EventConfigurationBuilder eventConfigurationBuilder = eventConfiguration()
            .withExtensionName(WebhookservicesConstants.EXTENSIONNAME)
            .withExport(true)
    private static final ConsumedDestinationBuilder consumedDestination = consumedDestinationBuilder()

    @Rule
    ItemTracker itemTracker = ItemTracker.track(WebhookConfigurationModel, ConsumedDestinationModel,
            EventConfigurationModel, DestinationTargetModel)

    def setupSpec() {
        integrationObject().withCode(IO).build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove EventConfigurationModel, { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        ConsumedDestinationBuilder.cleanup()
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def setup() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
    }

    @Test
    def 'event type can be defaulted for a webhook configuration'() {
        given: 'a valid destination'
        def destination = consumedDestination()

        when: 'webhook configuration is created without evenConfiguration attribute'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; destination[unique = true] ; integrationObject(code)[unique = true]",
                "                           ; $destination.pk            ; $IO")

        then: 'webhook configuration is created with default event configuration'
        itemTracker.isCreated(WebhookConfigurationModel) {
            it.destination.id == DESTINATION_ID && it.integrationObject.code == IO && it.eventType == ItemSavedEvent.name
        }

        cleanup:
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    @Test
    def 'consumed destination is required for webhook configuration'() {
        given: 'a supported event'
        def eventType = ItemSavedEvent

        when: 'webhook configuration is created without destination attribute'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; integrationObject(code)[unique = true]",
                "                           ; $eventType.canonicalName; $IO")

        then: 'exception is thrown'
        def e = thrown AssertionError
        e.message.contains 'missing values for [destination]'
    }

    @Test
    def 'integration object is required for webhook configuration'() {
        given: 'a supported event type'
        def eventType = ItemSavedEvent
        and: 'existing destination'
        def destination = consumedDestination()

        when: 'webhook configuration is created without integrationObject attribute'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; destination[unique = true]",
                "                           ; $eventType.canonicalName; $destination.pk")

        then: 'exception is thrown'
        def e = thrown AssertionError
        e.message.contains 'missing values for [integrationObject]'
    }

    @Test
    def 'webhook configuration is created when all required attributes are present'() {
        given:
        def eventType = ItemSavedEvent
        and:
        def destination = consumedDestination()

        when:
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; destination[unique = true]; integrationObject(code)[unique = true]",
                "                           ; $eventType.name         ; $destination.pk           ; $IO")

        then:
        IntegrationTestUtil.findAny(WebhookConfigurationModel) {
            it.eventType == eventType.canonicalName && it.destination.pk == destination.pk && it.integrationObject.code == IO
        }.present
    }

    @Test
    def 'other than ItemSavedEvent types cannot be used with webhooks'() {
        given: 'event type is different from ItemSavedEvent'
        def eventType = SubmitOrderEvent
        and: 'destination is valid'
        def destination = consumedDestination()

        when: 'webhook configuration is created with the event configuration'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; destination[unique = true]; integrationObject(code)[unique = true]",
                "                                  ; $eventType.canonicalName; $destination.pk           ; $IO")

        then: 'exception is thrown'
        def e = thrown AssertionError
        e.message.contains "$SubmitOrderEvent.name event type is not supported"
    }

    @Test
    def 'webhook event must be registered with the destination target'() {
        given: 'event configuration for the webhook event type is deleted'
        def eventType = ItemSavedEvent
        IntegrationTestUtil.importImpEx(
                'REMOVE EventConfiguration; eventClass[unique = true]; destinationTarget(id)[unique = true]',
                "                         ; $eventType.canonicalName ; webhookServices")
        and: 'destination is valid'
        def destination = consumedDestination()

        when: 'webhook configuration is created with the event configuration'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; destination[unique = true]; integrationObject(code)[unique = true]",
                "                           ; $eventType.canonicalName; $destination.pk           ; $IO")

        then: 'exception is thrown'
        def e = thrown AssertionError
        e.message.contains eventType.canonicalName
    }

    @Test
    def 'consumed destination used for webhook configuration must be associated with a webhook destination target'() {
        given: 'destination target is not for webhooks'
        def destinationTarget = destinationTarget()
                .withId("${TEST_NAME}_nonWebhookTarget")
                .withDestinationChannel(DestinationChannel.DEFAULT)
                .build()
        and: 'event config for the supported type is added to the destination target'
        def eventType = ItemSavedEvent
        eventConfigurationBuilder
                .withDestination(destinationTarget)
                .withEventClass(eventType)
                .withExtensionName(WebhookservicesConstants.EXTENSIONNAME)
                .withExport(true)
                .build()
        and: 'a destination associated with the wrong destination target'
        def destination = consumedDestination
                .withId("${TEST_NAME}_WrongDestination")
                .withDestinationTarget(destinationTarget)
                .build()

        when: 'webhook configuration is created with the event configuration'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; eventType[unique = true]; destination[unique = true]; integrationObject(code)[unique = true]",
                "                           ; $eventType.canonicalName; $destination.pk           ; $IO")

        then: 'exception is thrown'
        def e = thrown AssertionError
        e.message.contains destination.id
    }

    @Test
    def 'webhook configuration is created with a filter'() {
        given: 'a valid filter location'
        def filterLocation = 'model://myFilter'

        when: 'webhook configuration impex is executed'
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; destination[unique = true] ; integrationObject(code)[unique = true]; filterLocation",
                "                           ; ${consumedDestination().pk}; $IO                                   ; $filterLocation")

        then: 'the webhook configuration is created with the filter location'
        with(itemTracker.getCreatedItems(WebhookConfigurationModel)[0]) {
            it.destination.id == DESTINATION_ID
            it.integrationObject.code == IO
            it.filterLocation == filterLocation
        }
    }

    @Test
    def 'exception is thrown when webhook configuration is created with an invalid filter url'() {
        given: 'unsupported filter location'
        def filterLocation = 'file://myIncorrectFilter'

        when:
        IntegrationTestUtil.importImpEx(
                "INSERT WebhookConfiguration; destination[unique = true] ; integrationObject(code)[unique = true]; filterLocation",
                "                           ; ${consumedDestination().pk}; $IO                                   ; $filterLocation")

        then:
        def e = thrown AssertionError
        e.message.contains filterLocation
    }

    ConsumedDestinationModel consumedDestination() {
        consumedDestination
                .withId(DESTINATION_ID)
                .withDestinationTarget('webhookServices') // created in essential data
                .build()
    }
}
