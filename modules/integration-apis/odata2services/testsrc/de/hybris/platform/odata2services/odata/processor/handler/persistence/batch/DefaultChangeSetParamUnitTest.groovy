/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence.batch

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.batch.BatchHandler
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultChangeSetParamUnitTest extends Specification {

    @Test
    def "fields are null if not set"() {
        given:
        def param = DefaultChangeSetParam.changeSetParam().build()

        expect:
        with(param) {
            !batchHandler
            !requests
        }
    }

    @Test
    def "fields are not null if set"() {
        given:
        def batchHandler = Stub BatchHandler
        def requests = [Stub(ODataRequest)]

        and:
        def param = DefaultChangeSetParam.changeSetParam()
                        .withBatchHandler(batchHandler)
                        .withRequests(requests)
                        .build()

        expect:
        with(param) {
            batchHandler == batchHandler
            requests == requests
        }
    }
}
