/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.matchers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.config.InboundServicesConfiguration
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

@UnitTest
class BasicAuthMatcherUnitTest extends Specification {

    public static final String IO = 'MyIO'

    def integrationObjectService = Stub(IntegrationObjectService)
    def serviceNameExtractor = Stub(ServiceNameExtractor)
    def flexibleSearchService = Stub(FlexibleSearchService)
    def inboundServicesConfiguration = Stub(InboundServicesConfiguration)
    def matcher = new BasicAuthMatcher(integrationObjectService, serviceNameExtractor, flexibleSearchService, inboundServicesConfiguration)

    @Test
    @Unroll
    def "matches is #matches when InboundChannelConfiguration is present with authentication of type #authenticationType"() {
        given:
        def request = request()
        serviceNameExtractor.extract(request) >> IO
        def io = integrationObject()
        integrationObjectService.findIntegrationObject(IO) >> io
        flexibleSearchService.getModelByExample({ it.integrationObject == io }) >> channelConfiguration(authenticationType)

        expect:
        matcher.matches(request) == matches

        where:
        authenticationType       | matches
        AuthenticationType.OAUTH | false
        AuthenticationType.BASIC | true
    }

    @Test
    def "matches is #matches when integration object is not found and authorization header is #authorizationHeader"() {
        given:
        def request = requestWithAuth(authorizationHeader)
        serviceNameExtractor.extract(request) >> IO
        integrationObjectService.findIntegrationObject(IO) >> { throw new ModelNotFoundException('not found') }

        expect:
        matcher.matches(request) == matches

        where:
        authorizationHeader | matches
        "basic"             | true
        "bearer"            | false
        "randomValue"       | false
    }

    @Test
    @Unroll
    def "matches=#legacySecurity when channel configuration is not found and matchesSecurityLegacyMode=legacySecurity"() {
        given:
        def request = request()
        serviceNameExtractor.extract(request) >> IO
        integrationObjectService.findIntegrationObject(IO) >> integrationObject()
        flexibleSearchService.getModelByExample(_ as InboundChannelConfigurationModel) >> { throw new ModelNotFoundException('not found') }
        and: "inboundservices.legacy.security configuration property is #legacySecurity"
        inboundServicesConfiguration.isLegacySecurity() >> legacySecurity

        expect:
        matcher.matches(request) == legacySecurity

        where:
        legacySecurity << [true, false]
    }

    @Test
    def "match when integration object code is empty"() {
        given:
        def request = requestWithAuth("basic")
        serviceNameExtractor.extract(request) >> ""
        integrationObjectService.findIntegrationObject("") >> { throw new IllegalArgumentException() }

        expect:
        matcher.matches(request) == true
    }

    @Test
    @Unroll
    def "matchesSecurityLegacyMode() is #legacySecurity when isLegacySecurity() returns #legacySecurity"() {
        given:
        inboundServicesConfiguration.isLegacySecurity() >> legacySecurity

        expect:
        matcher.matchesSecurityLegacyMode() == legacySecurity

        where:
        legacySecurity << [true, false]
    }

    private InboundChannelConfigurationModel channelConfiguration(AuthenticationType authenticationType) {
        Stub(InboundChannelConfigurationModel) {
            getAuthenticationType() >> authenticationType
        }
    }

    private IntegrationObjectModel integrationObject() {
        Stub(IntegrationObjectModel)
    }

    private HttpServletRequest request() {
        Stub(HttpServletRequest)
    }

    private HttpServletRequest requestWithAuth(String auth) {
        Stub(HttpServletRequest) {
            getHeader("Authorization") >> auth
        }
    }
}
