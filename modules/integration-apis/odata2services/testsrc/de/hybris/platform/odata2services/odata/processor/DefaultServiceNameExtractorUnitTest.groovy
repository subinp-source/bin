/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import org.apache.commons.lang3.StringUtils
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.PathInfo
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

@UnitTest
class DefaultServiceNameExtractorUnitTest extends Specification {

    def serviceNameExtractor = new DefaultServiceNameExtractor()

    @Test
    @Unroll
    def "successfully extracted the service name from #uri"() {
        given:
        def context = Stub(ODataContext) {
            getPathInfo() >> Stub(PathInfo) {
                getServiceRoot() >> URI.create(uri)
            }
        }

        expect:
        def serviceName = serviceNameExtractor.extract(context)
        serviceName == "serviceName"

        where:
        uri << ["https://localhost:123/context/serviceName/",
                "https://localhost:123/context/serviceName",
                "serviceName",
                "/serviceName/"]
    }

    @Test
    def "getting pathInfo from context throws ODataException"() {
        given:
        def context = Stub(ODataContext) {
            getPathInfo() >> { throw new ODataException("testing throw exception") }
        }

        when:
        serviceNameExtractor.extract(context)

        then:
        thrown(InternalProcessingException)
    }

    @Test
    def "null service root throws InternalProcessingException"() {
        given:
        def context = Stub(ODataContext) {
            getPathInfo() >> Mock(PathInfo)
        }

        when:
        serviceNameExtractor.extract(context)

        then:
        thrown(InternalProcessingException)
    }

    @Test
    def "null path throws InternalProcessingException"() {
        given:
        def context = Stub(ODataContext) {
            getPathInfo() >> Stub(PathInfo) {
                getServiceRoot() >> GroovyStub(URI) {
                    getPath() >> null
                }
            }
        }

        when:
        serviceNameExtractor.extract(context)

        then:
        thrown(InternalProcessingException)
    }

    @Test
    @Unroll
    def "extract from path #pathInfo returns #ioName"() {
        given:
        def request = request(pathInfo)

        expect:
        serviceNameExtractor.extract(request) == expectedIo

        where:
        pathInfo                          | expectedIo        | ioName
        null                              | StringUtils.EMPTY | "empty String"
        ""                                | StringUtils.EMPTY | "empty String"
        "pathTooShort"                    | StringUtils.EMPTY | "empty String"
        "odata2webservices/MyIO/Products" | "MyIO"            | "MyIO"
        "odata2webservices/MyIO"          | "MyIO"            | "MyIO"
    }

    private HttpServletRequest request(String pathInfo) {
        Stub(HttpServletRequest) {
            getPathInfo() >> pathInfo
        }
    }
}
