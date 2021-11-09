/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.tx.Transaction
import de.hybris.platform.tx.TransactionBody
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ChangeSetPersistenceHandlerUnitTest extends Specification {

    def modelService = Mock ModelService
    def transaction = Mock Transaction
    def handler = Spy(ChangeSetPersistenceHandler)

    def setup() {
        transaction.execute(_ as TransactionBody) >> { args -> args[0].execute() }
        handler.modelService = modelService
        handler.getCurrentTransaction() >> transaction
    }

    @Test
    def "changeSet persisted"() {
        given:
        def odataResponse = ODataResponse.entity(response()).status(HttpStatusCodes.CREATED).build()
        def batchHandler = Stub(BatchHandler) {
            handleRequest(_ as ODataRequest) >> odataResponse
        }
        def requests = [Stub(ODataRequest)]
        def param = DefaultChangeSetParam.changeSetParam()
                .withBatchHandler(batchHandler)
                .withRequests(requests)
                .build()

        when:
        def responsePart = handler.handle param

        then:
        responsePart.responses == [odataResponse]
        responsePart.changeSet
    }

    @Test
    @Unroll
    def "changeSet persistence commits transaction when #condition"() {
        given:
        def batchHandler = Stub(BatchHandler) {
            handleRequest(_ as ODataRequest) >> ODataResponse.entity(response()).status(HttpStatusCodes.CREATED).build()
        }
        def param = DefaultChangeSetParam.changeSetParam()
                .withBatchHandler(batchHandler)
                .withRequests(requests)
                .build()

        when:
        handler.handle param

        then:
        transaction.commit()

        where:
        condition                              | requests
        'zero request produces zero responses' | []
        'a request produces a valid response'  | [Stub(ODataRequest)]
    }

    @Test
    def "changeSet persistence returns bad request if any of the requests is invalid"() {
        given:
        def createdResponse = ODataResponse.entity(response()).status(HttpStatusCodes.CREATED).build()
        def badRequestResponse = ODataResponse.entity(response(HttpStatusCodes.BAD_REQUEST)).status(HttpStatusCodes.BAD_REQUEST).build()
        def batchHandler = Stub(BatchHandler) {
            handleRequest(_ as ODataRequest) >> createdResponse >> badRequestResponse
        }
        def requests = [Stub(ODataRequest), Stub(ODataRequest)]
        def param = DefaultChangeSetParam.changeSetParam()
                .withBatchHandler(batchHandler)
                .withRequests(requests)
                .build()

        when:
        def responsePart = handler.handle param

        then:
        responsePart.responses == [badRequestResponse]
        responsePart.changeSet
    }

    @Test
    @Unroll
    def "changeSet persistence rolls back transaction when response is a bad request"() {
        given:
        def batchHandler = Stub(BatchHandler) {
            handleRequest(_ as ODataRequest) >> ODataResponse.entity(response(HttpStatusCodes.BAD_REQUEST)).status(HttpStatusCodes.BAD_REQUEST).build()
        }
        def requests = [Stub(ODataRequest)]
        def param = DefaultChangeSetParam.changeSetParam()
                .withBatchHandler(batchHandler)
                .withRequests(requests)
                .build()

        when:
        handler.handle param

        then:
        1 * transaction.rollback()
        1 * modelService.detachAll()
    }

    @Test
    def "changeSet persistence throws InternalProcessingException when an exception occurs during processing"() {
        given:
        def batchHandler = Stub(BatchHandler) {
            handleRequest(_ as ODataRequest) >> { throw new ODataException('IGNORE - testing exception') }
        }
        def requests = [Stub(ODataRequest)]
        def param = DefaultChangeSetParam.changeSetParam()
                .withBatchHandler(batchHandler)
                .withRequests(requests)
                .build()

        when:
        handler.handle param

        then:
        def e = thrown InternalProcessingException
        e.httpStatus == HttpStatusCodes.INTERNAL_SERVER_ERROR
        e.code == 'internal_error'
    }

    private static String response(HttpStatusCodes status = HttpStatusCodes.CREATED) {
        '--batch_eee05884-09ae-4b13-9901-a9a300a19fe8\n' +
                'Content-Type: multipart/mixed; boundary=changeset_22184ee7-3180-4486-9e65-07ff4a75a03c\n' +
                '\n' +
                '--changeset_22184ee7-3180-4486-9e65-07ff4a75a03c\n' +
                'Content-Type: application/http\n' +
                'Content-Transfer-Encoding: binary\n' +
                '\n' +
                "HTTP/1.1 ${status.statusCode} ${status.name()}\n" +
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
