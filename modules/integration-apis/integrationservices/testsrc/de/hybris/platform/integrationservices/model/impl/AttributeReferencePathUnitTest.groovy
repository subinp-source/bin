/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AttributeReferencePathUnitTest extends Specification {
    @Shared
    private def someItemType = typeDescriptor()
    private def modelService = Stub(ModelService)

    def setup() {
        AttributeReferencePath.modelService = modelService
    }

    def cleanup() {
        AttributeReferencePath.modelService = null
    }

    @Test
    def 'cannot be instantiated with null attribute descriptor'() {
        when:
        new AttributeReferencePath(null as TypeAttributeDescriptor)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "cannot be instantiated when #condition"() {
        when:
        new AttributeReferencePath(attributes as List)

        then:
        thrown IllegalArgumentException

        where:
        condition                            | attributes
        'attributes is null'                 | null
        'attributes is empty'                | []
        'attributes contain null descriptor' | [null]
    }

    @Test
    def "reads path's destination"() {
        given:
        def firstHopType = typeDescriptor()
        def lastHopType = typeDescriptor()
        def path = new AttributeReferencePath([attribute(firstHopType), attribute(lastHopType)])

        expect:
        path.destination == lastHopType
    }

    @Test
    @Unroll
    def "length is #expected when path contains #attributes.size() attributes"() {
        given:
        def path = new AttributeReferencePath(attributes)

        expect:
        path.length() == expected

        where:
        attributes                              | expected
        attribute()                             | 1
        [attribute(), attribute(), attribute()] | 3
    }

    @Test
    def "changes to the attributes list do not affect the path's state"() {
        given:
        def attributes = [attribute()]
        def path = new AttributeReferencePath(attributes)

        when: 'attributes are changed after instantiating the path'
        attributes.clear()

        then: 'the path is not changed'
        path.length() == 1
    }

    @Test
    @Unroll
    def "reaches() is #res when the specified type is #condition"() {
        given:
        def path = new AttributeReferencePath(attributes)

        expect:
        path.reaches(type) == res

        where:
        condition                   | type         | attributes                                               | res
        'source'                    | someItemType | [attribute(someItemType, typeDescriptor()), attribute()] | true
        'destination'               | someItemType | [attribute(), attribute(someItemType)]                   | true
        'in the middle of the path' | someItemType | [attribute(), attribute(someItemType), attribute()]      | true
        'not in the path'           | someItemType | [attribute()]                                            | false
        'null'                      | null         | [attribute()]                                            | false
    }

    @Test
    @Unroll
    def "expands to empty collection when destination does not have #condition"() {
        given:
        def firstHopWithReferenceAttributes = attribute(typeDescriptor([attribute()]))
        def lastHop = attribute(typeDescriptor(attributes))

        expect:
        new AttributeReferencePath([firstHopWithReferenceAttributes, lastHop]).expand().empty

        where:
        attributes             | condition
        []                     | 'attributes'
        [primitiveAttribute()] | 'references to other item types'
    }

    @Test
    @Unroll
    def "expand excludes references forming a loop to #condition"() {
        given:
        def path = new AttributeReferencePath([first, middle, last])

        expect:
        path.expand().empty

        where:
        condition            | first                                     | middle                  | last
        'the source type'    | attribute(someItemType, typeDescriptor()) | attribute()             | attribute(typeDescriptor([attribute(someItemType)]))
        'a type in the path' | attribute(someItemType)                   | attribute()             | attribute(typeDescriptor([attribute(someItemType)]))
        'itself'             | attribute()                               | attribute(someItemType) | attribute(someItemType, typeDescriptor([attribute(someItemType)]))
    }

    @Test
    def "expands into all possible references from the destination"() {
        given:
        def referencedType1 = typeDescriptor()
        def referencedType2 = typeDescriptor()
        def path = new AttributeReferencePath(attribute(typeDescriptor([primitiveAttribute(), attribute(referencedType1), attribute(referencedType2)])))

        when:
        def expandedToTypes = path.expand().collect {it.destination}

        then:
        expandedToTypes.size() == 2
        expandedToTypes.containsAll([referencedType1, referencedType2])
    }

    @Test
    def "expand excludes duplicate references"() {
        given:
        def attr = attribute(someItemType)

        when:
        def expanded = new AttributeReferencePath(attribute(typeDescriptor([attr, attr]))).expand()

        then:
        expanded.collect  {it.destination} == [someItemType]
    }

    @Test
    def "execute throws exception when item is not instance of the source type"() {
        given:
        def item = item()
        def type = Stub(TypeDescriptor) {
            isInstance(item) >> false
        }
        def path = new AttributeReferencePath(attribute(type, typeDescriptor()))

        when:
        path.execute(item)

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "execute throws exception when item referenced by an attribute is not instance of the attribute's item type"() {
        given:
        def item = item()
        def refType = Stub(TypeDescriptor) {
            isInstance(item) >> false
        }
        def path = new AttributeReferencePath([attribute('ref'), attribute(refType, typeDescriptor())])
        modelService.getAttributeValue(item, 'ref') >> item

        when:
        path.execute(item)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "execute returns #res when attribute value is #value"() {
        given:
        def item = item()
        def srcType = Stub(TypeDescriptor) {
            isInstance(item) >> true
        }
        modelService.getAttributeValue(item, 'attribute') >> value
        def path = new AttributeReferencePath(attribute('attribute', srcType))

        expect:
        path.execute(item) == res

        where:
        value     | res
        null      | []
        'string'  | ['string']
        ['value'] | ['value']
    }

    @Test
    @Unroll
    def "execute returns empty collection when #value value found on the path"() {
        given:
        def orig = item()
        def path = new AttributeReferencePath([attribute('item'), attribute('refFromNoItem')])
        modelService.getAttributeValue(orig, 'item') >> value
        modelService.getAttributeValue(_, 'refFromNoItem') >> 'should not get this value'

        expect:
        path.execute(orig) == []

        where:
        value << [null, [null, null], []]
    }

    @Test
    @Unroll
    def "execute returns #res when referenced item value is #value, which reference(s) to #refs"() {
        given:
        def orig = item()
        def path = new AttributeReferencePath([attribute('firstRef'), attribute('secondRef')])
        modelService.getAttributeValue(orig, 'firstRef') >> value
        modelService.getAttributeValue(_, 'secondRef') >>> [ref1] >> ref2

        expect:
        path.execute(orig) == res

        where:
        value            | ref1               | ref2               | res                                  | refs
        item()           | 'item'             | 'not called'       | ['item']                             | 'item'
        item()           | ['item1', 'item2'] | 'not called'       | ['item1', 'item2']                   | 'item1 and item2'
        [item()]         | ['item']           | 'not called'       | ['item']                             | '[item]'
        [null, item()]   | ['item']           | 'not called'       | ['item']                             | '[item]'
        [item(), item()] | ['item1', 'item2'] | ['item3', 'item4'] | ['item1', 'item2', 'item3', 'item4'] | '[item1, item2] and [item3, item4] respectively'
        [item(), item()] | null               | ['item1', 'item2'] | ['item1', 'item2']                   | 'null and [item1, item2] respectively'
        [item(), item()] | ['item1', 'item2'] | [null]             | ['item1', 'item2', null]             | '[item1, item2] and [null] respectively'
    }

    @Test
    @Unroll
    def "is not equal when other path #condition"() {
        given:
        def sample = new AttributeReferencePath(attribute())

        expect:
        sample != other

        where:
        condition                  | other
        'is null'                  | null
        'has different class'      | new EmptyReferencePath(typeDescriptor())
        'has different attributes' | new AttributeReferencePath(attribute())
    }

    @Test
    def "equals when other path has the same attributes"() {
        given:
        def attr = attribute()
        def sample = new AttributeReferencePath(attr)

        expect:
        sample == new AttributeReferencePath(attr)
    }

    @Test
    @Unroll
    def "hashCode is not equal when other path has different #condition"() {
        given:
        def sample = new AttributeReferencePath(attribute())

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition    | other
        'attributes' | new AttributeReferencePath([attribute()])
        'class path' | new EmptyReferencePath(typeDescriptor())
    }

    @Test
    def "hashCode equal when other path has the same attributes"() {
        given:
        def attr = attribute()
        def sample = new AttributeReferencePath(attr)

        expect:
        sample.hashCode() == new AttributeReferencePath(attr).hashCode()
    }

    private TypeAttributeDescriptor primitiveAttribute() {
        attribute(null as TypeDescriptor, null as TypeDescriptor)
    }

    private TypeAttributeDescriptor attribute(TypeDescriptor attrType=null) {
        attribute(null as TypeDescriptor, attrType)
    }

    private TypeAttributeDescriptor attribute(String name, TypeDescriptor srcType=null) {
        attribute(srcType, typeDescriptor(), name)
    }

    private TypeAttributeDescriptor attribute(TypeDescriptor itemType, TypeDescriptor attrType, String name='') {
        Stub(TypeAttributeDescriptor) {
            getQualifier() >> name
            getTypeDescriptor() >> (itemType ?: typeDescriptor())
            getAttributeType() >> (attrType ?: typeDescriptor())
            isPrimitive() >> (attrType == null)
        }
    }

    private TypeDescriptor typeDescriptor(List<TypeAttributeDescriptor> attributes=[]) {
        Stub(TypeDescriptor) {
            getAttributes() >> attributes
            isInstance(_) >> true
        }
    }

    private ItemModel item() {
        Stub(ItemModel)
    }
}
