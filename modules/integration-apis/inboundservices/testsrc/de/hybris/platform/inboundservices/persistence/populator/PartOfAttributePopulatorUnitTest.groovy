/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PartOfAttributePopulatorUnitTest extends Specification {

    def modelService = Stub ModelService
    def populator = new PartOfAttributePopulator(modelService: modelService)
    private static final Locale FRENCH = Locale.FRANCE
    private static final Locale ENGLISH_CA = Locale.CANADA

    @Test
    @Unroll
    def "isApplicable = #value when attribute partOf = #partOfValue and isPrimitive = #primitiveValue"() {
        given:
        def descriptor = Stub(TypeAttributeDescriptor) {
            isPartOf() >> partOfValue
            isPrimitive() >> primitiveValue
        }

        expect:
        populator.isApplicable(descriptor, Stub(PersistenceContext)) == value

        where:
        partOfValue | primitiveValue | value
        true        | true           | false
        true        | false          | true
        false       | true           | false
        false       | false          | false
    }

    @Test
    @Unroll
    def "populate #action owner in #itemDesc referenced item"() {
        given: 'a partOf attribute'
        def ownerAttributeName = "parentItem"
        def ownerAttributeDescriptor = attributeDescriptor(ownerAttributeName)

        def descriptor = partOfDescriptor()
        descriptor.reverse() >> Optional.of(ownerAttributeDescriptor)

        and: 'an item with an existing referenced item for the partOf attribute'
        def refItem = Mock(ItemModel) {
            getProperty(ownerAttributeName) >> propertyValue
        }
        modelService.isNew(refItem) >> isNew
        def item = createItem descriptor, refItem

        and:
        descriptor.isSettable(item) >> settable

        when:
        populator.populate item, persistenceContext([descriptor])

        then:
        count * refItem.setProperty(ownerAttributeName, item)

        where:
        propertyValue   | action    | itemDesc            | settable | isNew | count
        null            | 'sets'    | 'new'               | true     | true  | 1
        null            | 'ignores' | 'existing'          | true     | false | 0
        null            | 'ignores' | 'not settable'      | false    | true  | 0
        Stub(ItemModel) | 'ignores' | 'already populated' | true     | true  | 0
    }

    @Test
    @Unroll
    def "populate #action localized property when #desc been set on the owner property"() {
        given: 'a partOf referenced item'
        def descriptor = partOfDescriptor()

        and: 'with a localized attribute representing its owner'
        def ownerAttributeName = "parentItem"
        def ownerAttributeDescriptor = localizedAttributeDescriptor(ownerAttributeName)
        descriptor.reverse() >> Optional.of(ownerAttributeDescriptor)

        and: 'an item with an existing referenced item for the partOf attribute'
        def refItem = Mock(ItemModel)
        refItem.getProperty(_ as String, propertyLocale) >> propertyValue
        modelService.isNew(refItem) >> true
        def item = createItem descriptor, refItem

        and:
        descriptor.isSettable(item) >> true

        when:
        populator.populate item, persistenceContext([descriptor])

        then:
        count * refItem.setProperty(ownerAttributeName, _, item)

        where:
        propertyValue    | propertyLocale | count | action    | desc
        null             | FRENCH         | 1     | 'sets'    | 'same locale has not'
        Stub(ItemModel)  | FRENCH         | 0     | 'ignores' | 'same locale has'
        Stub(ItemModel)  | ENGLISH_CA     | 1     | 'sets'    | 'different locale has'
    }

    @Test
    @Unroll
    def "populate #action localized property for collection attribute when #desc been set on the owner property"() {
        given: 'a partOf attribute'
        def descriptor = partOfDescriptor()

        and: 'with a localized attribute representing its owner'
        def ownerAttributeName = "parentItem"
        def ownerAttributeDescriptor = localizedAttributeDescriptor(ownerAttributeName)
        descriptor.reverse() >> Optional.of(ownerAttributeDescriptor)

        and: 'an item with an existing referenced item for the partOf attribute'
        def refItem = Mock(ItemModel)
        refItem.getProperty(_ as String, propertyLocale) >> propertyValue
        modelService.isNew(refItem) >> true
        def item = createItem descriptor, [refItem]

        and:
        descriptor.isSettable(item) >> true

        when:
        populator.populate item, persistenceContext([descriptor])

        then:
        count * refItem.setProperty(ownerAttributeName, _, item)

        where:
        propertyValue    | propertyLocale | count | action    | desc
        null             | FRENCH         | 1     | 'sets'    | 'same locale has not'
        Stub(ItemModel)  | FRENCH         | 0     | 'ignores' | 'same locale has'
        Stub(ItemModel)  | ENGLISH_CA     | 1     | 'sets'    | 'different locale has'
    }

    @Test
    @Unroll
    def "populate #action owner in the collection of #itemDesc referenced items"() {
        given: 'a partOf attribute'
        def descriptor = partOfDescriptor()

        and: 'an item with an existing referenced item for the partOf attribute'
        def refItemOne = Mock(ItemModel)
        def refItemTwo = Mock(ItemModel)
        modelService.isNew(_) >> isNew
        def item = createItem descriptor, [refItemOne, refItemTwo]

        and:
        descriptor.isSettable(item) >> settable

        when:
        populator.populate item, persistenceContext([descriptor])

        then:
        cnt * refItemOne.setOwner(item)
        cnt * refItemTwo.setOwner(item)

        where:
        action    | itemDesc       | settable | isNew | cnt
        'sets'    | 'new'          | true     | true  | 1
        'ignores' | 'existing'     | true     | false | 0
        'ignores' | 'not settable' | false    | true  | 0
    }

    @Test
    def "does not populate owner for collection when owner is already set"() {
        given: 'a partOf attribute'
        def descriptor = partOfDescriptor()

        and: 'an item with an existing referenced item for the partOf attribute'
        def refItem = Mock(ItemModel)
        and: 'the referenced Item already has an owner set'
        refItem.getOwner() >> Stub(ItemModel)
        modelService.isNew(_) >> true
        def item = createItem descriptor, [refItem]

        and:
        descriptor.isSettable(item) >> true

        when:
        populator.populate item, persistenceContext([descriptor])

        then:
        0 * refItem.setOwner(item)
    }

    @Test
    def 'populate sets all applicable attributes'() {
        given: 'an applicable attribute'
        def owner1AttributeQualifier = "parentItem"
        def owner1ItemDescriptor = attributeDescriptor(owner1AttributeQualifier)

        def attrOne = partOfDescriptor('partOfEntityOne')
        attrOne.reverse() >> Optional.of(owner1ItemDescriptor)

        and: 'another applicable attribute'
        def attrTwo = partOfDescriptor('partOfEntityTwo')

        def owner2AttributeQualifier = "parentItem2"
        def owner2ItemDescriptor = attributeDescriptor(owner2AttributeQualifier)
        attrTwo.reverse() >> Optional.of(owner2ItemDescriptor)

        and: 'the item to populate'
        def partOfEntityOne = Mock(ItemModel)
        def partOfEntityTwo = Mock(ItemModel)
        def item = Stub(ItemModel) {
            getProperty('partOfEntityOne') >> partOfEntityOne
            getProperty('partOfEntityTwo') >> partOfEntityTwo
        }

        and:
        attrOne.isSettable(item) >> true
        attrTwo.isSettable(item) >> true

        and: 'all items are new'
        modelService.isNew(_) >> true

        when:
        populator.populate item, persistenceContext([attrOne, attrTwo])

        then:
        1 * partOfEntityOne.setProperty(owner1AttributeQualifier, item)
        1 * partOfEntityTwo.setProperty(owner2AttributeQualifier, item)
    }

    @Test
    def 'populate ignores not applicable attributes'() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor) {
            getQualifier() >> 'justEntity'
            isPartOf() >> false
        }

        and: 'the item to populate'
        def justEntity = Mock(ItemModel)
        def item = Stub(ItemModel) {
            getProperty('justEntity') >> justEntity
        }

        when:
        populator.populate item, persistenceContext([nonApplicable])

        then:
        0 * justEntity._
    }

    private TypeAttributeDescriptor partOfDescriptor(String qualifier = 'refItem') {
        Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
            isPartOf() >> true
            isPrimitive() >> false
        }
    }

    private TypeAttributeDescriptor attributeDescriptor(String qualifier) {
        Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
        }
    }

    private TypeAttributeDescriptor localizedAttributeDescriptor(String qualifier) {
        Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
            isLocalized() >> true
        }
    }

    private PersistenceContext persistenceContext(Collection descriptors) {
        GroovyMock(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> descriptors
            }
            getContentLocale() >> FRENCH
        }
    }

    private ItemModel createItem(TypeAttributeDescriptor descriptor, Object value) {
        Stub(ItemModel) {
            getProperty(descriptor.qualifier) >> value
        }
    }
}
