/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.item.LocalizedValue
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll
import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ReplaceAttributeSingularLocalizedAttributeValueConverterUnitTest extends Specification {
    static final def PATCH_METHOD = "PATCH";
    static final LocalizedValue LOCALIZED_VALUE = LocalizedValue.of(Locale.ENGLISH, 'value')
    static final LocalizedValue NULL_LOCALIZED_VALUE = LocalizedValue.of(Locale.ENGLISH, null)
            .combine(LocalizedValue.of(Locale.GERMAN, null))

    def provider = Stub(LocalizedValueProvider) {
        toLocalizedValue(_) >> LOCALIZED_VALUE
        toNullLocalizedValue(_) >> NULL_LOCALIZED_VALUE
    }
    def converter = new ReplaceAttributeSingularLocalizedAttributeValueConverter(provider)

    @Test
    @Unroll
    def "isApplicable returns #applicable when collection is #collection and localized is #localized and httpMethod is #httpMethod"() {
        given:
        def parameters = conversionParametersBuilder()
                .withContext(context(httpMethod))
                .withIntegrationItem(Stub(IntegrationItem) {
                    getItemType() >> Stub(TypeDescriptor) {
                        getAttribute(_) >> Optional.of(Stub(TypeAttributeDescriptor) {
                            isCollection() >> collection
                            isLocalized() >> localized
                        })
                    }
                })
                .build()

        expect:
        converter.isApplicable(parameters) == applicable

        where:
        collection | localized | applicable | httpMethod
        false      | true      | true       | PATCH_METHOD
        true       | true      | false      | PATCH_METHOD
        false      | false     | false      | PATCH_METHOD
        false      | true      | false      | null
        true       | true      | false      | null
        true       | false     | false      | null
        true       | false     | false      | PATCH_METHOD
        false      | false     | false      | null
    }

    @Test
    def "converts null attribute value to null localized value"() {
        given:
        def parameters = conversionParameters(null)

        when:
        LocalizedValue item = converter.convert(parameters) as LocalizedValue

        then:
        item == NULL_LOCALIZED_VALUE
    }

    @Test
    def "converts non-null attribute value to localized value for the context content language and resets all other locales to null"() {
        given:
        def parameters = conversionParameters("nonNullValue")

        when:
        LocalizedValue item = converter.convert(parameters) as LocalizedValue

        then:
        item == NULL_LOCALIZED_VALUE.combine(LOCALIZED_VALUE)
    }

    def conversionParameters(String value) {
        conversionParametersBuilder()
                .withAttributeValue(value)
                .build()
    }

    def context(final String httpMethod) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
