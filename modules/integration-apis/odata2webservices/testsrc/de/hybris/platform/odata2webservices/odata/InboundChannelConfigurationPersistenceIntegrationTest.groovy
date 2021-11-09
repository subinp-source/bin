/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class InboundChannelConfigurationPersistenceIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = "IntegrationService"
    private static final String EXISTING_IO = "InboundChannelConfigurationPersistenceIntegrationTest"
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        importCsv("/impex/essentialdata-integrationservices.impex", "UTF-8")
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $EXISTING_IO"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    @Unroll
    def "can create InboundChannelConfiguration when providing authenticationType #providedAuthType and integrationObject referencing an existing IntegrationObject"() {
        given: "the payload references an existing integrationObject"
        def request = postRequest().withBody(
                json()
                        .withField('integrationObject', json()
                                .withCode(EXISTING_IO))
                        .withField('authenticationType', json()
                                .withCode(providedAuthType)))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        def inboundChannelConfig = findExistingChannelConfigWithIO(EXISTING_IO)
        inboundChannelConfig.authenticationType == expectedAuthType

        where:
        providedAuthType | expectedAuthType
        "OAUTH"          | AuthenticationType.OAUTH
        "BASIC"          | AuthenticationType.BASIC
    }

    @Test
    def "can create InboundChannelConfiguration with the default authenticationType when existing integration object is provided"() {
        given:
        def request = postRequest().withBody(
                json()
                        .withField('integrationObject', json()
                                .withCode(EXISTING_IO)))
        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        def inboundChannelConfig = findExistingChannelConfigWithIO(EXISTING_IO)
        and: "authenticationType is set to the expected default value"
        inboundChannelConfig.authenticationType == AuthenticationType.BASIC
    }

    @Test
    def "can autocreate a integrationObject when creating a new InboundChannelConfiguration"() {
        given: "the payload references a non-existing integrationObject"
        def integrationObjectCode = 'newIOCode'
        def request = postRequest().withBody(
                json()
                        .withField('integrationObject', json()
                                .withCode(integrationObjectCode))
                        .withField('authenticationType', json()
                                .withCode('BASIC')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        def inboundChannelConfig = findExistingChannelConfigWithIO(integrationObjectCode)
        inboundChannelConfig.authenticationType == AuthenticationType.BASIC
    }

    @Test
    def "cannot create an InboundChannelConfiguration without providing a integration object"() {
        when:
        given: "the payload references an existing integrationObject"
        def request = postRequest().withBody(
                json()
                        .withField('authenticationType', json()
                                .withCode('BASIC')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_key'
        json.getString('error.message.value') == "Key NavigationProperty [integrationObject] is required for EntityType [InboundChannelConfiguration]."
    }

    @Test
    def "can update InboundChannelConfiguration authenticationType"() {
        given: "an InboundChannelConfiguration exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)',
                "                                         ; $EXISTING_IO                          ; OAUTH"
        )

        when: "an attempt is made to change the InboundChannelConfiguration authenticationType to null"
        def request = patchRequest(EXISTING_IO).withBody(
                json()
                        .withField('authenticationType', json()
                                .withCode('BASIC')))
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.OK
        def inboundChannelConfig = findExistingChannelConfigWithIO(EXISTING_IO)
        inboundChannelConfig.authenticationType == AuthenticationType.BASIC
    }

    @Test
    def "cannot update InboundChannelConfiguration authentication to null"() {
        given: "an InboundChannelConfiguration exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)',
                "                                         ; $EXISTING_IO                          ; OAUTH"
        )

        when: "an attempt is made to change the InboundChannelConfiguration authenticationType to null"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType',
                "                                         ; $EXISTING_IO                          ;"
        )

        then:
        thrown(AssertionError)
        def inboundChannelConfig = findExistingChannelConfigWithIO(EXISTING_IO)
        inboundChannelConfig.authenticationType == AuthenticationType.OAUTH
    }

    private static InboundChannelConfigurationModel findExistingChannelConfigWithIO(final String objectCode) {
        IntegrationTestUtil.findAny(InboundChannelConfigurationModel, {
            it?.integrationObject?.code == objectCode
        }).orElse(null)
    }

    ODataRequestBuilder postRequest() {
        ODataRequestBuilder.oDataPostRequest()
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet('InboundChannelConfigurations'))
    }

    private static ODataRequestBuilder patchRequest(final String... keys) {
        ODataRequestBuilder.oDataPatchRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet('InboundChannelConfigurations')
                        .withEntityKeys(keys))
    }
}