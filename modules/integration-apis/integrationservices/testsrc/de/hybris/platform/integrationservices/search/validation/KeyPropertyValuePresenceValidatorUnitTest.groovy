/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class KeyPropertyValuePresenceValidatorUnitTest extends Specification {
    def validator = new KeyPropertyValuePresenceValidator()

    @Test
    def 'request without requested item passes validation'() {
        given:
        def request = request(null)

        when:
        validator.validate(request)

        then:
        notThrown Throwable
    }

    @Test
    @Unroll
    def "request #condition passes validation"() {
        given:
        def keyItem = integrationItem([(attribute): attrValue])
        def request = request(keyItem)

        when:
        validator.validate(request)

        then:
        notThrown Throwable

        where:
        condition                                    | attribute               | attrValue
        'for type that has no key attributes'        | nonKeyAttribute()       | null
        'with null value for nullable key attribute' | nullableKeyAttribute()  | null
        'with value for required key attribute'      | primitiveKeyAttribute() | new Object()
    }

    @Test
    def 'rejects request with null value for required key attribute'() {
        given:
        def attribute = primitiveKeyAttribute()
        def keyItem = integrationItem([(attribute): null])
        def request = request(keyItem)

        when:
        validator.validate(request)

        then:
        def e = thrown MissingRequiredKeyAttributeValueException
        e.rejectedRequest == request
        e.violatedAttribute == attribute
    }

    @Test
    def 'rejects request with null value for required key attribute in the nested key item'() {
        given:
        def nestedKeyAttribute = primitiveKeyAttribute()
        def refKeyItem = integrationItem([(nestedKeyAttribute): null])
        def item = integrationItem([
                (primitiveKeyAttribute())        : 'some value',
                (requiredReferenceKeyAttribute()): refKeyItem])
        def request = request(item)

        when:
        validator.validate(request)

        then:
        def e = thrown MissingRequiredKeyAttributeValueException
        e.rejectedRequest == request
        e.violatedAttribute == nestedKeyAttribute
    }

    @Test
    def 'accepts request with null value for optional key attribute in the nested key item'() {
        given:
        def nestedKeyAttribute = primitiveKeyAttribute()
        def refKeyItem = integrationItem([(nestedKeyAttribute): null])
        def item = integrationItem([
                (primitiveKeyAttribute())        : 'some value',
                (optionalReferenceKeyAttribute()): refKeyItem])
        def request = request(item)

        when:
        validator.validate(request)

        then:
        notThrown Throwable
    }

    ItemSearchRequest request(IntegrationItem item) {
        Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.ofNullable(item)
        }
    }

    IntegrationItem integrationItem(Map<TypeAttributeDescriptor, ?> attributes) {
        Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttributes() >> (attributes.keySet() as List)
            }
            getAttribute(_) >> { args -> attributes[args[0]] }
            getReferencedItem(_) >> { args -> attributes[args[0]] }
        }
    }

    TypeAttributeDescriptor nonKeyAttribute() {
        Stub(TypeAttributeDescriptor) {
            isKeyAttribute() >> false
        }
    }

    TypeAttributeDescriptor nullableKeyAttribute() {
        Stub(TypeAttributeDescriptor) {
            isKeyAttribute() >> true
            isNullable() >> true
        }
    }

    TypeAttributeDescriptor primitiveKeyAttribute() {
        Stub(TypeAttributeDescriptor) {
            isKeyAttribute() >> true
            isNullable() >> false
            isPrimitive() >> true
            getAttributeName() >> 'someAttribute'
            getAttributeType() >> Stub(TypeDescriptor) {
                getItemCode() >> 'SomeItem'
            }
        }
    }

    TypeAttributeDescriptor requiredReferenceKeyAttribute() {
        referenceKeyAttribute(false)
    }

    TypeAttributeDescriptor optionalReferenceKeyAttribute() {
        referenceKeyAttribute(true)
    }

    TypeAttributeDescriptor referenceKeyAttribute(final Boolean nullable) {
        Stub(TypeAttributeDescriptor) {
            isKeyAttribute() >> true
            isNullable() >> nullable
            isPrimitive() >> false
            getAttributeName() >> 'someRefAttribute'
            getAttributeType() >> Stub(TypeDescriptor) {
                getItemCode() >> 'SomeItem'
            }
        }
    }
}
