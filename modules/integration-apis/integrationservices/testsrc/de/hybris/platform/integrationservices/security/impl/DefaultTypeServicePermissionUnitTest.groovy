package de.hybris.platform.integrationservices.security.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor
import de.hybris.platform.integrationservices.model.impl.MapTypeDescriptor
import de.hybris.platform.integrationservices.model.impl.PrimitiveTypeDescriptor
import de.hybris.platform.integrationservices.security.AccessRightsService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultTypeServicePermissionUnitTest extends Specification {

    private static final def ITEM_TYPE = "Product"
    private static final def PRIMITIVE_TYPE = "Boolean"
    private static final def MAP_TYPE = "String2StringMapType"

    def accessRightsService = Mock(AccessRightsService)

    def defaultTypePermissionService = new DefaultTypePermissionService(accessRightsService)

    @Test
    def "checks for read permission for a TypeDescriptor that is an instance of ItemTypeDescriptor"() {
        given: "an itemTypeDescriptor"
        def itemTypeDescriptor = itemTypeDescriptor(ITEM_TYPE)

        when:
        defaultTypePermissionService.checkReadPermission(itemTypeDescriptor)

        then: "the checkReadPermission should be executed for an ItemTypeDescriptor"
        1 * accessRightsService.checkReadPermission("Product")
    }

    @Test
    @Unroll
    def "checks for read permission not executed for a TypeDescriptor that is an instance of MapTypeDescriptor or PrimitiveTypeDescriptor"() {
        given: "a none ItemtypeDescriptor"
        def typeDescriptor = noneItemTypeDescriptor

        when:
        defaultTypePermissionService.checkReadPermission(typeDescriptor)

        then: "the checkReadPermission should not be executed for a MapTypeDescriptor"
        0 * accessRightsService.checkReadPermission(typeCode)

        where:
        noneItemTypeDescriptor                  | typeCode
        mapTypeDescriptor(MAP_TYPE)             | "String2StringMapType"
        primitiveTypeDescriptor(PRIMITIVE_TYPE) | "Boolean"
    }

    private TypeDescriptor primitiveTypeDescriptor(String navPropertyType) {
        Stub(TypeDescriptor) {
            isPrimitive() >> true
            isMap() >> false
            getTypeCode() >> navPropertyType
        }
    }

    private TypeDescriptor itemTypeDescriptor(String navPropertyType) {
        Stub(TypeDescriptor) {
            isPrimitive() >> false
            isMap() >> false
            getTypeCode() >> navPropertyType
        }
    }

    private TypeDescriptor mapTypeDescriptor(String navPropertyType) {
        Stub(TypeDescriptor) {
            isPrimitive() >> false
            isMap() >> true
            getTypeCode() >> navPropertyType
        }
    }
}
