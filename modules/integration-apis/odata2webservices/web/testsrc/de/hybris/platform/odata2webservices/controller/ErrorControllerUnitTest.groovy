package de.hybris.platform.odata2webservices.controller

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.integrationservices.util.XmlObject
import net.minidev.json.parser.JSONParser
import org.apache.http.HttpStatus
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.transform.TransformerFactory

@UnitTest
class ErrorControllerUnitTest extends Specification {
    def controller = new ErrorController()

    @Test
    @Unroll
    def "converts response with status code #status to JSON error"() {
        given:
        def err = controller.handleError httpRequest('application/json'), httpResponse(status)

        expect:
        def json = JsonObject.createFrom err
        json.getString('error.code') == code
        json.getString('error.message.lang') == 'en'
        json.getString('error.message.value') == message

        where:
        status                     | code           | message
        HttpStatus.SC_UNAUTHORIZED | 'unauthorized' | 'user must be authenticated'
        HttpStatus.SC_FORBIDDEN    | 'forbidden'    | 'credentials provided are not authorized for the operation'
        HttpStatus.SC_NOT_FOUND    | 'not_found'    | 'requested resource does not exist'
    }

    @Test
    def "json is not malformed"() {
        when:
        def err = controller.handleError httpRequest('application/json'), httpResponse(HttpStatus.SC_FORBIDDEN)

        then:
        new JSONParser(JSONParser.MODE_STRICTEST).parse(err)
    }

    @Test
    @Unroll
    def "converts response with status code #status to XML error"() {
        given:
        def err = controller.handleError httpRequest('application/xml'), httpResponse(status)

        expect:
        def xml = XmlObject.createFrom err
        xml.get('/error/code') == code
        xml.get('/error/message/@lang') == 'en'
        xml.get('/error/message') == message

        where:
        status                     | code           | message
        HttpStatus.SC_UNAUTHORIZED | 'unauthorized' | 'user must be authenticated'
        HttpStatus.SC_FORBIDDEN    | 'forbidden'    | 'credentials provided are not authorized for the operation'
        HttpStatus.SC_NOT_FOUND    | 'not_found'    | 'requested resource does not exist'
    }

    @Test
    @Unroll
    def "converts response to XML error when content-type is #condition"() {
        given:
        def err = controller.handleError httpRequest(accept), httpResponse(HttpStatus.SC_FORBIDDEN)

        expect:
        def xml = XmlObject.createFrom err
        xml.get('/error/code') == 'forbidden'

        where:
        condition     | accept
        'null'        | null
        'empty'       | ''
        'unsupported' | 'application/octet-stream'
        'invalid'     | 'blah'
    }

    @Test
    def 'converts non-standard status codes to unknown error'() {
        given:
        def err = controller.handleError httpRequest('application/json'), httpResponse(0)

        expect:
        def json = JsonObject.createFrom err
        json.getString('error.code') == 'unknown'
        json.getString('error.message.lang') == 'en'
        json.getString('error.message.value') == 'unexpected error occurred'
    }

    HttpServletRequest httpRequest(String accept) {
        Stub(HttpServletRequest) {
            getHeader('Accept') >> accept
        }
    }

    HttpServletResponse httpResponse(int httpStatus) {
        Stub(HttpServletResponse) {
            getStatus() >> httpStatus
        }
    }
}
