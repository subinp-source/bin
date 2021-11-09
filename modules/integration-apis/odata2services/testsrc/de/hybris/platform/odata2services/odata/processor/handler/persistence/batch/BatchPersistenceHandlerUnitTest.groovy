/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.config.ODataServicesConfiguration
import de.hybris.platform.odata2services.odata.processor.BatchLimitExceededException
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.batch.BatchRequestPart
import org.apache.olingo.odata2.api.batch.BatchResponsePart
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Specification

@UnitTest
class BatchPersistenceHandlerUnitTest extends Specification {

    def configuration = Stub ODataServicesConfiguration
    def handler = new BatchPersistenceHandler(oDataServicesConfiguration: configuration)

    @Test
    def "batch is persisted"() {
        given:
        def batchHandler = batchHandler()

        and:
        def param = Stub(BatchParam) {
            getBatchHandler() >> batchHandler
            getBatchRequestParts() >> [Stub(BatchRequestPart)]
            getBatchRequestPartSize() >> 1
        }

        and:
        configuration.getBatchLimit() >> 5

        when:
        def response = handler.handle param

        then:
        response.status == HttpStatusCodes.ACCEPTED
        def payload = response.entity as String
        payload.contains '201 Created'
        payload.contains 'hello world'
    }

    @Test
    def "fail to persist when the number of batches is over the limit"() {
        given:
        def param = Stub(BatchParam) {
            getBatchHandler() >> Stub(BatchHandler)
            getBatchRequestParts() >> [Stub(BatchRequestPart)]
            getBatchRequestPartSize() >> 1
        }

        and: "set limit to 0"
        configuration.getBatchLimit() >> 0

        when: "there is one batch payload"
        handler.handle param

        then:
        def e = thrown BatchLimitExceededException
        e.httpStatus == HttpStatusCodes.BAD_REQUEST
        e.code == 'batch_limit_exceeded'
    }

    @Test
    def "fail to persist when the batch handler throws ODataException"() {
        given:
        def param = Stub(BatchParam) {
            getBatchHandler() >> Stub(BatchHandler) {
                handleBatchPart(_ as BatchRequestPart) >> { throw new ODataException('IGNORE - testing failure') }
            }
            getBatchRequestParts() >> [Stub(BatchRequestPart)]
            getBatchRequestPartSize() >> 1
        }

        and:
        configuration.getBatchLimit() >> 5

        when:
        handler.handle param

        then:
        thrown ODataException
    }

    private BatchHandler batchHandler() {
        Stub(BatchHandler) {
            handleBatchPart(_ as BatchRequestPart) >> Stub(BatchResponsePart) {
                getResponses() >> [ODataResponse.entity(response()).status(HttpStatusCodes.CREATED).build()]
                isChangeSet() >> true
            }
        }
    }
    
    private static String response() {
        '--batch_eee05884-09ae-4b13-9901-a9a300a19fe8\n' +
        'Content-Type: multipart/mixed; boundary=changeset_22184ee7-3180-4486-9e65-07ff4a75a03c\n' +
        '\n' +
        '--changeset_22184ee7-3180-4486-9e65-07ff4a75a03c\n' +
        'Content-Type: application/http\n' +
        'Content-Transfer-Encoding: binary\n' +
        '\n' +
        'HTTP/1.1 201 Created\n' +
        'Content-Language: en\n' +
        'DataServiceVersion: 2.0\n' +
        'Location: https://url/to/item("xyz")\n' +
        'Content-Type: application/json\n' +
        'Content-Length: 123\n' +
        '\n' +
        '{"d": "hello world"}' +
        '\n' +
        '--changeset_22184ee7-3180-4486-9e65-07ff4a75a03c\n' +
        '--batch_eee05884-09ae-4b13-9901-a9a300a19fe8--\n'
    }
}
