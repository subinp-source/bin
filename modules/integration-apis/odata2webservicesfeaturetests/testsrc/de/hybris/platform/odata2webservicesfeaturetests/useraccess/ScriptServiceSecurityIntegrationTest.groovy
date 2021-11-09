/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.type.SearchRestrictionModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class ScriptServiceSecurityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String SERVICE_NAME = "ScriptService"
    private static final String PASSWORD = 'password'
    private static final String GROUP_ADMIN = 'integrationadmingroup'
    private static final String GROUP_SERVICE = 'integrationservicegroup'
    private static final String GROUP_MONITOR = 'integrationmonitoringgroup'
    private static final String GROUP_OUTBOUNDSYNC = 'outboundsyncgroup'
    private static final String GROUP_CREATE = 'integrationcreategroup'
    private static final String GROUP_VIEW = 'integrationviewgroup'
    private static final String GROUP_DELETE = 'integrationdeletegroup'
    private static final String GROUP_SCRIPT_SERVICE = 'scriptservicegroup'

    private static final String ADMIN_USER = 'integrationadmingroup-user'
    private static final String SCRIPT_SERVICE_USER = 'scriptservicegroup-user'
    private static final String MONITOR_USER = 'integrationmonitoringgroup-user'
    private static final String SERVICE_USER = 'integrationservicegroup-user'
    private static final String CREATE_USER = 'integrationcreategroup-user'
    private static final String VIEW_USER = 'integrationviewgroup-user'
    private static final String DELETE_USER = 'integrationdeletegroup-user'
    private static final String ADMIN_MONITOR_USER = 'integrationmonitoringgroup-and-integrationadmingroup-user'
    private static final String ADMIN_SERVICE_USER = 'integrationservicegroup-and-integrationadmingroup-user'
    private static final String ADMIN_SCRIPT_SERVICE_USER = 'scriptservicegroup-and-integrationadmingroup-user'
    private static final String ADMIN_OUTBOUNDSYNC_USER = 'outboundsyncgroup-and-integrationadmingroup-user'
    private static final String ADMIN_OUTBOUNDSYNC_MONITOR_USER = 'outboundsyncgroup-and-integrationmonitoringgroup-user'
    private static final String ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER = 'outboundsyncgroup-integrationmonitoringgroup-integrationservicegroup-integrationadmingroup-scriptservicegroup-user'

    private static final Collection users = [ADMIN_USER, SCRIPT_SERVICE_USER, MONITOR_USER, SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER, ADMIN_MONITOR_USER, ADMIN_SERVICE_USER,
                                             ADMIN_SCRIPT_SERVICE_USER, ADMIN_OUTBOUNDSYNC_USER, ADMIN_OUTBOUNDSYNC_MONITOR_USER, ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER]
    private static final Collection searchRestrictions = ['inboundMonitoringIntegrationVisibility', 'outboundMonitoringIntegrationVisibility', 'integrationServiceVisibility', 'outboundChannelConfigVisibility', 'scriptServiceVisibility']

    private static final String SCRIPT_PAYLOAD = '{"code": "MyScript", "scriptType": {"code":"GROOVY"}, "content": "helloWorld"}'

    def setupSpec() {
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundsync-setup.impex', 'UTF-8'
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-scriptservices.impex', 'UTF-8'

        userInGroups(ADMIN_USER, GROUP_ADMIN)
        userInGroups(SCRIPT_SERVICE_USER, GROUP_SCRIPT_SERVICE)
        userInGroups(MONITOR_USER, GROUP_MONITOR)
        userInGroups(SERVICE_USER, GROUP_SERVICE)
        userInGroups(CREATE_USER, GROUP_CREATE)
        userInGroups(VIEW_USER, GROUP_VIEW)
        userInGroups(DELETE_USER, GROUP_DELETE)
        userInGroups(ADMIN_MONITOR_USER, "$GROUP_ADMIN,$GROUP_MONITOR")
        userInGroups(ADMIN_SERVICE_USER, "$GROUP_ADMIN,$GROUP_SERVICE")
        userInGroups(ADMIN_SCRIPT_SERVICE_USER, "$GROUP_ADMIN,$GROUP_SCRIPT_SERVICE")
        userInGroups(ADMIN_OUTBOUNDSYNC_USER, "$GROUP_ADMIN,$GROUP_OUTBOUNDSYNC")
        userInGroups(ADMIN_OUTBOUNDSYNC_MONITOR_USER, "$GROUP_ADMIN,$GROUP_OUTBOUNDSYNC,$GROUP_MONITOR")
        userInGroups(ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER, "$GROUP_ADMIN,$GROUP_SERVICE,$GROUP_OUTBOUNDSYNC,$GROUP_MONITOR")

        inboundChannelConfigurationBuilder()
                .withCode(SERVICE_NAME)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == SERVICE_NAME }
        IntegrationTestUtil.removeAll IntegrationObjectModel
        users.each { user -> IntegrationTestUtil.remove EmployeeModel, { users.contains it.uid } }
        searchRestrictions.each { searchRestriction -> IntegrationTestUtil.remove SearchRestrictionModel, { searchRestrictions.contains it.code } }
    }

    @Test
    @Unroll
    def "User must be authenticated in order to #method /ScriptService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .build()
                .method method

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        method << ['GET', 'POST', 'DELETE', 'PATCH']
    }

    @Test
    def "User must be authenticated in order to GET any resource in /ScriptService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('resourceNameThatDoesNotMatter')
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode
    }


    @Test
    def "User in groups integrationadmingroup AND scriptservicegroup gets Forbidden when requesting with unsupported HTTP verb at any resource in /ScriptService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('resourceNameThatDoesNotMatter')
                .credentials(ADMIN_SCRIPT_SERVICE_USER, PASSWORD)
                .build()
                .method 'COPY'

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode
    }

    @Test
    @Unroll
    def "#user gets #status for GET /ScriptService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        user                      | status
        ADMIN_USER                | HttpStatusCodes.NOT_FOUND
        SERVICE_USER              | HttpStatusCodes.FORBIDDEN
        SCRIPT_SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        ADMIN_SERVICE_USER        | HttpStatusCodes.NOT_FOUND
        ADMIN_MONITOR_USER        | HttpStatusCodes.NOT_FOUND
        ADMIN_OUTBOUNDSYNC_USER   | HttpStatusCodes.NOT_FOUND
        CREATE_USER               | HttpStatusCodes.FORBIDDEN
        VIEW_USER                 | HttpStatusCodes.FORBIDDEN
        DELETE_USER               | HttpStatusCodes.FORBIDDEN
        ADMIN_SCRIPT_SERVICE_USER | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "#user gets Forbidden for GET for /ScriptService/scriptThatDoesNotMatter"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('scriptThatDoesNotMatter')
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user << [SCRIPT_SERVICE_USER, SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER]
    }

    @Test
    @Unroll
    def "#user gets #status for GET /ScriptService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        feed          | user                      | status
        'Scripts'     | ADMIN_SCRIPT_SERVICE_USER | HttpStatusCodes.OK
        'Scripts'     | ADMIN_SERVICE_USER        | HttpStatusCodes.NOT_FOUND
        'Scripts'     | ADMIN_USER                | HttpStatusCodes.NOT_FOUND
        'Scripts'     | ADMIN_MONITOR_USER        | HttpStatusCodes.NOT_FOUND
        'Scripts'     | ADMIN_OUTBOUNDSYNC_USER   | HttpStatusCodes.NOT_FOUND
        'ScriptTypes' | ADMIN_SCRIPT_SERVICE_USER | HttpStatusCodes.OK
        'ScriptTypes' | ADMIN_SERVICE_USER        | HttpStatusCodes.NOT_FOUND
        'ScriptTypes' | ADMIN_USER                | HttpStatusCodes.NOT_FOUND
        'ScriptTypes' | ADMIN_MONITOR_USER        | HttpStatusCodes.NOT_FOUND
        'ScriptTypes' | ADMIN_OUTBOUNDSYNC_USER   | HttpStatusCodes.NOT_FOUND
    }

    @Test
    @Unroll
    def "#user is Forbidden to #httpMethod to /ScriptService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .credentials(user, PASSWORD)
                .build()
                .method(httpMethod, Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user                | httpMethod
        SCRIPT_SERVICE_USER | 'POST'
        SERVICE_USER        | 'POST'
        CREATE_USER         | 'POST'
        VIEW_USER           | 'POST'
        DELETE_USER         | 'POST'
        MONITOR_USER        | 'POST'

        SERVICE_USER        | 'PATCH'
        CREATE_USER         | 'PATCH'
        VIEW_USER           | 'PATCH'
        DELETE_USER         | 'PATCH'
        MONITOR_USER        | 'PATCH'
        SCRIPT_SERVICE_USER | 'PATCH'

        SERVICE_USER        | 'DELETE'
        CREATE_USER         | 'DELETE'
        VIEW_USER           | 'DELETE'
        DELETE_USER         | 'DELETE'
        MONITOR_USER        | 'DELETE'
        SCRIPT_SERVICE_USER | 'DELETE'
    }

    @Test
    @Unroll
    def "#user gets Not Found when sending a POST to /ScriptService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode

        where:
        feed      | user
        'Scripts' | ADMIN_MONITOR_USER
        'Scripts' | ADMIN_OUTBOUNDSYNC_USER
        'Scripts' | ADMIN_SERVICE_USER
    }

    @Test
    def "User in groups integrationadmingroup AND scriptservicegroup is authorized to POST to /ScriptService/Scripts"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('Scripts')
                .credentials(ADMIN_SCRIPT_SERVICE_USER, PASSWORD)
                .build()
                .post Entity.json(SCRIPT_PAYLOAD)

        then:
        response.status == HttpStatusCodes.CREATED.statusCode
    }

    @Test
    def "User in groups integrationadmingroup AND scriptservicegroup is authorized to PATCH to /ScriptService/Scripts('MyScript')"() {
        given:
        defaultScriptExists()

        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path("Scripts('MyScript')")
                .credentials(ADMIN_SCRIPT_SERVICE_USER, PASSWORD)
                .build()
                .patch(Entity.json(SCRIPT_PAYLOAD))

        then:
        response.status == HttpStatusCodes.OK.statusCode
    }

    @Test
    @Unroll
    def "#user gets Not Found when sending a PATCH to /ScriptService/#feed"() {
        given:
        defaultScriptExists()

        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .patch(Entity.json(jsonBody))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode

        where:
        feed                  | user                    | jsonBody
        "Scripts('MyScript')" | ADMIN_OUTBOUNDSYNC_USER | SCRIPT_PAYLOAD
        "Scripts('MyScript')" | ADMIN_SERVICE_USER      | SCRIPT_PAYLOAD
        "Scripts('MyScript')" | ADMIN_MONITOR_USER      | SCRIPT_PAYLOAD
    }

    @Test
    @Unroll
    def "#user gets #responseStatus when DELETE /ScriptService/#feed"() {
        given:
        defaultScriptExists()

        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path(feed)
                .credentials(user, PASSWORD)
                .build()
                .delete()

        then:
        response.status == responseStatus.statusCode

        where:
        feed                  | user                      | responseStatus
        "Scripts('MyScript')" | ADMIN_OUTBOUNDSYNC_USER   | HttpStatusCodes.NOT_FOUND

        "Scripts('MyScript')" | ADMIN_MONITOR_USER        | HttpStatusCodes.NOT_FOUND

        "Scripts('MyScript')" | ADMIN_SERVICE_USER        | HttpStatusCodes.NOT_FOUND

        "Scripts('MyScript')" | ADMIN_SCRIPT_SERVICE_USER | HttpStatusCodes.OK
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
        feed << ["IntegrationObjects('ScriptService')",
                 "IntegrationObjects('IntegrationService')",
                 "IntegrationObjects('InboundIntegrationMonitoring')",
                 "IntegrationObjects('OutboundChannelConfig')",

                 "IntegrationObjectItems('doesNotMatter|IntegrationService')",
                 "IntegrationObjectItems('doesNotMatter|ScriptService')",
                 "IntegrationObjectItems('doesNotMatter|InboundIntegrationMonitoring')",
                 "IntegrationObjectItems('doesNotMatter|OutboundChannelConfig')",

                 "IntegrationObjectItemAttributes('doesNotMatter|IntegrationService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|ScriptService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|InboundIntegrationMonitoring')",
                 "IntegrationObjectItemAttributes('doesNotMatter|OutboundChannelConfig')"
        ]
    }

    @Test
    @Unroll
    def "User in groups scriptservicegroup, integrationadmingroup,integrationservicegroup,outboundsyncgroup & integrationmonitoringgroup gets Forbidden for PATCH /IntegrationService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path("IntegrationService")
                .path(feed)
                .credentials(ADMIN_SCRIPT_SERVICE_OUTBOUNDSYNC_MONITOR_USER, PASSWORD)
                .build()
                .patch Entity.json('{}')

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        feed << ["IntegrationObjects('IntegrationService')",
                 "IntegrationObjects('InboundIntegrationMonitoring')",
                 "IntegrationObjects('OutboundChannelConfig')",
                 "IntegrationObjects('ScriptService')",

                 "IntegrationObjectItems('doesNotMatter|IntegrationService')",
                 "IntegrationObjectItems('doesNotMatter|InboundIntegrationMonitoring')",
                 "IntegrationObjectItems('doesNotMatter|OutboundChannelConfig')",
                 "IntegrationObjectItems('doesNotMatter|ScriptService')",

                 "IntegrationObjectItemAttributes('doesNotMatter|IntegrationService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|InboundIntegrationMonitoring')",
                 "IntegrationObjectItemAttributes('doesNotMatter|OutboundChannelConfig')",
                 "IntegrationObjectItemAttributes('doesNotMatter|ScriptService')"
        ]
    }

    def userInGroups(final String user, final String groups) {
        IntegrationTestUtil.importImpEx(
                '$password=@password[translator = de.hybris.platform.impex.jalo.translators.UserPasswordTranslator]',
                'INSERT_UPDATE Employee; UID[unique = true]; $password  ; groups(uid)',
                "                      ; $user             ; *:$PASSWORD; $groups")
    }

    def basicAuthRequest() {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .accept('application/json')
    }

    private static defaultScriptExists() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                '                               ; MyScript ; GROOVY; "hello"')
    }
}

