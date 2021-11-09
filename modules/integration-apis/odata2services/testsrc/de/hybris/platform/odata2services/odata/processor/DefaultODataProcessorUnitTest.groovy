/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor

import com.google.common.collect.Lists
import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.odata2services.config.ODataServicesConfiguration
import de.hybris.platform.odata2services.odata.InvalidDataException
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.InvalidEntryDataException
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException
import de.hybris.platform.odata2services.odata.persistence.PersistenceService
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequestFactory
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidPropertyValueException
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException
import de.hybris.platform.odata2services.odata.processor.reader.EntityReaderRegistry
import de.hybris.platform.servicelayer.exceptions.ModelSavingException
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.batch.BatchRequestPart
import org.apache.olingo.odata2.api.batch.BatchResponsePart
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.apache.olingo.odata2.api.uri.UriInfo
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo
import org.junit.Test
import spock.lang.Specification

import javax.ws.rs.core.MediaType

import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_JSON
import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_XML

@UnitTest
class DefaultODataProcessorUnitTest extends Specification {
    private static final String INTEGRATION_KEY = 'defaultKey'

    def entry = Stub(ODataEntry)
    def storageRequest = Stub(StorageRequest) {
        getIntegrationKey() >> INTEGRATION_KEY
        getAcceptLocale() >> Locale.ENGLISH
        getIntegrationItem() >> Stub(IntegrationItem) {
            getIntegrationKey() >> INTEGRATION_KEY
        }
    }
    def storageRequestFactory = Stub(StorageRequestFactory) {
        create(_, _, _, _) >> storageRequest
    }
    def persistenceService = Mock(PersistenceService)
    def entityProviderWriteProperties = Stub(EntityProviderWriteProperties)
    def oDataResponse = Stub(ODataResponse)
    def itemLookupRequestFactory = Mock(ItemLookupRequestFactory)
    def entityReaderRegistry = Stub(EntityReaderRegistry)
    def responseBuilder = Mock(ODataResponse.ODataResponseBuilder)
    def oDataServicesConfiguration = Stub(ODataServicesConfiguration) {
        getBatchLimit() >> 2
    }
    def oDataProcessor = Spy(DefaultODataProcessor)

    def setup() {
        oDataProcessor.getoDataResponseBuilder(_ as ODataResponse) >> responseBuilder
        oDataProcessor.writeProperties() >> entityProviderWriteProperties
        oDataProcessor.readEntry(_, _, _) >> entry
        oDataProcessor.writeEntry(_, _, _, entityProviderWriteProperties) >> oDataResponse

        oDataProcessor.setStorageRequestFactory(storageRequestFactory)
        oDataProcessor.setPersistenceService(persistenceService)
        oDataProcessor.setEntityReaderRegistry(entityReaderRegistry)
        oDataProcessor.setItemLookupRequestFactory(itemLookupRequestFactory)
        oDataProcessor.setContext Stub(ODataContext)
        oDataProcessor.setODataServicesConfiguration(oDataServicesConfiguration)
    }

    @Test
    def "create entity success returns ODataResponse"() throws ODataException {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> entry

        when:
        final ODataResponse oDataResponse = oDataProcessor.createEntity(
                Stub(UriInfo),
                Stub(InputStream),
                APPLICATION_XML, APPLICATION_XML)

        then:
        1 * responseBuilder.header("Content-Language", _) >> responseBuilder
        1 * responseBuilder.build() >> oDataResponse
        oDataResponse != null

    }

    @Test
    def "readEntity sets response content language header"() {
        given:
        itemLookupRequestFactory.create(_, _, _) >> Stub(ItemLookupRequest) {
            getAcceptLocale() >> new Locale("fr")
        }

        when:
        oDataProcessor.readEntity(Stub(UriInfo), MediaType.APPLICATION_JSON)

        then:
        1 * responseBuilder.header("Content-Language", "fr") >> responseBuilder
        1 * responseBuilder.build()
    }

    @Test
    def "readEntitySet returns response"() {
        given:
        itemLookupRequestFactory.create(_, _, _) >> Stub(ItemLookupRequest) {
            getAcceptLocale() >> new Locale("fr")
        }

        when:
        def response = oDataProcessor.readEntitySet(Stub(UriInfo), MediaType.APPLICATION_JSON)

        then:
        1 * responseBuilder.header("Content-Language", "fr") >> responseBuilder
        1 * responseBuilder.build() >> oDataResponse
        response == oDataResponse
    }

    @Test
    def "create entity sets response content language header"() {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> entry

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        1 * responseBuilder.header("Content-Language", "en") >> responseBuilder
        1 * responseBuilder.build()
    }

    @Test
    def "countEntitySet returns response"() {
        given:
        itemLookupRequestFactory.create(_, _, _) >> Stub(ItemLookupRequest) {
            getAcceptLocale() >> new Locale("en")
        }

        when:
        def response = oDataProcessor.countEntitySet Stub(UriInfo), APPLICATION_JSON

        then:
        1 * responseBuilder.header("Content-Language", "en") >> responseBuilder
        1 * responseBuilder.build() >> oDataResponse
        response == oDataResponse
    }

    @Test
    def "an exception is thrown when EntityReader cannot find applicable reader on read entity"() {
        given:
        entityReaderRegistry.getReader(_ as UriInfo) >> { throw new InternalProcessingException() }

        when:
        oDataProcessor.readEntity(Stub(UriInfo), APPLICATION_XML)

        then:
        thrown(RetrievalErrorRuntimeException)
    }

    @Test
    def "an exception is thrown when EntityReader cannot find applicable reader on read entity set"() {
        given:
        entityReaderRegistry.getReader(_ as UriInfo) >> { throw new ItemNotFoundException() }

        when:
        oDataProcessor.readEntitySet(Stub(UriInfo), APPLICATION_XML)

        then:
        thrown(RetrievalErrorRuntimeException)
    }

    @Test
    def "the exception is handled when an exception occurs creating an entity"() {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> entry

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        oDataProcessor.readEntry(_, _, _) >> { throw new RuntimeException('Something went wrong') }
        def e = thrown(ODataPayloadProcessingException)
        e.code == 'odata_error'
    }

    @Test
    def "the exception is handled when an exception occurs parsing the batch request"() {
        given:
        oDataProcessor.parseBatchRequest(_, _, _) >> { throw new RuntimeException('Something went wrong') }

        when:
        oDataProcessor.executeBatch Stub(BatchHandler), APPLICATION_XML, Stub(InputStream)

        then:
        def e = thrown(ODataPayloadProcessingException)
        e.code == 'odata_error'
    }

    @Test
    def "the exception is handled when an exception occurs creating an entity in the persistence service"() {
        given:
        persistenceService.createEntityData(_) >> { throw new RuntimeException() }

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        def e = thrown(PersistenceRuntimeApplicationException)
        e.code == 'runtime_error'
        e.integrationKey == INTEGRATION_KEY
    }

    @Test
    def "the exception is handled when the persistence service throws a InvalidDataException while creating an entity"() {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> {
            throw new InvalidDataException("message", "test_code", "EntityType")
        }

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        def e = thrown(InvalidEntryDataException)
        e.code == 'test_code'
        e.integrationKey == INTEGRATION_KEY
    }

    @Test
    def "the exception is handled when the persistence service throws a ModelServiceException while creating an entity"() {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> { throw new ModelSavingException() }

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        def e = thrown(PersistenceRuntimeApplicationException)
        e.code == 'runtime_error'
        e.integrationKey == INTEGRATION_KEY
    }


    @Test
    def "the exception is handled when a batch exceeds the batch limit"() {
        given:
        def exceptionCause = 'Something went wrong'
        oDataProcessor.parseBatchRequest(_, _, _) >> [Stub(BatchRequestPart), Stub(BatchRequestPart), Stub(BatchRequestPart)]

        when:
        oDataProcessor.executeBatch Stub(BatchHandler), APPLICATION_XML, Stub(InputStream)

        then:
        def e = thrown(BatchLimitExceededException)
        e.message == String.format(
                "The number of integration objects sent in the " +
                        "request has exceeded the 'odata2services.batch.limit' setting currently set to 2",
                exceptionCause)
        e.code == 'batch_limit_exceeded'
    }

    @Test
    def "an exception is thrown when the persistence service throws a #exception while creating an entity"() {
        given:
        persistenceService.createEntityData(_ as StorageRequest) >> { throw exception }

        when:
        oDataProcessor.createEntity Stub(UriInfo), Stub(InputStream), APPLICATION_XML, APPLICATION_XML

        then:
        thrown exceptionThrown

        where:
        exception                            | exceptionThrown
        Stub(IntegrationAttributeException)  | IntegrationAttributeException
        Stub(InvalidAttributeValueException) | InvalidPropertyValueException
    }

    @Test
    def "processor parses batch successfully"() {
        given:
        def part1 = Stub(BatchRequestPart)
        def part2 = Stub(BatchRequestPart)
        oDataProcessor.parseBatchRequest(_, _, _) >> [part1, part2]
        and:
        def batchHandler = Mock(BatchHandler) {
            handleBatchPart(part1) >> Stub(BatchResponsePart)
            handleBatchPart(part2) >> Stub(BatchResponsePart)
        }

        when:
        oDataProcessor.executeBatch(batchHandler, "multipart/mixed", Stub(InputStream))

        then:
        1 * oDataProcessor.writeBatchResponse(_ as List) >> Stub(ODataResponse)

        1 * batchHandler.handleBatchPart(part1)
        1 * batchHandler.handleBatchPart(part2)
    }

    @Test
    def "processor parses change set in transaction"() {
        given:
        def request1 = Stub(ODataRequest) {
            getStatus() >> HttpStatusCodes.CREATED
        }
        def request2 = Stub(ODataRequest) {
            getStatus() >> HttpStatusCodes.CREATED
        }
        def response1 = Stub(ODataResponse)
        def response2 = Stub(ODataResponse)

        def batchHandler = Mock(BatchHandler) {
            handleRequest(request1) >> Stub(BatchResponsePart)
            handleRequest(request2) >> Stub(BatchResponsePart)
        }

        when:
        oDataProcessor.executeChangeSet(batchHandler, Lists.newArrayList(request1, request2))

        then:
        1 * oDataProcessor.beginTransaction() >> { void }
        1 * oDataProcessor.executeInTransaction(_, _) >> Lists.newArrayList(response1, response2)
        1 * oDataProcessor.commitTransaction() >> { void }

        1 * oDataProcessor.partFromResponses(_ as List) >> { args ->
            assert args[0].size == 2
            assert args[0].containsAll(response1, response2)
        }
    }

    @Test
    def "delete entity when an EdmException is thrown"() {
        given:
        persistenceService.deleteItem(_) >> { throw Stub(EdmException) }

        when:
        def response = oDataProcessor.deleteEntity(null, null)

        then:
        response.entity == ""
        response.status == HttpStatusCodes.INTERNAL_SERVER_ERROR
    }

    @Test
    def "delete entity success"() {
        given:
        def uriInfo = Stub(DeleteUriInfo)

        when:
        def response = oDataProcessor.deleteEntity(uriInfo, null)

        then:
        response.entity == ""
        response.status == HttpStatusCodes.OK
        1 * itemLookupRequestFactory.create(uriInfo, _)
        1 * persistenceService.deleteItem(_)
    }
}