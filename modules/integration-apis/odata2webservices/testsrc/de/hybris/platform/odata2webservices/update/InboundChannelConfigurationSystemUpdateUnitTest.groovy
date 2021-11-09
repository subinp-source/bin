/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.update

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.config.InboundServicesConfiguration
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class InboundChannelConfigurationSystemUpdateUnitTest extends Specification {

    def modelService = Mock(ModelService)
    def flexSearch = Mock(FlexibleSearchService)
    def inboundServicesConfiguration = Stub(InboundServicesConfiguration)

    def inboundChannelConfigurationSystemUpdater = new InboundChannelConfigurationSystemUpdater(modelService,
            flexSearch, inboundServicesConfiguration)

    @Test
    def "legacy mode flag enabled does not create ICC's"() {
        given:
        inboundServicesConfiguration.isLegacySecurity() >> true

        when:
        inboundChannelConfigurationSystemUpdater.associateIntegrationObjectWithInboundConfigChannel()

        then:
        0 * flexSearch.<IntegrationObjectModel> search(_ as FlexibleSearchQuery)
        0 * modelService.save(_ as InboundChannelConfigurationModel)
    }

    @Test
    @Unroll
    def "#condition integration object item(s) without an ICC to be created"() {
        given: "the flex search result returns #condition IO(s) not associated to an ICC"
        flexSearch.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> result
        }
        and:
        inboundServicesConfiguration.isLegacySecurity() >> false

        when:
        inboundChannelConfigurationSystemUpdater.associateIntegrationObjectWithInboundConfigChannel()

        then:
        iterations * modelService.save(_ as InboundChannelConfigurationModel)

        where:
        condition | result                                                | iterations
        'Zero'    | []                                                    | 0
        'One'     | [Stub(IntegrationObjectModel) { getCode() >> "IO1" }] | 1
    }
}
