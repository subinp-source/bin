/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.XmlObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.commons.lang3.RandomStringUtils
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class AdminAccessRightsIntegrationTest extends ServicelayerSpockSpecification {
    private static final def USER = 'test-user'
    private static final def PWD = RandomStringUtils.randomAlphanumeric(10)
    private static final String INTEGRATION_SERVICE = 'IntegrationService'
    private static final String SCRIPT_SERVICE = 'ScriptService'
    private static final String OUTBOUND_CHANNEL_CONFIG_SERVICE = 'OutboundChannelConfig'
    private static final Entity<String> PAYLOAD = Entity.json JsonBuilder.json().withField('anyField', 'doesNotMatter').build()

    private static final List<String> INTEGRATION_SERVICE_ENTITIES =
            ['InboundChannelConfigurations', 'AuthenticationTypes', 'IntegrationObjects',
             'IntegrationTypes', 'ItemTypeMatchEnums', 'IntegrationObjectItems',
             'IntegrationObjectItemAttributes', 'IntegrationObjectItemClassificationAttributes', 'ClassificationSystems',
             'ClassificationSystemVersions', 'ClassAttributeAssignments', 'ClassificationClasses',
             'ClassificationAttributes', 'AttributeDescriptors', 'ComposedTypes',
             'IntegrationObjectVirtualAttributeDescriptors', 'IntegrationObjectItemVirtualAttributes', 'Types']
    private static final List<String> SCRIPT_SERVICE_ENTITIES = ['Scripts', 'ScriptTypes']
    private static final List<String> OUTBOUND_CHANNEL_CONFIG_SERVICE_ENTITIES =
            ['OutboundChannelConfigurations', 'IntegrationObjects', 'OutboundSyncStreamConfigurationContainers',
             'OutboundSyncStreamConfigurations', 'ComposedTypes', 'OutboundSyncJobs',
             'OutboundSyncCronJobs', 'ConsumedDestinations', 'DestinationTargets',
             'Endpoints', 'BasicCredentials', 'ConsumedOAuthCredentials',
             'Triggers']

    @Resource(name = "defaultConfigurationService")
    private ConfigurationService configurationService

    def setupSpec() {
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-scriptservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundsync-setup.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        UserAccessTestUtils.createUser USER, PWD, 'integrationadmingroup,scriptservicegroup,integrationservicegroup,outboundsyncgroup'
    }

    def setup() {
        setAccessRights(true)
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        UserAccessTestUtils.deleteUser USER
    }

    def cleanup() {
        setAccessRights(false)
    }

    @Test
    @Unroll
    def "access rights granted to GET /IntegrationService/#entitySet"() {
        when:
        def response = getRequest(entitySet, INTEGRATION_SERVICE)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        entitySet << INTEGRATION_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to POST /IntegrationService/#entitySet"() {
        when:
        def response = postRequest(entitySet, INTEGRATION_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << INTEGRATION_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to PATCH /IntegrationService/#entitySet"() {
        when:
        def path = entitySet.concat('(doesNotExist)')
        def response = patchRequest(path, INTEGRATION_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << INTEGRATION_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to DELETE /IntegrationService/#entitySet"() {
        when:
        def path = entitySet.concat("('$integrationKey')")
        def response = deleteRequest(path, INTEGRATION_SERVICE)

        then:
        integrationKeyNotFound(response, integrationKey)

        where:
        entitySet                                       | integrationKey
        'InboundChannelConfigurations'                  | 'doesNotExist'
        'AuthenticationTypes'                           | 'doesNotExist'
        'IntegrationObjects'                            | 'doesNotExist'
        'IntegrationTypes'                              | 'doesNotExist'
        'ItemTypeMatchEnums'                            | 'doesNotExist'
        'IntegrationObjectItems'                        | 'doesNot|Exist'
        'IntegrationObjectItemAttributes'               | 'does|Not|Exist'
        'IntegrationObjectItemClassificationAttributes' | 'does|Not|Exist|aa|aa|aa|aa'
        'ClassificationSystems'                         | 'doesNotExist'
        'ClassificationSystemVersions'                  | 'doesNot|Exist'
        'ClassAttributeAssignments'                     | 'does|Not|Exist|aa'
        'ClassificationClasses'                         | 'does|Not|Exist'
        'ClassificationAttributes'                      | 'does|Not|Exist'
        'AttributeDescriptors'                          | 'doesNot|Exist'
        'ComposedTypes'                                 | 'doesNotExist'
        'IntegrationObjectVirtualAttributeDescriptors'  | 'doesNotExist'
        'IntegrationObjectItemVirtualAttributes'        | 'does|Not|Exist'
        'Types'                                         | 'doesNotExist'
    }

    @Test
    @Unroll
    def "access rights granted to GET /ScriptService/#entitySet"() {
        when:
        def response = getRequest(entitySet, SCRIPT_SERVICE)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        entitySet << SCRIPT_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to POST /ScriptService/#entitySet"() {
        when:
        def response = postRequest(entitySet, SCRIPT_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << SCRIPT_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to PATCH /ScriptService/#entitySet"() {
        when:
        def path = entitySet.concat("('doesNotExist')")
        def response = patchRequest(path, SCRIPT_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << SCRIPT_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to DELETE /ScriptService/#entitySet"() {
        when:
        def path = entitySet.concat("('doesNotExist')")
        def response = deleteRequest(path, SCRIPT_SERVICE)

        then:
        integrationKeyNotFound(response, 'doesNotExist')

        where:
        entitySet << SCRIPT_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to GET /OutboundChannelConfig/#entitySet"() {
        when:
        def response = getRequest(entitySet, OUTBOUND_CHANNEL_CONFIG_SERVICE)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        entitySet << OUTBOUND_CHANNEL_CONFIG_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to POST /OutboundChannelConfig/#entitySet"() {
        when:
        def response = postRequest(entitySet, OUTBOUND_CHANNEL_CONFIG_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << OUTBOUND_CHANNEL_CONFIG_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to PATCH /OutboundChannelConfig/#entitySet"() {
        when:
        def path = entitySet.concat('(doesNotExist)')
        def response = patchRequest(path, OUTBOUND_CHANNEL_CONFIG_SERVICE, PAYLOAD)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST.statusCode

        where:
        entitySet << OUTBOUND_CHANNEL_CONFIG_SERVICE_ENTITIES
    }

    @Test
    @Unroll
    def "access rights granted to DELETE /OutboundChannelConfig/#entitySet"() {
        when:
        def path = entitySet.concat("('$integrationKey')")
        def response = deleteRequest(path, OUTBOUND_CHANNEL_CONFIG_SERVICE)

        then:
        integrationKeyNotFound(response, integrationKey)

        where:
        entitySet                                   | integrationKey
        'OutboundChannelConfigurations'             | 'doesNotExist'
        'IntegrationObjects'                        | 'doesNotExist'
        'OutboundSyncStreamConfigurationContainers' | 'doesNotExist'
        'OutboundSyncStreamConfigurations'          | 'doesNotExist'
        'ComposedTypes'                             | 'doesNotExist'
        'OutboundSyncJobs'                          | 'doesNotExist'
        'OutboundSyncCronJobs'                      | 'doesNotExist'
        'ConsumedDestinations'                      | 'doesNot|Exist'
        'DestinationTargets'                        | 'doesNotExist'
        'Endpoints'                                 | 'doesNot|Exist'
        'BasicCredentials'                          | 'doesNotExist'
        'ConsumedOAuthCredentials'                  | 'doesNotExist'
        'Triggers'                                  | 'doesNot|Exist'
    }

    def getRequest(String path, String service) {
        UserAccessTestUtils.basicAuthRequest(service)
                .credentials(USER, PWD)
                .accept(APPLICATION_JSON_VALUE)
                .path(path)
                .build().get()
    }

    def postRequest(String path, String service, Entity body) {
        UserAccessTestUtils.basicAuthRequest(service)
                .credentials(USER, PWD)
                .accept(APPLICATION_JSON_VALUE)
                .path(path)
                .build().post(body)
    }

    def patchRequest(String path, String service, Entity body) {
        UserAccessTestUtils.basicAuthRequest(service)
                .credentials(USER, PWD)
                .accept(APPLICATION_JSON_VALUE)
                .path(path)
                .build().patch(body)
    }

    def deleteRequest(String path, String service) {
        UserAccessTestUtils.basicAuthRequest(service)
                .credentials(USER, PWD)
                .path(path)
                .build()
                .accept(APPLICATION_JSON_VALUE)
                .delete()
    }

    def setAccessRights(final boolean enabled) {
        configurationService.getConfiguration().setProperty("integrationservices.authorization.accessrights.enabled", String.valueOf(enabled))
    }

    def integrationKeyNotFound(Response response, String integrationKey) {
        assert response.status == HttpStatusCodes.NOT_FOUND.statusCode
        def xml = XmlObject.createFrom response.readEntity(String)
        assert xml.get('/error/message').contains("with integration key [$integrationKey] was not found")
        true
    }
}
