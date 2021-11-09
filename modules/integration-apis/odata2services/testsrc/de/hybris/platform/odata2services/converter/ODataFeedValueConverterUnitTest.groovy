/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ODataFeedValueConverterUnitTest extends Specification {
    def entryToItemConverter = Stub(ODataEntryToIntegrationItemConverter)
    def valueConverter = Stub(PayloadAttributeValueConverter)

    def converter = new ODataFeedValueConverter(entryToItemConverter, valueConverter)

    @Test
    @Unroll
    def "isApplicable returns #applicable when attribute value is #msg and httpMethod is #httpMethod, isLocalized=#localized and isMap=#map"() {
        given:
        def parameters = conversionParametersBuilder()
                .withContext(context(httpMethod))
                .withAttributeValue(attrValue)
                .withIntegrationItem(Stub(IntegrationItem) {
                    getItemType() >> Stub(TypeDescriptor) {
                        getAttribute(_) >> Optional.of(Stub(TypeAttributeDescriptor) {
                            isMap() >> map
                            isLocalized() >> localized
                        })
                    }
                })
                .build()


        expect:
        converter.isApplicable(parameters) == applicable

        where:
        msg                        | localized | map   | attrValue       | httpMethod | applicable
        "an instance of ODataFeed" | true      | false | Stub(ODataFeed) | null       | true
        "an instance of ODataFeed" | true      | true  | Stub(ODataFeed) | null       | true
        "an instance of ODataFeed" | false     | false | Stub(ODataFeed) | null       | true
        "an instance of ODataFeed" | false     | false | Stub(ODataFeed) | "PATCH"    | false
        "null"                     | false     | false | null            | null       | false
        "an instance of ODataFeed" | false     | true  | Stub(ODataFeed) | null       | false

    }

    @Test
    def "empty collection is converted to an empty collection"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue(oDataFeedWithEntries([]))
                .build()

        expect:
        converter.convert(parameters) == []
    }

    @Test
    def "collection is converted to a collection of converted attribute values"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue(oDataFeedWithEntries([Stub(ODataEntry), Stub(ODataEntry)]))
                .build()

        def convertedEntryValue1 = Stub(ItemModel)
        def convertedEntryValue2 = Stub(ItemModel)
        valueConverter.convertAttributeValue(_ as ConversionParameters) >>> [convertedEntryValue1, convertedEntryValue2]

        expect:
        converter.convert(parameters) == [convertedEntryValue1, convertedEntryValue2]
    }

    private ODataFeed oDataFeedWithEntries(final List<ODataEntry> entries) {
        Stub(ODataFeed) {
            getEntries() >> entries
        }
    }

    def oDataEntry(Map<String, Object> properties) {
        Stub(ODataEntry) {
            getProperties() >> properties
            getProperty(_ as String) >> { properties.get(it) }
        }
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
