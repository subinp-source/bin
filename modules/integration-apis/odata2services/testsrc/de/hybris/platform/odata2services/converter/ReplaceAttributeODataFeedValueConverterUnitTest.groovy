/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.item.LocalizedValue
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull
import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ReplaceAttributeODataFeedValueConverterUnitTest extends Specification {
    private static final String LOCALIZED_ATTRIBUTES = "localizedAttributes"
    private static final String LANGUAGE = "language"
    private static final String NOT_LOCALIZED_ATTR_NAME = LOCALIZED_ATTRIBUTES + 'NOOOOOOOTT'
    private static final String PATCH_METHOD = "PATCH"
    private static final LocalizedValue NULL_LOCALIZED_VALUE = LocalizedValue.of(Locale.ENGLISH, null)
    private static final LocalizedValue NULL_VALUE_FOR_ALL_LANGUAGES = LocalizedValue.of(Locale.ENGLISH, null)
            .combine(Locale.FRENCH, null)
    def oDataConverter = Stub(ODataEntryToIntegrationItemConverter)
    def valueConverter = Stub(PayloadAttributeValueConverter)
    def provider = Stub(LocalizedValueProvider) {
        toNullLocalizedValue(_) >> NULL_LOCALIZED_VALUE
        getNullLocalizedValueForAllLanguages() >> NULL_VALUE_FOR_ALL_LANGUAGES
    }
    def converter = new ReplaceAttributeODataFeedValueConverter(oDataConverter, valueConverter, provider)

    @Test
    @Unroll
    def "isApplicable returns #applicable if replace attribute request is #replaceAttributeRequest, map attribute value is #mapAttributeValue, and attribute value is #attributeValue"() {
        given:
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(_) >> Optional.of(attribute('', [ismap: mapAttributeValue]))
            }
        }
        def parameters = conversionParametersBuilder()
                .withContext(context)
                .withAttributeValue(attributeValue)
                .withIntegrationItem(item)
                .build()

        expect:
        converter.isApplicable(parameters) == applicable

        where:
        replaceAttributeRequest | context               | mapAttributeValue | attributeValue  | applicable
        true                    | context(PATCH_METHOD) | false             | Stub(ODataFeed) | true
        false                   | context()             | false             | Stub(ODataFeed) | false
        true                    | context(PATCH_METHOD) | true              | Stub(ODataFeed) | false
        true                    | context(PATCH_METHOD) | false             | 'String'        | false
    }

    @Test
    def "non-localized empty collection is converted to an empty collection"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeName(NOT_LOCALIZED_ATTR_NAME)
                .withAttributeValue(oDataFeedWithEntries([]))
                .build()

        expect:
        converter.convert(parameters) == []
    }

    @Test
    def "non-localized collection is converted to a collection of converted attribute values"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeName(NOT_LOCALIZED_ATTR_NAME)
                .withAttributeValue(oDataFeedWithEntries([Stub(ODataEntry), Stub(ODataEntry)]))
                .build()

        def convertedEntryValue1 = Stub(ItemModel)
        def convertedEntryValue2 = Stub(ItemModel)
        valueConverter.convertAttributeValue(_ as ConversionParameters) >>> [convertedEntryValue1, convertedEntryValue2]

        expect:
        converter.convert(parameters) == [convertedEntryValue1, convertedEntryValue2]
    }

    @Test
    def "localized collection with no entries is converted to localized attributes with null localized value for all localized attributes in the integration item"() {
        given:
        def localizedAttrName1 = 'someName1'
        def localizedAttrName2 = 'someName2'
        ConversionParameters parameters = conversionParametersBuilder()
                .withAttributeName(LOCALIZED_ATTRIBUTES)
                .withAttributeValue(oDataFeedWithEntries([]))
                .withIntegrationItem(integrationItem([localizedAttribute(localizedAttrName1), localizedAttribute(localizedAttrName2), attribute('someNonLocalizedAttr')]))
                .build()

        when:
        def converted = converter.convert(parameters) as LocalizedAttributes

        then:
        converted.attributes == [(localizedAttrName1): NULL_LOCALIZED_VALUE, (localizedAttrName2): NULL_LOCALIZED_VALUE]
    }

    @Test
    def "convert when feed has entries for localized collection with multiple translations for the same attribute returns LocalizedAttributes with 1 attribute and its translations"() {
        given:
        def englishValue = 'english translation'
        def frenchValue = 'french translation'
        def localizedAttrName1 = 'someName1'
        def feed = oDataFeedWithEntries([
                oDataEntry([(LANGUAGE): 'en', (localizedAttrName1): englishValue]),
                oDataEntry([(LANGUAGE): 'fr', (localizedAttrName1): frenchValue])
        ])
        ConversionParameters parameters = conversionParametersBuilder()
                .withAttributeName(LOCALIZED_ATTRIBUTES)
                .withAttributeValue(feed)
                .withIntegrationItem(integrationItem([localizedAttribute(localizedAttrName1)]))
                .build()

        when:
        def converted = converter.convert(parameters) as LocalizedAttributes

        then:
        converted.attributes == [(localizedAttrName1): LocalizedValue.of(Locale.ENGLISH, englishValue).combine(Locale.FRENCH, frenchValue)]
    }

    @Test
    def "convert when feed has entries for localized collection with english translation for multiple attributes returns LocalizedAttributes with english translation and null for all other languages"() {
        given:
        def attr1EnglishValue = 'english translation 1'
        def attr2EnglishValue = 'english translation 2'
        def localizedAttrName1 = 'someName1'
        def localizedAttrName2 = 'someName2'
        def feed = oDataFeedWithEntries([
                oDataEntry([
                        (LANGUAGE)          : 'en',
                        (localizedAttrName1): attr1EnglishValue,
                        (localizedAttrName2): attr2EnglishValue
                ])
        ])
        ConversionParameters parameters = conversionParametersBuilder()
                .withAttributeName(LOCALIZED_ATTRIBUTES)
                .withAttributeValue(feed)
                .withIntegrationItem(integrationItem([localizedAttribute(localizedAttrName1), localizedAttribute(localizedAttrName2)]))
                .build()

        when:
        def converted = converter.convert(parameters) as LocalizedAttributes

        then:
        converted.attributes == [
                (localizedAttrName1): LocalizedValue.of(Locale.FRENCH, null).combine(Locale.ENGLISH, attr1EnglishValue),
                (localizedAttrName2): LocalizedValue.of(Locale.FRENCH, null).combine(Locale.ENGLISH, attr2EnglishValue)
        ]
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }

    private ODataFeed oDataFeedWithEntries(final List<ODataEntry> entries) {
        Stub(ODataFeed) {
            getEntries() >> entries
        }
    }

    private IntegrationItem integrationItem(final Collection<TypeAttributeDescriptor> typeAttributeDescriptors) {
        Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttributes() >> typeAttributeDescriptors
            }
        }
    }

    private TypeAttributeDescriptor localizedAttribute(localizedAttrName) {
        Stub(TypeAttributeDescriptor) {
            isLocalized() >> true
            getAttributeName() >> localizedAttrName
        }
    }

    private TypeAttributeDescriptor attribute(final String localizedAttrName, final Map<String, Boolean> params = [:]) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> localizedAttrName
            isMap() >> falseIfNull(params['ismap'])
        }
    }

    def oDataEntry(Map<String, Object> properties) {
        Stub(ODataEntry) {
            getProperties() >> properties
            getProperty(_ as String) >> { properties.get(it) }
        }
    }
}