/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.integrationkey.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultODataEntryToMapConverterUnitTest extends Specification {
    private static final String PRIMITIVE_COLLECTION_ELEMENT_PROPERTY_NAME = "value"
    def converter = new DefaultODataEntryToMapConverter()

    @Test
    @Unroll
    def "returns an empty Map when ODataEntry #param"() {
        expect:
        converter.convert(typeDescriptor, entry).isEmpty()

        where:
        param                                                   | typeDescriptor                       | entry
        'is empty'                                              | typeDescriptor([attribute('attr1')]) | oDataEntry([:])
        'includes attributes not defined on the typeDescriptor' | typeDescriptor([attribute('attr1')]) | oDataEntry(['someOtherAttr': 'value'])
    }

    @Test
    def "populates a Map when ODataEntry includes a ODataFeed with non-primitive ODataEntries"() {
        given:
        def attributeName = 'items'
        def nestedAttributeName = 'itemInnerItem'
        def nestedAttributeValue = 'stringVal'
        def entry = oDataEntry(
                [(attributeName):
                         oDataFeed([
                                 oDataEntry([(nestedAttributeName): nestedAttributeValue])
                         ])
                ]
        )
        def descriptor = typeDescriptor([
                attribute(attributeName, typeDescriptor([attribute(nestedAttributeName)]))
        ])

        when:
        def resultingMap = converter.convert(descriptor, entry)

        then:
        resultingMap == [(attributeName): [[(nestedAttributeName): nestedAttributeValue]]]
    }

    @Test
    def "populates a Map when ODataEntry includes a ODataFeed with primitive ODataEntries"() {
        given:
        def attributeName = 'stringAttr'
        def primitiveValue = 'stringVal'
        def entry = oDataEntry(
                [(attributeName):
                         oDataFeed([
                                 primitiveEntry(primitiveValue)
                         ])
                ]
        )
        def descriptor = typeDescriptor([
                primitiveAttribute(attributeName)
        ])

        when:
        def resultingMap = converter.convert(descriptor, entry)

        then:
        resultingMap == [(attributeName): [[(PRIMITIVE_COLLECTION_ELEMENT_PROPERTY_NAME): primitiveValue]]]
    }

    @Test
    def "populates a Map when ODataEntry includes a attribute for a referenced ODataEntry"() {
        given:
        def attributeName = 'item'
        def primitiveValue = 'stringVal'
        def nestedAttributeName = 'stringAttr'
        def entry = oDataEntry(
                [(attributeName):
                         oDataEntry([(nestedAttributeName): primitiveValue])
                ]
        )
        def descriptor = typeDescriptor([
                attribute(attributeName, typeDescriptor([attribute(nestedAttributeName)]))
        ])

        when:
        def resultingMap = converter.convert(descriptor, entry)

        then:
        resultingMap == [(attributeName): [(nestedAttributeName): primitiveValue]]
    }

    @Test
    def "populates a Map when ODataEntry includes a primitive attribute value"() {
        given:
        def attributeName = 'item'
        def primitiveValue = 'stringVal'
        def entry = oDataEntry([
                (attributeName): primitiveValue
        ])
        def descriptor = typeDescriptor([
                primitiveAttribute(attributeName)
        ])

        when:
        def resultingMap = converter.convert(descriptor, entry)

        then:
        resultingMap == [(attributeName): primitiveValue]
    }

    @Test
    def "populates Map when ODataEntry includes a null value"() {
        given:
        def attributeName = 'item'
        def entry = oDataEntry([
                (attributeName): null
        ])
        def descriptor = typeDescriptor([
                primitiveAttribute(attributeName)
        ])

        when:
        def resultingMap = converter.convert(descriptor, entry)

        then:
        resultingMap == [(attributeName): null]
    }

    def typeDescriptor(attributes = []) {
        Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }
    }

    def attribute(String name, TypeDescriptor typeDescriptor = null) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
            getAttributeType() >> typeDescriptor
        }
    }

    def primitiveAttribute(String name) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
            isPrimitive() >> true
        }
    }

    def oDataEntry(Map<String, Object> properties) {
        Stub(ODataEntry) {
            getProperties() >> properties
            getProperty(_ as String) >> { properties.get(it) }
        }
    }

    def oDataFeed(final Collection<ODataEntry> entries) {
        Stub(ODataFeed) {
            getEntries() >> entries
        }
    }

    def primitiveEntry(Object value) {
        oDataEntry([(PRIMITIVE_COLLECTION_ELEMENT_PROPERTY_NAME): value])
    }
}
