/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultAttributeValueSetterFactoryUnitTest extends Specification {

    def factory = new DefaultAttributeValueSetterFactory(
            modelService: Stub(ModelService),
            valueHandlers: [],
            valueConverters: [])

    @Test
    @Unroll
    def "create with #descriptor type attribute descriptor returns #valueSetterType value getter"() {
        when:
        def setter = factory.create descriptor

        then:
        valueSetterType.isInstance setter

        where:
        descriptor                                  | valueSetterType
        Stub(TypeAttributeDescriptor)               | StandardAttributeValueSetter
        Stub(ClassificationTypeAttributeDescriptor) | ClassificationAttributeValueSetter
        null                                        | NullAttributeValueSetter
    }

    @Test
    def "handlers and converters are never null"() {
        given:
        def factory = new DefaultAttributeValueSetterFactory(
                modelService: Stub(ModelService),
                valueHandlers:  null,
                valueConverters:  null)

        expect:
        with(factory) {
            valueHandlers == []
            valueConverters == []
        }
    }
}
