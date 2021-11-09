/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ODataCollectionValueConverterUnitTest extends Specification {
    def entryToItemConverter = Stub(ODataEntryToIntegrationItemConverter)
    def attributeValueConverter = Stub(PayloadAttributeValueConverter)

    def converter = new ODataCollectionValueConverter(entryToItemConverter, attributeValueConverter)

    @Test
    @Unroll
    def "isApplicable returns #applicable when attribute value is #msg and httpMethod is #httpMethod"() {
        given:
        def parameters = conversionParametersBuilder()
                .withContext(context(httpMethod))
                .withAttributeValue(attrValue)
                .build()


        expect:
        converter.isApplicable(parameters) == applicable

        where:
        msg            | attrValue        | httpMethod | applicable
        "a collection" | [] as Collection | null       | true
        "a collection" | [] as Collection | "PATCH"    | false
        "null"         | null             | null       | false
        "a string"     | 'String'         | null       | false
    }

    @Test
    def "converts an empty collection as an object to a collection"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue([])
                .build()

        when:
        def converted = converter.convert(parameters)

        then:
        converted as Collection == []
    }

    @Test
    def "converts a collection with values to a collection of converted values"() {
        given:
        def value1 = 'string1'
        def value2 = 'string2'
        def convertedValue1 = 'convertedString1'
        def convertedValue2 = 'convertedString2'
        def parameters = conversionParametersBuilder()
                .withAttributeValue([value1, value2])
                .build()
        attributeValueConverter.convertAttributeValue(_ as ConversionParameters) >>> [convertedValue1, convertedValue2]

        when:
        def converted = converter.convert(parameters)

        then:
        converted as Collection == [convertedValue1, convertedValue2]
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
