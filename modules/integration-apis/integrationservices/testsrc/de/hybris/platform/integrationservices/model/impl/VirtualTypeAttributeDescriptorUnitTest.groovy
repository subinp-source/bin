/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class VirtualTypeAttributeDescriptorUnitTest extends Specification {

    def factory = Stub(DescriptorFactory)
    def attributeModel = Stub(IntegrationObjectItemVirtualAttributeModel)
    def descriptor = new VirtualTypeAttributeDescriptor(attributeModel, factory)

    @Test
    def "attributeName is set on virtual attribute descriptor"() {
        given:
        attributeModel.getAttributeName() >> 'myTestName'

        expect:
        descriptor.getAttributeName() == 'myTestName'
    }

    @Test
    def "qualifier is set to virtual attribute's name"() {
        given:
        attributeModel.getAttributeName() >> 'myTestName'

        expect:
        descriptor.getQualifier() == 'myTestName'
    }

    @Test
    def "isCollection is always false for virtual attributes"() {
        expect:
        !descriptor.isCollection()
    }

    @Test
    def "virtual attribute with model of primitive type returns the attribute type"() {
        given:
        attributeModel.getRetrievalDescriptor() >> Stub(IntegrationObjectVirtualAttributeDescriptorModel) {
            getType() >> Stub(AtomicTypeModel) {
                getCode() >> "java.lang.String"
            }
        }

        expect:
        descriptor.getAttributeType().getTypeCode() == "java.lang.String"
    }

    @Test
    def "virtual attribute with model of non primitive type throws exception when accessing the attribute type"() {
        given:
        attributeModel.getRetrievalDescriptor() >> Stub(IntegrationObjectVirtualAttributeDescriptorModel) {
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> "MyNonPrimitiveType"
            }
        }

        when:
        descriptor.getAttributeType().getTypeCode()

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "typeCode is correctly populated from the model's type code"() {
        given:
        factory.createItemTypeDescriptor(_ as IntegrationObjectItemModel) >> Stub(TypeDescriptor) {
            getTypeCode() >> "MyTestType"
        }

        expect:
        descriptor.getTypeDescriptor().getTypeCode() == "MyTestType"
    }

    @Test
    def "reverse is always empty for virtual attributes"() {
        expect:
        descriptor.reverse().isEmpty()
    }

    @Test
    def "isNullable is always true for virtual attributes"() {
        expect:
        descriptor.isNullable()
    }

    @Test
    def "isPartOf is always false for virtual attributes"() {
        expect:
        !descriptor.isPartOf()
    }

    @Test
    def "isAutoCreate is always false for virtual attributes"() {
        expect:
        !descriptor.isAutoCreate()
    }

    @Test
    def "virtual attribute is not localized"() {
        expect:
        !descriptor.isLocalized()
    }

    @Test
    def "virtual attribute is always primitive"() {
        expect:
        descriptor.isPrimitive()
    }

    @Test
    def "virtual attribute is never a map"() {
        expect:
        !descriptor.isMap()
    }

    @Test
    def "virtual attribute is never writable"() {
        expect:
        !descriptor.isWritable()
    }

    @Test
    def "virtual attribute is never initializable"() {
        expect:
        !descriptor.isInitializable()
    }

    @Test
    def "virtual attribute is never settable"() {
        expect:
        !descriptor.isSettable()
    }

    @Test
    def "virtual attribute is never part of the key"() {
        expect:
        !descriptor.isKeyAttribute()
    }

    @Test
    def "virtual attributes never have a collection descriptor"() {
        expect:
        descriptor.getCollectionDescriptor() == null
    }

    @Test
    def "virtual attributes never have a map descriptor"() {
        expect:
        descriptor.getMapDescriptor().isEmpty()
    }

    @Test
    def 'accessor() is created from the provided factory'() {
        given:
        def accessorProducedByFactory = Stub AttributeValueAccessor
        def factory = Stub(DescriptorFactory) {
            getAttributeValueAccessorFactory() >> Stub(AttributeValueAccessorFactory) {
                create(_) >> accessorProducedByFactory
            }
        }

        expect:
        new VirtualTypeAttributeDescriptor(attributeModel, factory).accessor().is accessorProducedByFactory
    }

    @Test
    def 'accessor() can be retrieved even when the factory is not provided'() {
        given: 'factory is not provided in the constructor'
        def noFactoryDescriptor = new VirtualTypeAttributeDescriptor(attributeModel, null)

        expect: 'accessor is still retrieved and not null'
        noFactoryDescriptor.accessor()
    }

    @Test
    def "getTypeDescriptor throws exception when createItemTypeDescriptor throws exception"() {
        given:
        def factory = Stub(DescriptorFactory) {
            createItemTypeDescriptor(_ as IntegrationObjectItemModel) >> { throw new IllegalArgumentException() }
        }

        and:
        def descriptor = new VirtualTypeAttributeDescriptor(attributeModel, factory)

        when:
        descriptor.getTypeDescriptor()

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "equals() is #res when compared to #condition"() {
        given:
        def descriptor = typeAttributeDescriptor('Sample', 'id')

        expect:
        (descriptor == other) == res

        where:
        condition                     | other                                                   | res
        'has null type and attribute' | Stub(TypeAttributeDescriptor)                           | false
        'has different attribute'     | typeAttributeDescriptor('Sample', 'differentAttribute') | false
        'has different type'          | typeAttributeDescriptor('DifferentType', 'id')          | false
        'has same type and attribute' | typeAttributeDescriptor('Sample', 'id')                 | true
    }

    @Test
    def "equals() is true when comparing to the same object"() {
        given:
        def descriptor = typeAttributeDescriptor('Sample', 'id')

        expect:
        descriptor.equals(descriptor)
    }

    @Test
    @Unroll
    def "hashCode() are #resDesc when the other instance #condition"() {
        given:
        def descriptor = typeAttributeDescriptor('Sample', 'id')

        expect:
        (descriptor.hashCode() == other.hashCode()) == res

        where:
        condition                     | other                                                   | res   | resDesc
        'has null type and attribute' | Stub(TypeAttributeDescriptor)                           | false | 'not equal'
        'has different attribute'     | typeAttributeDescriptor('Sample', 'differentAttribute') | false | 'not equal'
        'has different type'          | typeAttributeDescriptor('DifferentType', 'id')          | false | 'not equal'
        'has same type and attribute' | typeAttributeDescriptor('Sample', 'id')                 | true  | 'equal'
    }

    @Test
    def "returns LogicLocation with assigned attributeModel"() {
        given:
        def validURI = "model://validURI"
        attributeModel.getRetrievalDescriptor() >> retrievalDescriptor(validURI)

        when:
        def logicLocation = descriptor.getLogicLocation()

        then:
        logicLocation == validURI
    }

    def retrievalDescriptor(String logicLocation) {
        Stub(IntegrationObjectVirtualAttributeDescriptorModel) {
            getLogicLocation() >> logicLocation
        }
    }

    def typeAttributeDescriptor(def itemCode, def attributeName) {
        def attributeModel = Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> attributeName
        }
        def descriptorFactory = Stub(DescriptorFactory) {
            createItemTypeDescriptor(_ as IntegrationObjectItemModel) >> Stub(TypeDescriptor) {
                getItemCode() >> itemCode
            }
        }
        new VirtualTypeAttributeDescriptor(attributeModel, descriptorFactory)
    }
}
