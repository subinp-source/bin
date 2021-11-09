/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import org.springframework.http.MediaType
import spock.lang.Issue
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.PASSWORD
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.SERVICE
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class HttpSecurityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String CATALOG_ID = 'testNonRestrictedCatalog'
    private static final String USER = 'tester'

    def setupSpec() {
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        UserAccessTestUtils.givenCatalogIOExists()
        inboundChannelConfigurationBuilder()
                .withCode(SERVICE)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == SERVICE }
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def setup() {
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
    }

    def cleanup() {
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == USER }).ifPresent { IntegrationTestUtil.remove(it) }
        IntegrationTestUtil.findAny(CatalogModel, { it.id == CATALOG_ID }).ifPresent { IntegrationTestUtil.remove it }
    }

    @Test
    @Unroll
    def "User must be authenticated in order to GET #path"() {
        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path(path)
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        path << ['$metadata', "Catalogs('${CATALOG_ID}')"]
    }

    @Test
    @Unroll
    def "User must be authenticated in order to POST to #path"() {
        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path(path)
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        path << ['Catalogs', '$batch']
    }

    @Test
    def "User must be authenticated in order to DELETE"() {
        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path('Catalogs')
                .build()
                .delete()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    def "User must be authenticated in order to PATCH"() {
        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path("Catalogs('${CATALOG_ID}')")
                .build()
                .patch Entity.json('{}')

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    def "Wrong credentials for GET"() {
        given: "User exist"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationviewgroup")

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .credentials(USER, "wrong-$PASSWORD")
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    @Unroll
    def "Wrong credentials for POST to #path"() {
        given: "User exist"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationcreategroup")

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path(path)
                .credentials(USER, "wrong-${PASSWORD}")
                .build()
                .post Entity.json(json().withField("id", "testNewCatalog").build())

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        path << ['Catalogs', '$batch']
    }

    @Test
    def "Wrong credentials for DELETE"() {
        given: "User exist"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationdeletegroup")

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path("Catalogs('$CATALOG_ID')")
                .credentials(USER, '123')
                .build()
                .delete()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    def "Wrong credentials for PATCH"() {
        given: "User exist"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationcreategroup")

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path("Catalogs('$CATALOG_ID')")
                .credentials(USER, '123')
                .build()
                .patch Entity.json('{}')

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    @Unroll
    def "User in group '#group' gets #status for GET #path"() {
        given: "User exist with #group"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, group)

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path(path)
                .credentials(USER, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        group                        | status                    | path
        ''                           | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        'integrationusergroup'       | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        'integrationadmingroup'      | HttpStatusCodes.OK        | 'Catalogs'
        'integrationcreategroup'     | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        'integrationviewgroup'       | HttpStatusCodes.OK        | 'Catalogs'
        'integrationdeletegroup'     | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        'integrationservicegroup'    | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        'integrationmonitoringgroup' | HttpStatusCodes.FORBIDDEN | 'Catalogs'
        ''                           | HttpStatusCodes.FORBIDDEN | '$metadata'
        'integrationusergroup'       | HttpStatusCodes.FORBIDDEN | '$metadata'
        'integrationadmingroup'      | HttpStatusCodes.OK        | '$metadata'
        'integrationcreategroup'     | HttpStatusCodes.OK        | '$metadata'
        'integrationviewgroup'       | HttpStatusCodes.OK        | '$metadata'
        'integrationdeletegroup'     | HttpStatusCodes.FORBIDDEN | '$metadata'
        'integrationservicegroup'    | HttpStatusCodes.FORBIDDEN | '$metadata'
        'integrationmonitoringgroup' | HttpStatusCodes.FORBIDDEN | '$metadata'
    }

    @Test
    @Unroll
    def "User in group '#group' gets #status for POST"() {
        given: "User exist with #group"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, group)

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path('Catalogs')
                .credentials(USER, PASSWORD)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post Entity.json(json().withField("id", "testNewCatalog").build())

        then:
        response.status == status.statusCode

        where:
        group                        | status
        ''                           | HttpStatusCodes.FORBIDDEN
        'integrationusergroup'       | HttpStatusCodes.FORBIDDEN
        'integrationadmingroup'      | HttpStatusCodes.CREATED
        'integrationcreategroup'     | HttpStatusCodes.CREATED
        'integrationviewgroup'       | HttpStatusCodes.FORBIDDEN
        'integrationdeletegroup'     | HttpStatusCodes.FORBIDDEN
        'integrationservicegroup'    | HttpStatusCodes.FORBIDDEN
        'integrationmonitoringgroup' | HttpStatusCodes.FORBIDDEN
    }

    @Test
    @Unroll
    def "User in group '#group' gets #status for DELETE"() {
        given: "User exist with #group"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, group)

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path("Catalogs('$CATALOG_ID')")
                .credentials(USER, PASSWORD)
                .build()
                .delete()

        then:
        response.status == status.statusCode

        where:
        group                        | status
        ''                           | HttpStatusCodes.FORBIDDEN
        'integrationusergroup'       | HttpStatusCodes.FORBIDDEN
        'integrationadmingroup'      | HttpStatusCodes.OK
        'integrationcreategroup'     | HttpStatusCodes.FORBIDDEN
        'integrationviewgroup'       | HttpStatusCodes.FORBIDDEN
        'integrationdeletegroup'     | HttpStatusCodes.OK
        'integrationservicegroup'    | HttpStatusCodes.FORBIDDEN
        'integrationmonitoringgroup' | HttpStatusCodes.FORBIDDEN
    }

    @Issue('https://jira.hybris.com/browse/STOUT-1608')
    @Test
    @Unroll
    def "User in group '#group' gets #status for PATCH"() {
        given: "User exist with #group"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, group)

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path("Catalogs('$CATALOG_ID')")
                .credentials(USER, PASSWORD)
                .build()
                .patch Entity.json(json().withField("name", "testNewName").build())

        then:
        response.status == status.statusCode

        where:
        group                        | status
        ''                           | HttpStatusCodes.FORBIDDEN
        'integrationusergroup'       | HttpStatusCodes.FORBIDDEN
        'integrationadmingroup'      | HttpStatusCodes.OK
        'integrationcreategroup'     | HttpStatusCodes.OK
        'integrationviewgroup'       | HttpStatusCodes.FORBIDDEN
        'integrationdeletegroup'     | HttpStatusCodes.FORBIDDEN
        'integrationservicegroup'    | HttpStatusCodes.FORBIDDEN
        'integrationmonitoringgroup' | HttpStatusCodes.FORBIDDEN
    }

    @Test
    @Unroll
    def "User in group '#group' gets Forbidden when requesting with unsupported HTTP verb"() {
        given: "User exist with #group"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, group)

        when:
        def response = UserAccessTestUtils.basicAuthRequest(SERVICE)
                .path('Catalogs')
                .credentials(USER, PASSWORD)
                .build()
                .method 'PURGE'

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        group << ['integrationusergroup', 'integrationadmingroup', 'integrationcreategroup', 'integrationviewgroup', 'integrationdeletegroup', 'integrationservicegroup', 'integrationmonitoringgroup']
    }
}