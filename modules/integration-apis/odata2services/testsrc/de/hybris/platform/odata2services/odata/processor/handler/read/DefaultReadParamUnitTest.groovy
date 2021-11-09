/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.read

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.UriInfo
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultReadParamUnitTest extends Specification {

    @Test
    def "fields are null if not set"() {
        given:
        def param = DefaultReadParam.readParam().build()

        expect:
        with(param) {
            !uriInfo
            !responseContentType
            !context
        }
    }

    @Test
    def "fields are not null if set"() {
        given:
        def contentType = 'application/json'
        def context = Stub ODataContext
        def uriInfo = Stub UriInfo

        and:
        def param = DefaultReadParam.readParam()
                        .withResponseContentType(contentType)
                        .withContext(context)
                        .withUriInfo(uriInfo)
                        .build()

        expect:
        with(param) {
            contentType == contentType
            context == context
            uriInfo == uriInfo
        }
    }
}
