/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.enums.EventPriority
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.outboundservices.BasicCredentialBuilder
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundservices.EndpointBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Shared
import spock.lang.Unroll

import javax.ws.rs.client.Entity

import static de.hybris.platform.integrationservices.IntegrationObjectModelBuilder.integrationObject
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.outboundservices.BasicCredentialBuilder.basicCredentialBuilder
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.outboundservices.DestinationTargetBuilder.destinationTarget
import static de.hybris.platform.outboundservices.EndpointBuilder.endpointBuilder
import static de.hybris.platform.webhookservices.EventConfigurationBuilder.eventConfiguration
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class WebhookServiceSecurityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String SERVICE_NAME = "WebhookService"
    private static final String TEST_NAME = "WebhookServiceSecurity"
    private static final String IO_CODE = "${TEST_NAME}IO"
    private static final String CONSUMED_DESTINATION_ID = "${TEST_NAME}ConsumedDestination"
    private static final String DESTINATION_TARGET_ID = "${TEST_NAME}DestinationTarget"
    private static final String EVENT_TYPE = ItemSavedEvent.canonicalName
    private static final String DEFAULT_WEBHOOK_CONFIG_KEY = "${CONSUMED_DESTINATION_ID}|${DESTINATION_TARGET_ID}|${IO_CODE}|${EVENT_TYPE}"
    private static final String PASSWORD = 'password'
    private static final String GROUP_ADMIN = 'integrationadmingroup'
    private static final String GROUP_SERVICE = 'integrationservicegroup'
    private static final String GROUP_CREATE = 'integrationcreategroup'
    private static final String GROUP_VIEW = 'integrationviewgroup'
    private static final String GROUP_DELETE = 'integrationdeletegroup'
    private static final String GROUP_WEBHOOK_SERVICE = 'webhookservicegroup'

    private static final String ADMIN_USER = 'integrationadmingroup-user'
    private static final String WEBHOOK_SERVICE_USER = 'webhookservicegroup-user'
    private static final String SERVICE_USER = 'integrationservicegroup-user'
    private static final String CREATE_USER = 'integrationcreategroup-user'
    private static final String VIEW_USER = 'integrationviewgroup-user'
    private static final String DELETE_USER = 'integrationdeletegroup-user'
    private static final String ADMIN_SERVICE_USER = 'integrationservicegroup-and-integrationadmingroup-user'
    private static final String ADMIN_WEBHOOK_SERVICE_USER = 'webhookservicegroup-and-integrationadmingroup-user'
    private static final String ADMIN_WEBHOOK_SERVICE_SERVICE_USER = 'integrationservicegroup-integrationadmingroup-webhookservicegroup-user'

    private static final def users = [ADMIN_USER, SERVICE_USER, CREATE_USER, VIEW_USER,
                                      DELETE_USER, ADMIN_SERVICE_USER, ADMIN_WEBHOOK_SERVICE_USER,
                                      ADMIN_WEBHOOK_SERVICE_SERVICE_USER]

    @Shared
    DestinationTargetModel destinationTarget
    @Shared
    EventConfigurationModel eventConfiguration
    @Shared
    EndpointBuilder endpointBuilder
    @Shared
    BasicCredentialBuilder basicCredentialBuilder
    @Shared
    ConsumedDestinationBuilder consumedDestinationBuilder
    @Shared
    WebhookConfigurationModel webhookConfiguration

    def setupSpec() {
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-webhookservice-api.impex', 'UTF-8'

        integrationObject().withCode(IO_CODE).build()
        destinationTarget = destinationTarget()
                .withId(DESTINATION_TARGET_ID)
                .withDestinationChannel(DestinationChannel.WEBHOOKSERVICES)
                .withRegistrationStatus(RegistrationStatus.REGISTERED)
                .build()
        eventConfiguration = eventConfiguration()
                .withEventClass(ItemSavedEvent)
                .withDestination(destinationTarget)
                .withVersion(1)
                .withExport(true)
                .withExportName("${TEST_NAME}.${ItemSavedEvent.simpleName}")
                .withExtensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .withPriority(EventPriority.CRITICAL)
                .build()
        endpointBuilder = endpointBuilder()
                .withId("${TEST_NAME}Endpoint")
                .withName("${TEST_NAME}Endpoint")
                .withSpecUrl("https://${TEST_NAME}")
                .withVersion("1")
        basicCredentialBuilder = basicCredentialBuilder()
                .withId("${TEST_NAME}BasicCredential")
                .withUsername("testUser")
                .withPassword(PASSWORD)
        consumedDestinationBuilder = consumedDestinationBuilder()
                .withId(CONSUMED_DESTINATION_ID)
                .withDestinationTarget(destinationTarget)
                .withEndpoint(endpointBuilder)
                .withCredential(basicCredentialBuilder)
                .withUrl('https://somewhere')
        webhookConfiguration = webhookConfiguration()
                .withIntegrationObject(IO_CODE)
                .withDestination(consumedDestinationBuilder)
                .withEvent(ItemSavedEvent)
                .build()

        userInGroups(ADMIN_USER, GROUP_ADMIN)
        userInGroups(WEBHOOK_SERVICE_USER, GROUP_WEBHOOK_SERVICE)
        userInGroups(SERVICE_USER, GROUP_SERVICE)
        userInGroups(CREATE_USER, GROUP_CREATE)
        userInGroups(VIEW_USER, GROUP_VIEW)
        userInGroups(DELETE_USER, GROUP_DELETE)
        userInGroups(ADMIN_SERVICE_USER, "$GROUP_ADMIN,$GROUP_SERVICE")
        userInGroups(ADMIN_WEBHOOK_SERVICE_USER, "$GROUP_ADMIN,$GROUP_WEBHOOK_SERVICE")
        userInGroups(ADMIN_WEBHOOK_SERVICE_SERVICE_USER, "$GROUP_ADMIN,$GROUP_SERVICE,$GROUP_WEBHOOK_SERVICE")
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove EmployeeModel, { users.contains it.uid }
        IntegrationTestUtil.remove WebhookConfigurationModel, {it == webhookConfiguration }
        IntegrationTestUtil.remove eventConfiguration
        IntegrationTestUtil.remove EventConfigurationModel, { it.extensionName == WebhookservicesConstants.EXTENSIONNAME }
        IntegrationTestUtil.remove DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        ConsumedDestinationBuilder.cleanup()
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    @Unroll
    def "User must be authenticated in order to #method /WebhookService"() {
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
    def "User must be authenticated in order to GET any resource in /WebhookService"() {
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
    def "User in groups integrationadmingroup AND webhookservicegroup gets Forbidden when requesting with unsupported HTTP verb at any resource in /WebhookService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('resourceNameThatDoesNotMatter')
                .credentials(ADMIN_WEBHOOK_SERVICE_USER, PASSWORD)
                .build()
                .method 'COPY'

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode
    }

    @Test
    @Unroll
    def "#user gets #status for GET /WebhookService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == status.statusCode

        where:
        user                       | status
        ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        SERVICE_USER               | HttpStatusCodes.FORBIDDEN
        WEBHOOK_SERVICE_USER       | HttpStatusCodes.FORBIDDEN
        ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        CREATE_USER                | HttpStatusCodes.FORBIDDEN
        VIEW_USER                  | HttpStatusCodes.FORBIDDEN
        DELETE_USER                | HttpStatusCodes.FORBIDDEN
        ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "#user gets Forbidden for GET for /WebhookService/webhookConfigThatDoesNotMatter"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('webhookConfigThatDoesNotMatter')
                .credentials(user, PASSWORD)
                .build()
                .get()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user << [WEBHOOK_SERVICE_USER, SERVICE_USER, CREATE_USER, VIEW_USER, DELETE_USER]
    }

    @Test
    @Unroll
    def "#user gets #status for GET /WebhookService/#feed"() {
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
        feed                       | user                       | status
        'WebhookConfigurations'    | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'WebhookConfigurations'    | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'WebhookConfigurations'    | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'IntegrationObjects'       | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'IntegrationObjects'       | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'IntegrationObjects'       | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'ConsumedDestinations'     | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'ConsumedDestinations'     | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'ConsumedDestinations'     | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'DestinationTargets'       | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'DestinationTargets'       | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'DestinationTargets'       | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'DestinationChannels'      | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'DestinationChannels'      | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'DestinationChannels'      | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'EventConfigurations'      | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'EventConfigurations'      | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'EventConfigurations'      | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'Endpoints'                | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'Endpoints'                | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'Endpoints'                | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'BasicCredentials'         | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'BasicCredentials'         | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'BasicCredentials'         | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
        'ConsumedOAuthCredentials' | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
        'ConsumedOAuthCredentials' | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        'ConsumedOAuthCredentials' | ADMIN_USER                 | HttpStatusCodes.NOT_FOUND
    }

    @Test
    @Unroll
    def "#user is Forbidden to #httpMethod to /WebhookService"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .credentials(user, PASSWORD)
                .build()
                .method(httpMethod, Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        user                 | httpMethod
        WEBHOOK_SERVICE_USER | 'POST'
        SERVICE_USER         | 'POST'
        CREATE_USER          | 'POST'
        VIEW_USER            | 'POST'
        DELETE_USER          | 'POST'

        SERVICE_USER         | 'PATCH'
        CREATE_USER          | 'PATCH'
        VIEW_USER            | 'PATCH'
        DELETE_USER          | 'PATCH'
        WEBHOOK_SERVICE_USER | 'PATCH'

        SERVICE_USER         | 'DELETE'
        CREATE_USER          | 'DELETE'
        VIEW_USER            | 'DELETE'
        DELETE_USER          | 'DELETE'
        WEBHOOK_SERVICE_USER | 'DELETE'
    }

    @Test
    def 'User in integrationadmingroup and not in webhookservicegroup gets Not Found when sending a POST to /WebhookService/WebhookConfigurations'() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('WebhookConfigurations')
                .credentials(ADMIN_SERVICE_USER, PASSWORD)
                .build()
                .post(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode
    }

    @Test
    def "User in groups integrationadmingroup AND webhookservicegroup is authorized to POST to /WebhookService/WebhookConfigurations"() {
        given:
        def json = json()
                .withField('integrationObject', json().withCode(IO_CODE))
                .withField('destination', json()
                        .withId(CONSUMED_DESTINATION_ID)
                        .withField('destinationTarget', json().withId(DESTINATION_TARGET_ID))
                        .withField('url', 'https://somewhere'))
                .withField('eventType', EVENT_TYPE)
                .build()

        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path('WebhookConfigurations')
                .credentials(ADMIN_WEBHOOK_SERVICE_USER, PASSWORD)
                .build()
                .post Entity.json(json)

        then:
        response.status == HttpStatusCodes.CREATED.statusCode
    }

    @Test
    def "User in groups integrationadmingroup AND webhookservicegroup is authorized to PATCH to /WebhookService/WebhookConfigurations('key')"() {
        given: 'add script location to webhook configuration'
        def json = json()
                .withField('filterLocation', 'model://myScript')
                .build()

        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path("WebhookConfigurations('$DEFAULT_WEBHOOK_CONFIG_KEY')")
                .credentials(ADMIN_WEBHOOK_SERVICE_USER, PASSWORD)
                .build()
                .patch(Entity.json(json))

        then:
        response.status == HttpStatusCodes.OK.statusCode
    }

    @Test
    def "User in integrationadmingroup and not in webhookservicegroup gets Not Found when sending a PATCH to /WebhookService/WebhookConfigurations('key')"() {
        when:
        def response = basicAuthRequest()
                .path(SERVICE_NAME)
                .path("WebhookConfigurations('$DEFAULT_WEBHOOK_CONFIG_KEY'")
                .credentials(ADMIN_SERVICE_USER, PASSWORD)
                .build()
                .patch(Entity.json('{}'))

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode
    }

    @Test
    @Unroll
    def "#user gets #responseStatus when DELETE /WebhookService/#feed"() {
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
        feed                                                   | user                       | responseStatus
        "WebhookConfigurations('$DEFAULT_WEBHOOK_CONFIG_KEY')" | ADMIN_SERVICE_USER         | HttpStatusCodes.NOT_FOUND
        "WebhookConfigurations('$DEFAULT_WEBHOOK_CONFIG_KEY')" | ADMIN_WEBHOOK_SERVICE_USER | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "User in groups webhookservicegroup, integrationadmingroup, integrationservicegroup gets Forbidden for DELETE /IntegrationService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path("IntegrationService")
                .path(feed)
                .credentials(ADMIN_WEBHOOK_SERVICE_SERVICE_USER, PASSWORD)
                .build()
                .delete()

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        feed << ["IntegrationObjects('WebhookService')",
                 "IntegrationObjectItems('doesNotMatter|WebhookService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|WebhookService')"
        ]
    }

    @Test
    @Unroll
    def "User in groups webhookservicegroup, integrationadmingroup, integrationservicegroup gets Forbidden for PATCH /IntegrationService/#feed"() {
        when:
        def response = basicAuthRequest()
                .path("IntegrationService")
                .path(feed)
                .credentials(ADMIN_WEBHOOK_SERVICE_SERVICE_USER, PASSWORD)
                .build()
                .patch Entity.json('{}')

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        feed << ["IntegrationObjects('WebhookService')",
                 "IntegrationObjectItems('doesNotMatter|WebhookService')",
                 "IntegrationObjectItemAttributes('doesNotMatter|WebhookService')"
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
}