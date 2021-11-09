/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SettableForReadOnlyAttributeHandlerUnitTest extends Specification {
    def readOnlyAttributesConfiguration = Stub ReadOnlyAttributesConfiguration
    def handler = new SettableForReadOnlyAttributeHandler(readOnlyAttributesConfiguration)

    @Test
    def "handler is applicable for any ItemModel"() {
        expect:
        handler.isApplicable(Stub(ItemModel))
    }

    @Test
    @Unroll
    def "attribute isSettable=#result when attributesSet=#attributesSet"() {
        given:
        def descriptorName = 'modifiedtime'
        def attributeDescriptor = Stub(AttributeDescriptorModel) {
            getQualifier() >> descriptorName
        }

        and:
        readOnlyAttributesConfiguration.getReadOnlyAttributes() >> attributesSet

        expect:
        handler.isSettable(attributeDescriptor) == result

        where:
        attributesSet    | result
        []               | true
        [""]             | true
        ["modifiedtime"] | false
    }
}