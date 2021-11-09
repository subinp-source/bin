/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.type.SearchRestrictionModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.core.model.user.UserGroupModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.RuntimeAttributeDescriptorBuilder
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.AutoCleanup
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class RuntimeAttributeSecurityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String TEST_NAME = "RuntimeAttributeSecurity"
    private static final String RUNTIME_ATTRIBUTE_IO = "RuntimeAttributeService"
    private static final String PASSWORD = 'password'
    private static final String GROUP_ADMIN = 'integrationadmingroup'
    private static final String GROUP_SERVICE = 'integrationservicegroup'
    private static final String GROUP_MONITOR = 'integrationmonitoringgroup'
    private static final String GROUP_OUTBOUNDSYNC = 'outboundsyncgroup'
    private static final String GROUP_CREATE = 'integrationcreategroup'
    private static final String GROUP_VIEW = 'integrationviewgroup'
    private static final String GROUP_DELETE = 'integrationdeletegroup'
    private static final String ADMIN_USER = "$TEST_NAME-integrationadmingroup-user"
    private static final String MONITOR_USER = "$TEST_NAME-integrationmonitoringgroup-user"
    private static final String SERVICE_USER = "$TEST_NAME-integrationservicegroup-user"
    private static final String CREATE_USER = "$TEST_NAME-integrationcreategroup-user"
    private static final String VIEW_USER = "$TEST_NAME-integrationviewgroup-user"
    private static final String DELETE_USER = "$TEST_NAME-integrationdeletegroup-user"
    private static final String GROUP_SCRIPT_SERVICE = 'scriptservicegroup'
    private static final String ADMIN_MONITOR_USER = "$TEST_NAME-integrationmonitoringgroup-and-integrationadmingroup-user"
    private static final String ADMIN_SERVICE_USER = "$TEST_NAME-integrationservicegroup-and-integrationadmingroup-user"
    private static final String ADMIN_OUTBOUNDSYNC_USER = "$TEST_NAME-outboundsyncgroup-and-integrationadmingroup-user"
    private static final String ADMIN_SCRIPT_USER = "$TEST_NAME-scriptservicegroup-and-integrationadmingroup-user"
    private static final String ADMIN_OUTBOUNDSYNC_MONITOR_USER = "$TEST_NAME-outboundsyncgroup-and-integrationmonitoringgroup-user"
    private static final String ADMIN_SERVICE_OUTBOUNDSYNC_MONITOR_USER = "$TEST_NAME-outboundsyncgroup-integrationmonitoringgroup-integrationservicegroup-integrationadmingroup-user"
    private static final String ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER = 'outboundsyncgroup-integrationmonitoringgroup-integrationservicegroup-integrationadmingroup-scriptservicegroup-user'
    private static final Collection users = [ADMIN_USER, MONITOR_USER, SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER, ADMIN_MONITOR_USER, ADMIN_SERVICE_USER,
                                             ADMIN_OUTBOUNDSYNC_USER, ADMIN_OUTBOUNDSYNC_MONITOR_USER, ADMIN_SERVICE_OUTBOUNDSYNC_MONITOR_USER, ADMIN_SCRIPT_USER]
    private static final Collection userGroups = ["integrationdeletegroup",
                                                  "integrationviewgroup",
                                                  "integrationcreategroup",
                                                  "integrationadmingroup",
                                                  "integrationusergroup"]

    private static final String QUALIFIER = "$TEST_NAME-RuntimeAttributeService"
    private static final String ENCLOSING_TYPE = "Catalog"
    private static final String INTEGRATION_KEY = "$QUALIFIER|$ENCLOSING_TYPE"
    private static final String ATTRIBUTE_DESCRIPTOR_PAYLOAD = json().withField("qualifier", "$QUALIFIER")
            .withField("enclosingType", json().withCode("$ENCLOSING_TYPE"))
            .withField("attributeType", json().withCode("java.lang.String"))
            .withField("optional", true)
            .withField("localized", false)
            .withField("partOf", false)
            .withField("generate", false)
            .build()

    private static final Collection searchRestrictions = ['inboundMonitoringIntegrationVisibility',
                                                          'outboundMonitoringIntegrationVisibility',
                                                          'integrationServiceVisibility',
                                                          'outboundChannelConfigVisibility',
                                                          'scriptServiceVisibility',
                                                          'webhookServiceVisibility',
                                                          'runtimeAttributeVisibility']


    @AutoCleanup("cleanup")
    RuntimeAttributeDescriptorBuilder runtimeAttributeDescriptorBuilder = RuntimeAttributeDescriptorBuilder
            .attributeDescriptor()
            .withQualifier(QUALIFIER)
            .withEnclosingType(ENCLOSING_TYPE)

    def setupSpec() {
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-runtimeattributes.impex', 'UTF-8'

        userInGroups(ADMIN_USER, GROUP_ADMIN)
        userInGroups(MONITOR_USER, GROUP_MONITOR)
        userInGroups(SERVICE_USER, GROUP_SERVICE)
        userInGroups(CREATE_USER, GROUP_CREATE)
        userInGroups(VIEW_USER, GROUP_VIEW)
        userInGroups(DELETE_USER, GROUP_DELETE)
        userInGroups(ADMIN_MONITOR_USER, "$GROUP_ADMIN,$GROUP_MONITOR")
        userInGroups(ADMIN_SCRIPT_USER, "$GROUP_ADMIN,$GROUP_SCRIPT_SERVICE")
        userInGroups(ADMIN_SERVICE_USER, "$GROUP_ADMIN,$GROUP_SERVICE")
        userInGroups(ADMIN_OUTBOUNDSYNC_USER, "$GROUP_ADMIN,$GROUP_OUTBOUNDSYNC")
        userInGroups(ADMIN_OUTBOUNDSYNC_MONITOR_USER, "$GROUP_ADMIN,$GROUP_OUTBOUNDSYNC,$GROUP_MONITOR")
        userInGroups(ADMIN_SERVICE_OUTBOUNDSYNC_MONITOR_USER, "$GROUP_ADMIN,$GROUP_SERVICE,$GROUP_OUTBOUNDSYNC,$GROUP_MONITOR")
        userInGroups(ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER, "$GROUP_ADMIN,$GROUP_SERVICE,$GROUP_OUTBOUNDSYNC,$GROUP_MONITOR,$GROUP_SCRIPT_SERVICE")
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { icc -> icc?.integrationObject?.code == RUNTIME_ATTRIBUTE_IO}
        IntegrationTestUtil.remove IntegrationObjectModel, { io -> io.code == RUNTIME_ATTRIBUTE_IO}
        userGroups.each { user -> IntegrationTestUtil.remove UserGroupModel, { userGroups.contains it.uid } }
        users.each { user -> IntegrationTestUtil.remove EmployeeModel, { users.contains it.uid } }
        searchRestrictions.each { searchRestriction -> IntegrationTestUtil.remove SearchRestrictionModel, { searchRestrictions.contains it.code } }
    }

    @Test
    @Unroll
    def "User must be authenticated in order to #method /RuntimeAttributeService"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .build()
                .method method

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        method << ['GET', 'POST', 'DELETE', 'PATCH']
    }

    @Test
    def "User must be authenticated in order to GET /RuntimeAttributeService/someFeedNameThatDoesNotMatter"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path('someFeedNameThatDoesNotMatter')
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }

    @Test
    def "User in groups integrationadmingroup AND integrationservicegroup gets Forbidden when requesting with unsupported HTTP verb at /RuntimeAttributeService/someFeedNameThatDoesNotMatter"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path('someFeedNameThatDoesNotMatter')
                .credentials(ADMIN_SERVICE_USER, PASSWORD)
                .build()
                .method 'COPY'

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode
    }

    @Test
    @Unroll
    def "#user gets #status for GET /RuntimeAttributeService"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        user                    | status
        ADMIN_SERVICE_USER      | HttpStatusCodes.OK
        ADMIN_USER              | HttpStatusCodes.NOT_FOUND
        SERVICE_USER            | HttpStatusCodes.FORBIDDEN
        ADMIN_MONITOR_USER      | HttpStatusCodes.NOT_FOUND
        ADMIN_OUTBOUNDSYNC_USER | HttpStatusCodes.NOT_FOUND
        CREATE_USER             | HttpStatusCodes.FORBIDDEN
        VIEW_USER               | HttpStatusCodes.FORBIDDEN
        DELETE_USER             | HttpStatusCodes.FORBIDDEN
        MONITOR_USER            | HttpStatusCodes.FORBIDDEN
    }

    @Test
    @Unroll
    def "#user gets Forbidden for GET /RuntimeAttributeService/doesNotMatter"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path('doesNotMatter')
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user << [SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER]
    }

    @Test
    @Unroll
    def "#user gets #status for GET /RuntimeAttributeService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        feed                   | user                    | status
        'AttributeDescriptors' | ADMIN_SERVICE_USER      | HttpStatusCodes.OK
        'AttributeDescriptors' | MONITOR_USER            | HttpStatusCodes.FORBIDDEN
        'AttributeDescriptors' | SERVICE_USER            | HttpStatusCodes.FORBIDDEN
        'AttributeDescriptors' | ADMIN_MONITOR_USER      | HttpStatusCodes.NOT_FOUND
        'AttributeDescriptors' | ADMIN_OUTBOUNDSYNC_USER | HttpStatusCodes.NOT_FOUND
        'AttributeDescriptors' | SERVICE_USER            | HttpStatusCodes.FORBIDDEN
        'ComposedTypes'        | ADMIN_SERVICE_USER      | HttpStatusCodes.OK
        'ComposedTypes'        | MONITOR_USER            | HttpStatusCodes.FORBIDDEN
        'ComposedTypes'        | SERVICE_USER            | HttpStatusCodes.FORBIDDEN
        'ComposedTypes'        | ADMIN_MONITOR_USER      | HttpStatusCodes.NOT_FOUND
        'ComposedTypes'        | ADMIN_OUTBOUNDSYNC_USER | HttpStatusCodes.NOT_FOUND
        'Types'                | ADMIN_SERVICE_USER      | HttpStatusCodes.OK
        'Types'                | MONITOR_USER            | HttpStatusCodes.FORBIDDEN
        'Types'                | SERVICE_USER            | HttpStatusCodes.FORBIDDEN
        'Types'                | ADMIN_MONITOR_USER      | HttpStatusCodes.NOT_FOUND
        'Types'                | ADMIN_OUTBOUNDSYNC_USER | HttpStatusCodes.NOT_FOUND
    }

    @Test
    @Unroll
    def "#user is Forbidden to #httpMethod to /RuntimeAttributeService"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .credentials(user, PASSWORD)
                .build()
                .method(httpMethod, Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user         | httpMethod
        SERVICE_USER | 'POST'
        CREATE_USER  | 'POST'
        VIEW_USER    | 'POST'
        DELETE_USER  | 'POST'
        MONITOR_USER | 'POST'

        SERVICE_USER | 'PATCH'
        CREATE_USER  | 'PATCH'
        VIEW_USER    | 'PATCH'
        DELETE_USER  | 'PATCH'
        MONITOR_USER | 'PATCH'

        SERVICE_USER | 'DELETE'
        CREATE_USER  | 'DELETE'
        VIEW_USER    | 'DELETE'
        DELETE_USER  | 'DELETE'
        MONITOR_USER | 'DELETE'
    }

    @Test
    @Unroll
    def "#user gets Not Found when sending a POST to /RuntimeAttributeService/AttributeDescriptors"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path('AttributeDescriptors')
                .credentials(user, PASSWORD)
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode

        where:
        user << [ADMIN_MONITOR_USER, ADMIN_OUTBOUNDSYNC_USER, ADMIN_SCRIPT_USER]
    }

    @Test
    def "User in groups integrationadmingroup and integrationservicegroup is authorized to POST to /RuntimeAttributeService/AttributeDescriptors"() {
        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path('AttributeDescriptors')
                .credentials(ADMIN_SERVICE_USER, PASSWORD)
                .build()
                .post Entity.json(ATTRIBUTE_DESCRIPTOR_PAYLOAD)

        then:
        response.status == HttpStatusCodes.CREATED.statusCode
    }

    @Test
    def "User in groups integrationadmingroup and integrationservicegroup is authorized to PATCH to /RuntimeAttributeService/AttributeDescriptors('existingIntegrationKey')"() {
        given:
        runtimeAttributeExists()

        and:
        def body = json().withField("integrationKey", "$INTEGRATION_KEY")
                .withField("optional", false).build()

        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path("AttributeDescriptors('$INTEGRATION_KEY')")
                .credentials(ADMIN_SERVICE_USER, PASSWORD)
                .build()
                .patch Entity.json(body)

        then:
        response.status == HttpStatusCodes.OK.statusCode
    }

    @Test
    @Unroll
    def "#user gets Not Found when sending a PATCH to /RuntimeAttributeService/#feed"() {
        given:
        runtimeAttributeExists()

        and:
        def body = json().withField("integrationKey", "$INTEGRATION_KEY")
                .withField("optional", false).build()

        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .patch(Entity.json(body))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode

        where:
        feed                                       | user
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_OUTBOUNDSYNC_USER
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_MONITOR_USER
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_SCRIPT_USER
    }

    @Test
    @Unroll
    def "#user gets #responseStatus when DELETE /RuntimeAttributeService/#feed"() {
        given:
        runtimeAttributeExists()

        when:
        def response = basicAuthRequest()
                .path(RUNTIME_ATTRIBUTE_IO)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .delete()

        then:
        response.status == responseStatus.statusCode

        where:
        feed                                       | user                    | responseStatus
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_SERVICE_USER      | HttpStatusCodes.OK
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_MONITOR_USER      | HttpStatusCodes.NOT_FOUND
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_SCRIPT_USER       | HttpStatusCodes.NOT_FOUND
        "AttributeDescriptors('$INTEGRATION_KEY')" | ADMIN_OUTBOUNDSYNC_USER | HttpStatusCodes.NOT_FOUND
    }

    @Test
    @Unroll
    def "User in groups scriptservicegroup, integrationadmingroup,integrationservicegroup,outboundsyncgroup & integrationmonitoringgroup gets Forbidden for DELETE /IntegrationService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path("IntegrationService")
                .path(feed)
                .credentials(ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER, PASSWORD)
                .build()
                .delete()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        feed << ["IntegrationObjects('RuntimeAttributeService')",
                 "IntegrationObjectItems('doesNotMatter|RuntimeAttributeService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|RuntimeAttributeService')"
        ]
    }

    @Test
    @Unroll
    def "User in groups scriptservicegroup, integrationadmingroup,integrationservicegroup,outboundsyncgroup & integrationmonitoringgroup gets Forbidden for PATCH /IntegrationService/#feed"() {
        given:
        def body = json().withField("integrationKey", "$INTEGRATION_KEY")
                .withField("optional", false).build()

        when:
        def response = basicAuthRequest()
                .path("IntegrationService")
                .path(feed)
                .credentials(ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER, PASSWORD)
                .build()
                .patch Entity.json(body)

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        feed << ["IntegrationObjects('RuntimeAttributeService')",
                 "IntegrationObjectItems('doesNotMatter|RuntimeAttributeService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|RuntimeAttributeService')"
        ]
    }

    def userInGroups(final String user, final String groups) {
        UserAccessTestUtils.createUser(user, PASSWORD, groups)
    }

    def basicAuthRequest() {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .accept('application/json')
    }

    def runtimeAttributeExists() {
        runtimeAttributeDescriptorBuilder
                .attributeDescriptor()
                .withQualifier("$QUALIFIER")
                .withEnclosingType("$ENCLOSING_TYPE")
                .withAttributeType("java.lang.String")
                .withOptional(true)
                .withLocalized(false)
                .withPartOf(false)
                .withGenerate(false)
                .withUnique(false)
                .setup()
    }
}