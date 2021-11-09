package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class MapValueConverterUnitTest extends Specification {
    def converter = new MapValueConverter()

    @Test
    @Unroll
    def "isApplicable returns #applicable if isMapAttributeValue is #mapAttributeValue and localizedValue is #localizedValue"() {
        given:
        def parameters = conversionParametersBuilder()
                .withIntegrationItem(Stub(IntegrationItem) {
                    getItemType() >> Stub(TypeDescriptor) {
                        getAttribute(_) >> Optional.of(attribute(['localized': localizedValue, 'map': mapAttributeValue]))
                    }
                })
                .build()

        expect:
        converter.isApplicable(parameters) == applicable

        where:
        mapAttributeValue | localizedValue | applicable
        true              | true           | false
        true              | false          | true
        false             | true           | false
        false             | false          | false
    }

    @Test
    def "convert returns #convertedValue if #attrValue is an instance of ODataFeed"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue(attrValue)
                .build()

        expect:
        converter.convert(parameters) == convertedValue

        where:
        attrValue                                            | convertedValue
        oDataFeed([mapEntry('param', 1), mapEntry(2, true)]) | [2: true, param: 1]
        oDataFeed([])                                        | [:]
    }

    @Test
    @Unroll
    def "convert returns null if #attrValue is not an instance of ODataFeed"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue(attrValue)
                .build()

        expect:
        converter.convert(parameters) == null

        where:
        attrValue << ['notInstanceOfODataFeed', [], oDataEntry([:])]
    }

    def mapEntry(def key, def value) {
        oDataEntry(key: key, value: value)
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

    def attribute(Map<String, Boolean> params) {
        Stub(TypeAttributeDescriptor) {
            isLocalized() >> params['localized']
            isMap() >> params['map']
        }
    }
}
