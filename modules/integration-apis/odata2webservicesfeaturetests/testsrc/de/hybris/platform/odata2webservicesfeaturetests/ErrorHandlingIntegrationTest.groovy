/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.integrationservices.util.XmlObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.http.HttpStatus
import org.junit.Test
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class ErrorHandlingIntegrationTest extends ServicelayerSpockSpecification {
    private static final String USER = 'tester'
    private static final String PASSWORD = 'password'
    private static final String INTEGRATION_SERVICE = "IntegrationService"
    private static final String INTEGRATION_INBOUND_MONITORING = "InboundIntegrationMonitoring"
    private static final String INTEGRATION_OUTBOUND_MONITORING = "OutboundIntegrationMonitoring"
    private static final String INBOUND_PRODUCT = "InboundProduct"

    private static final def IOs = [INTEGRATION_SERVICE, INTEGRATION_INBOUND_MONITORING, INTEGRATION_OUTBOUND_MONITORING, INBOUND_PRODUCT]

    def setupSpec() {
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundservices.impex', 'UTF-8'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                                      ; $INBOUND_PRODUCT")
        IOs.each { io ->
            inboundChannelConfigurationBuilder()
                    .withCode(io)
                    .withAuthType(AuthenticationType.BASIC)
                    .build()
        }
    }

    def cleanupSpec() {
        IOs.each { io -> IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == io } }
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def cleanup() {
        IntegrationTestUtil.removeSafely EmployeeModel, { it.uid == USER }
    }

    @Test
    def 'handles 401 Unauthorized in application/json content'() {
        when:
        def response = basicAuthRequest()
                .accept('application/json')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_UNAUTHORIZED
        response.getHeaderString('Content-Type') == 'application/json;charset=UTF-8'
        def json = JsonObject.createFrom response.readEntity(String)
        json.getString('error.code') == 'unauthorized'
        json.getString('error.message.lang') == 'en'
    }

    @Test
    def 'handles 401 Unauthorized in application/xml content'() {
        when:
        def response = basicAuthRequest()
                .accept('application/xml')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_UNAUTHORIZED
        response.getHeaderString('Content-Type') == 'application/xml;charset=UTF-8'
        def xml = XmlObject.createFrom response.readEntity(String)
        xml.get('/error/code') == 'unauthorized'
        xml.get('/error/message/@lang') == 'en'
    }

    @Test
    @Unroll
    def "GET /#io: 404 Not Found in application/json content"() {
        given: "A User with integrationadmingroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/json')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_NOT_FOUND
        response.getHeaderString('Content-Type') == 'application/json;charset=UTF-8'
        def json = JsonObject.createFrom response.readEntity(String)
        json.getString('error.code') == 'not_found'
        json.getString('error.message.lang') == 'en'

        where:
        io << [INTEGRATION_SERVICE, "NonExistentIO"]
    }

    @Test
    @Unroll
    def "GET /#io: 404 Not Found in application/xml content"() {
        given: "A User with integrationadmingroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/xml')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_NOT_FOUND
        response.getHeaderString('Content-Type') == 'application/xml;charset=UTF-8'
        def xml = XmlObject.createFrom response.readEntity(String)
        xml.get('/error/code') == 'not_found'
        xml.get('/error/message/@lang') == 'en'

        where:
        io << [INTEGRATION_SERVICE, "NonExistentIO"]
    }

    @Test
    @Unroll
    def "GET /#io: 403 Forbidden in application/json content for integrationadmingroup"() {
        given: "User is in integrationadmingroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/json')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/json;charset=UTF-8'
        def json = JsonObject.createFrom response.readEntity(String)
        json.getString('error.code') == 'forbidden'
        json.getString('error.message.lang') == 'en'

        where:
        io << [INTEGRATION_INBOUND_MONITORING, INTEGRATION_OUTBOUND_MONITORING]
    }

    @Test
    @Unroll
    def "GET /#io: 403 Forbidden in application/xml content for integrationadmingroup"() {
        given: "User is in integrationadmingroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/xml')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/xml;charset=UTF-8'
        def xml = XmlObject.createFrom response.readEntity(String)
        xml.get('/error/code') == 'forbidden'
        xml.get('/error/message/@lang') == 'en'

        where:
        io << [INTEGRATION_INBOUND_MONITORING, INTEGRATION_OUTBOUND_MONITORING]
    }

    @Test
    @Unroll
    def "GET /#io: 403 Forbidden in application/json content for integrationcreategroup"() {
        given: "User is in integrationcreategroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationcreategroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/json')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/json;charset=UTF-8'
        def json = JsonObject.createFrom response.readEntity(String)
        json.getString('error.code') == 'forbidden'
        json.getString('error.message.lang') == 'en'

        where:
        io << [INTEGRATION_SERVICE, INTEGRATION_INBOUND_MONITORING, INTEGRATION_OUTBOUND_MONITORING, INBOUND_PRODUCT]
    }

    @Test
    @Unroll
    def "GET /#io: 403 Forbidden in application/xml content"() {
        given: "User is in integrationcreategroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationcreategroup")

        when:
        def response = basicAuthRequest(io)
                .credentials(USER, PASSWORD)
                .accept('application/xml')
                .build()
                .get()

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/xml;charset=UTF-8'
        def xml = XmlObject.createFrom response.readEntity(String)
        xml.get('/error/code') == 'forbidden'
        xml.get('/error/message/@lang') == 'en'

        where:
        io << [INTEGRATION_SERVICE, INTEGRATION_INBOUND_MONITORING, INTEGRATION_OUTBOUND_MONITORING, INBOUND_PRODUCT]
    }

    @Test
    def 'POST /InboundProduct 403 Forbidden with application/json content'() {
        given: "User is in integrationviewgroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationviewgroup")

        when:
        def response = basicAuthRequest(INBOUND_PRODUCT)
                .credentials(USER, PASSWORD)
                .accept('application/json')
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/json;charset=UTF-8'
        def json = JsonObject.createFrom response.readEntity(String)
        json.getString('error.code') == 'forbidden'
        json.getString('error.message.lang') == 'en'
    }

    @Test
    def 'POST /InboundProduct 403 Forbidden with application/xml content'() {
        given: "User is in integrationviewgroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationviewgroup")

        when:
        def response = basicAuthRequest(INBOUND_PRODUCT)
                .credentials(USER, PASSWORD)
                .accept('application/xml')
                .build()
                .post(Entity.xml('<product />'))

        then:
        response.status == HttpStatus.SC_FORBIDDEN
        response.getHeaderString('Content-Type') == 'application/xml;charset=UTF-8'
        def xml = XmlObject.createFrom response.readEntity(String)
        xml.get('/error/code') == 'forbidden'
        xml.get('/error/message/@lang') == 'en'
    }

    def basicAuthRequest(String path = '') {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .path(path)
    }
}
