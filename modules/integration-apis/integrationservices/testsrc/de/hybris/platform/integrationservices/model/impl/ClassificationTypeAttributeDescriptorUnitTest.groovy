/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.core.HybrisEnumValue
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeSettableChecker
import de.hybris.platform.integrationservices.model.AttributeSettableCheckerFactory
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum.NOT_VISIBLE
import static de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum.VISIBLE
import static de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum.VISIBLE_IN_BASE
import static de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum.VISIBLE_IN_VARIANT

@UnitTest
class ClassificationTypeAttributeDescriptorUnitTest extends Specification {

    def classAttributeAssignment = Stub(ClassAttributeAssignmentModel)
    def attributeModel = Stub(IntegrationObjectItemClassificationAttributeModel) {
        getClassAttributeAssignment() >> classAttributeAssignment
    }
    def descriptor = new ClassificationTypeAttributeDescriptor(attributeModel)

    @Test
    def "name is set on classification attribute descriptor"() {
        given:
        attributeModel.getAttributeName() >> 'weight'

        expect:
        descriptor.getAttributeName() == 'weight'
    }

    @Test
    def "qualifier is set to classification attribute's code"() {
        given:
        classAttributeAssignment.getClassificationAttribute() >> Stub(ClassificationAttributeModel) {
            getCode() >> 'classificationIdentifier'
        }

        expect:
        descriptor.getQualifier() == 'classificationIdentifier'
    }

    @Test
    def "isPartOf is always false for classification attributes"() {
        expect:
        !descriptor.isPartOf()
    }

    @Test
    @Unroll
    def "isAutoCreate is #expectedValue when classification attribute model autocreate=#autoCreateVal"() {
        given:
        attributeModel.getAutoCreate() >> autoCreateVal

        expect:
        descriptor.isAutoCreate() == expectedValue

        where:
        autoCreateVal | expectedValue
        true          | true
        false         | false
        null          | false
    }

    @Test
    @Unroll
    def "classification attribute is #nullable"() {
        given:
        classAttributeAssignment.getMandatory() >> val

        expect:
        descriptor.isNullable() != val

        where:
        nullable       | val
        'nullable'     | false
        'not nullable' | true
    }

    @Test
    @Unroll
    def "classification attribute of type #enumType isPrimitive returns #result"() {
        given:
        classAttributeAssignment.getAttributeType() >> enumType

        expect:
        descriptor.isPrimitive() == result

        where:
        enumType                                  | result
        ClassificationAttributeTypeEnum.REFERENCE | false
        ClassificationAttributeTypeEnum.STRING    | true
        ClassificationAttributeTypeEnum.NUMBER    | true
        ClassificationAttributeTypeEnum.BOOLEAN   | true
        ClassificationAttributeTypeEnum.DATE      | true
        ClassificationAttributeTypeEnum.ENUM      | true
    }

    @Test
    def "classification attribute is never part of the key"() {
        expect:
        !descriptor.isKeyAttribute()
    }

    @Test
    def "type descriptor is set on attribute descriptor"() {
        given:
        attributeModel.getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel)

        expect:
        descriptor.typeDescriptor
    }

    @Test
    @Unroll
    def "classification attribute is #scenario"() {
        given:
        classAttributeAssignment.getLocalized() >> val

        expect:
        descriptor.isLocalized() == val

        where:
        scenario        | val
        'localized'     | true
        'not localized' | false
    }

    @Test
    @Unroll
    def "classification attribute is #collection"() {
        given:
        classAttributeAssignment.getMultiValued() >> multiValue

        expect:
        descriptor.isCollection() == collectionVal

        where:
        collection         | multiValue | collectionVal
        'collection'       | true       | true
        'not a collection' | false      | false
        'not a collection' | null       | false
    }

    @Test
    @Unroll
    def "classification attribute type is #enumType"() {
        given:
        classAttributeAssignment.getAttributeType() >> enumType
        and:
        attributeModel.getIntegrationObjectItem() >> stubIntegrationObjectItem(ioCode)

        expect:
        descriptor.attributeType.typeCode == type
        descriptor.attributeType.integrationObjectCode == ioCode

        where:
        enumType                                | ioCode | type
        ClassificationAttributeTypeEnum.DATE    | 'IO1'  | 'java.util.Date'
        ClassificationAttributeTypeEnum.BOOLEAN | 'IO2'  | 'java.lang.Boolean'
        ClassificationAttributeTypeEnum.NUMBER  | 'IO3'  | 'java.lang.Double'
        ClassificationAttributeTypeEnum.STRING  | 'IO4'  | 'java.lang.String'
        ClassificationAttributeTypeEnum.ENUM    | 'IO5'  | 'java.lang.String'
    }

    @Test
    def "getAttributeType throws exception when createItemTypeDescriptor throws exception"() {
        given:
        def model = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getAttributeType() >> ClassificationAttributeTypeEnum.REFERENCE
            }
        }
        def factory = Stub(DescriptorFactory) {
            createItemTypeDescriptor(_ as IntegrationObjectItemModel) >> { throw new IllegalArgumentException() }
        }

        and:
        def descriptor = new ClassificationTypeAttributeDescriptor(model, factory)

        when:
        descriptor.getAttributeType()

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "correctly derives IO and item codes for item reference attributes"() {
        given:
        classAttributeAssignment.getAttributeType() >> ClassificationAttributeTypeEnum.REFERENCE
        and:
        def expectedIOCode = 'IO5'
        def expectedItemCode = 'ReturnItemCode'
        attributeModel.getIntegrationObjectItem() >> stubIntegrationObjectItem(expectedIOCode)
        attributeModel.getReturnIntegrationObjectItem() >> stubIntegrationObjectItem(expectedIOCode, expectedItemCode)

        when:
        def typeDescriptor = descriptor.getAttributeType()

        then:
        with(typeDescriptor) {
            itemCode == expectedItemCode
            integrationObjectCode == expectedIOCode
        }
    }

    @Test
    def "classification attribute is never a map"() {
        expect:
        !descriptor.isMap()
    }

    @Test
    def "classification attribute is always writable"() {
        expect:
        descriptor.isWritable()
    }

    @Test
    def "classification attribute is always initializable"() {
        expect:
        descriptor.isInitializable()
    }

    @Unroll
    @Test
    def "classification attribute is readable when attribute assignment visibility is '#enumValue'"() {
        given:
        classAttributeAssignment.visibility >> enumValue

        expect:
        descriptor.isReadable()

        where:
        enumValue << [VISIBLE, VISIBLE_IN_BASE, VISIBLE_IN_VARIANT]
    }

    @Test
    def "classification attribute is not readable when attribute assignment visibility is set to 'NOT_VISIBLE'"() {
        given:
        classAttributeAssignment.visibility >> NOT_VISIBLE

        expect:
        !descriptor.isReadable()
    }

    @Test
    def 'accessor() can be retrieved even when the factory is not provided'() {
        given: 'factory is not provided in the constructor'
        def descriptor = new ClassificationTypeAttributeDescriptor(Stub(IntegrationObjectItemClassificationAttributeModel))

        expect: 'accessor is still retrieved and not null'
        descriptor.accessor()
    }

    @Test
    def 'accessor() is created from the provided factory'() {
        given:
        def model = Stub IntegrationObjectItemClassificationAttributeModel
        and:
        def accessorProducedByFactory = Stub AttributeValueAccessor
        def factory = Stub(DescriptorFactory) {
            getAttributeValueAccessorFactory() >> Stub(AttributeValueAccessorFactory) {
                create(_) >> accessorProducedByFactory
            }
        }

        expect:
        new ClassificationTypeAttributeDescriptor(model, factory).accessor().is accessorProducedByFactory
    }

    @Test
    @Unroll
    def "getCollectionDescriptor() for #scenario"() {
        given:
        classAttributeAssignment.getMultiValued() >> isMultiVal

        when:
        def collectionDescriptor = descriptor.getCollectionDescriptor()

        then:
        collectionDescriptor.newCollection() == val

        where:
        scenario                 | isMultiVal | val
        'multivalued attribute'  | true       | []
        'single value attribute' | false      | null
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

    private def stubIntegrationObjectItem(String ioCode, String itemCode = 'integrationObjectItemCode') {
        Stub(IntegrationObjectItemModel) {
            getCode() >> itemCode
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> ioCode
            }
        }
    }

    @Test
    @Unroll
    def "isSettable returns #result when settable checker returns #isSettable"() {
        given:
        def item = Stub ItemModel
        and: "attribute settable checker that return true"
        def settableChecker = Stub(AttributeSettableChecker) {
            isSettable(item, _ as TypeAttributeDescriptor) >> settable
        }
        and: "descriptor factory that returns the class attribute settable checker"
        def factory = factoryWithSettableChecker settableChecker
        and:
        def descriptor = new ClassificationTypeAttributeDescriptor(Stub(IntegrationObjectItemClassificationAttributeModel), factory)

        expect:
        descriptor.isSettable(item) == result

        where:
        settable | result
        true     | true
        false    | false
    }

    @Test
    def "isSettable is not delegated to settable checker when item is not of type ItemModel and settable=false"() {
        given:
        def settableChecker = Mock AttributeSettableChecker
        and: "descriptor factory that returns the class attribute settable checker"
        def factory = factoryWithSettableChecker settableChecker
        and:
        def descriptor = new ClassificationTypeAttributeDescriptor(Stub(IntegrationObjectItemClassificationAttributeModel), factory)
        and:
        def item = Stub HybrisEnumValue

        when:
        def settable = descriptor.isSettable(item)

        then:
        !settable
        and:
        0 * settableChecker.isSettable(item, descriptor)
    }

    @Test
    def 'getMapDescriptor() is always empty because classification attributes do not support Map values'() {
        expect:
        descriptor.getMapDescriptor().empty
    }

    private DescriptorFactory factoryWithSettableChecker(settableChecker) {
        Stub(DescriptorFactory) {
            getAttributeSettableCheckerFactory() >> Stub(AttributeSettableCheckerFactory) {
                create(_ as ClassificationTypeAttributeDescriptor) >> settableChecker
            }
        }
    }

    def typeAttributeDescriptor(def itemCode, def attributeName) {
        def attributeModel = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getAttributeName() >> attributeName
        }
        def descriptorFactory = Stub(DescriptorFactory) {
            createItemTypeDescriptor(_ as IntegrationObjectItemModel) >> Stub(TypeDescriptor) {
                getItemCode() >> itemCode
            }
        }
        new ClassificationTypeAttributeDescriptor(attributeModel, descriptorFactory)
    }
}
