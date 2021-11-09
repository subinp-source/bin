/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.outboundservices.enums.OutboundSource
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.outboundservices.decorator.DecoratorContext.decoratorContextBuilder

@UnitTest
class DecoratorContextBuilderUnitTest extends Specification {
    def OUTBOUND_SOURCE = OutboundSource.WEBHOOKSERVICES
    private static final ITEM = new ItemModel()
    private static final DESTINATION = new ConsumedDestinationModel()
    @Shared
    def INTEGRATION_OBJECT = Stub IntegrationObjectDescriptor

    @Test
    def "test build when all fields are provided"() {
        when:
        def providedErrors = ["error 1", "error 2"]
        final DecoratorContext context = decoratorContextBuilder()
                .withDestinationModel(DESTINATION)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withItemModel(ITEM)
                .withOutboundSource(OUTBOUND_SOURCE)
                .withErrors(providedErrors)
                .build()

        then:
        with(context) {
            itemModel == itemModel
            destinationModel == DESTINATION
            integrationObject == INTEGRATION_OBJECT
            source == OUTBOUND_SOURCE
            errors == providedErrors
            hasErrors()
        }
    }

    @Test
    def "test build when errors are not provided"() {
        when:
        final DecoratorContext context = decoratorContextBuilder()
                .withDestinationModel(DESTINATION)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withItemModel(ITEM)
                .build()

        then:
        with(context) {
            errors == []
            !hasErrors()
        }
    }

    @Test
    @Unroll
    def "IllegalArgumentException is thrown when request is built with null #condition"() {
        when:
        decoratorContextBuilder()
                .withDestinationModel(destination)
                .withIntegrationObject(io)
                .withItemModel(item)
                .withOutboundSource(OUTBOUND_SOURCE)
                .build()

        then:
        def e = thrown IllegalArgumentException
        e.message == "$condition cannot be null"

        where:
        condition                     | item | destination | io
        'ConsumedDestinationModel'    | ITEM | null        | INTEGRATION_OBJECT
        'ItemModel'                   | null | DESTINATION | INTEGRATION_OBJECT
        'IntegrationObjectDescriptor' | ITEM | DESTINATION | null
    }

    @Test
    def "test build with no source defaults to unknown source"() {
        given:
        final DecoratorContext context = decoratorContextBuilder()
                .withDestinationModel(DESTINATION)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withItemModel(ITEM)
                .build()

        expect:
        context.source == OutboundSource.UNKNOWN
    }

    @Test
    def 'default to UNKNOWN source when null source was provided'() {
        when:
        def context = decoratorContextBuilder()
                .withDestinationModel(DESTINATION)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withItemModel(ITEM)
                .withOutboundSource(null)
                .build()

        then:
        context.source == OutboundSource.UNKNOWN
    }

    @Test
    @Unroll
    def 'builds valid context with empty errors list when #cond errors were provided'() {
        when:
        def context = decoratorContextBuilder()
                .withDestinationModel(DESTINATION)
                .withIntegrationObject(INTEGRATION_OBJECT)
                .withItemModel(ITEM)
                .withErrors(providedErrors)
                .build()

        then:
        with(context) {
            errors == []
            !hasErrors()
        }

        where:
        cond    | providedErrors
        "null"  | null
        "empty" | []
    }

    @Test
    def "2 different decoratorContext instances can be created using the same decorator context builder"() {
        given:
        def sharedBuilder = decoratorContextBuilder()

        when:
        def context1 = sharedBuilder
                .withDestinationModel(new ConsumedDestinationModel())
                .withIntegrationObject(Stub(IntegrationObjectDescriptor))
                .withOutboundSource(OutboundSource.WEBHOOKSERVICES)
                .withItemModel(new ItemModel())
                .build()
        def context2 = sharedBuilder
                .withDestinationModel(new ConsumedDestinationModel())
                .withIntegrationObject(Stub(IntegrationObjectDescriptor))
                .withOutboundSource(OutboundSource.OUTBOUNDSYNC)
                .withItemModel(new ItemModel())
                .build()

        then:
        !context1.is(context2)
        context1.itemModel != context2.itemModel
        context1.destinationModel != context2.destinationModel
        context1.integrationObject != context2.integrationObject
        context1.source != context2.source
    }
}
