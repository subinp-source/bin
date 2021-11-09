/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.XmlObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Rule
import org.junit.Test

import javax.ws.rs.core.Response

import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
class GetMetadataIntegrationTest extends ServicelayerSpockSpecification {

    private static final String USER = 'tester'
    private static final String PASSWORD = 'secret'
    private static final String IO = 'GetMetadataIntegrationTestIO'

    @Rule
    InboundMonitoringRule monitoring = InboundMonitoringRule.disabled()

    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $IO                ; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $IO:Catalog                                                        ; id                          ; Catalog:id"
        )
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationviewgroup")
        inboundChannelConfigurationBuilder()
                .withCode(IO)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == IO }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == IO }
        IntegrationTestUtil.removeSafely EmployeeModel, { it.uid == USER }
    }

    @Test
    def 'retrieves EDMX for /$metadata'() {
        when:
        def response = basicAuthRequest()
                .path('$metadata')
                .build()
                .get()

        then:
        response.status == 200
        def xml = extractBody response
        xml.exists '//Schema/EntityType/Key'
        xml.exists '//Schema/EntityType/Property'
        xml.exists '//EntityContainer/EntitySet'
    }

    @Test
    def 'retrieves EDMX for /$metadata?Catalog='() {
        when:
        def response = basicAuthRequest()
                .path('$metadata')
                .queryParam('Catalog', '')
                .build()
                .get()

        then:
        response.status == 200
        def xml = extractBody response
        xml.exists '//Schema/EntityType/Key'
        xml.exists '//Schema/EntityType/Property'
        xml.exists '//EntityContainer/EntitySet'
    }

    BasicAuthRequestBuilder basicAuthRequest() {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .credentials(USER, PASSWORD)
                .accept('application/xml')
                .path(IO)
    }

    XmlObject extractBody(Response response) {
        XmlObject.createFrom response.getEntity() as InputStream
    }
}
