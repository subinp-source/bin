/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class EmptyReferencePathUnitTest extends Specification {
    @Test
    def "cannot be instantiated with null type"() {
        when:
        new EmptyReferencePath(null)

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "reads path's destination"() {
        given:
        def type = typeDescriptor()
        def path = new EmptyReferencePath(type)

        expect:
        path.destination == type
    }

    @Test
    def "length is always zero"() {
        given:
        def path = new EmptyReferencePath(typeDescriptor())

        expect:
        path.length() == 0
    }

    @Test
    def "reaches its source/destination only"() {
        given:
        def type = typeDescriptor()
        def path = new EmptyReferencePath(type)

        expect:
        path.reaches(type)
        !path.reaches(null)
        !path.reaches(typeDescriptor())
    }

    @Test
    @Unroll
    def "expands to empty collection when current type does not have #condition"() {
        given:
        def type = typeDescriptor(attributes)

        expect:
        new EmptyReferencePath(type).expand().empty

        where:
        attributes                                   | condition
        []                                           | 'attributes'
        [primitiveAttribute(), primitiveAttribute()] | 'references to other item types'
    }

    @Test
    def "expand excludes self-references"() {
        given:
        def type = Stub(TypeDescriptor)
        type.getAttributes() >> [attribute(type)]

        expect:
        new EmptyReferencePath(type).expand().empty
    }

    @Test
    def "expands into all possible references from its type"() {
        given:
        def referencedType1 = typeDescriptor()
        def referencedType2 = typeDescriptor()
        def type = Stub(TypeDescriptor) {
            getAttributes() >> [ primitiveAttribute(), attribute(referencedType1), attribute(referencedType2) ]
        }

        when:
        def expandedToTypes = new EmptyReferencePath(type).expand().collect {it.destination}

        then:
        expandedToTypes.size() == 2
        expandedToTypes.containsAll([referencedType1, referencedType2])
    }

    @Test
    def "expand excludes duplicate references"() {
        given:
        def referencedType = typeDescriptor()
        def attr = attribute(referencedType)
        def type = Stub(TypeDescriptor) {
            getAttributes() >> [attr, attr]
        }

        when:
        def expanded = new EmptyReferencePath(type).expand()

        then:
        expanded.collect  {it.destination} == [referencedType]
    }

    @Test
    def "execute throws exception, if the item is not an instance of the path's type"() {
        given:
        def item = Stub(ItemModel)
        def type = Stub(TypeDescriptor) {
            isInstance(item) >> false
        }

        when:
        new EmptyReferencePath(type).execute(item)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "execute returns #res when the item is #item"() {
        given:
        def type = Stub(TypeDescriptor) {
            isInstance(null) >> false
            isInstance(!null) >> true
        }

        expect:
        new EmptyReferencePath(type).execute(item) == res

        where:
        item            | res
        Stub(ItemModel) | [item]
        null            | []
    }

    @Test
    @Unroll
    def "is not equal when other path #condition"() {
        given:
        def sample = new EmptyReferencePath(typeDescriptor())

        expect:
        sample != other

        where:
        condition                 | other
        'is null'                 | null
        'has different class'     | new AttributeReferencePath([attribute()])
        'has different item type' | new EmptyReferencePath(typeDescriptor())
    }

    @Test
    def "equals when other path has the same type"() {
        given:
        def type = typeDescriptor()
        def sample = new EmptyReferencePath(type)

        expect:
        sample == new EmptyReferencePath(type)
    }

    @Test
    @Unroll
    def "hashCode is not equal when other path has different #condition"() {
        given:
        def sample = new EmptyReferencePath(typeDescriptor())

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition   | other
        'class'     | new AttributeReferencePath([attribute()])
        'item type' | new EmptyReferencePath(typeDescriptor())
    }

    @Test
    def "hashCode equal when other path has the same type"() {
        given:
        def type = typeDescriptor()
        def sample = new EmptyReferencePath(type)

        expect:
        sample.hashCode() == new EmptyReferencePath(type).hashCode()
    }

    private TypeDescriptor typeDescriptor(List<TypeAttributeDescriptor> attributes = []) {
        Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }
    }

    private TypeAttributeDescriptor primitiveAttribute() {
        attribute(null)
    }

    private TypeAttributeDescriptor attribute(TypeDescriptor type) {
        Stub(TypeAttributeDescriptor) {
            getAttributeType() >> type
            isPrimitive() >> (type == null)
        }
    }
}
