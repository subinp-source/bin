/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.platform.core.enums.TypeOfCollectionEnum
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

class DefaultCollectionDescriptorUnitTest extends Specification {

    @Test
    @Unroll
    def "collection is a List for #type"() {
        given:
        def descriptor = collectionDescriptor(type)

        when:
        def collection = descriptor.newCollection()

        then:
        collection instanceof List
        collection.empty

        where:
        type << [TypeOfCollectionEnum.LIST, TypeOfCollectionEnum.COLLECTION]
    }

    @Test
    def "collection is a Set"() {
        given:
        def descriptor = collectionDescriptor(TypeOfCollectionEnum.SET)

        when:
        def collection = descriptor.newCollection()

        then:
        collection instanceof Set
        collection.empty
    }

    @Test
    def "null is returned if underlying attribute is not a CollectionTypeModel"() {
        given:
        def descriptor = nonCollectionDescriptor()

        expect:
        descriptor.newCollection() == null
    }

    @Test
    @Unroll
    def "equal is #res when #condition"() {
        expect:
        (descriptor1 == descriptor2) == res

        where:
        condition                             | descriptor1                                           | descriptor2                                     | res
        'same object'                         | collectionDescriptor(TypeOfCollectionEnum.SET)        | descriptor1                                     | true
        'two list descriptors'                | collectionDescriptor(TypeOfCollectionEnum.LIST)       | collectionDescriptor(TypeOfCollectionEnum.LIST) | true
        'non-collection descriptors'          | nonCollectionDescriptor()                             | nonCollectionDescriptor()                       | true
        'set and list descriptors'            | collectionDescriptor(TypeOfCollectionEnum.SET)        | collectionDescriptor(TypeOfCollectionEnum.LIST) | false
        'non-collection and list descriptors' | nonCollectionDescriptor()                             | collectionDescriptor(TypeOfCollectionEnum.LIST) | false
        'null'                                | collectionDescriptor(TypeOfCollectionEnum.COLLECTION) | null                                            | false
        'another object type'                 | collectionDescriptor(TypeOfCollectionEnum.SET)        | new Object()                                    | false
    }

    @Test
    @Unroll
    def "hashcode equality is #res when #condition"() {
        expect:
        (descriptor1.hashCode() == descriptor2.hashCode()) == res

        where:
        condition                             | descriptor1                                     | descriptor2                                     | res
        'same object'                         | collectionDescriptor(TypeOfCollectionEnum.SET)  | descriptor1                                     | true
        'two list descriptors'                | collectionDescriptor(TypeOfCollectionEnum.LIST) | collectionDescriptor(TypeOfCollectionEnum.LIST) | true
        'non-collection descriptors'          | nonCollectionDescriptor()                       | nonCollectionDescriptor()                       | true
        'set and list descriptors'            | collectionDescriptor(TypeOfCollectionEnum.SET)  | collectionDescriptor(TypeOfCollectionEnum.LIST) | false
        'non-collection and list descriptors' | nonCollectionDescriptor()                       | collectionDescriptor(TypeOfCollectionEnum.LIST) | false
        'another object type'                 | collectionDescriptor(TypeOfCollectionEnum.SET)  | new Object()                                    | false
    }

    def attributeDescriptor(TypeOfCollectionEnum collectionType) {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(CollectionTypeModel) {
                getTypeOfCollection() >> collectionType
            }
        }
    }

    def collectionDescriptor(TypeOfCollectionEnum collectionType) {
        def attr = attributeDescriptor(collectionType)
        DefaultCollectionDescriptor.create(attr)
    }

    def nonCollectionDescriptor() {
        def attr = Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(AtomicTypeModel)
        }
        DefaultCollectionDescriptor.create(attr)
    }
}
