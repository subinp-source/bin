/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitiveAttributePopulatorUnitTest extends Specification {

    def modelService = Stub ModelService
    def valueAccessor = Mock AttributeValueAccessor
    def populator = new PrimitiveAttributePopulator(modelService: modelService)

    @Test
    @Unroll
    def "populate ignores the attribute when it has isPrimitive = #primitive and isCollection = #collection"() {
        given: 'a non-applicable attribute'
        def nonApplicable = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> primitive
            isCollection() >> collection
            accessor() >> valueAccessor
            isSettable(_) >> true
        }

        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = persistenceContext(nonApplicable)

        when:
        populator.populate item, context

        then:
        0 * valueAccessor.setValue(item, _)

        where:
        primitive | collection
        false     | false
        true      | true
        false     | true
    }

    @Test
    def "populate converts primitive value before it is set"() {
        given:
        def attribute = attributeDescriptor()
        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def value = 5
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(attribute) >> value
                getAttributes() >> [attribute]
            }
        }
        and: 'handler converts value to string'
        def handler = Stub(PrimitiveValueHandler) {
            convert(attribute, _) >> { a, val -> val as String }
        }
        and:
        populator.setPrimitiveValueHandler(handler)

        when:
        populator.populate item, context

        then: 'converted value is set'
        1 * valueAccessor.setValue(item, '5')
    }

    @Test
    def 'populate sets all applicable attributes'() {
        given: 'an applicable attribute'
        def stringValue = 'text'
        def stringAttr = attributeDescriptor()

        and: 'another applicable attribute with its own attribute setter'
        def intValue = 7
        def setterForIntAttr = Mock AttributeValueAccessor
        def intAttr = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            isSettable(_) >> true
            accessor() >> setterForIntAttr
        }

        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(stringAttr) >> stringValue
                getAttribute(intAttr) >> intValue
                getAttributes() >> [stringAttr, intAttr]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * valueAccessor.setValue(item, stringValue)
        1 * setterForIntAttr.setValue(item, intValue)
    }

    @Test
    @Unroll
    def "populate #action #attrKind attribute for an item"() {
        given: 'an attribute'
        def keyAttr = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            accessor() >> valueAccessor
            isSettable(_) >> settable
        }

        and: "an item to populate"
        def item = Stub(ItemModel)

        when:
        populator.populate item, persistenceContext(keyAttr, 'value')

        then:
        cnt * valueAccessor.setValue(item, 'value')

        where:
        action    | attrKind    | settable | cnt
        'sets'    | 'writable'  | true     | 1
        'ignores' | 'read-only' | false    | 0
    }

    @Test
    def "populate throws exception when null value is provided for a required attribute"() {
        given: 'an applicable attribute'
        def parentItemCode = 'itemCode'
        def name = 'stringAttrName'
        def stringAttr = attributeDescriptor()
        stringAttr.isNullable() >> false
        stringAttr.getAttributeName() >> name
        stringAttr.getTypeDescriptor() >> Stub(TypeDescriptor) {
            getItemCode() >> parentItemCode
        }

        and: 'a context for the item to populate'
        def item = Stub ItemModel
        def context = persistenceContext stringAttr, null

        when:
        populator.populate item, context

        then:
        def e = thrown(MissingRequiredAttributeValueException)
        with(e) {
            message.contains(name)
            message.contains(parentItemCode)
        }
    }

    @Test
    @Unroll
    def 'populate sets nullable attribute to null value when nullable'() {
        given: 'an applicable attribute'
        def stringValue = null
        def stringAttr = attributeDescriptor(clazz)
        stringAttr.isNullable() >> true

        and: 'a context for the item to populate'
        def item = Stub(ItemModel)
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(stringAttr) >> stringValue
                getAttributes() >> [stringAttr]
            }
        }

        when:
        populator.populate item, context

        then:
        1 * valueAccessor.setValue(item, stringValue)

        where:
        clazz << [Calendar, String, Character, Integer, Boolean, BigInteger]
    }

    private TypeAttributeDescriptor attributeDescriptor(Class attrType = String.class) {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> attrType.name
            }
            accessor() >> valueAccessor
            isSettable(_) >> true
        }
    }

    private PersistenceContext persistenceContext(TypeAttributeDescriptor attr, def value = 'value') {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getAttribute(attr) >> value
                getAttributes() >> [attr]
            }
        }
    }
}
