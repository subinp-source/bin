/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.item

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultIntegrationItemUnitTest extends Specification {
    @Shared
    def someItem = Stub(IntegrationItem)
    @Shared
    def itemType = Stub(TypeDescriptor)

    @Test
    def "cannot be instantiated with null item type"() {
        when:
        new DefaultIntegrationItem(null, 'key')

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "reads integration object code"() {
        given:
        def typeDescriptor = Stub(TypeDescriptor) {
            getIntegrationObjectCode() >> 'TestObject'
        }

        when:
        def item = new DefaultIntegrationItem(typeDescriptor, '')

        then:
        item.integrationObjectCode == 'TestObject'
        item.itemType == typeDescriptor
    }

    @Test
    @Unroll
    def "integration key #key passed to constructor can be read back"() {
        given:
        def item = new DefaultIntegrationItem(itemType, key)

        expect:
        item.integrationKey == key

        where:
        key << [null, "", "some|value"]
    }

    @Test
    @Unroll
    def "integration key #key can be set on an existing item"() {
        given:
        def item = new DefaultIntegrationItem(itemType, "shouldNotMatter")

        when:
        item.setIntegrationKey(key)

        then:
        item.integrationKey == key

        where:
        key << [null, "", "some|value"]
    }

    @Test
    def 'getAttributes() is empty when no attributes were set'() {
        given:
        def item = new DefaultIntegrationItem(itemType, '')

        expect:
        item.attributes.empty
    }

    @Test
    @Unroll
    def "getAttributes() includes an attribute that was set to #value"() {
        given: 'item type has two attributes defined'
        def attrOneDesc = Stub(TypeAttributeDescriptor)
        def attrTwoDesc = Stub(TypeAttributeDescriptor)
        def typeDesc = Stub(TypeDescriptor) {
            getAttribute('one') >> Optional.of(attrOneDesc)
            getAttribute('two') >> Optional.of(attrTwoDesc)
        }
        and: 'an integration item for the defined type'
        def item = new DefaultIntegrationItem(typeDesc, '')

        when:
        item.setAttribute 'one', 1
        item.setAttribute 'two', value

        then:
        item.attributes.size() == 2
        item.attributes.containsAll([attrOneDesc, attrTwoDesc])

        where:
        value << ['a value', null]
    }

    @Test
    def 'getAttributes() does not include attributes, which are not present in the type descriptor'() {
        given: 'an integration item for an item type that does not have attributes defined'
        def item = new DefaultIntegrationItem(itemType, '')

        when:
        item.setAttribute('non-existent', 'value')

        then:
        item.attributes.empty
    }

    @Test
    def "setAttribute returns #res if the attribute #condition"() {
        given: "attribute 'existing' is defined in the item type"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('existing') >> Optional.of(Stub(TypeAttributeDescriptor))
        }
        def item = new DefaultIntegrationItem(itemType, '')

        expect:
        item.setAttribute(attr, 'value') == res

        where:
        condition        | attr          | res
        'exists'         | 'existing'    | true
        'does not exist' | 'nonExistent' | false
    }

    @Test
    @Unroll
    def "getAttribute returns #value for #condition attribute"() {
        given: "attribute 'existing' is defined in the item type"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('existing') >> Optional.of(Stub(TypeAttributeDescriptor))
        }

        and: "the attribute value is set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute(attr, 'value')

        expect:
        item.getAttribute(attr) == value

        where:
        condition      | attr          | value
        'existing'     | 'existing'    | 'value'
        'not existing' | 'nonExistent' | null
    }

    @Test
    @Unroll
    def "getAttribute with TypeAttributeDescriptor returns #value for #condition attribute"() {
        given: "attribute 'existing' is defined in the item type"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('existing') >> Optional.of(Stub(TypeAttributeDescriptor))
        }

        and: "the attribute value is set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute(attr.getAttributeName(), 'value')

        expect:
        item.getAttribute(attr) == value

        where:
        condition      | attr                                                                  | value
        'existing'     | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'existing' }    | 'value'
        'not existing' | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'nonExistent' } | null
    }

    @Test
    def "getLocalizedAttribute() throws exception when the attribute is not localized"() {
        given: "attribute 'text' is not defined as localized"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('text') >> Optional.of(Stub(TypeAttributeDescriptor))
        }
        def item = new DefaultIntegrationItem(itemType, '')

        when:
        item.getLocalizedAttribute('text', 'en')

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "getLocalizedAttribute() returns #expected when #condition"() {
        given: "attribute 'text' is defined as localized"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('text') >> Optional.of(Stub(TypeAttributeDescriptor) {
                getAttributeName() >> 'text'
                isLocalized() >> true
            })
        }
        and: "localized values are set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('text', value)

        expect:
        item.getLocalizedAttribute(attribute, locale) == expected

        where:
        condition                      | attribute      | locale         | value                                                  | expected
        'attribute does not exist'     | 'non-existent' | Locale.ENGLISH | LocalizedValue.of(Locale.ENGLISH, 'a localized value') | null
        'value was not set for locale' | 'text'         | Locale.GERMAN  | LocalizedValue.of(Locale.ENGLISH, 'a localized value') | null
        'value was set for locale'     | 'text'         | Locale.ENGLISH | LocalizedValue.of(Locale.ENGLISH, 'a localized value') | 'a localized value'
        'value is not localized'       | 'text'         | Locale.ENGLISH | 'a localized value'                                    | null
    }

    @Test
    @Unroll
    def "getLocalizedAttribute(String) handles non-standard language tags"() {
        given: "attribute 'text' is defined as localized"
        def itemType = Stub(TypeDescriptor) {
            getAttribute('text') >> Optional.of(Stub(TypeAttributeDescriptor) {
                getAttributeName() >> 'text'
                isLocalized() >> true
            })
        }
        and: "localized values are set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('text', LocalizedValue.of(Locale.US, 'a value'))

        expect:
        item.getLocalizedAttribute('text', 'en_us') == 'a value'
    }

    @Test
    @Unroll
    def "getReferencedItem(TypeAttributeDescriptor) throws exception when the specified attribute #condition"() {
        given: "attribute named 'association' that does not refer to another item"
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                isPrimitive() >> primitive
            }
            isCollection() >> collection
        }
        itemType = Stub(TypeDescriptor) {
            getAttribute('association') >> Optional.of(attributeDescriptor)
        }

        when:
        new DefaultIntegrationItem(itemType, '').getReferencedItem(attributeDescriptor)

        then:
        thrown(IllegalArgumentException)

        where:
        condition                              | primitive | collection
        'has a primitive value'                | true      | false
        'has a collection of primitives value' | true      | true
        'has a collection of items value'      | false     | true
    }

    @Test
    @Unroll
    def "getReferencedItem(String) throws exception when the specified attribute #condition"() {
        given: "attribute named 'association' that does not refer to another item"
        itemType = Stub(TypeDescriptor) {
            getAttribute('association') >> Optional.of(Stub(TypeAttributeDescriptor) {
                getAttributeType() >> Stub(TypeDescriptor) {
                    isPrimitive() >> primitive
                }
                isCollection() >> collection
            })
        }

        when:
        new DefaultIntegrationItem(itemType, '').getReferencedItem('association')

        then:
        thrown(IllegalArgumentException)

        where:
        condition                              | primitive | collection
        'has a primitive value'                | true      | false
        'has a collection of primitives value' | true      | true
        'has a collection of items value'      | false     | true
    }

    @Test
    @Unroll
    def "getReferencedItem(TypeAttributeDescriptor) returns #attrValue when the attribute #condition"() {
        given: "item type is defined"
        itemType = Stub(TypeDescriptor) {
            getAttribute('reference') >> Optional.ofNullable(attribute)
        }
        and: "item has the attribute value set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('reference', attrValue)

        expect:
        item.getReferencedItem(attribute) == attrValue

        where:
        condition                         | attribute                                                           | attrValue
        'exists and its value is not set' | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'reference' } | null
        'exists and its value is set'     | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'reference' } | someItem
    }

    @Test
    @Unroll
    def "getReferencedItem(String) returns #result when the attribute #condition"() {
        given: "item type is defined"
        itemType = Stub(TypeDescriptor) {
            getAttribute('reference') >> Optional.ofNullable(attribute)
        }
        and: "item has the attribute value set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('reference', result)

        expect:
        item.getReferencedItem('reference') == result

        where:
        condition                         | attribute                                                           | result
        'does not exist'                  | null                                                                | null
        'exists and its value is not set' | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'reference' } | null
        'exists and its value is set'     | Stub(TypeAttributeDescriptor) { getAttributeName() >> 'reference' } | someItem
    }

    @Test
    def "getReferencedItems(TypeAttributeDescriptor) throws exception when the specified attribute does not reference another item"() {
        given: "attribute named 'primitives' that does not refer to another item"
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                isPrimitive() >> true
            }
        }
        itemType = Stub(TypeDescriptor) {
            getAttribute('primitives') >> Optional.of(attributeDescriptor)
        }

        when:
        new DefaultIntegrationItem(itemType, '').getReferencedItems(attributeDescriptor)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "getReferencedItems(String) throws exception when the specified attribute does not reference another item"() {
        given: "attribute named 'primitives' that does not refer to another item"
        itemType = Stub(TypeDescriptor) {
            getAttribute('primitives') >> Optional.of(Stub(TypeAttributeDescriptor) {
                getAttributeType() >> Stub(TypeDescriptor) {
                    isPrimitive() >> true
                }
            })
        }

        when:
        new DefaultIntegrationItem(itemType, '').getReferencedItems('primitives')

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "getReferencedItems(String) returns [] when the attribute does not exist"() {
        given: "item type is defined"
        itemType = Stub(TypeDescriptor) {
            getAttribute('reference') >> Optional.empty()
        }

        expect:
        new DefaultIntegrationItem(itemType, '').getReferencedItems('reference') == []
    }

    @Test
    @Unroll
    def "getReferencedItems(TypeAttributeDescriptor) returns #expected when the attribute exists and is set to #value"() {
        given: "item type is defined"
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> 'reference'
        }
        itemType = Stub(TypeDescriptor) {
            getAttribute('reference') >> Optional.of(attributeDescriptor)
        }
        and: "item has the attribute value set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('reference', value)

        expect:
        def actual = item.getReferencedItems(attributeDescriptor)
        actual.size() == expected.size()
        actual.containsAll expected

        where:
        value                | expected
        [someItem, someItem] | [someItem, someItem]
        [someItem]           | [someItem]
        []                   | []
        someItem             | [someItem]
    }

    @Test
    @Unroll
    def "getReferencedItems(String) returns #expected when the attribute exists and is set to #value"() {
        given: "item type is defined"
        itemType = Stub(TypeDescriptor) {
            getAttribute('reference') >> Optional.of(Stub(TypeAttributeDescriptor) { getAttributeName() >> 'reference' })
        }
        and: "item has the attribute value set"
        def item = new DefaultIntegrationItem(itemType, '')
        item.setAttribute('reference', value)

        expect:
        def actual = item.getReferencedItems('reference')
        actual.size() == expected.size()
        actual.containsAll expected

        where:
        value                | expected
        [someItem, someItem] | [someItem, someItem]
        [someItem]           | [someItem]
        []                   | []
        someItem             | [someItem]
    }

    @Test
    @Unroll
    def "item is equal to the other item when #condition"() {
        expect:
        myself == other

        where:
        condition                               | myself                                           | other
        'the other item is same'                | new DefaultIntegrationItem(itemType, 'some|key') | myself
        'both items have equal integration key' | new DefaultIntegrationItem(itemType, 'some|key') | new DefaultIntegrationItem(itemType, 'some|key')
        'both items have null integration key'  | new DefaultIntegrationItem(itemType, null)       | new DefaultIntegrationItem(itemType, null)
    }

    @Test
    @Unroll
    def "item is not equal to the other item when its key is #myKey and the other item's key is #otherKey"() {
        given:
        def myself = new DefaultIntegrationItem(itemType, myKey)
        def other = new DefaultIntegrationItem(itemType, otherKey)

        expect:
        myself != other

        where:
        myKey      | otherKey
        'some|key' | 'other|key'
        'some|key' | null
        null       | ''
    }

    @Test
    @Unroll
    def "item is not equal to the other item type is different from this item thype"() {
        expect:
        new DefaultIntegrationItem(itemType, '') != new DefaultIntegrationItem(Stub(TypeDescriptor), '')
    }

    @Test
    @Unroll
    def "hashCode is #result when item's integration key is #myKey and the other item's key is #otherKey"() {
        given:
        def myself = new DefaultIntegrationItem(itemType, myKey)
        def other = new DefaultIntegrationItem(itemType, otherKey)

        expect:
        (myself.hashCode() == other.hashCode()) == expected

        where:
        myKey      | otherKey    | result      | expected
        'some|key' | 'some|key'  | 'equal'     | true
        null       | null        | 'equal'     | true
        'some|key' | 'other|key' | 'different' | false
        'some|key' | null        | 'different' | false
        null       | ''          | 'different' | false
    }

    @Test
    def "hashCode is different when item's type is different from the other item's type"() {
        given:
        def myself = new DefaultIntegrationItem(Stub(TypeDescriptor), 'key')
        def other = new DefaultIntegrationItem(Stub(TypeDescriptor), 'key')

        expect:
        myself.hashCode() != other.hashCode()
    }

    @Test
    def "container item is null on integration item"() {
        given:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), 'key')
        and:
        def contextDescriptor = Stub(TypeDescriptor)

        expect:
        item.getContextItem(contextDescriptor) == Optional.empty()
    }

    @Test
    def "container item exists but context item not found"() {
        given:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), Stub(IntegrationItem))
        and:
        def contextDescriptor = Stub(TypeDescriptor)

        expect:
        item.getContextItem(contextDescriptor) == Optional.empty()
    }

    @Test
    def "context item is found in container item with simple key attributes"() {
        given: 'attribute descriptors'
        def nonKeyAttrName = 'nonKeyAttr'
        def attr = nonKeyAttribute(nonKeyAttrName)
        def keyAttrName = 'keyAttr'
        def keyAttrValue = 'keyValue'
        def keyAttr = primitiveKeyAttribute(keyAttrName)
        def parentTypeDescriptor = Stub(TypeDescriptor) {
            getAttribute(nonKeyAttrName) >> Optional.of(attr)
            getAttribute(keyAttrName) >> Optional.of(keyAttr)
        }

        and: 'attribute values are set on integration item'
        DefaultIntegrationItem parentContextItem = new DefaultIntegrationItem(parentTypeDescriptor, 'parentKey')
        parentContextItem.setAttribute(nonKeyAttrName, 'nonKeyAttrvalue')
        parentContextItem.setAttribute(keyAttrName, keyAttrValue)

        and:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), parentContextItem)

        when:
        def optionalContextItem = item.getContextItem(parentTypeDescriptor)

        then:
        optionalContextItem.isPresent()
        def parentContextItemCopy = optionalContextItem.get()
        and: 'context item containing only key attributes is equivalent to parent item'
        parentContextItemCopy == parentContextItem
        and: 'context item is not the same object as parent'
        !parentContextItemCopy.is(parentContextItem)
        and: 'returned context item contains only the key attribute'
        parentContextItemCopy.attributes.size() == 1
        parentContextItemCopy.getAttribute(keyAttrName) == keyAttrValue
        and: "the context item's integrationKey is the same as the parent's key"
        parentContextItemCopy.integrationKey == 'parentKey'
    }

    @Test
    def "context item is found in container item with referenced key attributes"() {
        given: 'attribute descriptors for outer item'
        def nonKeyAttrName = 'nonKeyAttr'
        def attr = nonKeyAttribute(nonKeyAttrName)
        def keyAttrName = 'keyAttr'
        def parentKeyValue = 'parentKey'
        def refKeyAttr = referenceKeyAttribute(keyAttrName)
        def parentTypeDescriptor = Stub(TypeDescriptor) {
            getAttribute(nonKeyAttrName) >> Optional.of(attr)
            getAttribute(keyAttrName) >> Optional.of(refKeyAttr)
        }

        and: 'non key attribute value is set'
        DefaultIntegrationItem parentItem = new DefaultIntegrationItem(parentTypeDescriptor, parentKeyValue)
        parentItem.setAttribute(nonKeyAttrName, Stub(DefaultIntegrationItem))

        and: 'reference item attribute descriptors'
        def refTypeDescriptor = Stub(TypeDescriptor)
        def primitiveKeyAttrName = 'primitiveKeyAttr'
        def primitiveKeyAttr = primitiveKeyAttribute(primitiveKeyAttrName)
        refTypeDescriptor.getAttribute(nonKeyAttrName) >> Optional.of(attr)
        refTypeDescriptor.getAttribute(keyAttrName) >> Optional.of(primitiveKeyAttr)

        and: 'attributes are set on reference integration item'
        def nestedItemKey = 'parentNestedKeyItemsKey'
        def parentReferencedKeyItem = new DefaultIntegrationItem(refTypeDescriptor, nestedItemKey)
        parentReferencedKeyItem.setAttribute(primitiveKeyAttrName, primitiveKeyAttr)
        parentReferencedKeyItem.setAttribute(nonKeyAttrName, primitiveKeyAttr)

        and: 'reference item is set on the outer item'
        parentItem.setAttribute(keyAttrName, parentReferencedKeyItem)

        and:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), parentItem)

        when:
        def optionalContextItem = item.getContextItem(parentTypeDescriptor)

        then:
        optionalContextItem.isPresent()
        def copy = optionalContextItem.get()
        copy.attributes.size() == 1
        copy.integrationKey == parentKeyValue
        final IntegrationItem copyReferenceAttr = (IntegrationItem) copy.getAttribute(keyAttrName)
        copyReferenceAttr == parentReferencedKeyItem
        !copyReferenceAttr.is(parentReferencedKeyItem)
        copyReferenceAttr.integrationKey == nestedItemKey
    }

    @Test
    def "context item is found in second level container"() {
        given:
        def grandParentDescriptor = Stub(TypeDescriptor)
        and:
        def grandParentItem = new DefaultIntegrationItem(grandParentDescriptor, 'grandParentKey')
        def parentItem = new DefaultIntegrationItem(Stub(TypeDescriptor), grandParentItem)
        and:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), parentItem)

        when:
        def optionalContextItem = item.getContextItem(grandParentDescriptor)

        then:
        optionalContextItem.isPresent()
        def contextItem = optionalContextItem.get()
        contextItem == grandParentItem
        !contextItem.is(grandParentItem)
    }

    @Test
    def "first context item is returned when multiple matching items exist"() {
        given:
        def requestedType = Stub(TypeDescriptor)
        and:
        def grandParentItem = new DefaultIntegrationItem(requestedType, 'grandParentKey')
        def parentItem = new DefaultIntegrationItem(requestedType, grandParentItem)
        and:
        def item = new DefaultIntegrationItem(Stub(TypeDescriptor), parentItem)

        when:
        def optionalContextItem = item.getContextItem(requestedType)

        then:
        optionalContextItem.isPresent()
        def contextItem = optionalContextItem.get()
        contextItem == parentItem
        !contextItem.is(parentItem)
    }

    private TypeAttributeDescriptor primitiveKeyAttribute(String attributeName) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName
            isKeyAttribute() >> true
            isPrimitive() >> true
        }
    }

    private TypeAttributeDescriptor referenceKeyAttribute(String keyAttrName) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> keyAttrName
            isKeyAttribute() >> true
            isPrimitive() >> false
        }
    }

    private TypeAttributeDescriptor nonKeyAttribute(String nonKeyAttrName) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> nonKeyAttrName
            isPrimitive() >> true
        }
    }
}
