/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultIntegrationItemToMapConverterUnitTest extends Specification {

    def generator = new DefaultIntegrationItemToMapConverter()

    @Test
    def "map generated for IntegrationItem with only primitive attributes"() {
        given: 'IntegrationItem only containing primitive attributes'
        def attributeName1 = 'id'
        def attributeName2 = 'name'
        def attributeName3 = 'altName'
        def descriptor1 = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName1
        }
        def descriptor2 = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName2
        }
        def descriptor3 = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attributeName3
        }
        def primitivesIntegrationItem = Stub(IntegrationItem) {
            getAttributes() >> [descriptor1, descriptor2, descriptor3]
            getAttribute(attributeName1) >> '123'
            getAttribute(attributeName2) >> 'shirt'
            getAttribute(attributeName3) >> null
        }
        when:
        def generatedMap = generator.convert(primitivesIntegrationItem)
        then:
        generatedMap == ['id': '123', 'name': 'shirt', 'altName': null]
    }

    @Test
    def "map generated for IntegrationItem with primitive attributes and reference attributes"() {
        given: 'IntegrationItem containing primitive and reference attributes'
        def name = 'name'
        def category = 'category'
        def categoryId = 'categoryId'
        def nameDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
        }
        def categoryDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> category
        }
        def categoryIdDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> categoryId
        }
        def nestedIntegrationItem = Stub(IntegrationItem) {
            getAttributes() >> [categoryIdDescriptor]
            getAttribute(categoryId) >> 'clothes'
        }
        def mixedIntegrationItem = Stub(IntegrationItem) {
            getAttributes() >> [nameDescriptor, categoryDescriptor]
            getAttribute(name) >> 'shirt'
            getAttribute(category) >> nestedIntegrationItem
        }
        when:
        def generatedMap = generator.convert(mixedIntegrationItem)
        then:
        generatedMap == ['name': 'shirt', 'category': ['categoryId': 'clothes']]
    }


}
