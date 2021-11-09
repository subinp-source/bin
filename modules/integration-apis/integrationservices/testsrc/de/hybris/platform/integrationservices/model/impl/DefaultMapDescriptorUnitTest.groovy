/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultMapDescriptorUnitTest extends Specification {
    private static final String IO_CODE = 'TestIO'

    @Test
    def 'cannot be instantiated with null attribute model'() {
        when:
        new DefaultMapDescriptor(null)

        then:
        def e = thrown IllegalArgumentException
        e.message
    }

    @Test
    def 'cannot be instantiated with model with null Attribute Descriptor'() {
        given:
        def attribute = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> null
        }

        when:
        new DefaultMapDescriptor(attribute)

        then:
        def e = thrown IllegalArgumentException
        e.message == 'Attribute descriptor cannot be null'
    }

    @Test
    @Unroll
    def "cannot be instantiated for an attribute of #typeName type"() {
        given: "an attribute model of #typeName type"
        def attribute = attributeOfType(type)

        when:
        new DefaultMapDescriptor(attribute)

        then:
        def e = thrown IllegalArgumentException
        e.message.contains 'MapType'

        where:
        typeName     | type
        'primitive'  | Stub(AtomicTypeModel)
        'collection' | Stub(CollectionTypeModel)
        'composed'   | Stub(ComposedTypeModel)
    }

    @Test
    @Unroll
    def "cannot be instantiated when the Map key type is a #typeName"() {
        given: "an attribute of Map type with #typeName key"
        def attribute = attributeOfType Stub(MapTypeModel) {
            getArgumentType() >> argType
            getReturntype() >> atomicType('AnyAtomicType')
        }

        when:
        new DefaultMapDescriptor(attribute)

        then:
        thrown IllegalArgumentException

        where:
        typeName                       | argType
        'collection of primitives'     | collectionWithElementsOf(atomicType('java.lang.String'))
        'collection of composed types' | collectionWithElementsOf(Stub(ComposedTypeModel))
        'composed type'                | Stub(ComposedTypeModel)
        'map type'                     | Stub(MapTypeModel)
    }

    @Test
    @Unroll
    def "cannot be instantiated when the Map value type is a #typeName"() {
        given: "an attribute of Map type with #typeName value"
        def attribute = attributeOfType Stub(MapTypeModel) {
            getArgumentType() >> atomicType('AnyAtomicType')
            getReturntype() >> argType
        }

        when:
        new DefaultMapDescriptor(attribute)

        then:
        thrown IllegalArgumentException

        where:
        typeName                       | argType
        'collection of primitives'     | collectionWithElementsOf(atomicType('java.lang.String'))
        'collection of composed types' | collectionWithElementsOf(Stub(ComposedTypeModel))
        'composed type'                | Stub(ComposedTypeModel)
        'map type'                     | Stub(MapTypeModel)
    }

    @Test
    def 'provides type descriptor for the map key of primitive type'() {
        given: 'an attribute of Map type having Integer key'
        def attribute = attributeOfType Stub(MapTypeModel) {
            getArgumentType() >> atomicType('Integer')
            getReturntype() >> atomicType('String')
        }
        def mapDescriptor = new DefaultMapDescriptor(attribute)

        expect:
        mapDescriptor.keyType.integrationObjectCode == IO_CODE
        mapDescriptor.keyType.typeCode == 'Integer'
    }

    @Test
    def 'provides type descriptor for the map value'() {
        given: 'an attribute of Map type having String value'
        def attribute = attributeOfType Stub(MapTypeModel) {
            getArgumentType() >> atomicType('Integer')
            getReturntype() >> atomicType('String')
        }
        def mapDescriptor = new DefaultMapDescriptor(attribute)

        expect:
        mapDescriptor.valueType.integrationObjectCode == IO_CODE
        mapDescriptor.valueType.typeCode == 'String'
    }

    private CollectionTypeModel collectionWithElementsOf(TypeModel elType) {
        Stub(CollectionTypeModel) {
            getElementType() >> elType
        }
    }

    private AtomicTypeModel atomicType(String typeCode) {
        Stub(AtomicTypeModel) {
            getCode() >> typeCode
        }
    }

    private IntegrationObjectItemAttributeModel attributeOfType(TypeModel mapType) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> mapType
            }
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getIntegrationObject() >> Stub(IntegrationObjectModel) {
                    getCode() >> IO_CODE
                }
            }
        }
    }
}
