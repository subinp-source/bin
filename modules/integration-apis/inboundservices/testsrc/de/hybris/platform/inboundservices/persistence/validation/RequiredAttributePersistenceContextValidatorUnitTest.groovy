/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.MissingRequiredAttributeValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class RequiredAttributePersistenceContextValidatorUnitTest extends Specification {
    private def validator = new RequiredAttributePersistenceContextValidator()
    
    @Unroll
    @Test
    def "#condition result in valid integration item"() {
        given:
        def item = integrationItem(attributes)

        when:
        validator.validate(persistenceContext(item))

        then:
        noExceptionThrown()

        where:
        condition             | attributes
        'no attributes'       | []
        'nullable attributes' | [nullableAttr(), nullableAttr()]
    }

    @Unroll
    @Test
    def "non-nullable attribute with #value results in valid item"() {
        given:
        def item = integrationItem([attr])
        item.getAttribute(_ as TypeAttributeDescriptor) >> value

        when:
        validator.validate(persistenceContext(item))

        then:
        noExceptionThrown()

        where:
        attr             | value
        primitiveAttr()  | 'value'
        referenceAttr()  | Stub(IntegrationItem)
        collectionAttr() | ['value1', 'value2']
        mapAttr()        | [key: 'value']
    }

    @Unroll
    @Test
    def "non-nullable #attributeType attribute with null value results in exception thrown"() {
        given:
        def item = integrationItem([attr])
        item.getAttribute(_ as TypeAttributeDescriptor) >> null

        when:
        validator.validate(persistenceContext(item))

        then:
        thrown MissingRequiredAttributeValueException

        where:
        attributeType          | attr
        'reference'            | referenceAttr()
        'collection reference' | collectionAttr()
        'map reference'        | mapAttr()
    }

    @Unroll
    @Test
    def "non-nullable primitive attribute with null value results in exception thrown"() {
        given:
        def item = integrationItem([primitiveAttr()])
        item.getAttribute(_ as TypeAttributeDescriptor) >> null

        when:
        validator.validate(persistenceContext(item))

        then:
        thrown MissingRequiredAttributeValueException
    }

    private PersistenceContext persistenceContext(IntegrationItem item) {
        Stub(PersistenceContext) {
            getIntegrationItem() >> item
        }
    }

    private IntegrationItem integrationItem(List attributes = []) {
        Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttributes() >> attributes
            }
        }
    }

    private def nullableAttr() {
        attr(isNullable: true)
    }

    private def primitiveAttr() {
        attr(isNullable: false, isPrimitive: true)
    }

    private def referenceAttr() {
        attr(isNullable: false, isPrimitive: false)
    }

    private def mapAttr() {
        attr([isNullable: false, isPrimitive: true, isMap: true])
    }

    private def collectionAttr() {
        attr([isNullable: false, isPrimitive: true, isCollection: true])
    }

    private def attr(Map metadata) {
        Stub(TypeAttributeDescriptor) {
            isNullable() >> { metadata.isNullable ?: false }
            isPrimitive() >> { metadata.isPrimitive ?: false }
            isCollection() >> { metadata.isCollection ?: false }
            isMap() >> { metadata.isMap ?: false }
        }
    }
}

