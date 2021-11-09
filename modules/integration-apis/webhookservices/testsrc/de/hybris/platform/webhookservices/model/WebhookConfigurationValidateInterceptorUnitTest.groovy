/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.model

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.order.events.SubmitOrderEvent
import de.hybris.platform.servicelayer.event.events.TestingEvent
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class WebhookConfigurationValidateInterceptorUnitTest extends Specification {
    def interceptor = new WebhookConfigurationValidateInterceptor()

    @Test
    def "exception is thrown when the WebhookConfigurationModel's eventType is not supported"() {
        given:
        def unsupportedEventClass = SubmitOrderEvent
        def configuration = Stub(WebhookConfigurationModel) {
            getEventType() >> unsupportedEventClass.canonicalName
        }

        when:
        interceptor.onValidate configuration, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "$unsupportedEventClass.canonicalName event type is not supported. Supported type is de.hybris.platform.webhookservices.event.ItemSavedEvent."
        e.interceptor == interceptor
    }

    @Test
    @Unroll
    def "exception is thrown when webhook configuration destination target #condition"() {
        given: 'destination target is not associated with the event configuration matching the event type'
        def eventType = ItemSavedEvent
        def configuration = Stub(WebhookConfigurationModel) {
            getDestination() >> destinationFor(events)
            getEventType() >> eventType.canonicalName
        }

        when:
        interceptor.onValidate configuration, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "WebhookConfigurationModel is misconfigured: $eventType.canonicalName is not registered with the destination target"
        e.interceptor == interceptor

        where:
        condition                         | events
        'has no events configurations'    | []
        'does not contain the event type' | [SubmitOrderEvent, TestingEvent]
    }

    @Test
    def 'exception is thrown when webhook configuration does not contain consumed destination'() {
        given:
        def config = Stub(WebhookConfigurationModel) {
            getDestination() >> null
            getEventType() >> ItemSavedEvent.canonicalName
        }

        when:
        interceptor.onValidate config, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "WebhookConfigurationModel is misconfigured: $config.eventType is not registered with the destination target"
        e.interceptor == interceptor

        where:
        condition                         | events
        'has no events configurations'    | []
        'does not contain the event type' | [SubmitOrderEvent, TestingEvent]
    }

    @Test
    def "validation passes when WebhookConfigurationModel is valid"() {
        given: 'a configuration with supported event type'
        def configuration = Stub(WebhookConfigurationModel) {
            getEventType() >> ItemSavedEvent.canonicalName
        }
        and: 'the event type is registered with the destination target'
        configuration.getDestination() >> destinationFor([ItemSavedEvent])

        when:
        interceptor.onValidate configuration, Stub(InterceptorContext)

        then:
        noExceptionThrown()
    }

    @Test
    @Unroll
    def "validation passes when filter location is #location"() {
        given: 'a configuration with supported event type'
        def configuration = Stub(WebhookConfigurationModel) {
            getDestination() >> destinationFor([ItemSavedEvent])
            getEventType() >> ItemSavedEvent.canonicalName
            getFilterLocation() >> location
        }

        when:
        interceptor.onValidate configuration, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        location << ['', null, 'model://myScript']
    }

    @Test
    @Unroll
    def "exception is thrown when webhook configuration has a malformed filter location #location"() {
        given:
        def config = Stub(WebhookConfigurationModel) {
            getDestination() >> destinationFor([ItemSavedEvent])
            getEventType() >> ItemSavedEvent.canonicalName
            getFilterLocation() >> location
        }

        when:
        interceptor.onValidate config, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "WebhookConfigurationModel is misconfigured: Filter location '$location' provided does not meet the pattern model://<script_code>"
        e.interceptor == interceptor

        where:
        location << ['file://someFile', 'model', 'model://', 'model:/', 'model/', 'model:// ']
    }

    ConsumedDestinationModel destinationFor(List<Class> events) {
        Stub(ConsumedDestinationModel) {
            getDestinationTarget() >> Stub(DestinationTargetModel) {
                getEventConfigurations() >> events.collect({ toEventConfig(it) })
            }
        }
    }

    EventConfigurationModel toEventConfig(Class eventClass) {
        Stub(EventConfigurationModel) {
            getEventClass() >> eventClass.canonicalName
        }
    }
}
