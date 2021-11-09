/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class StandardAttributeValueSetterUnitTest extends Specification {
    private static final def DEFAULT_NAME = 'testAttribute'
    private static final def DEFAULT_QUALIFIER = 'qualifier'

    def attributeDescriptor = Stub TypeAttributeDescriptor
    def modelService = Mock ModelService
    def attributeValueSetter = new StandardAttributeValueSetter(attributeDescriptor, modelService)

    @Test
    @Unroll
    def "throws exception when instantiated with null #paramName"() {
        when:
        new StandardAttributeValueSetter(descriptor, service)

        then:
        def e = thrown IllegalArgumentException
        e.message.toLowerCase().contains paramName

        where:
        paramName              | descriptor                    | service
        'attribute descriptor' | null                          | Stub(ModelService)
        'model service'        | Stub(TypeAttributeDescriptor) | null
    }

    @Test
    @Unroll
    def 'value #isValueSet when attribute #isReadable is an ItemModel and value #equalsValue to existing one'() {
        given: 'attribute name differs from attribute qualifier'
        attributeDescriptor.getAttributeName() >> DEFAULT_NAME
        attributeDescriptor.getQualifier() >> DEFAULT_QUALIFIER
        attributeDescriptor.isReadable() >> isReadable
        and: 'given a non-null item & value'
        def value = Stub(ItemModel)
        def existingModel = Stub(ItemModel) {
            equals(value) >> isEquals
        }
        def item = Stub(ItemModel)
        modelService.getAttributeValue(item, DEFAULT_QUALIFIER) >> existingModel

        when:
        attributeValueSetter.setValue(item, value)

        then:
        setInvocations * modelService.setAttributeValue(item, DEFAULT_QUALIFIER, value)

        where:
        setInvocations | isValueSet   | isEquals | equalsValue     | isReadable | readable
        1              | 'is set'     | true     | 'is equals'     | false      | 'is not readable'
        1              | 'is set'     | false    | 'is not equals' | true       | 'is readable'
        0              | 'is not set' | true     | 'is equals'     | true       | 'is readable'
    }

    @Test
    def 'value is not set when value and model are null'() {
        given: 'attribute name differs from attribute qualifier'
        attributeDescriptor.getAttributeName() >> DEFAULT_NAME
        attributeDescriptor.getQualifier() >> DEFAULT_QUALIFIER
        attributeDescriptor.isReadable() >> true
        and: 'given a non-null item & value'
        def value = null
        def existingModel = null
        def item = Stub(ItemModel)
        modelService.getAttributeValue(item, DEFAULT_QUALIFIER) >> existingModel

        when:
        attributeValueSetter.setValue(item, value)

        then:
        0 * modelService.setAttributeValue(item, DEFAULT_QUALIFIER, value)
    }
}
