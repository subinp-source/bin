/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.filters

import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.user.UserService
import de.hybris.platform.webservicescommons.oauth2.HybrisOauth2UserFilter
import org.junit.Test
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletResponse

class IntegrationServicesOauth2UserFilterUnitTest extends Specification {

    public static final String PRINCIPAL = 'testClient'
    public static final String USER = 'testUser'

    def userService = Mock(UserService)
    def flexibleSearchService = Stub(FlexibleSearchService)
    def hybrisOauth2UserFilter = Mock(HybrisOauth2UserFilter)
    def filter = new IntegrationServicesOauth2UserFilter(userService, flexibleSearchService, hybrisOauth2UserFilter)

    def servletRequest = Stub(ServletRequest)
    def servletResponse = Mock(HttpServletResponse)
    def filterChain = Stub(FilterChain)

    @Test
    def "user is set in the session when auth is of type client credentials and client is configured with integration client credentials model"() {
        given:
        SecurityContextHolder.setContext(securityContext(oAuth2Authentication(PRINCIPAL, true)))

        def user = user(USER)
        flexibleSearchService.getModelByExample(_ as IntegrationClientCredentialsDetailsModel) >> integrationClientCredentials(user)

        when:
        filter.doFilter(servletRequest, servletResponse, filterChain)
        then:
        1 * userService.setCurrentUser(user)
    }

    @Test
    def "response is set to error when auth is of type client credentials and client is not configured with integration client credentials model"() {
        given:
        SecurityContextHolder.setContext(securityContext(oAuth2Authentication(PRINCIPAL, true)))

        flexibleSearchService.getModelByExample(_ as IntegrationClientCredentialsDetailsModel) >> { throw new ModelNotFoundException("not found") }

        when:
        filter.doFilter(servletRequest, servletResponse, filterChain)
        then:
        1 * servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN)
    }

    @Test
    def "response is set to FORBIDDEN with a message including the cause when the user is null"() {
        given:
        SecurityContextHolder.setContext(securityContext(oAuth2Authentication(PRINCIPAL, true)))

        flexibleSearchService.getModelByExample(_ as IntegrationClientCredentialsDetailsModel) >> {
            Stub(IntegrationClientCredentialsDetailsModel) {
                getUser() >> null
            }
        }

        when:
        filter.doFilter(servletRequest, servletResponse, filterChain)
        then:
        0 * userService.setCurrentUser()
        1 * servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN)
    }

    @Test
    @Unroll
    def "delegates to hybris oauth filter when #condition"() {
        given:
        SecurityContextHolder.setContext(securityContext(authentication))

        when:
        filter.doFilter(servletRequest, servletResponse, filterChain)
        then:
        1 * hybrisOauth2UserFilter.doFilter(servletRequest, servletResponse, filterChain)

        where:
        condition                                  | authentication
        "authentication is null"                   | null
        "authentication is not client credentials" | oAuth2Authentication(PRINCIPAL, false)
        "authentication is not Oauth2"             | Stub(AnonymousAuthenticationToken)
    }

    private SecurityContext securityContext(AbstractAuthenticationToken authentication) {
        Stub(SecurityContext) {
            getAuthentication() >> authentication
        }
    }

    private OAuth2Authentication oAuth2Authentication(String principal, boolean clientCredentials) {
        Stub(OAuth2Authentication) {
            getPrincipal() >> principal
            isClientOnly() >> clientCredentials

        }
    }

    private IntegrationClientCredentialsDetailsModel integrationClientCredentials(EmployeeModel user) {
        Stub(IntegrationClientCredentialsDetailsModel) {
            getUser() >> user
        }
    }

    private EmployeeModel user(String userName) {
        Stub(EmployeeModel) {
            getUid() >> userName
        }
    }
}
