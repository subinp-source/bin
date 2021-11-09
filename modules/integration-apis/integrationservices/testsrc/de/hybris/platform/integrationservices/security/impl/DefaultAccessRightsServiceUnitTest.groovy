/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.security.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.config.IntegrationServicesConfiguration
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultAccessRightsServiceUnitTest extends Specification {

    private static final def ITEM_TYPE = "Product"


    def permissionCRUDService = Mock(PermissionCRUDService)

    def integrationServicesConfiguration = Stub(IntegrationServicesConfiguration)

    def defaultAccessRightsService = new DefaultAccessRightsService(
            permissionCRUDService,
            integrationServicesConfiguration)

    @Test
    def "check create permission when access right check is enabled and permission is granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canCreateTypeInstance(ITEM_TYPE) >> true

        when:
        defaultAccessRightsService.checkCreatePermission(ITEM_TYPE)

        then:
        notThrown(TypeAccessPermissionException)
    }

    @Test
    def "check create permission when access right check is enabled and permission is not granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canCreateTypeInstance(ITEM_TYPE) >> false

        when:
        defaultAccessRightsService.checkCreatePermission(ITEM_TYPE)

        then:
        def e = thrown TypeAccessPermissionException
        e.itemType == ITEM_TYPE
        e.permission == 'create'
    }

    @Test
    @Unroll
    def "check create permission when access right check is not enabled and permission #verb granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> false

        when:
        defaultAccessRightsService.checkCreatePermission(ITEM_TYPE)

        then:
        0 * permissionCRUDService.canCreateTypeInstance(ITEM_TYPE) >> permssionGranted

        where:
        verb     | permssionGranted
        'is'     | true
        'is not' | false
    }

    @Test
    def "check update permission when access right check is enabled and permission is granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canChangeType(ITEM_TYPE) >> true

        when:
        defaultAccessRightsService.checkUpdatePermission(ITEM_TYPE)

        then:
        notThrown(TypeAccessPermissionException)
    }

    @Test
    def "check update permission when access right check is enabled and permission is not granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canChangeType(ITEM_TYPE) >> false

        when:
        defaultAccessRightsService.checkUpdatePermission(ITEM_TYPE)

        then:
        def e = thrown TypeAccessPermissionException
        e.itemType == ITEM_TYPE
        e.permission == 'change'
    }

    @Test
    @Unroll
    def "check update permission when access right check is not enabled and permission #verb granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> false

        when:
        defaultAccessRightsService.checkUpdatePermission(ITEM_TYPE)

        then:
        0 * permissionCRUDService.canChangeType(ITEM_TYPE) >> permssionGranted

        where:
        verb     | permssionGranted
        'is'     | true
        'is not' | false
    }

    @Test
    def "check read permission when access right check is enabled and permission is granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canReadType(ITEM_TYPE) >> true

        when:
        defaultAccessRightsService.checkReadPermission(ITEM_TYPE)

        then:
        notThrown(TypeAccessPermissionException)
    }

    @Test
    def "check read permission when access right check is enabled and permission is not granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canReadType(ITEM_TYPE) >> false

        when:
        defaultAccessRightsService.checkReadPermission(ITEM_TYPE)

        then:
        def e = thrown TypeAccessPermissionException
        e.itemType == ITEM_TYPE
        e.permission == 'read'
    }

    @Test
    @Unroll
    def "check read permission when access right check is not enabled and permission #verb granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> false

        when:
        defaultAccessRightsService.checkReadPermission(ITEM_TYPE)

        then:
        0 * permissionCRUDService.canReadType(ITEM_TYPE) >> permssionGranted

        where:
        verb     | permssionGranted
        'is'     | true
        'is not' | false
    }

    @Test
    def "check delete permission when access right check is enabled and permission is granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canRemoveTypeInstance(ITEM_TYPE) >> true

        when:
        defaultAccessRightsService.checkDeletePermission(ITEM_TYPE)

        then:
        notThrown(TypeAccessPermissionException)
    }

    @Test
    def "check delete permission when access right check is enabled and permission is not granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canRemoveTypeInstance(ITEM_TYPE) >> false

        when:
        defaultAccessRightsService.checkDeletePermission(ITEM_TYPE)

        then:
        def e = thrown TypeAccessPermissionException
        e.itemType == ITEM_TYPE
        e.permission == 'remove'
    }

    @Test
    @Unroll
    def "check delete permission when access right check is not enabled and permission #verb granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> false

        when:
        defaultAccessRightsService.checkDeletePermission(ITEM_TYPE)

        then:
        0 * permissionCRUDService.canRemoveTypeInstance(ITEM_TYPE) >> permssionGranted

        where:
        verb     | permssionGranted
        'is'     | true
        'is not' | false
    }
}
