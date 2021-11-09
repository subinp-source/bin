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
import de.hybris.platform.integrationservices.model.*
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LocalizedPropertyListGeneratorUnitTest extends Specification {
    @Shared
    def itemModel = Stub IntegrationObjectItemModel

    def descriptorFactory = Stub DescriptorFactory
    def propertyGenerator = Stub(SchemaElementGenerator) {
        generate(_) >> { args -> simpleEdmProperty(args[0].attributeName) }
    }
    def propertyListGenerator = new LocalizedPropertyListGenerator(
            descriptorFactory: descriptorFactory, simplePropertyGenerator: propertyGenerator)

    @Test
    @Unroll
    def "generates only 'language' property when item has #condition attributes"() {
        given: 'TypeDescriptor for the item model has no applicable attributes'
        descriptorFactory.createItemTypeDescriptor(itemModel) >> Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }

        when:
        def properties = propertyListGenerator.generate(itemModel)

        then: 'properties contain only language attribute'
        properties.size() == 1
        with (properties[0]) {
            name == 'language'
            type == EdmSimpleTypeKind.String
            !annotationAttributes.empty
            with (annotationAttributes[0]) {
                name == 'Nullable'
                text == 'false'
            }
        }

        where:
        condition            | attributes
        'no'                 | []
        'only non-primitive' | [nonPrimitiveAttribute()]
        'only collection'    | [collectionAttribute()]
        'only non-localized' | [nonLocalizedAttribute()]
    }

    @Test
    def 'generates properties when item model contains simple localized attribute'() {
        given: 'item model has a primitive attribute'
        descriptorFactory.createItemTypeDescriptor(itemModel) >> Stub(TypeDescriptor) {
            getAttributes() >> [primitiveLocalizedAttribute('name')]
        }

        when:
        def properties = propertyListGenerator.generate itemModel

        then:
        properties.collect({ it.name }) == ['name', 'language']
    }

    IntegrationObjectItemModel mockIntegrationObjectItemModel(final HashSet<IntegrationObjectItemAttributeModel> attributes) {
        Stub(IntegrationObjectItemModel) {
            getAttributes() >> attributes
        }
    }

    static SimpleProperty simpleEdmProperty(String name) {
        new SimpleProperty().setName(name)
    }

    TypeAttributeDescriptor nonPrimitiveAttribute() {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isLocalized() >> true
        }
    }

    TypeAttributeDescriptor collectionAttribute() {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> true
        }
    }

    TypeAttributeDescriptor nonLocalizedAttribute() {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            isLocalized() >> false
        }
    }

    TypeAttributeDescriptor primitiveLocalizedAttribute(String name) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
            isPrimitive() >> true
            isLocalized() >> true
        }
    }
}