/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService
import de.hybris.platform.inboundservices.persistence.populator.UnmodifiableAttributeException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.core.PK.fromLong

@UnitTest
class ItemModelAttributePopulatorUnitTest extends Specification {

    def modelService = Stub ModelService
    def attributeValueAccessor = Mock AttributeValueAccessor
    def itemModelService = Stub ContextReferencedItemModelService

    def populator = new ItemModelAttributePopulator(
            modelService: modelService,
            contextReferencedItemModelService: itemModelService)

    @Test
    @Unroll
    def "isApplicable is #expected when isCollection=#collection and isPrimitive=#primitive"() {
        given:
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> primitive
            isCollection() >> collection
            isSettable(_) >> true
        }

        when:
        def isApplicable = populator.isApplicable(typeAttributeDescriptor, Stub(PersistenceContext))

        then:
        isApplicable == expected

        where:
        collection | primitive | expected
        true       | true      | false
        true       | false     | false
        false      | true      | false
        false      | false     | true
    }

    @Test
    def "attribute is not populated when attribute is map"() {
        given:
        def attribute = Stub(TypeAttributeDescriptor) {
            isMap() >> true
            isSettable(_) >> true
        }
        and:
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> [attribute]
            }
        }

        when:
        populator.populate(Stub(ItemModel), context)

        then:
        0 * attributeValueAccessor.setValue(_, _)
    }

    @Test
    @Unroll
    def "populate #action #attrKind attribute of an item"() {
        given: 'an attribute'
        def attribute = Stub(TypeAttributeDescriptor) {
            getQualifier() >> 'refItem'
            accessor() >> attributeValueAccessor
            isSettable(_) >> settable
        }

        and:
        def item = Stub(ItemModel)

        and: 'a context for the item to populate'
        def context = persistenceContext(attribute)

        and: 'the attribute value resolves to the refItem'
        def refItem = Stub ItemModel
        itemModelService.deriveReferencedItemModel(attribute, context) >> refItem

        when:
        populator.populate item, context

        then:
        cnt * attributeValueAccessor.setValue(item, refItem)

        where:
        action    | attrKind    | settable | cnt
        'sets'    | 'writable'  | true     | 1
        'ignores' | 'read-only' | false    | 0
    }

    @Test
    def "populate sets attribute value to enum type value when attribute descriptor is an enumeration"() {
        given:
        def attributeValue = Stub ItemModel
        def parentItem = Stub ItemModel
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            getQualifier() >> "attributeQualifierForReferenceItemAttribute"
            getAttributeType() >> Stub(TypeDescriptor) {
                isEnumeration() >> true
            }
            accessor() >> attributeValueAccessor
            isSettable(_) >> true
        }
        def context = persistenceContext(typeAttributeDescriptor)
        final String enumTypeValue = "this can be any object"
        itemModelService.deriveReferencedItemModel(typeAttributeDescriptor, context) >> attributeValue
        attributeValue.getPk() >> fromLong(1234L)
        modelService.get(_ as PK) >> enumTypeValue

        when:
        populator.populate(parentItem, context)

        then:
        1 * attributeValueAccessor.setValue(parentItem, enumTypeValue)
    }

    @Test
    def 'populate throws exception when setting an attribute'() {
        given:
        def attributeValue = Stub ItemModel
        def parentItem = Stub ItemModel
        def attributeDescriptor = attributeDescriptor 'referenceAttribute'
        def context = persistenceContext(attributeDescriptor)
        and: 'attribute reference resolves to attributeValue'
        itemModelService.deriveReferencedItemModel(attributeDescriptor, context) >> attributeValue
        and: 'setting the attribute throws an exception'
        attributeValueAccessor.setValue(parentItem, attributeValue) >> {
            throw new AttributeNotSupportedException('IGNORE - testing exception', 'referenceAttribute')
        }

        when:
        populator.populate parentItem, context

        then:
        def e = thrown UnmodifiableAttributeException
        e.attributeDescriptor == attributeDescriptor
        e.persistenceContext == context
    }

    @Test
    def 'populates all applicable attributes in an item'() {
        given: 'an applicable attribute'
        def attributeOneValue = Stub ItemModel
        def attributeOne = attributeDescriptor 'one'
        and: 'another applicable attribute'
        def attributeTwoValue = Stub ItemModel
        def valueAccessor2 = Mock AttributeValueAccessor
        def attributeTwo = attributeDescriptor 'two', valueAccessor2
        and: 'a context for the item to populate'
        def parentItem = Stub ItemModel
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getReferencedItem(attributeOne) >> Stub(IntegrationItem)
                getAttributes() >> [attributeOne, attributeTwo]
            }
        }
        itemModelService.deriveReferencedItemModel(attributeOne, context) >> attributeOneValue
        itemModelService.deriveReferencedItemModel(attributeTwo, context) >> attributeTwoValue

        when:
        populator.populate(parentItem, context)

        then:
        1 * attributeValueAccessor.setValue(parentItem, attributeOneValue)
        1 * valueAccessor2.setValue(parentItem, attributeTwoValue)
    }

    @Test
    def 'populate ignores not applicable attributes'() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor) {
            getQualifier() >> 'primitive'
            isPrimitive() >> true
            accessor() >> attributeValueAccessor
            isSettable(_) >> true
        }
        and: 'a context for the item to populate'
        def item = Stub ItemModel
        def context = persistenceContext(nonApplicable)
        itemModelService.deriveReferencedItemModel(nonApplicable, context) >> Stub(ItemModel)

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(item, _)
    }

    private TypeAttributeDescriptor attributeDescriptor(String qualifier, AttributeValueAccessor valueAccessor = attributeValueAccessor) {
        Stub(TypeAttributeDescriptor) {
            getQualifier() >> qualifier
            accessor() >> valueAccessor
            isSettable(_) >> true
        }
    }

    private PersistenceContext persistenceContext(TypeAttributeDescriptor descriptor) {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getReferencedItem(descriptor) >> Stub(IntegrationItem)
                getAttributes() >> [descriptor]
            }
        }
    }
}
