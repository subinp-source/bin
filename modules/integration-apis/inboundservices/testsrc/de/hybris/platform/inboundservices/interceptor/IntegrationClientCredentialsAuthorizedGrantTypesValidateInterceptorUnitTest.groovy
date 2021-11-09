/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationClientCredentialsAuthorizedGrantTypesValidateInterceptorUnitTest extends Specification {
    private static final Set<String> AUTHORIZED_GRANT_TYPES_DEFAULT_VALUE = ["client_credentials"] as Set

    def interceptor = new IntegrationClientCredentialsAuthorizedGrantTypesValidateInterceptor()

    @Test
    @Unroll
    def "throws exception when authorizedGrantTypes is not equal to the default value"() {
        when:
        interceptor.onValidate clientCredentials, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Cannot create IntegrationClientCredentialsDetails with a provided value for authorizedGrantTypes."

        where:
        clientCredentials << [clientCredentialsDetailsWithAuthorizedGrantTypes(["password"] as Set), clientCredentialsDetailsWithAuthorizedGrantTypes(['client_credentials', 'password'] as Set)]

    }

    @Test
    def "does not throw an exception when authorizedGrantTypes is equal to the default value"() {
        when:
        interceptor.onValidate clientCredentialsDetailsWithAuthorizedGrantTypes(AUTHORIZED_GRANT_TYPES_DEFAULT_VALUE), Stub(InterceptorContext)

        then:
        noExceptionThrown()
    }

    private IntegrationClientCredentialsDetailsModel clientCredentialsDetailsWithAuthorizedGrantTypes(final Set grantTypes) {
        Stub(IntegrationClientCredentialsDetailsModel) {
            getAuthorizedGrantTypes() >> grantTypes
        }
    }
}
