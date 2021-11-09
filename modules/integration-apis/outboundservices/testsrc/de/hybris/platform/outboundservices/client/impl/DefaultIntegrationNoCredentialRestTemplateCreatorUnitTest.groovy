/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import org.junit.Test
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.ClientHttpRequestInterceptor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultIntegrationNoCredentialRestTemplateCreatorUnitTest extends Specification {

    @Shared
    def consumedDestination = Stub(ConsumedDestinationModel) {
        getCredential() >> null
    }
    def restTemplateCreator = new DefaultIntegrationNoCredentialRestTemplateCreator()

    @Test
    @Unroll
    def "isApplicable is #applicable when Credentials are #condition"() {
        expect:
        restTemplateCreator.isApplicable(destination) == applicable

        where:
        destination                                                                         | applicable | condition
        Stub(ConsumedDestinationModel) { getCredential() >> null }                          | true       | 'unassigned'
        Stub(ConsumedDestinationModel) { getCredential() >> Stub(AbstractCredentialModel) } | false      | 'assigned'
    }

    @Test
    def "creation of REST template is successful without Consumed Destination auth credentials"() {
        given:
        def interceptor = Stub(ClientHttpRequestInterceptor)
        restTemplateCreator.clientHttpRequestFactory = Stub(ClientHttpRequestFactory)
        restTemplateCreator.requestInterceptors = [interceptor]

        when:
        def restTemplate = restTemplateCreator.createRestTemplate(consumedDestination)

        then:
        restTemplate
        restTemplate.interceptors == [interceptor]
        !restTemplate.messageConverters.empty

    }

    @Test
    def "destinationTemplateId is correctly assigned from a Consumed Destination without auth credentials"() {
        given:

        when:
        def destinationRestTemplateId = restTemplateCreator.getDestinationRestTemplateId(consumedDestination)

        then:
        destinationRestTemplateId.destination == consumedDestination
    }
}
