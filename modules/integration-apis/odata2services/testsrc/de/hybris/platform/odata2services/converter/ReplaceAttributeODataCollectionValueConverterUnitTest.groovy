/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ReplaceAttributeODataCollectionValueConverterUnitTest extends Specification {
    def valueConverter = Stub(PayloadAttributeValueConverter)
    def converter = new ReplaceAttributeODataCollectionValueConverter(Stub(ODataEntryToIntegrationItemConverter), valueConverter, Stub(LocalizedValueProvider))

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
        msg                           | attrValue        | httpMethod | applicable
        "an instance of a Collection" | [] as Collection | "PATCH"    | true
        "an instance of a Collection" | [] as Collection | null       | false
        "a String"                    | 'String'         | "PATCH"    | false
    }

    @Test
    def "empty collection is converted to an empty collection"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue([] as Collection)
                .build()

        expect:
        converter.convert(parameters) == []
    }

    @Test
    def "collection is converted to a collection of converted attribute values"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue([Stub(ODataEntry), Stub(ODataEntry)])
                .build()

        def convertedEntryValue1 = Stub(ItemModel)
        def convertedEntryValue2 = Stub(ItemModel)
        valueConverter.convertAttributeValue(_ as ConversionParameters) >>> [convertedEntryValue1, convertedEntryValue2]

        expect:
        converter.convert(parameters) == [convertedEntryValue1, convertedEntryValue2]
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
