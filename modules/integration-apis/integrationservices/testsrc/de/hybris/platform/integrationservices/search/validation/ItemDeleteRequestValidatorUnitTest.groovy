/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.security.AccessRightsService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ItemDeleteRequestValidatorUnitTest extends Specification {
    @Test
    def "validate does not throw an exception when the ItemSearchRequest is not set"() {
        given:
        def validator = new ItemDeleteRequestValidator(Stub(AccessRightsService))

        when:
        validator.validate(null)

        then:
        noExceptionThrown()
    }

    @Test
    def "validate re-throws exception when permissionService throws TypeAccessPermissionsException"() {
        given:
        def searchRequest = Stub(ItemSearchRequest) {
            getTypeDescriptor() >> Stub(TypeDescriptor)
        }
        and:
        def exception = new TypeAccessPermissionException('SomeType', 'delete')
        def permissionService = Stub(AccessRightsService) {
            checkDeletePermission(_) >> { throw exception }
        }

        def validator = new ItemDeleteRequestValidator(permissionService)

        when:
        validator.validate(searchRequest)

        then:
        def e = thrown TypeAccessPermissionException
        e.is exception
    }
}
