/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.constants.Odata2servicesConstants
import de.hybris.platform.odata2services.converter.ContentTypeNotSupportedException
import de.hybris.platform.odata2services.converter.PathInfoInvalidException
import de.hybris.platform.odata2services.converter.RequestMethodNotSupportedException
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.*

@UnitTest
class HttpServletRequestToODataRequestConverterUnitTest extends Specification {
    private static final String MERGE = 'MERGE'
    private static final String NON_SUPPORTED = 'BADBADMETHOD'

    private static final String APPLICATION_JSON = 'application/json'
    private static final String APPLICATION_XML = 'application/xml'
    private static final String BAD_CONTENT_TYPE = 'badContentType'
    private static final String INVALID_ACCEPT_HEADER = 'invalidHeader'
    private static final String VALID_SERVER_NAME = 'my.server'
    private static final int DEFAULT_PORT = 8080
    private static final String PRODUCT_REQUEST = 'product'

    def converter = new HttpServletRequestToODataRequestConverter()

    @Test
    @Unroll
    def "converts #method request"() {
        when:
        def converted = converter.convert createHttpServletRequest(method as String)

        then:
        converted?.method?.name() == method

        where:
        method << [HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(),
                   HttpMethod.DELETE.name(), HttpMethod.PATCH.name(), MERGE]
    }

    @Test
    def 'throws exception when converts unsupported HTTP method'() {
        when:
        converter.convert createHttpServletRequest(NON_SUPPORTED)

        then:
        def e = thrown RequestMethodNotSupportedException
        e.method == NON_SUPPORTED
    }

    @Test
    def 'convert well formed Content-Type'() {
        given:
        def converted = converter.convert createHttpServletRequest(HttpMethod.GET.name(), APPLICATION_JSON)

        expect:
        converted?.contentType == APPLICATION_JSON
    }

    @Test
    def 'throws exception when Content-Type is unknown'() {
        when:
        converter.convert createHttpServletRequest(HttpMethod.GET.name(), BAD_CONTENT_TYPE)

        then:
        def e = thrown ContentTypeNotSupportedException
        e.contentType == BAD_CONTENT_TYPE
    }

    @Test
    @Unroll
    def "converts header Accept: #value"() {
        given:
        def httpServletRequest = createHttpServletRequest HttpMethod.GET.name(), APPLICATION_JSON
        httpServletRequest.addHeader 'Accept', value

        when:
        def converted = converter.convert httpServletRequest

        then:
        converted?.acceptHeaders == headers

        where:
        value                                 | headers
        APPLICATION_JSON                      | [APPLICATION_JSON]
        "$APPLICATION_JSON, $APPLICATION_XML" | [APPLICATION_JSON, APPLICATION_XML]
        INVALID_ACCEPT_HEADER                 | []
    }

    @Test
    @Unroll
    def "converts header Accept-Language: #header"() {
        given:
        def httpServletRequest = createHttpServletRequest HttpMethod.GET.name(), APPLICATION_JSON
        httpServletRequest.addHeader 'Accept-Language', header

        when:
        def converted = converter.convert httpServletRequest

        then:
        converted.acceptableLanguages == locales

        where:
        header                 | locales
        'en'                   | [Locale.ENGLISH]
        'en, fr, es_CO, zh-TW' | [Locale.ENGLISH, Locale.FRENCH, createLocale('es', 'CO'), createLocale('zh', 'TW')]
        '////'                 | []
    }

    @Test
    def 'converts valid request path'() {
        given:
        def httpServletRequest = createHttpServletRequest(HttpMethod.GET.name(), APPLICATION_JSON).tap {
            serverName = VALID_SERVER_NAME
            serverPort = DEFAULT_PORT
        }

        when:
        def convertedRequest = converter.convert httpServletRequest

        then:
        convertedRequest.pathInfo.requestUri == URI.create("http://$VALID_SERVER_NAME:$DEFAULT_PORT/$PRODUCT_REQUEST")
    }

    @Test
    def 'throws excepton if request path is invalid'() {
        given:
        def httpServletRequest = createHttpServletRequest(HttpMethod.GET.name(), APPLICATION_JSON).tap {
            serverName = 'l;kfdsf;adslkdfs;'
            serverPort = DEFAULT_PORT
        }

        when:
        converter.convert httpServletRequest

        then:
        thrown PathInfoInvalidException
    }

    @Test
    def 'converts one arbitrary header'() {
        given:
        def httpServletRequest = createHttpServletRequest HttpMethod.GET.name()
        httpServletRequest.addHeader 'someHeader', 'header-value'

        when:
        def convertedReq = converter.convert httpServletRequest

        then:
        convertedReq.requestHeaders == [someheader: ['header-value']]
    }

    @Test
    def 'converts multiple headers with different value types into headers with String values'() {
        given:
        def httpServletRequest = createHttpServletRequest(HttpMethod.GET.name(), APPLICATION_JSON)
        httpServletRequest.addHeader 'Header-One', 1
        httpServletRequest.addHeader 'Header-Two', true

        when:
        def convertedReq = converter.convert httpServletRequest

        then:
        convertedReq.requestHeaders == ['content-type': [APPLICATION_JSON], 'header-one': ['1'], 'header-two': ['true']]
    }

    @Test
    def "conversion supports forms encoding for parameter values"() {
        given:
        def httpServletRequest = createHttpServletRequest HttpMethod.GET.name()

        when:
        def oDataRequest = converter.convert httpServletRequest

        then:
        oDataRequest.allQueryParameters == ['odata-accept-forms-encoding': ['true']]
    }

    @Test
    @Unroll
    def "conversion does not support forms encoding for #uri requests"() {
        given:
        def httpRequest = new MockHttpServletRequest(method: HttpMethod.GET.name(), requestURI: uri)

        when:
        def oDataRequest = converter.convert httpRequest

        then:
        !oDataRequest.allQueryParameters.containsKey('odata-accept-forms-encoding')

        where:
        uri << ['/$metadata', '/$metadata?Product']
    }

    @Test
    @Unroll
    def "converts query string ?#query into parameters #params"() {
        given:
        def httpServletRequest = createHttpServletRequest HttpMethod.GET.name()
        httpServletRequest.queryString = query

        when:
        def oDataRequest = converter.convert httpServletRequest

        then:
        oDataRequest.allQueryParameters.remove 'odata-accept-forms-encoding'
        oDataRequest.allQueryParameters == params

        where:
        query                    | params
        'Product'                | [Product: ['']]
        'name=Jane'              | [name: ['Jane']]
        'name=Jane&lastName=Doe' | [name: ['Jane'], lastName: ['Doe']]
        'name=Jane%20Doe'        | [name: ['Jane Doe']]
        'name=Jane+Doe'          | [name: ['Jane+Doe']]
    }

    private static MockHttpServletRequest createHttpServletRequest(String method, String contentType = null) {
        new MockHttpServletRequest(method: method, contentType: contentType, requestURI: "/$PRODUCT_REQUEST")
    }

    private static Locale createLocale(final String language, final String region) {
        new Locale.Builder()
                .setLanguage(language)
                .setRegion(region)
                .build()
    }
}