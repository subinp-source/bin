/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.UnmodifiableAttributeException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.CollectionDescriptor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ItemModelCollectionAttributePopulatorUnitTest extends Specification {
    def attributeValueAccessor = Mock AttributeValueAccessor
    def itemModelService = Stub de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService
    def populator = new ItemModelCollectionAttributePopulator(contextReferencedItemModelService: itemModelService)

    @Shared
    def item1 = Stub(ItemModel)
    @Shared
    def item2 = Stub(ItemModel)
    @Shared
    def item3 = Stub(ItemModel)
    @Shared
    def object1 = new Object()

    @Test
    @Unroll
    def "isApplicable is #expected when isCollection=#collection and isPrimitive=#primitive"() {
        given: "an attribute: collection=#collection, primitive=#primitive"
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> primitive
            isCollection() >> collection
            isSettable(_) >> true
        }

        and: 'a persistence context that does not require attribute replacement'
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> false
        }

        when:
        def isApplicable = populator.isApplicable(typeAttributeDescriptor, context)

        then:
        isApplicable == expected

        where:
        collection | primitive | expected
        true       | true      | false
        true       | false     | true
        false      | true      | false
        false      | false     | false
    }

    @Test
    def 'isApplicable is false when persistence context is for replacing attributes'() {
        given: 'a type attribute to which the attribute populator is applicable'
        def attributeDescriptor = attributeDescriptor()

        and: 'a persistence context that requires attribute replacement'
        def context = Stub(PersistenceContext) {
            isReplaceAttributes() >> true
        }

        expect:
        ! populator.isApplicable(attributeDescriptor, context)
    }

    @Test
    @Unroll
    def "populate() combines existing #existingValues values and new #newValues values to produce #combinedValues"() {
        given:
        def item = Stub ItemModel

        and:
        def descriptor = attributeDescriptor()
        descriptor.getCollectionDescriptor() >> Stub(CollectionDescriptor) {
            newCollection() >> newValues
        }

        and: "persistence context which resolves payload attribute value to #payloadItems"
        def context = contextWithAttributesInPayload(descriptor)
        itemModelService.deriveItemsReferencedInAttributeValue(context, descriptor) >> payloadItems

        and: 'values already exist for the attribute'
        attributeValueAccessor.getValue(item) >> existingValues

        when:
        populator.populate item, context

        then:
        1 * attributeValueAccessor.setValue(item, combinedValues)

        where:
        newValues | existingValues        | payloadItems          | combinedValues
        []        | []                    | []                    | []
        [] as Set | [item1, item2]        | []                    | [item1, item2] as Set
        null      | []                    | [item1, item2, item3] | [item1, item2, item3]
        null      | new Object()          | [item1, item2, item3] | [item1, item2, item3]
        [object1] | [item1, item2, item3] | []                    | [object1, item1, item2, item3]
    }

    @Test
    def 'populate throws exception when setting an attribute'() {
        given:
        def item = Stub ItemModel
        def descriptor = attributeDescriptor()
        def context = contextWithAttributesInPayload(descriptor)
        itemModelService.deriveItemsReferencedInAttributeValue(context, descriptor) >> [item1, item2]
        and: 'the attribute is not settable'
        attributeValueAccessor.setValue(item, [item1, item2]) >> { throw new AttributeNotSupportedException('IGNORE - testing exception', 'items')}

        when:
        populator.populate item, context

        then:
        def e = thrown UnmodifiableAttributeException
        e.attributeDescriptor == descriptor
        e.persistenceContext == context
    }

    @Test
    def 'populate sets all applicable attributes'() {
        given: 'an applicable attribute'
        def attributeOne = attributeDescriptor()
        and: 'another applicable attribute'
        def valueAccessor2 = Mock AttributeValueAccessor
        def attributeTwo = attributeDescriptor(valueAccessor2)
        and: 'a context for the item to populate'
        def item = Stub ItemModel
        def context = contextWithAttributesInPayload([attributeOne, attributeTwo])
        itemModelService.deriveItemsReferencedInAttributeValue(context, attributeOne) >> [item1]
        itemModelService.deriveItemsReferencedInAttributeValue(context, attributeTwo) >> [item2]

        when:
        populator.populate(item, context)

        then:
        1 * attributeValueAccessor.setValue(item, [item1])
        1 * valueAccessor2.setValue(item, [item2])
    }

    @Test
    def 'populate ignores not applicable attributes'() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor) {
            isCollection() >> false
            isPrimitive() >> true
            setter() >> attributeValueAccessor
            isSettable(_) >> true
        }
        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = contextWithAttributesInPayload(nonApplicable)
        itemModelService.deriveItemsReferencedInAttributeValue(context, nonApplicable) >> [Stub(ItemModel)]

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(item, _)
    }

    @Test
    def 'populate ignores non settable attributes of old items'() {
        given: "a non-settable attribute"
        def attribute = Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            setter() >> attributeValueAccessor
            isSettable(_) >> false
        }
        and:
        def item = Stub ItemModel

        and: 'a context for the item to populate'
        def context = contextWithAttributesInPayload(attribute)
        itemModelService.deriveItemsReferencedInAttributeValue(context, attribute) >> [Stub(ItemModel)]

        when:
        populator.populate item, context

        then:
        0 * attributeValueAccessor.setValue(item, _)
    }

    private TypeAttributeDescriptor attributeDescriptor(AttributeValueAccessor valueAccessor = attributeValueAccessor) {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
            accessor() >> valueAccessor
            isSettable(_) >> true
        }
    }

    private PersistenceContext contextWithAttributesInPayload(TypeAttributeDescriptor descriptor) {
        contextWithAttributesInPayload([descriptor])
    }

    private PersistenceContext contextWithAttributesInPayload(Collection<TypeAttributeDescriptor> descriptors) {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttributes() >> descriptors
            }
        }
    }
}
