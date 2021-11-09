/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.HybrisEnumValue
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.util.lifecycle.TenantLifecycle
import de.hybris.platform.jalo.JaloObjectNoLongerValidException
import de.hybris.platform.servicelayer.event.EventSender
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.tx.AfterSaveEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import de.hybris.platform.webhookservices.service.WebhookConfigurationService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class WebhookServicesAfterSaveEventListenerUnitTest extends Specification {

    def eventSender = Mock EventSender
    def modelService = Stub ModelService
    def tenantLifecycle = Stub TenantLifecycle
    def webhookConfigService = Stub WebhookConfigurationService
    def eventListener = new WebhookServicesAfterSaveEventListener(eventSender, tenantLifecycle)

    @Test
    @Unroll
    def "Instantiation fails when eventSender is #sender and tenantLifecycle is #lifecycle"() {
        when:
        new WebhookServicesAfterSaveEventListener(sender, lifecycle)

        then:
        def e = thrown IllegalArgumentException
        e.message == msg

        where:
        sender            | lifecycle             | msg
        null              | Stub(TenantLifecycle) | 'eventSender cannot be null'
        Stub(EventSender) | null                  | 'tenantLifecycle cannot be null'
    }

    @Test
    @Unroll
    def "sends an ItemSavedEvent after an AfterSaveEvent is captured for event type #eventType"() {
        given:
        tenantLifecycle.isOperational() >> true
        def event = afterSaveEvent eventType

        when:
        eventListener.afterSave([event])

        then:
        1 * eventSender.sendEvent(_ as ItemSavedEvent) >> { args ->
            assert args[0].pk == event.pk
            assert args[0].type.type == event.type
        }

        where:
        eventType << [AfterSaveEvent.CREATE, AfterSaveEvent.UPDATE]
    }

    @Test
    def "does not send an ItemSavedEvent after an AfterSaveEvent is captured for remove event type"() {
        given:
        tenantLifecycle.isOperational() >> true
        def event = afterSaveEvent AfterSaveEvent.REMOVE

        when:
        eventListener.afterSave([event])

        then:
        0 * eventSender.sendEvent(_ as ItemSavedEvent)
    }

    @Test
    def "no AfterSaveEvent is sent when platform is starting up or shutting down"() {
        given:
        tenantLifecycle.isOperational() >> false
        def event = afterSaveEvent eventType

        when:
        eventListener.afterSave([event])

        then:
        0 * eventSender.sendEvent(_ as ItemSavedEvent)

        where:
        eventType << [AfterSaveEvent.CREATE]
    }

    @Test
    def "Saved event is not filtered out when modelService is #modelServ and webhoohokConfigurationService is #webhookConfigServ"() {
        given:
        eventListener.modelService = modelServ
        eventListener.webhookConfigurationService = webhookConfigServ
        tenantLifecycle.isOperational() >> true

        def event = afterSaveEvent AfterSaveEvent.CREATE

        when:
        eventListener.afterSave([event])

        then:
        1 * eventSender.sendEvent(_ as ItemSavedEvent)

        where:
        modelServ          | webhookConfigServ
        null               | null
        Stub(ModelService) | null
        null               | Stub(WebhookConfigurationService)
    }

    @Test
    @Unroll
    def 'Saved event is filtered out when model service cannot load item due to #exceptionName'() {
        given:
        eventListener.modelService = modelService
        eventListener.webhookConfigurationService = webhookConfigService
        tenantLifecycle.isOperational() >> true

        and: 'Model service fails to load the item'
        modelService.get(_ as PK) >> { throw exception }

        and:
        def event = afterSaveEvent AfterSaveEvent.CREATE

        when:
        eventListener.afterSave([event])

        then:
        noExceptionThrown()
        0 * eventSender.sendEvent(_ as ItemSavedEvent)

        where:
        exception                                                                                                              | exceptionName
        new ModelLoadingException("IGNORE - testing exception")                                                                | 'ModelLoadingException'
        new JaloObjectNoLongerValidException(new PK(4567), new RuntimeException("item not found"), "Item does not exist", 123) | 'JaloObjectNoLongerValidException'
        new ObscureModelServiceExceptionWeDontKnowAbout("what could go wrong?")                                                | 'ObscureModelServiceExceptionWeDontKnowAbout'
    }

    @Test
    def 'Saved event is filtered out when no webhook configurations support the saved item'() {
        given:
        eventListener.modelService = modelService
        eventListener.webhookConfigurationService = webhookConfigService
        tenantLifecycle.isOperational() >> true

        and:
        def item = Stub ItemModel
        modelService.get(_ as PK) >> item

        and: 'No webhook configuration supports the item'
        webhookConfigService.getWebhookConfigurationsByEventAndItemModel(_ as ItemSavedEvent, item) >> []

        and:
        def event = afterSaveEvent AfterSaveEvent.CREATE

        when:
        eventListener.afterSave([event])

        then:
        0 * eventSender.sendEvent(_ as ItemSavedEvent)
    }

    @Test
    def 'Saved event is filtered out when item saved is not of ItemModel type'() {
        given:
        eventListener.modelService = modelService
        eventListener.webhookConfigurationService = webhookConfigService
        tenantLifecycle.isOperational() >> true

        and:
        def item = Stub HybrisEnumValue
        modelService.get(_ as PK) >> item

        and:
        def event = afterSaveEvent AfterSaveEvent.CREATE

        when:
        eventListener.afterSave([event])

        then:
        0 * webhookConfigService._
        0 * eventSender._
    }

    @Test
    def 'Saved event is sent when model service finds item and an existing webhook configuration supports the item'() {
        given:
        eventListener.modelService = modelService
        eventListener.webhookConfigurationService = webhookConfigService
        tenantLifecycle.isOperational() >> true

        and:
        def item = Stub(ProductModel)
        modelService.get(_ as PK) >> item
        webhookConfigService.getWebhookConfigurationsByEventAndItemModel(_ as ItemSavedEvent, item) >> [Stub(WebhookConfigurationModel)]

        and:
        def event = afterSaveEvent AfterSaveEvent.CREATE

        when:
        eventListener.afterSave([event])

        then:
        1 * eventSender.sendEvent(_ as ItemSavedEvent)
    }

    AfterSaveEvent afterSaveEvent(int eventType) {
        Stub(AfterSaveEvent) {
            getPk() >> PK.fromLong(1)
            getType() >> eventType
        }
    }

    private static class ObscureModelServiceExceptionWeDontKnowAbout extends RuntimeException {
        ObscureModelServiceExceptionWeDontKnowAbout(final String message) {
            super(message)
        }
    }
}
