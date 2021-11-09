/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.odata.persistence.ConversionOptions.conversionOptionsBuilder
import static de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest.itemConversionRequestBuilder

@UnitTest
class ItemConversionRequestBuilderUnitTest extends Specification {
    private static final Locale locale = Locale.ENGLISH
    private static String code = "IntegrationObjectCode"

    @Shared
    def itemModel = Stub(ItemModel)
    @Shared
    def entityType = Stub(EdmEntityType)
    @Shared
    def entitySet = Stub(EdmEntitySet) {
        getEntityType() >> entityType
    }

    @Test
    @Unroll
    def "build() throws IllegalArgumentException when #attr is null"() {
        when:
        itemConversionRequestBuilder()
                .withEntitySet(set)
                .withAcceptLocale(loc)
                .withValue(value)
                .withIntegrationObject(objCode)
                .build()

        then:
        thrown(IllegalArgumentException)

        where:
        attr                      | set       | loc    | value     | objCode
        "value"                   | entitySet | locale | null      | code
        "entity set"              | null      | locale | itemModel | code
        "locale"                  | entitySet | null   | itemModel | code
        "integration object code" | entitySet | locale | itemModel | null
    }

    @Test
    def "build() throws IllegalArgumentException when entity set has null entity type"() {
        given:
        def entitySet = Stub(EdmEntitySet)

        when:
        itemConversionRequestBuilder().withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withValue(itemModel)
                .build()

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "build() creates request with #valueDesc value and the specified properties"() {
        setup:
        def options = conversionOptionsBuilder().build()

        when:
        def request = itemConversionRequestBuilder().withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withValue(value)
                .withIntegrationObject(code)
                .withOptions(options)
                .build()

        then:
        request.entitySet.is entitySet
        request.entityType.is entityType
        request.acceptLocale.is locale
        request.itemModel.is expItemModel
        request.value.is value
        request.integrationObjectCode.is code
        request.options.is options

        where:
        valueDesc    | value        | expItemModel
        'item model' | itemModel    | itemModel
        'object'     | new Object() | null
    }

    @Test
    def "build() creates a copy of another request"() {
        given:
        def options = conversionOptionsBuilder().build()
        def preRequest = itemConversionRequestBuilder().withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withValue(itemModel)
                .withIntegrationObject(code)
                .withOptions(options)
                .build()

        when:
        def request = itemConversionRequestBuilder().from(preRequest).build()

        then:
        !preRequest.is(request)
        request?.entitySet?.is entitySet
        request?.entityType?.is entityType
        request?.acceptLocale?.is locale
        request?.itemModel?.is itemModel
        request?.value?.is itemModel
        request?.integrationObjectCode?.is code
        request?.options?.is options
    }

    @Test
    def 'build() creates request when an item model is provided instead of value'() {
        when:
        def request = itemConversionRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withItemModel(itemModel)
                .withIntegrationObject(code)
                .withOptions(Stub(ConversionOptions))
                .build()

        then:
        request.itemModel.is itemModel
        request.value.is itemModel
    }

    @Test
    def 'can build a request with default conversion options'() {
        when:
        def request = itemConversionRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withValue(new Object())
                .withIntegrationObject(code)
                .withOptions(Stub(ConversionOptions))
                .build()

        then:
        with (request.options) {
            navigationSegments.empty
            expand.empty
        }
    }

    @Test
    def 'build() throws exception when provided item model is null'() {
        when:
        itemConversionRequestBuilder()
                .withEntitySet(entitySet)
                .withAcceptLocale(locale)
                .withItemModel(null)
                .withIntegrationObject(code)
                .withOptions(Stub(ConversionOptions))
                .build()

        then:
        thrown IllegalArgumentException
    }
}