/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.enums.SyncItemStatus
import de.hybris.platform.catalog.model.CompanyModel
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.user.UserGroupModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ItemTypeDescriptorUnitTest extends Specification {
    private static final String DEFAULT_IO_CODE = 'IO'

    @Test
    def "creates new ItemTypeDescriptor"() {
        expect:
        def descriptor = ItemTypeDescriptor.create Stub(IntegrationObjectItemModel)
        descriptor.factory
    }

    @Test
    def "create(null) throws exception"() {
        when:
        ItemTypeDescriptor.create null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "reads integration object code"() {
        given:
        def descriptor = typeDescriptor('TestObject', 'TestItem')

        expect:
        descriptor.integrationObjectCode == 'TestObject'
    }

    @Test
    def "reads integration object item code"() {
        given:
        def descriptor = typeDescriptor Stub(IntegrationObjectItemModel) {
            getCode() >> 'MyProduct'
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> 'Product'
            }
        }

        expect:
        descriptor.itemCode == "MyProduct"
    }

    @Test
    def "read integration object item type code"() {
        given:
        def descriptor = typeDescriptor Stub(IntegrationObjectItemModel) {
            getCode() >> 'MyProduct'
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> 'Product'
            }
        }

        expect:
        descriptor.typeCode == "Product"
    }

    @Test
    def "isPrimitive() is always false"() {
        expect:
        !typeDescriptor().primitive
    }

    @Test
    def "getAttributes() is empty when item does not have attributes defined"() {
        expect:
        typeDescriptor().attributes.empty
    }

    @Test
    def "getAttributes() returns attributes defined in the item model"() {
        given:
        def itemModel = Stub(IntegrationObjectItemModel) {
            getAttributes() >> [attributeModel('code'), attributeModel('name')]
            getClassificationAttributes() >> [classificationAttributeModel('weight'), classificationAttributeModel('size')]
            getVirtualAttributes() >> [virtualAttributeModel('myVirtualAttribute')]
        }
        def descriptor = typeDescriptor itemModel

        expect:
        descriptor.attributes.size() == itemModel.attributes.size() + itemModel.classificationAttributes.size() + itemModel.getVirtualAttributes().size()
        descriptor.attributes.collect({ it.getAttributeName() }).containsAll(['code', 'name', 'weight', 'size', 'myVirtualAttribute'])
    }

    @Test
    def "getAttributes() does not leak immutability"() {
        given:
        def descriptor = typeDescriptor itemModel([attributeModel()])

        when:
        descriptor.attributes.clear()

        then:
        descriptor.attributes.size() == 1
    }

    @Test
    def "getAttribute() returns empty result when attribute does not exist"() {
        expect:
        !typeDescriptor().getAttribute('someAttribute').present
    }

    @Test
    def "getAttribute() returns matching attribute descriptor when the attribute exists"() {
        given:
        def descriptor = typeDescriptor itemModel([attributeModel('One'), attributeModel('Two')])

        when:
        def attribute = descriptor.getAttribute("Two").orElse(null)

        then:
        attribute.attributeName == 'Two'
    }

    @Test
    @Unroll
    def "isEnumeration() is #result for #desc types in the type system"() {
        given:
        def descriptor = typeDescriptor Stub(IntegrationObjectItemModel) {
            getType() >> type
        }

        expect:
        descriptor.enumeration == result

        where:
        desc          | type                           | result
        'enumeration' | Stub(EnumerationMetaTypeModel) | true
        'composed'    | Stub(ComposedTypeModel)        | false
    }

    @Unroll
    @Test
    def "isAbstract() is #result when getAbstract() is #isAbstract"() {
        given:
        def descriptor = typeDescriptor Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getAbstract() >> isAbstract
            }
        }

        expect:
        descriptor.abstract == result

        where:
        isAbstract | result
        true       | true
        false      | false
        null       | false
    }

    @Test
    def "retrieves KeyDescriptor"() {
        expect:
        typeDescriptor().getKeyDescriptor() != null
    }

    @Test
    @Unroll
    def "isInstance() returns false when the object is #condition"() {
        given:
        def itemModel = Stub(IntegrationObjectItemModel) {
            getCode() >> 'Company'
            getType() >> composedType('Company')
        }

        expect:
        !typeDescriptor(itemModel).isInstance(obj)

        where:
        condition                                | obj
        'null'                                   | null
        'not an ItemModel or HybrisEnumValue'    | new Object()
        'not an instance of the type descriptor' | new UserGroupModel()
    }

    @Test
    @Unroll
    def "isInstance() returns true when the object's type is #condition"() {
        given:
        def itemModel = Stub(IntegrationObjectItemModel) {
            getType() >> composedType(descriptorType, composedType('Company'), composedType('Union'))
        }

        expect:
        typeDescriptor(itemModel).isInstance(obj)

        where:
        condition                           | descriptorType   | obj
        'same as of item descriptor'        | 'UserGroup'      | new UserGroupModel()
        'a sub-type of the item descriptor' | 'UserGroup'      | new CompanyModel()
        'same as of enum descriptor'        | 'SyncItemStatus' | SyncItemStatus.IN_PROGRESS
        'a sub-type of the enum descriptor' | 'SyncItemStatus' | new CompanyModel()
    }

    @Test
    @Unroll
    def "descriptor is root=#value"() {
        given:
        def item = Stub(IntegrationObjectItemModel) {
            getRoot() >> isRoot
        }
        expect:
        typeDescriptor(item).isRoot() == value

        where:
        isRoot | value
        true   | true
        false  | false
        null   | false
    }

    @Test
    def "getPathsToRoot() is empty when integration object does not have a root item"() {
        given:
        def item = Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getRootItem() >> null
            }
        }
        def descriptor = typeDescriptor(item)

        expect:
        descriptor.pathsToRoot.empty
        !descriptor.hasPathToRoot()
    }

    @Test
    @Unroll
    def "getPathsToRoot() is empty when the item type #condition"() {
        given:
        def item = Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getRootItem() >> Stub(IntegrationObjectItemModel) // different from default item type in attributes
            }
            getAttributes() >> attributes
        }
        def descriptor = typeDescriptor(item)

        expect:
        descriptor.pathsToRoot.empty
        !descriptor.hasPathToRoot()

        where:
        condition                                           | attributes
        'contains only primitive attributes'                | [primitiveAttributeModel('one'), primitiveAttributeModel('two')]
        'does not have attributes leading to the root item' | [attributeModel('one'), attributeModel('two')]
    }

    @Test
    def "getPathsToRoot() contains one path when the item type is root"() {
        given:
        def root = Stub(IntegrationObjectItemModel)
        root.getIntegrationObject() >> Stub(IntegrationObjectModel) {
            getRootItem() >> root
        }
        def descriptor = typeDescriptor(root)

        expect:
        descriptor.pathsToRoot.size() == 1
        descriptor.hasPathToRoot()
    }

    @Test
    def "getPathsToRoot() is ordered from shortest path to the longest"() {
        given:
        def root = Stub(IntegrationObjectItemModel) {
            getRoot() >> true
        }
        def item = Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getRootItem() >> root
            }
            getAttributes() >> [
                    primitiveAttributeModel('primitive'),
                    attributeModel('noPathToRoot', itemModel()),
                    // path: root -> root item
                    attributeModel('root', root),
                    // path: nonRoot -> root -> root item
                    attributeModel('nonRoot', itemModel('NonRoot', [attributeModel('root', root), primitiveAttributeModel('primitive')])),
                    // path: level2 -> level1 -> level0 -> root item
                    attributeModel('level2', itemModel('Level2', [attributeModel('deadEnd'), attributeModel('level1', itemModel('Level1', [attributeModel('level0', root)]))]))
            ]
        }
        def descriptor = typeDescriptor(item)

        expect:
        descriptor.pathsToRoot.collect({ it.length() }) == [1, 2, 3]
        descriptor.hasPathToRoot()
    }

    @Test
    @Unroll
    def "not equal to another type descriptor when the other type descriptor #condition"() {
        given:
        def sample = typeDescriptor("Inbound", "Item")

        expect:
        sample != other

        where:
        condition                               | other
        'is null'                               | null
        'is not instance of ItemTypeDescriptor' | Stub(TypeDescriptor)
        'has different integration object code' | typeDescriptor("Outbound", "Item")
        'had different item code'               | typeDescriptor("Inbound", "Primitive")
    }

    @Test
    def "equals to another type descriptor when it has same object and item codes"() {
        expect:
        typeDescriptor("Inbound", "Item") == typeDescriptor("Inbound", "Item")
    }

    @Test
    @Unroll
    def "hashCode not equal when type descriptors #condition"() {
        given:
        def sample = typeDescriptor("Inbound", "Item")

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition                                 | other
        'have different class'                    | Stub(TypeDescriptor)
        'have different integration object codes' | typeDescriptor("Outbound", "Item")
        'have different item codes'               | typeDescriptor("Inbound", "Primitive")
    }

    @Test
    def "hashCode is equal when type descriptors have same integration object and item codes"() {
        expect:
        typeDescriptor("Inbound", "Item").hashCode() == typeDescriptor("Inbound", "Item").hashCode()
    }

    private ComposedTypeModel composedType(String code, ComposedTypeModel... subtypes) {
        Stub(ComposedTypeModel) {
            getCode() >> code
            getAllSubTypes() >> subtypes.toList()
        }
    }

    private ItemTypeDescriptor typeDescriptor(final String object, final String item) {
        def itemModel = Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> object
            }
            getCode() >> item
        }
        typeDescriptor(itemModel)
    }

    private ItemTypeDescriptor typeDescriptor() {
        typeDescriptor(Stub(IntegrationObjectItemModel))
    }

    private static ItemTypeDescriptor typeDescriptor(IntegrationObjectItemModel model) {
        new ItemTypeDescriptor(model)
    }

    private IntegrationObjectItemAttributeModel attributeModel(String name = '', IntegrationObjectItemModel item = null) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> name
            getIntegrationObjectItem() >> itemModel()
            getReturnIntegrationObjectItem() >> (item ?: itemModel())
        }
    }

    private IntegrationObjectItemClassificationAttributeModel classificationAttributeModel(String name) {
        Stub(IntegrationObjectItemClassificationAttributeModel) {
            getAttributeName() >> name
        }
    }

    private IntegrationObjectItemVirtualAttributeModel virtualAttributeModel(String name) {
        Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> name
        }
    }

    private IntegrationObjectItemAttributeModel primitiveAttributeModel(String name) {
        Stub(IntegrationObjectItemAttributeModel) {
            getIntegrationObjectItem() >> itemModel()
            getReturnIntegrationObjectItem() >> null
            getAttributeName() >> name
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> Stub(AtomicTypeModel)
            }
        }
    }

    private IntegrationObjectItemModel itemModel(List<IntegrationObjectItemAttributeModel> attributes = []) {
        itemModel('', attributes)
    }

    private IntegrationObjectItemModel itemModel(String type, List<IntegrationObjectItemAttributeModel> attributes = []) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> type
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> DEFAULT_IO_CODE
            }
            getAttributes() >> attributes
        }
    }
}