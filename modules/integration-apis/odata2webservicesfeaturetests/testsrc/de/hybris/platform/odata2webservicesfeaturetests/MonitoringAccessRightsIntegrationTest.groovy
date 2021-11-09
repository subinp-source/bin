/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@NeedsEmbeddedServer(webExtensions = [Odata2webservicesConstants.EXTENSIONNAME])
@IntegrationTest
class MonitoringAccessRightsIntegrationTest extends ServicelayerSpockSpecification {
    private static final def USER = 'test-monitoring-user'
    private static final def PWD = RandomStringUtils.randomAlphanumeric(10)
    private static final String INBOUND_MONITORING_SERVICE = 'InboundIntegrationMonitoring'
    private static final String OUTBOUND_MONITORING_SERVICE = 'OutboundIntegrationMonitoring'

    @Resource(name = "defaultConfigurationService")
    private ConfigurationService configurationService

    def setupSpec() {
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        UserAccessTestUtils.createUser USER, PWD, 'integrationmonitoringgroup'
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
    def "access rights granted to GET /InboundIntegrationMonitoring/#entitySet"() {
        when:
        def response = getRequest(entitySet, INBOUND_MONITORING_SERVICE)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        entitySet << ['IntegrationRequestStatuses', 'InboundRequests', 'InboundRequestErrors', 'InboundUsers']
    }

    @Test
    @Unroll
    def "access rights granted to GET /OutboundIntegrationMonitoring/#entitySet"() {
        when:
        def response = getRequest(entitySet, OUTBOUND_MONITORING_SERVICE)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        entitySet << ['OutboundRequests', 'IntegrationRequestStatuses']
    }

    def getRequest(String path, String service) {
        UserAccessTestUtils.basicAuthRequest(service)
                .credentials(USER, PWD)
                .accept(APPLICATION_JSON_VALUE)
                .path(path)
                .build().get()
    }

    def setAccessRights(final boolean enabled) {
        configurationService.getConfiguration().setProperty("integrationservices.authorization.accessrights.enabled", String.valueOf(enabled))
    }
}
