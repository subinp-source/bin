/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.outboundservices.config.OutboundServicesConfiguration
import de.hybris.platform.outboundservices.decorator.DecoratorContext
import de.hybris.platform.outboundservices.decorator.DecoratorContextFactory
import de.hybris.platform.outboundservices.decorator.DecoratorExecution
import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator
import de.hybris.platform.outboundservices.facade.ConsumedDestinationNotFoundModel
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import rx.observers.TestSubscriber
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultOutboundServiceFacadeUnitTest extends Specification {
    private static final String ENDPOINT_URL = "http://my.consumed.destination/some/path"
    private static final String DESTINATION_ID = 'destination'
    private static final String IOI_CODE = 'integrationObjectItemCode'
    private static final String ITEM_TYPE = 'MyType'
    private static final String IO_CODE = 'integrationObjectCode'
    def HEADERS = new HttpHeaders()
    def PAYLOAD = [:]
    def RESPONSE = new ResponseEntity(HttpStatus.ACCEPTED)
    def DESTINATION =
            new ConsumedDestinationModel(
                    id: DESTINATION_ID, url: 'http://my.consumed.destination/some/path')

    def INTEGRATION_OBJECT = new IntegrationObjectModel(IO_CODE)

    @Shared
    def itemModel = Stub(ItemModel) {
        getItemtype() >> ITEM_TYPE
    }
    def integrationObjectDescriptor = Stub(IntegrationObjectDescriptor) {
        getItemTypeDescriptor(itemModel) >> Stub(ItemTypeDescriptor) {
            getItemCode() >> IOI_CODE
            getTypeCode() >> ITEM_TYPE
        }
    }

    def flexibleSearchService = Stub(FlexibleSearchService) {
        getModelByExample(_ as ConsumedDestinationModel) >> DESTINATION
    }
    def restTemplate = Mock(RestTemplate) {
        postForEntity(DESTINATION.url, _, Map) >> RESPONSE
    }

    def restTemplateFactory = Mock(IntegrationRestTemplateFactory) {
        create(_ as ConsumedDestinationModel) >> restTemplate
    }

    def contextFactory = Stub(DecoratorContextFactory)

    @Shared
    def integrationObjectService = Stub(IntegrationObjectService) {
        findIntegrationObject(IO_CODE) >> INTEGRATION_OBJECT
        findIntegrationObjectItemByTypeCode(IO_CODE, ITEM_TYPE) >> Stub(IntegrationObjectItemModel) {
            getCode() >> IOI_CODE
        }
    }
    def monitoringDecorator = Mock OutboundRequestDecorator
    def outboundServicesConfiguration = Stub(OutboundServicesConfiguration)
    def decorator1 = Mock OutboundRequestDecorator
    def decorator2 = Mock OutboundRequestDecorator
    def facade = new DefaultOutboundServiceFacade(
            decorators: [decorator1, decorator2],
            flexibleSearchService: flexibleSearchService,
            integrationObjectService: integrationObjectService,
            integrationRestTemplateFactory: restTemplateFactory,
            monitoringDecorator: monitoringDecorator,
            outboundServicesConfiguration: outboundServicesConfiguration,
            contextFactory: contextFactory
    )

    @Test
    @Unroll
    def "send payload to destination with monitoring enabled and integration object item #condition"() {
        given: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true
        and: "integration object item #condition"
        facade.integrationObjectService = intObjService
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext()

        when:
        facade.send(itemModel, IO_CODE, DESTINATION_ID).subscribe()

        then:
        1 * monitoringDecorator.decorate(_, _, _, _) >> {
            HttpHeaders httpHeaders, Map payload, DecoratorContext context, DecoratorExecution execution ->
                assert httpHeaders.isEmpty()
                assert payload == PAYLOAD
                with(context) {
                    integrationObjectCode == IO_CODE
                    integrationObjectItemCode == ioCode
                    itemModel == itemModel
                    destinationModel == DESTINATION
                }; Stub(HttpEntity)
        }
        1 * restTemplate.postForEntity(ENDPOINT_URL, _, Map.class)

        where:
        condition                  | intObjService                                                                                          | ioCode
        'model is found'           | integrationObjectService                                                                               | IOI_CODE
        'model is not found'       | findIntegrationObjectItemWithException(new ModelNotFoundException('IGNORE - testing exception'))       | IOI_CODE
        'has ambiguous identifier' | findIntegrationObjectItemWithException(new AmbiguousIdentifierException('IGNORE - testing exception')) | IOI_CODE
    }

    @Test
    def 'send payload to destination with monitoring disabled then monitoring decorator is not invoked'() {
        given: 'monitoring is disabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> false
        and:
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext()

        when:
        facade.send(itemModel, IO_CODE, DESTINATION_ID).subscribe()

        then:
        0 * monitoringDecorator._
    }

    @Test
    @Unroll
    def "send throws exception when #condition"() {
        when:
        facade.send(item, ioCode, destination)

        then:
        def ex = thrown IllegalArgumentException
        ex.message == message

        where:
        condition              | item      | ioCode  | destination    | message
        'item model is null'   | null      | IO_CODE | DESTINATION_ID | 'itemModel cannot be null'
        'destination is empty' | itemModel | IO_CODE | ""             | 'destination cannot be null or empty'
        'destination is null'  | itemModel | IO_CODE | null           | 'destination cannot be null or empty'
    }

    @Test
    def 'send adds a monitoring entry and throws exception when consumed destination is not found and monitoring is enabled'() {
        given: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true
        and: 'destination not found'
        facade.flexibleSearchService = Stub(FlexibleSearchService) {
            getModelByExample(_ as ConsumedDestinationModel) >> {
                throw new ModelNotFoundException('IGNORE - testing exception')
            }
        }
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext(Stub(ConsumedDestinationNotFoundModel) {
            getId() >> DESTINATION_ID
            getUrl() >> "Destination '$DESTINATION_ID' was not found."
        })

        when:
        facade.send(itemModel, IO_CODE, DESTINATION_ID)

        then:
        def ex = thrown ModelNotFoundException
        ex.message == 'Provided destination was not found.'

        and: 'monitoring decorator is initialized'
        1 * monitoringDecorator.decorate(_, _, _, _) >> {
            HttpHeaders httpHeaders, Map payload, DecoratorContext context, DecoratorExecution execution ->
                assert httpHeaders.isEmpty()
                assert payload == PAYLOAD
                with(context) {
                    integrationObjectCode == IO_CODE
                    itemModel == itemModel
                    destinationModel.url == "Destination '$DESTINATION_ID' was not found."
                }; Stub(HttpEntity)
        }
    }

    @Test
    def 'send parameters is not monitored when monitoring is disabled'() {
        given: 'monitoring is disabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> false
        and: 'valid decoratorContext is created'
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext()

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters()).subscribe probe

        then: 'request is sent successfully'
        probe.assertValue RESPONSE
        probe.assertCompleted()
        probe.assertNoErrors()
        and: 'request is not monitored'
        0 * monitoringDecorator._
    }

    @Test
    def 'send parameters works'() {
        given: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true
        and: 'monitoring decorator is not present'
        facade.monitoringDecorator = null
        and: 'valid decoratorContext is created'
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext()

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters()).subscribe probe

        then: 'the item is sent and the response is received'
        probe.assertValue RESPONSE
        probe.assertCompleted()
        probe.assertNoErrors()
    }

    @Test
    def 'send parameters monitors the request before other decorators are called'() {
        given: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true
        and:
        def decoratorContext = decoratorContext()
        facade.contextFactory = Stub(DecoratorContextFactory) {
            createContext(_ as SyncParameters) >> decoratorContext
        }

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(itemModel, IO_CODE, DESTINATION.id).subscribe probe

        then: 'request is completed'
        probe.assertCompleted()
        and: 'monitoring is executed first'
        1 * monitoringDecorator.decorate([:], [:], decoratorContext, _) >> { args ->
            (args[3] as DecoratorExecution).createHttpEntity(args[0], args[1], args[2])
        }

        then: 'request is decorated'
        1 * decorator1.decorate([:], [:], decoratorContext, _) >> { args ->
            (args[3] as DecoratorExecution).createHttpEntity(args[0], args[1], args[2])
        }
    }

    @Test
    def 'does not report an exception when a decorator context contains errors'() {
        given: 'created decorator context contains errors'
        def decoratorContext = decoratorContext()
        decoratorContext.getErrors() >> ['problem 1', 'problem 2']
        decoratorContext.hasErrors() >> true
        facade.contextFactory = Stub(DecoratorContextFactory) {
            createContext(_ as SyncParameters) >> decoratorContext
        }
        and: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters()).subscribe probe

        then: 'monitoring was once'
        probe.assertNotCompleted()
        1 * monitoringDecorator.decorate(HEADERS, PAYLOAD, decoratorContext, _)
        and: 'the rest template was created'
        1 * restTemplateFactory._
    }

    @Test
    def 'reports an exception when a decorator crashes'() {
        given: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true
        and: 'a decorator that crashes'
        def decoratorContext = decoratorContext()
        contextFactory.createContext(_ as SyncParameters) >> decoratorContext
        def exception = new RuntimeException('IGNORE - testing exception')
        def failingDecorator = Mock OutboundRequestDecorator
        facade.outboundRequestDecorators = [failingDecorator]
        and: 'monitoring is enabled'
        outboundServicesConfiguration.isMonitoringEnabled() >> true

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters()).subscribe probe

        then: 'monitoring was called before the decorator crashed'
        1 * monitoringDecorator.decorate(HEADERS, PAYLOAD, decoratorContext, _) >> { args ->
            (args[3] as DecoratorExecution).createHttpEntity(args[0], args[1], args[2])
        }

        then: 'the failing decorator was called'
        1 * failingDecorator.decorate(HEADERS, PAYLOAD, decoratorContext, _) >> { throw exception }

        and: 'the rest template was not event created'
        0 * restTemplateFactory._
    }

    def findIntegrationObjectItemWithException(def ex) {
        Stub(IntegrationObjectService) {
            findIntegrationObject(IOI_CODE) >> {
                throw ex
            }
        }
    }

    private DecoratorContext decoratorContext() {
        decoratorContext(DESTINATION)
    }

    private DecoratorContext decoratorContext(final ConsumedDestinationModel dest) {
        Stub(DecoratorContext) {
            getItemModel() >> itemModel
            getDestinationModel() >> dest
            getIntegrationObjectCode() >> IO_CODE
            getIntegrationObject() >> integrationObjectDescriptor
            getIntegrationObjectItemCode() >> IOI_CODE
            getIntegrationObjectItem() >> itemModel
        }
    }

    SyncParameters parameters() {
        Stub(SyncParameters) {
            getItem() >> itemModel
            getDestinationId() >> DESTINATION.id
            getIntegrationObjectCode() >> IO_CODE
        }
    }
}