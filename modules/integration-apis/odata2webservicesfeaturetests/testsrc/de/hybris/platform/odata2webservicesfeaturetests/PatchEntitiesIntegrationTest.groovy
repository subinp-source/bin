/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.AddressModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-1608')
class PatchEntitiesIntegrationTest extends ServicelayerSpockSpecification {

    private static final String USER = 'tester'
    private static final String PASSWORD = 'secret'
    private static final String IO_CODE = 'Test'

    @Rule
    InboundMonitoringRule monitoring = InboundMonitoringRule.disabled()

    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8") // For the integrationadmingroup
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "; $IO_CODE; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root',
                "                                   ; $IO_CODE                              ; Customer           ; Customer  ; true",
                "                                   ; $IO_CODE                              ; Address	         ; Address   ; false",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor       ; $attributeType   ; unique[default = false]',
                "                                            ; $IO_CODE:Customer   ; uid                         ; User:uid          ;",
                "                                            ; $IO_CODE:Customer   ; disabled                    ; User:loginDisabled;",
                "                                            ; $IO_CODE:Customer   ; addresses                   ; User:addresses    ; $IO_CODE:Address",
                "                                            ; $IO_CODE:Address    ; owner                       ; Address:owner     ; $IO_CODE:Customer",
                "                                            ; $IO_CODE:Address    ; cellphone                   ; Address:cellphone ;                  ; true",
                "                                            ; $IO_CODE:Address    ; company                     ; Address:company ")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique=true]; loginDisabled; &refId',
                '                      ; john.doe        ; false        ; jd',
                'INSERT_UPDATE Address; cellphone[unique = true]; company; owner(&refId)',
                '                     ; 1-927-398-3909          ; Sun    ; jd',
                '                     ; 1-927-847-2490          ; Sun    ; jd')
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationcreategroup")
        inboundChannelConfigurationBuilder()
                .withCode(IO_CODE)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == IO_CODE }
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll AddressModel
        IntegrationTestUtil.removeSafely CustomerModel, { it.uid == 'john.doe' }
        IntegrationTestUtil.removeSafely EmployeeModel, { it.uid == USER }
    }

    @Test
    @Unroll
    def "PATCH responds with 200 OK when an #desc item is successfully patched"() {
        when:
        def response = basicAuthRequest(IO_CODE)
                .path(entity)
                .build()
                .patch Entity.json(json)

        then:
        response.status == 200

        where:
        desc    | entity                        | json
        'owner' | "Customers('john.doe')"       | json().withField("disabled", true).build()
        'owned' | "Addresses('1-927-847-2490')" | json().withField("company", "SAP").build()
    }

    @Test
    @Unroll
    def "PATCH responds with 405 Not Implemented when the URI points to all #desc items"() {
        when:
        def response = basicAuthRequest(IO_CODE)
                .path(entitySet)
                .build()
                .patch Entity.json()

        then:
        response.status == 405

        where:
        desc    | entitySet
        'owner' | 'Customers'
        'owned' | 'Addresses'
    }

    @Test
    @Unroll
    def "PATCH responds with 405 Not Implemented when the URI contains a filter condition for #desc items"() {
        when:
        def response = basicAuthRequest(IO_CODE)
                .path(entitySet)
                .queryParam('$filter', filter)
                .build()
                .patch Entity.json()

        then:
        response.status == 405

        where:
        desc    | entitySet   | filter
        'owner' | 'Customers' | "disabled%20eq%20true"
        'owned' | 'Addresses' | "company%20eq%20%27Oracle%27"
    }

    @Test
    def "PATCH responds with 501 Not Implemented when the URI contains a navigation property"() {
        when:
        def response = basicAuthRequest(IO_CODE)
                .path("Customers('john.doe')")
                .path('addresses')
                .build()
                .patch Entity.json()

        then:
        response.status == 501
    }

    @Test
    def "PATCH responds with 404 Not Found when the URI references a non-existent integration object"() {
        when:
        def response = basicAuthRequest('NonExistentIO')
                .path("/Customers('john.doe')")
                .build()
                .patch Entity.json()

        then:
        response.status == 404
    }

    @Test
    @Unroll
    def "PATCH responds with 404 Not Found when an #desc item does not exist"() {
        when:
        def response = basicAuthRequest(IO_CODE)
                .path(entity)
                .build()
                .patch Entity.json(json)

        then:
        response.status == 404

        where:
        desc    | entity                        | json
        'owner' | "Customers('scott.tiger')"    | json().withField("disabled", true).build()
        'owned' | "Addresses('1-404-404-4004')" | json().withField("company", "SAP").build()
    }

    BasicAuthRequestBuilder basicAuthRequest(final String path) {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .credentials(USER, PASSWORD) // created in setup()
                .path(path)
                .accept("application/json")
    }
}
