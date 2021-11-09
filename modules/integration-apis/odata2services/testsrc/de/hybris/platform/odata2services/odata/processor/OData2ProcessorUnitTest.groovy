/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor
import de.hybris.platform.integrationservices.security.AccessRightsService
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.odata.processor.handler.delete.DeleteHandler
import de.hybris.platform.odata2services.odata.processor.handler.delete.DeleteParam
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PatchPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceParam
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.BatchParam
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.BatchPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.ChangeSetParam
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.ChangeSetPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.read.ReadHandler
import de.hybris.platform.odata2services.odata.processor.handler.read.ReadParam
import org.apache.commons.io.IOUtils
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.batch.BatchResponsePart
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.UriInfo
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2services.odata.content.ODataBatchBuilder.batchBuilder
import static de.hybris.platform.odata2services.odata.content.ODataChangeSetBuilder.changeSetBuilder
import static de.hybris.platform.odata2services.odata.content.ODataChangeSetPartBuilder.partBuilder

@UnitTest
class OData2ProcessorUnitTest extends Specification {
    private static final def INTEGRATION_OBJECT = 'MyTestProductIO'
    private static final def ITEM_CODE = 'TestProduct'
    private static final def ITEM_TYPE = 'Product'

    def batchPersistenceHandler = Mock BatchPersistenceHandler
    def changeSetPersistenceHandler = Mock ChangeSetPersistenceHandler
    def persistenceHandler = Mock PersistenceHandler
    def patchPersistenceHandler = Mock PatchPersistenceHandler
    def deleteHandler = Mock DeleteHandler
    def readHandler = Mock ReadHandler
    def accessRightsService = Mock AccessRightsService

    def typeDescriptor = Optional.of(stubTypeDescriptor(INTEGRATION_OBJECT, ITEM_CODE, ITEM_TYPE))
    def serviceNameExtractor = Stub(ServiceNameExtractor) { extract(_ as ODataContext) >> INTEGRATION_OBJECT }
    def itemTypeDescriptorService = Stub(ItemTypeDescriptorService) { getTypeDescriptor(INTEGRATION_OBJECT, ITEM_CODE) >> typeDescriptor }

    def context = context()
    def processor = new OData2Processor(batchPersistenceHandler: batchPersistenceHandler,
            changeSetPersistenceHandler: changeSetPersistenceHandler,
            persistenceHandler: persistenceHandler,
            patchPersistenceHandler: patchPersistenceHandler,
            deleteHandler: deleteHandler,
            readHandler: readHandler,
            accessRightsService: accessRightsService,
            serviceNameExtractor: serviceNameExtractor,
            itemTypeDescriptorService: itemTypeDescriptorService,
            context: context)

    @Test
    def "BatchPersistenceHandler is called for executeBatch"() {
        given:
        def batchHandler = Stub BatchHandler
        def contentType = 'multipart/mixed; boundary=batch'
        def content = content()
        def response = Stub ODataResponse

        when:
        def actualResponse = processor.executeBatch batchHandler, contentType, content

        then:
        1 * batchPersistenceHandler.handle(_ as BatchParam) >> { args ->
            def param = args[0] as BatchParam
            assert param.batchHandler == batchHandler
            assert !param.batchRequestParts.isEmpty()
            assert param.batchRequestPartSize == 1; response
        }
        actualResponse == response
    }

    @Test
    def "ChangeSetPersistenceHandler is called for executeChangeSet"() {
        given:
        def batchHandler = Stub BatchHandler
        def requests = [Stub(ODataRequest)]
        def response = Stub BatchResponsePart

        when:
        def actualResponse = processor.executeChangeSet batchHandler, requests

        then:
        1 * changeSetPersistenceHandler.handle(_ as ChangeSetParam) >> { args ->
            def param = args[0] as ChangeSetParam
            assert param.batchHandler == batchHandler
            assert param.requests == requests; response
        }
        actualResponse == response
    }

    @Test
    def "PersistenceHandler is called for createEntity"() {
        given:
        def entityType = Stub(EdmEntityType) {
            getName() >> ITEM_CODE
        }
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> entityType
        }
        def uriInfo = Stub(UriInfo) {
            getStartEntitySet() >> entitySet
        }
        def content = Stub InputStream
        def requestContentType = 'application/json'
        def responseContentType = 'application/xml'
        def response = Stub ODataResponse

        when:
        def actualResponse = processor.createEntity uriInfo, content, requestContentType, responseContentType

        then:
        1 * persistenceHandler.handle(_ as PersistenceParam) >> { args ->
            def param = args[0] as PersistenceParam
            assert param.content == content
            assert param.context == context
            assert param.entityType == entityType
            assert param.entitySet == entitySet
            assert param.requestContentType == requestContentType
            assert param.responseContentType == responseContentType
            assert param.uriInfo == uriInfo; response
        }
        actualResponse == response
        1 * accessRightsService.checkCreatePermission(ITEM_TYPE)
    }

    @Test
    def "PatchPersistenceHandler is called for updateEntity"() {
        given:
        def entityType = Stub(EdmEntityType) {
            getName() >> ITEM_CODE
        }
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> entityType
        }
        def uriInfo = Stub(UriInfo) {
            getStartEntitySet() >> entitySet
        }
        def content = Stub InputStream
        def requestContentType = 'application/json'
        def responseContentType = 'application/xml'
        def doesNotMatterMerge = true
        def response = Stub ODataResponse

        when:
        def actualResponse = processor.updateEntity uriInfo, content, requestContentType, doesNotMatterMerge, responseContentType

        then:
        1 * patchPersistenceHandler.handle(_ as PersistenceParam) >> { args ->
            def param = args[0] as PersistenceParam
            assert param.content == content
            assert param.context == context
            assert param.entityType == entityType
            assert param.entitySet == entitySet
            assert param.requestContentType == requestContentType
            assert param.responseContentType == responseContentType
            assert param.uriInfo == uriInfo; response
        }
        actualResponse == response
        1 * accessRightsService.checkUpdatePermission(ITEM_TYPE)
    }

    @Test
    def "DeleteHandler is called for deleteEntity"() {
        given:
        def uriInfo = Stub(DeleteUriInfo) {
            getStartEntitySet() >> entitySetForItemType(ITEM_CODE)
        }
        def responseContentType = 'Does Not Matter'
        def response = Stub ODataResponse

        when:
        def actualResponse = processor.deleteEntity uriInfo, responseContentType

        then:
        1 * deleteHandler.handle(_ as DeleteParam) >> { args ->
            def param = args[0] as DeleteParam
            assert param.context == context
            assert param.uriInfo == uriInfo; response
        }
        actualResponse == response
        1 * accessRightsService.checkDeletePermission(ITEM_TYPE)
    }

    @Test
    @Unroll
    def "ReadHandler is called for #method"() {
        given:
        def uriInfo = Stub(UriInfo) {
            getStartEntitySet() >> entitySetForItemType(ITEM_CODE)
        }
        def responseContentType = 'application/json'
        def response = Stub ODataResponse

        when:
        def actualResponse = processor."${method}" uriInfo, responseContentType

        then:
        1 * readHandler.handle(_ as ReadParam) >> { args ->
            def param = args[0] as ReadParam
            assert param.context == context
            assert param.responseContentType == responseContentType
            assert param.uriInfo == uriInfo; response
        }
        actualResponse == response
        1 * accessRightsService.checkReadPermission(ITEM_TYPE)

        where:
        method << ['countEntitySet', 'readEntity', 'readEntitySet']
    }

    private ODataContext context() {
        Stub(ODataContext) {
            getPathInfo() >> Stub(PathInfo) {
                getServiceRoot() >> URI.create('http://url/to/service/root')
            }
        }
    }

    private static InputStream content() {
        def payload = batchBuilder()
                .withBoundary('batch')
                .withChangeSet(changeSetBuilder()
                        .withUri('Products')
                        .withPart(partBuilder().withBody(json().withField('property', 'value')))
                ).build()
        IOUtils.toInputStream(payload, Charset.defaultCharset())
    }

    private ItemTypeDescriptor stubTypeDescriptor(final String object, final String itemCode, final String itemType) {
        def itemModel = Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> object
            }
            getCode() >> itemCode
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> itemType
            }
        }
        new ItemTypeDescriptor(itemModel)
    }

    def entitySetForItemType(String itemType) {
        def entityType = Stub(EdmEntityType) {
            getName() >> itemType
        }
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> entityType
        }
        entitySet
    }
}
