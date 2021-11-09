/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.facade.SyncParameters
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SyncParametersUnitTest extends Specification {
    @Shared
    private static final def IO_CODE = 'TestObject'
    @Shared
    def INTEGRATION_MODEL = Stub(IntegrationObjectModel) {
        getCode() >> IO_CODE
    }
    @Shared
    def ITEM = Stub(ItemModel) {
        toString() >> "itemToString"
    }
    @Shared
    def DESTINATION_ID = 'TestConsumedDest'
    @Shared
    def DESTINATION_MODEL = Stub(ConsumedDestinationModel) {
        getId() >> DESTINATION_ID
    }
    @Shared
    def PARAMS = params(ITEM, IO_CODE, INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)

    @Test
    @Unroll
    def '#initialParameters equals #other is #expectedEquals'() {
        expect:
        (initialParameters == other) == expectedEquals

        where:
        initialParameters | other                                                                                                                                                | expectedEquals
        PARAMS            | params(ITEM, IO_CODE, INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)                                          | true
        PARAMS            | params(ITEM, null, INTEGRATION_MODEL, null, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)                                                       | true
        PARAMS            | params(ITEM, IO_CODE, null, DESTINATION_ID, null, OutboundSource.WEBHOOKSERVICES)                                                                    | true
        PARAMS            | params(Stub(ItemModel), 'intObjCode2', Stub(IntegrationObjectModel), 'consumeDest2', Stub(ConsumedDestinationModel), OutboundSource.WEBHOOKSERVICES) | false
        PARAMS            | params(ITEM, 'intObjCode1', INTEGRATION_MODEL, 'consumeDest1', DESTINATION_MODEL, OutboundSource.OUTBOUNDSYNC)                                       | false
        PARAMS            | null                                                                                                                                                 | false
    }

    @Test
    @Unroll
    def "hashCode is #expectedEquals between #initialParameters and #other"() {
        expect:
        (initialParameters.hashCode() == other.hashCode()) == expectedEquals

        where:
        initialParameters | other                                                                                                                                                | expectedEquals
        PARAMS            | params(ITEM, IO_CODE, INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)                                          | true
        PARAMS            | params(ITEM, null, INTEGRATION_MODEL, null, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)                                                       | true
        PARAMS            | params(ITEM, IO_CODE, null, DESTINATION_ID, null, OutboundSource.WEBHOOKSERVICES)                                                                    | true
        PARAMS            | params(Stub(ItemModel), 'intObjCode2', Stub(IntegrationObjectModel), 'consumeDest2', Stub(ConsumedDestinationModel), OutboundSource.WEBHOOKSERVICES) | false
        PARAMS            | params(ITEM, 'intObjCode1', INTEGRATION_MODEL, 'consumeDest1', DESTINATION_MODEL, OutboundSource.OUTBOUNDSYNC)                                       | false
    }

    @Test
    def 'test build when all fields are provided'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .withDestination(DESTINATION_MODEL)
                .withIntegrationObject(INTEGRATION_MODEL)
                .withSource(OutboundSource.OUTBOUNDSYNC)
                .build()

        expect:
        with(context) {
            integrationObjectCode == IO_CODE
            integrationObject == INTEGRATION_MODEL
            item == ITEM
            destinationId == DESTINATION_ID
            destination == DESTINATION_MODEL
            source == OutboundSource.OUTBOUNDSYNC
        }
        context.toString() == "SyncParameters{" +
                "item='" + ITEM.toString() +
                "', integrationObject='" + IO_CODE +
                "', destination='" + DESTINATION_ID +
                "', source='" + OutboundSource.OUTBOUNDSYNC.code +
                "'}"
    }

    @Test
    @Unroll
    def '#field is populated when #model is provided'() {
        expect:
        with(context) {
            integrationObjectCode == IO_CODE
            item == ITEM
            destinationId == DESTINATION_ID
            source == OutboundSource.WEBHOOKSERVICES
        }

        where:
        field                   | model                    | context
        'integrationObjectCode' | 'integrationObjectModel' | params(ITEM, null, INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)
        'destinationId'         | 'destinationModel'       | params(ITEM, IO_CODE, INTEGRATION_MODEL, null, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)
    }

    @Test
    def 'when both integrationObjectCode and integrationObjectModel are provided, getIntegrationObjectCode returns the code of the integrationObjectModel'() {
        given:
        def parameters = params(ITEM, "alternativeID", INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)

        expect:
        parameters.getIntegrationObjectCode() == IO_CODE
    }

    @Test
    def 'when both destinationId and destinationModel are provided, getDestinationId returns the id of the destinationModel'() {
        given:
        def parameters = params(ITEM, IO_CODE, INTEGRATION_MODEL, "alternative_destination_id", DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)

        expect:
        parameters.getDestinationId() == DESTINATION_ID
    }

    @Test
    def 'default to UNKNOWN source when null source is not provided'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .build()

        expect:
        context.source == OutboundSource.UNKNOWN
    }

    @Test
    @Unroll
    def 'no exception is thrown when request is built with null #attrr'() {
        when:
        SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withDestination(destinationModel)
                .withDestinationId(destination)
                .withIntegrationObject(integrationModel)
                .withIntegrationObjectCode(io)
                .build()

        then:
        noExceptionThrown()

        where:
        attrr              | destination    | io      | destinationModel  | integrationModel
        'destination'      | null           | IO_CODE | DESTINATION_MODEL | INTEGRATION_MODEL
        'destinationModel' | DESTINATION_ID | IO_CODE | null              | INTEGRATION_MODEL
        'io'               | DESTINATION_ID | null    | DESTINATION_MODEL | INTEGRATION_MODEL
        'integrationModel' | DESTINATION_ID | IO_CODE | DESTINATION_MODEL | null
    }

    @Test
    @Unroll
    def "IllegalArgumentException is thrown when request is built with null #attrr"() {
        when:
        SyncParameters.syncParametersBuilder()
                .withItem(itemModel)
                .build()

        then:
        def e = thrown IllegalArgumentException
        e.message == condition

        where:
        attrr       | condition                  | destination    | io      | itemModel
        'itemModel' | 'itemModel cannot be null' | DESTINATION_ID | IO_CODE | null
    }

    private static SyncParameters params(ItemModel item, String integrationObjCode, IntegrationObjectModel integrationObjectModel, String destinationId, ConsumedDestinationModel destinationModel, OutboundSource source) {
        return SyncParameters.syncParametersBuilder()
                .withItem(item)
                .withIntegrationObjectCode(integrationObjCode)
                .withIntegrationObject(integrationObjectModel)
                .withDestinationId(destinationId)
                .withDestination(destinationModel)
                .withSource(source)
                .build()
    }
}