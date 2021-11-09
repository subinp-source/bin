/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.security.AccessRightsService
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ItemPersistRequestPermissionValidatorUnitTest extends Specification {
    private static final CONTEXT_TYPE = 'SomeType'

    def modelService = Stub(ModelService)
    def accessRightsService = Stub(AccessRightsService)
    def validator = new ItemPersistRequestPermissionValidator(accessRightsService, modelService)

    @Test
    @Unroll
    def "validate does not throw an exception when the ItemSearchRequest is not set"() {
        when:
        validator.validate(persistenceContext, item)

        then:
        noExceptionThrown()

        where:
        msg                  | persistenceContext       | item
        "item"               | Stub(PersistenceContext) | null
        "persistenceContext" | null                     | Stub(ItemModel)
    }

    @Test
    def "validate re-throws exception when permissionService checkCreatePermission throws TypeAccessPermissionsException"() {
        given:
        modelService.isNew(_) >> true
        and:
        def exception = new TypeAccessPermissionException(CONTEXT_TYPE, 'create')
        accessRightsService.checkCreatePermission(CONTEXT_TYPE) >> { throw exception }

        when:
        validator.validate(Stub(PersistenceContext), item())

        then: 'the exception is propagated or rethrown'
        def e = thrown TypeAccessPermissionException
        e.is exception
    }

    @Test
    def "validate re-throws exception when permissionService checkUpdatePermission throws TypeAccessPermissionsException"() {
        given:
        modelService.isNew(_) >> false
        modelService.isModified(_) >> true
        and:
        def exception = new TypeAccessPermissionException(CONTEXT_TYPE, 'update')
        accessRightsService.checkUpdatePermission(CONTEXT_TYPE) >> { throw exception }

        when:
        validator.validate(Stub(PersistenceContext), item())

        then: 'the exception is propagated or rethrown'
        def e = thrown TypeAccessPermissionException
        e.is exception
    }

    @Test
    def "validate does not throw exception when permissionService is not new but has not been modified"() {
        given:
        modelService.isNew(_) >> false
        modelService.isModified(_) >> false
        and:
        accessRightsService.checkUpdatePermission(CONTEXT_TYPE) >> { throw new TypeAccessPermissionException(CONTEXT_TYPE, 'update') }

        when:
        validator.validate(Stub(PersistenceContext), item())

        then:
        notThrown(TypeAccessPermissionException)
    }

    ItemModel item() {
        Stub(ItemModel) {
            getItemtype() >> CONTEXT_TYPE
        }
    }
}
