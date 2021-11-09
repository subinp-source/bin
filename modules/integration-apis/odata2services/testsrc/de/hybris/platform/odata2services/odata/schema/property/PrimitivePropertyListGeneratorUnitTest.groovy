/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.schema.property


import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitivePropertyListGeneratorUnitTest extends Specification {
    @Shared
    def itemModel = Stub IntegrationObjectItemModel

    def descriptorFactory = Stub DescriptorFactory
    def keyPropertyGenerator = Stub SchemaElementGenerator
    def propertyGenerator = Stub(SchemaElementGenerator) {
        generate(_) >> { args -> simpleEdmProperty(args[0].attributeName) }
    }
    def propertyListGenerator = new PrimitivePropertyListGenerator(
            descriptorFactory: descriptorFactory, simplePropertyGenerator: propertyGenerator, integrationKeyPropertyGenerator: keyPropertyGenerator)

    @Test
    @Unroll
    def "generates empty properties when item has #condition attributes"() {
        given: 'TypeDescriptor for the item model has no applicable attributes'
        descriptorFactory.createItemTypeDescriptor(itemModel) >> Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }
        and: 'key generator does not generate a property for the item model'
        keyPropertyGenerator.generate(itemModel) >> Optional.empty()

        expect:
        propertyListGenerator.generate(itemModel).empty

        where:
        condition            | attributes
        'no'                 | []
        'only non-primitive' | [nonPrimitiveAttribute()]
        'only collection'    | [collectionAttribute()]
    }

    @Test
    def 'generates properties when item model contains simple primitive attribute'() {
        given: 'item model has a primitive attribute'
        descriptorFactory.createItemTypeDescriptor(itemModel) >> Stub(TypeDescriptor) {
            getAttributes() >> [primitiveAttribute('code')]
        }
        and: 'key generator does not generate a property for the item model'
        keyPropertyGenerator.generate(itemModel) >> Optional.empty()

        when:
        def properties = propertyListGenerator.generate itemModel

        then:
        properties.collect({ it.name }) == ['code']
    }

    @Test
    def 'generated properties include key property when it is generated'() {
        given: 'item model has primitive attributes'
        descriptorFactory.createItemTypeDescriptor(itemModel) >> Stub(TypeDescriptor) {
            getAttributes() >> [primitiveAttribute('code'), primitiveAttribute('name')]
        }
        and: 'key generator generates a key property'
        keyPropertyGenerator.generate(itemModel) >> Optional.of(simpleEdmProperty('integrationKey'))

        when:
        def properties = propertyListGenerator.generate itemModel

        then:
        properties.collect({ it.name }) == ['code', 'name', 'integrationKey']
    }

    static SimpleProperty simpleEdmProperty(String name) {
        new SimpleProperty().setName(name)
    }

    TypeAttributeDescriptor nonPrimitiveAttribute() {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
        }
    }

    TypeAttributeDescriptor collectionAttribute() {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
        }
    }

    TypeAttributeDescriptor primitiveAttribute(String name) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
            isPrimitive() >> true
        }
    }
}
