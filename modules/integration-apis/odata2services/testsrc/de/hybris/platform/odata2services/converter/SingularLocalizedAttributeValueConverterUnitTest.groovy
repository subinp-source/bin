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
class SingularLocalizedAttributeValueConverterUnitTest extends Specification {
    static final def LOCALIZED_VALUE = LocalizedValue.of(Locale.ENGLISH, 'value')
    static final def CONVERSION_PARAMETERS = conversionParametersBuilder().build()
    static final def PATCH_METHOD = "PATCH";

    def provider = Stub(LocalizedValueProvider) {
        toLocalizedValue(CONVERSION_PARAMETERS) >> LOCALIZED_VALUE
    }
    def converter = new SingularLocalizedAttributeValueConverter(provider)

    @Test
    def "converts parameters to a localizedValue"() {
        expect:
        converter.convert(CONVERSION_PARAMETERS) == LOCALIZED_VALUE
    }

    @Test
    @Unroll
    def "isApplicable returns #applicable when collection is #collection and localized is #localized and httpMethod is #httpMethod"() {
        given:
        def parameters = conversionParametersBuilder()
                .withContext(context(httpMethod))
                .withIntegrationItem(Stub(IntegrationItem) {
                    getItemType() >> Stub(TypeDescriptor) {
                        getAttribute(_) >> Optional.of(Stub(TypeAttributeDescriptor) {
                            isLocalized() >> localized
                            isCollection() >> collection
                        })
                    }
                })
                .build()


        expect:
        converter.isApplicable(parameters) == applicable

        where:
        collection | localized | applicable | httpMethod
        true       | true      | false      | PATCH_METHOD
        true       | true      | false      | null
        true       | false     | false      | PATCH_METHOD
        true       | false     | false      | null
        false      | true      | false      | PATCH_METHOD
        false      | true      | true       | null
        false      | false     | false      | PATCH_METHOD
        false      | false     | false      | null
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
