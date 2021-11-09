/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationClientCredentialsUserValidateInterceptorUnitTest extends Specification {

    def interceptor = new IntegrationClientCredentialsUserValidateInterceptor()

    @Test
    def "throws error message if user is admin"() {
        given:
        def integrationClientCredentials = Stub(IntegrationClientCredentialsDetailsModel) {
            getUser() >> userWithUid("admin")
        }

        when:
        interceptor.onValidate integrationClientCredentials, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Cannot create IntegrationClientCredentialsDetails with admin user."
    }

    @Test
    @Unroll
    def "no error thrown if user is not admin"() {
        given:
        def integrationClientCredentials = Stub(IntegrationClientCredentialsDetailsModel) {
            getUser() >> user
        }

        when:
        interceptor.onValidate integrationClientCredentials, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        user << [userWithUid("user"), null]
    }

    def userWithUid(final String name) {
        Stub(EmployeeModel) {
            getUid() >> name
        }
    }
}