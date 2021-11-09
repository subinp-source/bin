package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.type.SearchRestrictionModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext

@IntegrationTest
class ExposedMetaAPIServicesIntegrationTest extends ServicelayerSpockSpecification {
    private static final Collection searchRestrictions = ['inboundMonitoringIntegrationVisibility', 'outboundMonitoringIntegrationVisibility', 'integrationServiceVisibility', 'outboundChannelConfigVisibility', 'scriptServiceVisibility']

    @Resource(name = "oDataWebMonitoringFacade")
    private ODataFacade facade

    def setupSpec() {
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-inboundservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-scriptservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-outboundsync-setup.impex', 'UTF-8'
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectItemAttributeModel
        IntegrationTestUtil.removeAll IntegrationObjectItemModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        searchRestrictions.each { searchRestriction -> IntegrationTestUtil.remove SearchRestrictionModel, { searchRestrictions.contains it.code } }
    }

    @Test
    @Unroll
    def "access rights provided for exposed meta api service /#service/\$metadata"() {
        when:
        def response = facade.handleRequest request(service)

        then:
        response.getStatus() == HttpStatusCodes.OK

        where:
        service << ['InboundIntegrationMonitoring', 'OutboundIntegrationMonitoring', 'IntegrationService',
                    'ScriptService', 'OutboundChannelConfig']
    }

    ODataContext request(String service) {
        createContext ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(service)
                        .withRequestPath('$metadata'))
    }
}