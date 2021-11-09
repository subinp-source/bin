/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.impl.NullIntegrationObjectDescriptor
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.facade.ConsumedDestinationNotFoundModel
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultDecoratorContextFactoryUnitTest extends Specification {
    def integrationObjectService = Stub(IntegrationObjectService)
    def flexibleSearchService = Stub(FlexibleSearchService)
    def descriptorFactory = Stub(DescriptorFactory)
    def factory = new DefaultDecoratorContextFactory(integrationObjectService, flexibleSearchService, descriptorFactory)

    def OUTBOUND_SOURCE = OutboundSource.WEBHOOKSERVICES
    def ITEM = Stub(ItemModel)
    def DESTINATION_ID = 'testDestinationId'
    def DESTINATION = Stub(ConsumedDestinationModel)
    def IO_CODE = 'TestIOCode'
    def INTEGRATION_OBJECT = Stub(IntegrationObjectModel)
    def IO_DESCRIPTOR = Stub(IntegrationObjectDescriptor)

    private SyncParameters params = SyncParameters.syncParametersBuilder()
            .withItem(ITEM)
            .withIntegrationObjectCode(IO_CODE)
            .withDestinationId(DESTINATION_ID)
            .withSource(OUTBOUND_SOURCE)
            .build()

    def DEST_NOT_FOUND_ERROR_MSG = "Provided destination '$DESTINATION_ID' was not found."
    def IO_NOT_FOUND_ERROR_MSG = "Provided integration object '$IO_CODE' was not found."

    @Test
    def "IllegalArgumentException is thrown when a null #prop is provided"() {
        when:
        new DefaultDecoratorContextFactory(ioService, flexSearchService, descFactory)

        then:
        thrown(IllegalArgumentException)

        where:
        cond                       | ioService                      | flexSearchService           | descFactory
        'IntegrationObjectService' | null                           | Stub(FlexibleSearchService) | Stub(DescriptorFactory)
        'FlexibleSearchService'    | Stub(IntegrationObjectService) | null                        | Stub(DescriptorFactory)
        'DescriptorFactory'        | Stub(IntegrationObjectService) | Stub(FlexibleSearchService) | null
    }

    @Test
    def "creates context with no errors when destination and integrationObject are found"() {
        given:
        integrationObjectService.findIntegrationObject(IO_CODE) >> INTEGRATION_OBJECT
        descriptorFactory.createIntegrationObjectDescriptor(INTEGRATION_OBJECT) >> IO_DESCRIPTOR
        flexibleSearchService.getModelByExample(_ as ConsumedDestinationModel) >> DESTINATION

        when:
        def decoratorContext = factory.createContext(params)

        then:
        with(decoratorContext) {
            destinationModel == DESTINATION
            integrationObject == IO_DESCRIPTOR
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == []
            !hasErrors()
        }
    }

    @Test
    def "creates context with no errors when destinationModel and integrationObjectModel are provided in SyncParameters"() {
        given:
        final SyncParameters paramsWithModels = SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withDestination(DESTINATION)
                .withSource(OUTBOUND_SOURCE)
                .build()
        and:
        descriptorFactory.createIntegrationObjectDescriptor(INTEGRATION_OBJECT) >> IO_DESCRIPTOR

        when:
        def decoratorContext = factory.createContext(paramsWithModels)

        then:
        with(decoratorContext) {
            destinationModel == DESTINATION
            integrationObject == IO_DESCRIPTOR
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == []
            !hasErrors()
        }
    }

    @Test
    def "creates context with errors and destinationModel is set to a ConsumedDestinationNotFoundModel if destinationId and destinationModel are not provided"() {
        given:
        final SyncParameters paramsWithoutDestination = SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withSource(OUTBOUND_SOURCE)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .build()

        and:
        descriptorFactory.createIntegrationObjectDescriptor(INTEGRATION_OBJECT) >> IO_DESCRIPTOR

        when:
        def decoratorContext = factory.createContext(paramsWithoutDestination)

        then:
        with(decoratorContext) {
            destinationModel instanceof ConsumedDestinationNotFoundModel
            integrationObject == IO_DESCRIPTOR
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == ["Provided destination 'null' was not found."]
            hasErrors()
        }
    }

    @Test
    def "sets integrationObject to a NullIntegrationObjectDescriptor if integrationObjectModel and integrationObjectCode are not provided"() {
        given:
        final SyncParameters paramsWithoutIO = SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withSource(OUTBOUND_SOURCE)
                .withDestination(DESTINATION)
                .build()

        when:
        def decoratorContext = factory.createContext(paramsWithoutIO)

        then:
        with(decoratorContext) {
            destinationModel == DESTINATION
            integrationObject instanceof NullIntegrationObjectDescriptor
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == ["Provided integration object 'null' was not found."]
            hasErrors()
        }
    }

    @Test
    def "creates context with error when destination is not found"() {
        given:
        integrationObjectService.findIntegrationObject(IO_CODE) >> INTEGRATION_OBJECT
        descriptorFactory.createIntegrationObjectDescriptor(INTEGRATION_OBJECT) >> IO_DESCRIPTOR
        flexibleSearchService.getModelByExample(_ as ConsumedDestinationModel) >> { throw Stub(RuntimeException) }

        when:
        def decoratorContext = factory.createContext(params)

        then:
        with(decoratorContext) {
            destinationModel instanceof ConsumedDestinationNotFoundModel
            integrationObject == IO_DESCRIPTOR
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == [DEST_NOT_FOUND_ERROR_MSG]
            hasErrors()
        }
    }

    @Test
    def "creates context with error when destination is ConsumedDestinationNotFoundModel"() {
        given:
        final SyncParameters paramsWithErrorModel = SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withDestination(Stub(ConsumedDestinationNotFoundModel) {
                    getDestinationId() >> DESTINATION_ID
                    getId() >> DESTINATION_ID
                })
                .withSource(OUTBOUND_SOURCE)
                .build()

        and:
        descriptorFactory.createIntegrationObjectDescriptor(INTEGRATION_OBJECT) >> IO_DESCRIPTOR

        when:
        def decoratorContext = factory.createContext(paramsWithErrorModel)

        then:
        with(decoratorContext) {
            destinationModel instanceof ConsumedDestinationNotFoundModel
            integrationObject == IO_DESCRIPTOR
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == [DEST_NOT_FOUND_ERROR_MSG]
            hasErrors()
        }
    }

    @Test
    @Unroll
    def "creates context with error when findIntegrationObject throws #msg"() {
        given:
        integrationObjectService.findIntegrationObject(IO_CODE) >> { throw exception }
        flexibleSearchService.getModelByExample(_ as ConsumedDestinationModel) >> DESTINATION

        when:
        def decoratorContext = factory.createContext(params)

        then:
        with(decoratorContext) {
            destinationModel == DESTINATION
            integrationObject instanceof NullIntegrationObjectDescriptor
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == [IO_NOT_FOUND_ERROR_MSG]
            hasErrors()
        }

        where:
        msg                            | exception
        "AmbiguousIdentifierException" | Stub(AmbiguousIdentifierException)
        "ModelNotFoundException"       | Stub(ModelNotFoundException)
    }

    @Test
    def "creates context with 2 errors when destination and integrationObject are not found"() {
        given:
        integrationObjectService.findIntegrationObject(IO_CODE) >> { throw Stub(ModelNotFoundException) }
        flexibleSearchService.getModelByExample(_ as ConsumedDestinationModel) >> { throw Stub(RuntimeException) }

        when:
        def decoratorContext = factory.createContext(params)

        then:
        with(decoratorContext) {
            destinationModel instanceof ConsumedDestinationNotFoundModel
            integrationObject instanceof NullIntegrationObjectDescriptor
            itemModel == ITEM
            source == OUTBOUND_SOURCE
            errors == [DEST_NOT_FOUND_ERROR_MSG, IO_NOT_FOUND_ERROR_MSG]
            hasErrors()
        }
    }
}
