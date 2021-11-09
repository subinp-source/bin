/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.HybrisEnumValue
import de.hybris.platform.core.enums.TypeOfCollectionEnum
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationservices.model.AttributeSettableChecker
import de.hybris.platform.integrationservices.model.AttributeSettableCheckerFactory
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.AttributeValueAccessorFactory
import de.hybris.platform.integrationservices.model.BaseMockAttributeDescriptorModelBuilder
import de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.KeyDescriptor
import de.hybris.platform.integrationservices.model.MockItemAttributeModelBuilder
import de.hybris.platform.integrationservices.model.MockRelationDescriptorModelBuilder
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.IntegrationObjectItemBuilder.item
import static de.hybris.platform.integrationservices.model.BaseMockAttributeDescriptorModelBuilder.attributeDescriptor
import static de.hybris.platform.integrationservices.model.BaseMockAttributeDescriptorModelBuilder.collectionDescriptor
import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.collectionAttributeBuilder
import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.complexRelationAttributeBuilder
import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.oneToOneRelationAttributeBuilder
import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.simpleAttributeBuilder
import static de.hybris.platform.integrationservices.model.MockIntegrationObjectItemModelBuilder.itemModelBuilder
import static de.hybris.platform.integrationservices.model.MockMapAttributeDescriptorModelBuilder.mapAttributeDescriptor
import static de.hybris.platform.integrationservices.model.MockRelationAttributeDescriptorModelBuilder.relationAttribute
import static de.hybris.platform.integrationservices.model.MockRelationDescriptorModelBuilder.oneToOneRelation

@UnitTest
class DefaultTypeAttributeDescriptorUnitTest extends Specification {
    @Test
    def "creates new DefaultTypeAttributeDescriptor instance for a given attribute descriptor"() {
        given:
        def model = simpleAttributeBuilder()
                .withName("MyAttribute")
                .withAttributeDescriptor(attributeDescriptor().withQualifier('SomeAttribute'))
                .build()

        expect:
        def descriptor = DefaultTypeAttributeDescriptor.create(model)
        descriptor.attributeName == 'MyAttribute'
        descriptor.qualifier == 'SomeAttribute'
    }

    @Test
    def "fails to create new DefaultTypeAttributeDescriptor when provided attribute descriptor is null"() {
        when:
        DefaultTypeAttributeDescriptor.create(null)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "isCollection() is #res when the attribute is a #type attribute"() {
        given:
        def descriptor = typeAttributeDescriptor(attr)

        expect:
        descriptor.collection == res

        where:
        attr                        | type                            | res
        attributeDescriptor()       | 'simple'                        | false
        oneToOneRelation()          | 'one-to-one relation'           | false
        oneToManyRelation()         | 'one-to-many relation'          | true
        reversedOneToManyRelation() | 'reversed one-to-many relation' | true
        collectionDescriptor()      | 'collection'                    | true
    }

    @Test
    def "getCollectionDescriptor() returns a collection descriptor"() {
        given:
        def descriptor = collectionAttributeOfType TypeOfCollectionEnum.COLLECTION

        expect:
        descriptor.getCollectionDescriptor() != null
    }

    @Test
    @Unroll
    def "getAttributeType() correctly determines referenced item type for a #desc attribute"() {
        given:
        def descriptor = typeAttributeDescriptor(attr)

        expect:
        descriptor.attributeType?.itemCode == attrType

        where:
        attrType   | desc         | attr
        'SomeItem' | 'reference'  | simpleAttributeBuilder()
                .withIntegrationObjectItem(item().withIntegrationObjectCode('Test'))
                .withReturnIntegrationObject(attrType)
        'Integer'  | 'collection' | collectionAttributeBuilder()
                .withIntegrationObjectItem(item().withIntegrationObjectCode('Test'))
                .withAttributeDescriptor(collectionDescriptor().withPrimitiveTarget(attrType))
        'Child'    | 'relation'   | complexRelationAttributeBuilder().withReturnIntegrationObject(attrType)
    }

    @Test
    def "getAttributeType() uses container item integration object code for the primitive attribute type"() {
        given:
        def descriptor = typeAttributeDescriptor(simpleAttributeBuilder()
                .withIntegrationObjectItem(item().withIntegrationObjectCode('IntegrationObject'))
                .withAttributeDescriptor(attributeDescriptor().withPrimitiveTypeCode("java.lang.String")))

        expect:
        with(descriptor) {
            attributeType instanceof PrimitiveTypeDescriptor
            attributeType.integrationObjectCode == 'IntegrationObject'
        }
    }

    @Test
    def "getAttributeType() uses container item integration object code for the map attribute type"() {
        given:
        def descriptor = typeAttributeDescriptor(simpleAttributeBuilder()
                .withIntegrationObjectItem(item().withIntegrationObjectCode('IntegrationObject'))
                .withAttributeDescriptor(attributeDescriptor().withType(MapTypeModel.class)))

        expect:
        with(descriptor) {
            attributeType instanceof MapTypeDescriptor
            attributeType.integrationObjectCode == 'IntegrationObject'
        }
    }

    @Test
    def "getAttributeType() reports illegal state when referenced type is not Atomic and returnIntegrationObject is not modeled"() {
        given:
        def descriptor = typeAttributeDescriptor(collectionAttributeBuilder()
                .withIntegrationObjectItem(item().withIntegrationObjectCode('IntegrationObject'))
                .withAttributeDescriptor(collectionDescriptor().withTarget("Product")))

        when:
        descriptor.getAttributeType()

        then:
        thrown(IllegalStateException)
    }

    @Test
    def "getAttributeType() is cached"() {
        def attributeDescriptor = typeAttributeDescriptor(simpleAttributeBuilder().withReturnIntegrationObject("Item"))

        given:
        def typeFromFirstCall = attributeDescriptor.getAttributeType()
        def typeFromSecondCall = attributeDescriptor.getAttributeType()

        expect:
        typeFromFirstCall.is typeFromSecondCall
    }

    @Test
    def "getTypeDescriptor() returns types of the integration object item containing the attribute"() {
        given:
        def attribute = typeAttributeDescriptor('MyItem', 'someAttribute')

        expect:
        attribute.getTypeDescriptor()?.itemCode == 'MyItem'
    }

    @Test
    def "getTypeDescriptor() is cached"() {
        def attribute = typeAttributeDescriptor("ContainerItem", "someAttribute")

        given:
        def typeFromFirstCall = attribute.getTypeDescriptor()
        def typeFromSecondCall = attribute.getTypeDescriptor()

        expect:
        typeFromFirstCall.is typeFromSecondCall
    }

    @Test
    @Unroll
    def "reverse() is not applicable for #attrType attribute"() {
        given:
        def attr = typeAttributeDescriptor(descriptor)

        expect:
        !attr.reverse().isPresent()

        where:
        attrType     | descriptor
        'composite'  | attributeDescriptor()
        'collection' | collectionDescriptor()
    }

    @Test
    @Unroll
    def "reverse() is applicable for #relation relation model"() {
        def itemModel = itemModelBuilder().withAttribute(simpleAttributeBuilder().withName("reverse"))

        given:
        def attr = typeAttributeDescriptor(oneToOneRelationAttributeBuilder()
                .withReturnIntegrationObjectItem(itemModel)
                .withAttributeDescriptor(descriptor))

        expect:
        attr.reverse().isPresent()

        where:
        relation | descriptor
        'target' | oneToOneRelation().withTargetAttribute(relationAttribute().withQualifier("reverse"))
        'source' | oneToOneRelation().withSourceAttribute(relationAttribute().withQualifier("reverse"))
    }

    @Test
    @Unroll
    def "isNullable() is #res when attribute is #condition"() {
        given:
        def attr = typeAttributeDescriptor(attribute)

        expect:
        attr.isNullable() == res

        where:
        condition                                                    | res   | attribute
        'unique in integration metadata and optional in type system' | true  | simpleAttributeBuilder().unique().withAttributeDescriptor(attributeDescriptor().optional())
        'unique and optional in type system'                         | true  | simpleAttributeBuilder().withAttributeDescriptor(attributeDescriptor().unique().optional())
        'optional in type system'                                    | true  | simpleAttributeBuilder().withAttributeDescriptor(attributeDescriptor().optional())
        'not optional in type system'                                | false | simpleAttributeBuilder().withAttributeDescriptor(attributeDescriptor().withOptional(false))
        'defined with default value'                                 | true  | simpleAttributeBuilder().withAttributeDescriptor(attributeDescriptor().withOptional(false).withDefaultValue("some value"))
        'not defined in type system'                                 | true  | simpleAttributeBuilder()
    }

    @Test
    @Unroll
    def "isPartOf() is same as defined in the attribute model"() {
        given:
        def attr = typeAttributeDescriptor(Stub(IntegrationObjectItemAttributeModel) {
            getPartOf() >> value
        })

        expect:
        attr.partOf == res

        where:
        value | res
        true  | true
        false | false
        null  | false
    }

    @Test
    def "isPartOf() is false when attribute model has autoCreate set to true and attribute descriptor has partOf set to false"() {
        given:
        def attr = typeAttributeDescriptor(simpleAttributeBuilder()
                .withAutoCreate(true)
                .withAttributeDescriptor(collectionDescriptor().withPartOf(false)))

        expect:
        !attr.partOf
    }

    @Test
    @Unroll
    def "isAutoCreate() is #res when attribute model has autoCreate=#autoCreate and partOf=partOf"() {
        given:
        def attr = typeAttributeDescriptor(Stub(IntegrationObjectItemAttributeModel) {
            getAutoCreate() >> autoCreate
            getPartOf() >> partOf
        })

        expect:
        attr.autoCreate == res

        where:
        autoCreate | partOf | res
        true       | null   | true
        false      | null   | false
        null       | null   | false
        true       | false  | true
        false      | false  | false
        null       | false  | false
        true       | true   | true
        false      | true   | true
        null       | true   | true
    }

    @Test
    @Unroll
    def "isLocalized() is #res when attribute defined with localized=#value in type system"() {
        given:
        def attr = typeAttributeDescriptor(attributeDescriptor().withLocalized(value))

        expect:
        attr.isLocalized() == res

        where:
        value | res
        true  | true
        false | false
        null  | false
    }

    @Test
    @Unroll
    def "isPrimitive() is #res when attribute is #attr"() {
        expect:
        attr.isPrimitive() == res

        where:
        attr                  | res
        primitiveAttribute()  | true
        collectionAttribute() | false
        localizedAttribute()  | true
    }

    @Test
    @Unroll
    def "isMap is #res for #desc attributes"() {
        expect:
        attr.isMap() == res

        where:
        attr                                    | res   | desc
        primitiveAttribute()                    | false | 'primitive'
        collectionAttribute()                   | false | 'collection'
        localizedAttribute()                    | true  | 'localized'
        typeAttributeDescriptor(mapAttribute()) | true  | 'map'
    }

    @Test
    @Unroll
    def "getMapDescriptor().present() is #res for #desc attributes"() {
        expect:
        attr.getMapDescriptor().present == res

        where:
        attr                                    | res   | desc
        primitiveAttribute()                    | false | 'primitive'
        collectionAttribute()                   | false | 'collection'
        localizedAttribute()                    | false | 'localized'
        typeAttributeDescriptor(mapAttribute()) | true  | 'map'
    }

    @Test
    def 'getMapDescriptor() is empty when exception is thrown during map descriptor creation'() {
        given: 'map has unsupported value type'
        def attribute = typeAttributeDescriptor mapAttribute(Stub(ComposedTypeModel))

        expect:
        attribute.getMapDescriptor().empty
    }

    @Test
    @Unroll
    def "isWritable is #res when attribute is #value in the type system"() {
        given:
        def attrModel = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getWritable() >> value
            }
        }
        def attr = typeAttributeDescriptor attrModel

        expect:
        attr.writable == res

        where:
        value | res
        false | false
        true  | true
        null  | true
    }

    @Test
    @Unroll
    def "isInitializable is #res when attribute is #value in the type system"() {
        given:
        def attrModel = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getInitial() >> value
            }
        }
        def attr = typeAttributeDescriptor attrModel

        expect:
        attr.initializable == res

        where:
        value | res
        false | false
        true  | true
        null  | false
    }

    @Test
    @Unroll
    def "isKeyAttribute is #value"() {
        given:
        def attr = Spy typeAttributeDescriptor('Sample', 'id')
        attr.getTypeDescriptor() >> Stub(TypeDescriptor) {
            getKeyDescriptor() >> Stub(KeyDescriptor) {
                isKeyAttribute(_ as String) >> value
            }
        }

        expect:
        attr.isKeyAttribute() == value

        where:
        value << [true, false]
    }

    @Unroll
    @Test
    def "isReadable is #result when attribute descriptor.getReadable is #readable"() {
        given:
        def attrModel = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getReadable() >> readable
            }
        }
        and:
        def attr = typeAttributeDescriptor attrModel

        expect:
        attr.isReadable() == result

        where:
        readable | result
        true     | true
        null     | true
        false    | false
    }

    @Test
    @Unroll
    def "equals() is #res when compared to #condition"() {
        given:
        def attr = typeAttributeDescriptor("Sample", "id")

        expect:
        (attr == other) == res

        where:
        condition                                       | other                                                   | res
        'null'                                          | null                                                    | false
        'another instance'                              | Stub(TypeAttributeDescriptor)                           | false
        'another instance with different attribute'     | typeAttributeDescriptor("Sample", "differentAttribute") | false
        'another instance with different type'          | typeAttributeDescriptor("DifferentType", "id")          | false
        'another instance with same type and attribute' | typeAttributeDescriptor("Sample", "id")                 | true
    }

    @Test
    @Unroll
    def "hashCode() are #resDesc when the other instance #condition"() {
        given:
        def attr = typeAttributeDescriptor("Sample", "id")

        expect:
        (attr.hashCode() == other.hashCode()) == res

        where:
        condition                     | other                                                   | res   | resDesc
        'has null type and attribute' | Stub(TypeAttributeDescriptor)                           | false | 'not equal'
        'has different attribute'     | typeAttributeDescriptor("Sample", "differentAttribute") | false | 'not equal'
        'has different type'          | typeAttributeDescriptor("DifferentType", "id")          | false | 'not equal'
        'has same type and attribute' | typeAttributeDescriptor("Sample", "id")                 | true  | 'equal'
    }

    @Test
    def 'accessor() is created and returned even when the factory is not provided'() {
        given: 'factory is not provided to the constructor'
        def model = Stub IntegrationObjectItemAttributeModel
        def descriptor = new DefaultTypeAttributeDescriptor(model)

        expect: 'accessor is still created and not null'
        descriptor.accessor()
    }

    @Test
    def 'accessor() is created by the provided factory'() {
        given: 'factory that creates an accessor'
        def accessorCreatedByFactory = Stub AttributeValueAccessor
        def factory = Stub(DescriptorFactory) {
            getAttributeValueAccessorFactory() >> Stub(AttributeValueAccessorFactory) {
                create(_) >> accessorCreatedByFactory
            }
        }
        and: 'the factory is passed to the constructor'
        def model = Stub IntegrationObjectItemAttributeModel
        def descriptor = new DefaultTypeAttributeDescriptor(model, factory)

        expect:
        descriptor.accessor().is accessorCreatedByFactory
    }

    @Test
    @Unroll
    def "isSettable returns #result when settable checker returns #settable"() {
        given:
        def item = Stub ItemModel
        and:
        def settableChecker = Mock(AttributeSettableChecker) {
            isSettable(item, _ as TypeAttributeDescriptor) >> settable
        }
        and:
        def factory = factoryWithSettableChecker(settableChecker)
        and:
        def descriptor = new DefaultTypeAttributeDescriptor(Stub(IntegrationObjectItemAttributeModel), factory)

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
        and:
        def factory = factoryWithSettableChecker(settableChecker)
        and:
        def descriptor = new DefaultTypeAttributeDescriptor(Stub(IntegrationObjectItemAttributeModel), factory)
        and:
        def item = Stub HybrisEnumValue

        when:
        def settable = descriptor.isSettable(item)

        then:
        !settable
        and:
        0 * settableChecker.isSettable(item, descriptor)
    }

    def typeAttributeDescriptor(String containerType, String name) {
        return typeAttributeDescriptor(
                attributeDescriptor().withQualifier(name).withEnclosingType(containerType))
    }

    def typeAttributeDescriptor(BaseMockAttributeDescriptorModelBuilder builder) {
        def descriptor = builder.build()
        MockItemAttributeModelBuilder attribute = simpleAttributeBuilder()
                .withName(descriptor.getQualifier())
                .withAttributeDescriptor(builder)
                .withIntegrationObjectItemCode(deriveReturnType(descriptor.getEnclosingType()))
        typeAttributeDescriptor(attribute)
    }

    def typeAttributeDescriptor(BaseMockItemAttributeModelBuilder builder) {
        typeAttributeDescriptor(builder.build())
    }

    def typeAttributeDescriptor(IntegrationObjectItemAttributeModel attr) {
        new DefaultTypeAttributeDescriptor(attr)
    }

    def deriveReturnType(TypeModel attributeType) {
        attributeType != null ? attributeType.getCode() : "null"
    }

    def oneToManyRelation() {
        MockRelationDescriptorModelBuilder.oneToManyRelation()
                .withIsSource(true)
    }

    def reversedOneToManyRelation() {
        MockRelationDescriptorModelBuilder.manyToOneRelation()
                .withIsSource(false)
    }

    def primitiveAttribute() {
        typeAttributeDescriptor(simpleAttributeBuilder()
                .withName("PrimitiveAttribute")
                .withIntegrationObjectItem(item().withIntegrationObjectCode('IntegrationObject'))
                .withAttributeDescriptor(attributeDescriptor()
                        .withPrimitive(true)
                        .withType(AtomicTypeModel)))
    }

    def collectionAttribute() {
        typeAttributeDescriptor(simpleAttributeBuilder()
                .withName("CollectionOfPrimitiveAttribute")
                .withAttributeDescriptor(attributeDescriptor()
                        .withType(CollectionTypeModel))
                .withReturnIntegrationObject("testObj"))
    }

    def localizedAttribute() {
        typeAttributeDescriptor(simpleAttributeBuilder()
                .withName("LocalizedPrimitiveAttribute")
                .withIntegrationObjectItem(item().withIntegrationObjectCode('IntegrationObject'))
                .withAttributeDescriptor(mapAttributeDescriptor()
                        .withPrimitive(true)
                        .withLocalized(true)
                        .withReturnType(AtomicTypeModel)))
    }

    def collectionAttributeOfType(TypeOfCollectionEnum type) {
        def attribute = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> Stub(CollectionTypeModel) {
                    getTypeOfCollection() >> type
                }
            }
        }
        new DefaultTypeAttributeDescriptor(attribute)
    }

    IntegrationObjectItemAttributeModel mapAttribute() {
        mapAttribute Stub(AtomicTypeModel)
    }

    IntegrationObjectItemAttributeModel mapAttribute(TypeModel mapValueType) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> Stub(MapTypeModel) {
                    getArgumentType() >> Stub(AtomicTypeModel)
                    getReturntype() >> mapValueType
                }
            }
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getIntegrationObject() >> Stub(IntegrationObjectModel) {
                    getCode() >> 'SomeIO'
                }
            }
        }
    }

    private DescriptorFactory factoryWithSettableChecker(settableChecker) {
        Stub(DescriptorFactory) {
            getAttributeSettableCheckerFactory() >> Stub(AttributeSettableCheckerFactory) {
                create(_ as DefaultTypeAttributeDescriptor) >> settableChecker
            }
        }
    }
}