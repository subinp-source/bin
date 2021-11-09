/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
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
class IntegrationMonitoringSecurityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String PASSWORD = 'password'
    private static final String GROUP_ADMIN = 'integrationadmingroup'
    private static final String GROUP_MONITOR = 'integrationmonitoringgroup'
    private static final String GROUP_MONITOR_ADMIN = "$GROUP_ADMIN,$GROUP_MONITOR"
    private static final String GROUP_SERVICE = 'integrationservicegroup'
    private static final String GROUP_CREATE = 'integrationcreategroup'
    private static final String GROUP_VIEW = 'integrationviewgroup'
    private static final String GROUP_DELETE = 'integrationdeletegroup'

    private static final String ADMIN_USER = 'integrationadmingroup-user'
    private static final String MONITOR_USER = 'integrationmonitoringgroup-user'
    private static final String SERVICE_USER = 'integrationservicegroup-user'
    private static final String CREATE_USER = 'integrationcreategroup-user'
    private static final String VIEW_USER = 'integrationviewgroup-user'
    private static final String DELETE_USER = 'integrationdeletegroup-user'
    private static final String MONITOR_ADMIN_USER = 'integrationadmingroup-and-integrationmonitoringgroup-user'

    private static final def users = [ADMIN_USER, MONITOR_USER, SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER,]

    def setupSpec() {
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundservices.impex', 'UTF-8'

        userInGroups(ADMIN_USER, GROUP_ADMIN)
        userInGroups(MONITOR_USER, GROUP_MONITOR)
        userInGroups(SERVICE_USER, GROUP_SERVICE)
        userInGroups(CREATE_USER, GROUP_CREATE)
        userInGroups(VIEW_USER, GROUP_VIEW)
        userInGroups(DELETE_USER, GROUP_DELETE)
        userInGroups(MONITOR_ADMIN_USER, GROUP_MONITOR_ADMIN)

        inboundChannelConfigurationBuilder()
                .withCode("InboundIntegrationMonitoring")
                .withAuthType(AuthenticationType.BASIC)
                .build()

        inboundChannelConfigurationBuilder()
                .withCode("OutboundIntegrationMonitoring")
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "InboundIntegrationMonitoring" }
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "OutboundIntegrationMonitoring" }
        IntegrationTestUtil.removeAll IntegrationObjectModel
        users.each { user -> IntegrationTestUtil.remove EmployeeModel, { users.contains it.uid } }
    }

    def userInGroups(final String user, final String groups) {
        IntegrationTestUtil.importImpEx(
                '$password=@password[translator = de.hybris.platform.impex.jalo.translators.UserPasswordTranslator]',
                'INSERT_UPDATE Employee; UID[unique = true]; $password  ; groups(uid)',
                "                      ; $user             ; *:$PASSWORD; $groups")
    }

    @Test
    @Unroll
    def "User must be authenticated in order to GET /#service"() {
        when:
        def response = basicAuthRequest(service)
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        service << ['InboundIntegrationMonitoring', 'OutboundIntegrationMonitoring']
    }

    @Test
    @Unroll
    def "GET /#service returns #status for #user"() {
        when:
        def response = basicAuthRequest(service)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        service                         | user               | status
        'InboundIntegrationMonitoring'  | MONITOR_ADMIN_USER | HttpStatusCodes.OK
        'InboundIntegrationMonitoring'  | ADMIN_USER         | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | MONITOR_USER       | HttpStatusCodes.OK
        'InboundIntegrationMonitoring'  | SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | CREATE_USER        | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | VIEW_USER          | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | DELETE_USER        | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | ADMIN_USER         | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | MONITOR_USER       | HttpStatusCodes.OK
        'OutboundIntegrationMonitoring' | MONITOR_ADMIN_USER | HttpStatusCodes.OK
        'OutboundIntegrationMonitoring' | SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | CREATE_USER        | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | VIEW_USER          | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | DELETE_USER        | HttpStatusCodes.FORBIDDEN
    }

    @Test
    @Unroll
    def "GET /#service/anyfeed returns #status for #user"() {
        when:
        def response = basicAuthRequest(service)
                .path('IntegrationRequestStatuses')
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        service                         | user               | status
        'InboundIntegrationMonitoring'  | ADMIN_USER         | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | MONITOR_USER       | HttpStatusCodes.OK
        'InboundIntegrationMonitoring'  | MONITOR_ADMIN_USER | HttpStatusCodes.OK
        'InboundIntegrationMonitoring'  | SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | CREATE_USER        | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | VIEW_USER          | HttpStatusCodes.FORBIDDEN
        'InboundIntegrationMonitoring'  | DELETE_USER        | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | ADMIN_USER         | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | MONITOR_USER       | HttpStatusCodes.OK
        'OutboundIntegrationMonitoring' | MONITOR_ADMIN_USER | HttpStatusCodes.OK
        'OutboundIntegrationMonitoring' | SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | CREATE_USER        | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | VIEW_USER          | HttpStatusCodes.FORBIDDEN
        'OutboundIntegrationMonitoring' | DELETE_USER        | HttpStatusCodes.FORBIDDEN
    }

    @Test
    @Unroll
    def "POST to /#service/anyfeed is Forbidden for #user"() {
        when:
        def response = basicAuthRequest(service)
                .path('IntegrationRequestStatuses')
                .credentials(user, PASSWORD)
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        service                         | user
        'InboundIntegrationMonitoring'  | ADMIN_USER
        'InboundIntegrationMonitoring'  | MONITOR_USER
        'InboundIntegrationMonitoring'  | MONITOR_ADMIN_USER
        'InboundIntegrationMonitoring'  | SERVICE_USER
        'InboundIntegrationMonitoring'  | CREATE_USER
        'InboundIntegrationMonitoring'  | VIEW_USER
        'InboundIntegrationMonitoring'  | DELETE_USER
        'OutboundIntegrationMonitoring' | ADMIN_USER
        'OutboundIntegrationMonitoring' | MONITOR_USER
        'OutboundIntegrationMonitoring' | MONITOR_ADMIN_USER
        'OutboundIntegrationMonitoring' | SERVICE_USER
        'OutboundIntegrationMonitoring' | CREATE_USER
        'OutboundIntegrationMonitoring' | VIEW_USER
        'OutboundIntegrationMonitoring' | DELETE_USER
    }

    @Test
    @Unroll
    def "DELETE from /#service/anyfeed is Forbidden for #user"() {
        when:
        def response = basicAuthRequest(service)
                .path('IntegrationRequestStatuses')
                .credentials(user, PASSWORD)
                .build()
                .delete()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        service                         | user
        'InboundIntegrationMonitoring'  | ADMIN_USER
        'InboundIntegrationMonitoring'  | MONITOR_USER
        'InboundIntegrationMonitoring'  | MONITOR_ADMIN_USER
        'InboundIntegrationMonitoring'  | SERVICE_USER
        'InboundIntegrationMonitoring'  | CREATE_USER
        'InboundIntegrationMonitoring'  | VIEW_USER
        'InboundIntegrationMonitoring'  | DELETE_USER
        'OutboundIntegrationMonitoring' | ADMIN_USER
        'OutboundIntegrationMonitoring' | MONITOR_USER
        'OutboundIntegrationMonitoring' | MONITOR_ADMIN_USER
        'OutboundIntegrationMonitoring' | SERVICE_USER
        'OutboundIntegrationMonitoring' | CREATE_USER
        'OutboundIntegrationMonitoring' | VIEW_USER
        'OutboundIntegrationMonitoring' | DELETE_USER
    }

    @Test
    @Unroll
    def "PATCH /#service/anyfeed is Forbidden for #user"() {
        when:
        def response = basicAuthRequest(service)
                .path('IntegrationRequestStatuses')
                .credentials(user, PASSWORD)
                .build()
                .patch Entity.json('{}')

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        service                         | user
        'InboundIntegrationMonitoring'  | ADMIN_USER
        'InboundIntegrationMonitoring'  | MONITOR_USER
        'InboundIntegrationMonitoring'  | MONITOR_ADMIN_USER
        'InboundIntegrationMonitoring'  | SERVICE_USER
        'InboundIntegrationMonitoring'  | CREATE_USER
        'InboundIntegrationMonitoring'  | VIEW_USER
        'InboundIntegrationMonitoring'  | DELETE_USER
        'OutboundIntegrationMonitoring' | ADMIN_USER
        'OutboundIntegrationMonitoring' | MONITOR_USER
        'OutboundIntegrationMonitoring' | MONITOR_ADMIN_USER
        'OutboundIntegrationMonitoring' | SERVICE_USER
        'OutboundIntegrationMonitoring' | CREATE_USER
        'OutboundIntegrationMonitoring' | VIEW_USER
        'OutboundIntegrationMonitoring' | DELETE_USER
    }

    def basicAuthRequest(String path) {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .accept("application/json")
                .path(path)
    }
}
