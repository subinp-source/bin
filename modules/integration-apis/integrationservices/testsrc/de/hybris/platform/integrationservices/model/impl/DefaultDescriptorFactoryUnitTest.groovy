/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.AttributeValueSetterFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultDescriptorFactoryUnitTest extends Specification {

    def factory = new DefaultDescriptorFactory(attributeValueSetterFactory: Stub(AttributeValueSetterFactory))

    @Test
    @Unroll
    def "creates a descriptor for a given integration #input model"() {
        when:
        def descriptor = factory.&(method)(model)

        then:
        descriptor
        descriptor.factory.is factory

        where:
        input                      | method                              | model
        'object'                   | 'createIntegrationObjectDescriptor' | Stub(IntegrationObjectModel)
        'item'                     | 'createItemTypeDescriptor'          | Stub(IntegrationObjectItemModel)
        'standard attribute'       | 'createTypeAttributeDescriptor'     | Stub(IntegrationObjectItemAttributeModel)
        'classification attribute' | 'createTypeAttributeDescriptor'     | Stub(IntegrationObjectItemClassificationAttributeModel)
        'virtual attribute'        | 'createTypeAttributeDescriptor'     | Stub(IntegrationObjectItemVirtualAttributeModel)
    }

    @Test
    @Unroll
    def "throws exception when integration #component model is null"() {
        when:
        factory.&(method) null

        then:
        thrown IllegalArgumentException

        where:
        component   | method
        'object'    | 'createIntegrationObjectDescriptor'
        'item'      | 'createItemTypeDescriptor'
        'attribute' | 'createTypeAttributeDescriptor'
    }

    @Test
    @Unroll
    def "get attribute value accessor factory returns #condition"() {
        given:
        factory.attributeValueAccessorFactory = accessorFactory

        when:
        def actualAccessorFactory = factory.getAttributeValueAccessorFactory()

        then:
        expectedAccessorFactory.isInstance actualAccessorFactory

        where:
        condition          | accessorFactory                            | expectedAccessorFactory
        'default factory'  | null                                       | DefaultAttributeValueAccessorFactory
        'provided factory' | Stub(DefaultAttributeValueAccessorFactory) | DefaultAttributeValueAccessorFactory
    }


    @Test
    @Unroll
    def "get attribute value setter factory returns #condition"() {
        given:
        factory.attributeValueSetterFactory = setterFactory

        when:
        def actualSetterFactory = factory.getAttributeValueSetterFactory()

        then:
        expectedSetterFactory.isInstance actualSetterFactory

        where:
        condition          | setterFactory                            | expectedSetterFactory
        'default factory'  | null                                     | NullAttributeValueSetterFactory
        'provided factory' | Stub(DefaultAttributeValueSetterFactory) | DefaultAttributeValueSetterFactory
    }

    @Test
    @Unroll
    def "get attribute settable checker returns #condition"() {
        given:
        factory.attributeSettableCheckerFactory = settableCheckerFactory

        when:
        def actualSettableChecker = factory.getAttributeSettableCheckerFactory()

        then:
        expectedSettableChecker.isInstance actualSettableChecker

        where:
        condition                 | settableCheckerFactory                       | expectedSettableChecker
        'null checker factory'    | null                                         | NullAttributeSettableCheckerFactory
        'default checker factory' | Stub(DefaultAttributeSettableCheckerFactory) | DefaultAttributeSettableCheckerFactory
    }
}
