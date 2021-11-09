/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.delete

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultDeleteParamUnitTest extends Specification {

    @Test
    def "fields are null if not set"() {
        given:
        def param = DefaultDeleteParam.deleteParam().build()

        expect:
        with(param) {
            !context
            !uriInfo
        }
    }

    @Test
    def "fields are not null if set"() {
        given:
        def context = Stub ODataContext
        def uriInfo = Stub DeleteUriInfo

        and:
        def param = DefaultDeleteParam.deleteParam()
                        .withContext(context)
                        .withUriInfo(uriInfo)
                        .build()

        expect:
        with(param) {
            context == context
            uriInfo == uriInfo
        }
    }
}
