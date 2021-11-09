/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.security.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.config.IntegrationServicesConfiguration
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


@UnitTest
class DefaultIntegrationObjectMetadataPermissionServiceUnitTest extends Specification {

    private static final def ROOT_TYPE = "Product"
    private static final def INTEGRATION_OBJECT = "InboundProduct"

    @Shared
    def context = Stub(ODataContext)
    def contextIntegrationObject = Stub IntegrationObjectModel

    def permissionCRUDService = Stub(PermissionCRUDService)
    def serviceNameExtractor = Stub(ServiceNameExtractor) { extract(context) >> INTEGRATION_OBJECT }
    def integrationObjectService = Stub(IntegrationObjectService) {
        findIntegrationObject(INTEGRATION_OBJECT) >> contextIntegrationObject
    }
    def integrationServicesConfiguration = Stub(IntegrationServicesConfiguration)

    def defaultIntegrationObjectPermissionService = new DefaultIntegrationObjectMetadataPermissionService(
            permissionCRUDService,
            serviceNameExtractor,
            integrationObjectService,
            integrationServicesConfiguration)

    @Test
    @Unroll
    def "permits metadata access when there is no root type in an IO but #operation is granted for at least one type"() {
        given: 'access rights check is enabled'
        integrationServicesConfiguration.isAccessRightsEnabled() >> true
        and: 'the integration object has a couple item types but none of them is a root type'
        contextIntegrationObject.getRootItem() >> null
        contextIntegrationObject.getItems() >> [ioItem('Catalog'), ioItem('CatalogVersion')]
        and: 'CatalogVersion has at least permission granted'
        permissionCRUDService.canReadType("CatalogVersion") >> read
        permissionCRUDService.canCreateTypeInstance("CatalogVersion") >> create
        permissionCRUDService.canChangeType("CatalogVersion") >> change
        permissionCRUDService.canRemoveTypeInstance("CatalogVersion") >> remove
        and: 'Catalog has no permissions granted'
        permissionCRUDService.canReadType("Catalog") >> false
        permissionCRUDService.canCreateTypeInstance("Catalog") >> false
        permissionCRUDService.canChangeType("Catalog") >> false
        permissionCRUDService.canRemoveTypeInstance("Catalog") >> false

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        notThrown(TypeAccessPermissionException)

        where:
        operation | read  | create | change | remove
        'read'    | true  | false  | false  | false
        'create'  | false | true   | false  | false
        'change'  | false | false  | true   | false
        'remove'  | false | false  | false  | true
    }

    @Test
    def "check metadata read permission when access right check is enabled and #operation permission is granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canReadType(ROOT_TYPE) >> read
        permissionCRUDService.canCreateTypeInstance(ROOT_TYPE) >> create
        permissionCRUDService.canChangeType(ROOT_TYPE) >> change
        permissionCRUDService.canRemoveTypeInstance(ROOT_TYPE) >> remove
        and:
        contextIntegrationObject.getRootItem() >> ioItem(ROOT_TYPE)

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        notThrown(TypeAccessPermissionException)

        where:
        operation | read  | create | change | remove
        'read'    | true  | false  | false  | false
        'create'  | false | true   | false  | false
        'change'  | false | false  | true   | false
        'remove'  | false | false  | false  | true
    }

    @Test
    def "check metadata read permission when access right check is enabled and permission is not granted"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canReadType(ROOT_TYPE) >> false
        permissionCRUDService.canCreateTypeInstance(ROOT_TYPE) >> false
        permissionCRUDService.canChangeType(ROOT_TYPE) >> false
        permissionCRUDService.canRemoveTypeInstance(ROOT_TYPE) >> false
        and:
        contextIntegrationObject.getRootItem() >> ioItem(ROOT_TYPE)

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        def e = thrown TypeAccessPermissionException
        e.message.contains ROOT_TYPE
    }

    @Test
    def "check metadata read permission when access right check is enabled and permission is not granted and ROOT_TYPE is null"() {
        given:
        integrationServicesConfiguration.accessRightsEnabled >> true
        permissionCRUDService.canReadType(null) >> false
        permissionCRUDService.canCreateTypeInstance(null) >> false
        permissionCRUDService.canChangeType(null) >> false
        permissionCRUDService.canRemoveTypeInstance(null) >> false
        and:
        contextIntegrationObject.getRootItem() >> ioItem(null)

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        def e = thrown TypeAccessPermissionException
        e.itemType == null
        e.message.contains 'Integration Object'
    }

    @Test
    def "permits metadata access when access right check is disabled even if no permissions granted"() {
        given: 'access rights check is disabled'
        integrationServicesConfiguration.isAccessRightsEnabled() >> false
        and: 'no permissions granted'
        permissionCRUDService.canReadType(_ as String) >> false
        permissionCRUDService.canCreateTypeInstance(_ as String) >> false
        permissionCRUDService.canChangeType(_ as String) >> false
        permissionCRUDService.canRemoveTypeInstance(_ as String) >> false

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        notThrown(TypeAccessPermissionException)
    }

    @Test
    def "denies metadata access when there is no root type in the IO and no types with at least one permission granted"() {
        given: 'access rights check is enabled'
        integrationServicesConfiguration.isAccessRightsEnabled() >> true
        and: 'the integration object has a couple item types but none of them is a root type'
        contextIntegrationObject.getRootItem() >> null
        contextIntegrationObject.getItems() >> [ioItem("Product"), ioItem("Catalog")].toSet()
        and: 'none of the types have access permissions'
        permissionCRUDService.canReadType(_ as String) >> false
        permissionCRUDService.canCreateTypeInstance(_ as String) >> false
        permissionCRUDService.canChangeType(_ as String) >> false
        permissionCRUDService.canRemoveTypeInstance(_ as String) >> false

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        thrown TypeAccessPermissionException
    }

    @Test
    def "denies metadata access when the integration object does not contain any items"() {
        given: 'access right check is enabled'
        integrationServicesConfiguration.isAccessRightsEnabled() >> true
        and: 'the integration object does not contain a single item type'
        contextIntegrationObject.getRootItem() >> null
        contextIntegrationObject.getItems() >> [].toSet()

        when:
        defaultIntegrationObjectPermissionService.checkMetadataPermission(context)

        then:
        thrown TypeAccessPermissionException
    }

    private IntegrationObjectItemModel ioItem(final String typeCode) {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> typeCode
            }
            getCode() >> typeCode
        }
    }

}
