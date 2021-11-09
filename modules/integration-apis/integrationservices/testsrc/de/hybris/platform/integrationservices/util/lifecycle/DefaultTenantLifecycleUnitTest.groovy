/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.lifecycle

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.Tenant
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultTenantLifecycleUnitTest extends Specification {

    def tenant = Stub Tenant
    def tenantLifecycle = new DefaultTenantLifecycle(tenant)

    @Test
    def 'Instantiation fails if tenant is not provided'() {
        when:
        new DefaultTenantLifecycle(null)

        then:
        def e = thrown IllegalArgumentException
        e.message == 'Tenant must be provided.'
    }

    @Test
    def 'Tenant is not operational when it is initializing'() {
        given: 'An after initialization start event occurs'
        tenantLifecycle.onEvent Stub(AfterInitializationStartEvent)

        expect:
        !tenantLifecycle.isOperational()
    }

    @Test
    def 'Tenant is operational when it is done initializing'() {
        given: 'An after initialization end event occurs'
        tenantLifecycle.onEvent Stub(AfterInitializationEndEvent)

        expect:
        tenantLifecycle.isOperational()
    }

    @Test
    def 'Tenant is operational when it is done starting up'() {
        given: 'Tenant is started up'
        tenantLifecycle.afterTenantStartUp tenant

        expect:
        tenantLifecycle.isOperational()
    }

    @Test
    def "Tenant's operational state is not affected when another tenant is starting up"() {
        given: 'Tenant is shutting down'
        tenantLifecycle.beforeTenantShutDown tenant

        and: 'Another tenant is starting up'
        def anotherTenant = Stub Tenant
        tenantLifecycle.afterTenantStartUp anotherTenant

        expect:
        !tenantLifecycle.isOperational()
    }

    @Test
    def 'Tenant is not operational when it shutting down'() {
        given: 'Tenant is shutting down'
        tenantLifecycle.beforeTenantShutDown tenant

        expect:
        !tenantLifecycle.isOperational()
    }

    @Test
    def "Tenant's operational state is not affected when another tenant is shutting down"() {
        given: 'Tenant is started up'
        tenantLifecycle.afterTenantStartUp tenant

        and: 'Another tenant is shutting down'
        def anotherTenant = Stub Tenant
        tenantLifecycle.beforeTenantShutDown anotherTenant

        expect:
        tenantLifecycle.isOperational()
    }
}
