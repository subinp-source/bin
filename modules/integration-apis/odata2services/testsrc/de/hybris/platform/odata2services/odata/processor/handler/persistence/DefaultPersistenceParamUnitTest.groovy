/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.UriInfo
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultPersistenceParamUnitTest extends Specification {

    @Test
    def "fields are null if not set"() {
        given:
        def param = DefaultPersistenceParam.persistenceParam().build()

        expect:
        with(param) {
            !content
            !context
            !entitySet
            !entityType
            !requestContentType
            !responseContentType
            !uriInfo
        }
    }

    @Test
    def "fields are not null if set"() {
        given:
        def content = Stub InputStream
        def context = Stub ODataContext
        def requestContentType = 'application/json'
        def responseContentType = 'application/xml'
        def entityType = Stub EdmEntityType
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> entityType
        }
        def uriInfo = Stub(UriInfo) {
            getStartEntitySet() >> entitySet
        }

        and:
        def param = DefaultPersistenceParam.persistenceParam()
                        .withContent(content)
                        .withContext(context)
                        .withRequestContentType(requestContentType)
                        .withResponseContentType(responseContentType)
                        .withUriInfo(uriInfo)
                        .build()

        expect:
        with(param) {
            content == content
            context == context
            entitySet == entitySet
            entityType == entityType
            requestContentType == requestContentType
            responseContentType == responseContentType
            uriInfo == uriInfo
        }
    }

    @Test
    def "entity type is null when an exception is thrown"() {
        given:
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> { throw new EdmException(EdmException.COMMON) }
        }
        def uriInfo = Stub(UriInfo) {
            getStartEntitySet() >> entitySet
        }

        and:
        def param = DefaultPersistenceParam.persistenceParam()
                        .withUriInfo(uriInfo)
                        .build()

        expect:
        !param.entityType
    }
}
