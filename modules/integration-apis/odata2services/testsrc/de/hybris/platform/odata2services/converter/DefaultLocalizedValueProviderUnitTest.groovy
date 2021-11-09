/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.LocalizedValue
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll
import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class DefaultLocalizedValueProviderUnitTest extends Specification {

    private static final LocalizedValue NULL_LOCALIZED_VALUES = LocalizedValue.of(Locale.ENGLISH, null)
            .combine(Locale.GERMAN, null)
            .combine(Locale.FRENCH, null)
    def service = Stub(IntegrationLocalizationService) {
        getAllSupportedLocales() >> [Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN]
    }
    def provider = new DefaultLocalizedValueProvider(service)

    @Test
    def "takes content locale and attribute value from conversion parameters and returns a localizedValue"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue('string')
                .withContentLocale(Locale.ENGLISH)
                .build()

        expect:
        provider.toLocalizedValue(parameters) == LocalizedValue.of(Locale.ENGLISH, 'string')
    }

    @Test
    def "returns null localized value for all given languages"() {
        expect:
        provider.getNullLocalizedValueForAllLanguages() == NULL_LOCALIZED_VALUES
    }

    @Test
    @Unroll
    def "returns #localizedValue if content language is #present"() {
        given:
        def parameters = conversionParametersBuilder()
                .withContentLocale(Locale.ENGLISH)
                .withContext(Stub(ODataContext) {
                    getRequestHeader('Content-Language') >> present
                })
                .build()

        expect:
        provider.toNullLocalizedValue(parameters) == localizedValue

        where:
        localizedValue                          | present
        LocalizedValue.of(Locale.ENGLISH, null) | 'en'
        NULL_LOCALIZED_VALUES                   | null
    }
}
