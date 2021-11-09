/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.asserts.JsonObjectAssertion
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.processor.ODataPayloadProcessingException
import org.apache.commons.io.IOUtils
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.PathInfo
import org.junit.Test
import spock.lang.Specification

import java.nio.charset.Charset

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2services.odata.content.ODataBatchBuilder.batchBuilder
import static de.hybris.platform.odata2services.odata.content.ODataChangeSetBuilder.changeSetBuilder
import static de.hybris.platform.odata2services.odata.content.ODataChangeSetPartBuilder.partBuilder

@UnitTest
class DefaultBatchParamUnitTest extends Specification {

    private static final String PROPERTY_NAME = 'code'
    private static final String PROPERTY_VALUE = 'some value'

    @Test
    def "fields are default values when no parameters are passed in"() {
        given:
        def param = DefaultBatchParam.batchParam().build()

        expect:
        with(param) {
            !batchHandler
            batchRequestParts == []
            batchRequestPartSize == 0
        }
    }

    @Test
    def "fields are the correct values when parameters are passed in"() {
        given:
        def batchHandler = Stub BatchHandler
        def content = content()
        def contentType = 'multipart/mixed; boundary=batch'
        def context = context()

        and:
        def param = DefaultBatchParam.batchParam()
                .withBatchHandler(batchHandler)
                .withContent(content)
                .withResponseContentType(contentType)
                .withContext(context)
                .build()

        expect:
        param.batchHandler == batchHandler
        param.batchRequestPartSize == 1
        def batchRequestPart = param.batchRequestParts[0]
        batchRequestPart.changeSet
        JsonObjectAssertion.assertionOf(JsonObject
                .createFrom(batchRequestPart.requests[0].body))
                .hasPathWithValue(PROPERTY_NAME, PROPERTY_VALUE)
    }

    @Test
    def "cannot parse content to create batch request parts when #condition"() {
        given:
        def param = DefaultBatchParam.batchParam()
                .withContext(context)
                .withResponseContentType(contentType)
                .withContent(content)
                .build()

        expect:
        param.batchRequestParts == []
        param.batchRequestPartSize == 0

        where:
        parameter                  | content           | contentType        | context
        'content is null'          | null              | 'application/json' | Stub(ODataContext)
        'contentType is null'      | Stub(InputStream) | null               | Stub(ODataContext)
        'contentType is empty'     | Stub(InputStream) | ''                 | Stub(ODataContext)
        'context is null'          | Stub(InputStream) | 'application/xml'  | null
        'context.pathInfo is null' | Stub(InputStream) | 'application/xml'  | Stub(ODataContext) { getPathInfo() >> null }

    }

    @Test
    def "failed parsing the content throws exception"() {
        given:
        def batchHandler = Stub BatchHandler
        def content = IOUtils.toInputStream('Invalid input', Charset.defaultCharset())
        def contentType = 'multipart/mixed; boundary=batch'
        def context = context()

        when:
        DefaultBatchParam.batchParam()
                .withBatchHandler(batchHandler)
                .withContent(content)
                .withResponseContentType(contentType)
                .withContext(context)
                .build()

        then:
        thrown ODataPayloadProcessingException
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
                        .withPart(partBuilder().withBody(json().withField(PROPERTY_NAME, PROPERTY_VALUE)))
                ).build()
        IOUtils.toInputStream(payload, Charset.defaultCharset())
    }
}
