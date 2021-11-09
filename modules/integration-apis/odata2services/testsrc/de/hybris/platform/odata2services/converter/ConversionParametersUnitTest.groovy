/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.http.HttpHeaders
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ConversionParametersUnitTest extends Specification {

    @Test
    def "conversion parameter builder builds a ConversionParameters instance with the expected values"() {
        given:
        def attrName = 'someAttrName'
        def attrValue = 'String'
        def oDataContext = Stub(ODataContext)
        def item = Stub(IntegrationItem)
        def locale = Locale.ENGLISH

        when:
        def conversionParams = conversionParametersBuilder()
                .withAttributeName(attrName)
                .withAttributeValue(attrValue)
                .withContext(oDataContext)
                .withIntegrationItem(item)
                .withContentLocale(locale)
                .build()

        then:
        with(conversionParams) {
            attributeName == attrName
            attributeValue == attrValue
            context == oDataContext
            integrationItem == item
            contentLocale == locale
        }
    }

    @Test
    def "builder copies another ConversionParameters values"() {
        given:
        def original = conversionParametersBuilder()
                .withAttributeName('someAttrName')
                .withAttributeValue('String')
                .withContext(Stub(ODataContext))
                .withIntegrationItem(Stub(IntegrationItem))
                .withContentLocale(Locale.ENGLISH)
                .build()

        when:
        def copy = conversionParametersBuilder().from(original).build()

        then:
        !copy.is(original)
        with(copy) {
            attributeName == original.attributeName
            attributeValue == original.attributeValue
            context == original.context
            integrationItem == original.integrationItem
            contentLocale == original.contentLocale
        }
    }

    @Test
    def "getTypeDescriptor() returns integrationItem.itemType"() {
        given:
        def itemType = Stub(TypeDescriptor)
        def item = Stub(IntegrationItem) {
            getItemType() >> itemType
        }

        when:
        def conversionParams = conversionParametersBuilder()
                .withIntegrationItem(item)
                .build()

        then:
        conversionParams.getTypeDescriptor() == itemType
    }

    @Test
    def "getTypeAttributeDescriptor() returns attribute when integrationItem.itemType has attribute matching ConversionParameters attributeName"() {
        given:
        def attrName = 'someAttrName'
        def attributeDescriptor = Stub(TypeAttributeDescriptor)
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(attrName) >> Optional.of(attributeDescriptor)
            }
        }

        when:
        def conversionParams = conversionParametersBuilder()
                .withAttributeName(attrName)
                .withIntegrationItem(item)
                .build()

        then:
        conversionParams.getTypeAttributeDescriptor() == attributeDescriptor
    }

    @Test
    def "getTypeAttributeDescriptor() returns null when integrationItem.itemType has no matching ConversionParameters attributeName"() {
        given:
        def attrName = 'someAttrName'
        def attributeDescriptor = Stub(TypeAttributeDescriptor)
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(attrName) >> Optional.of(attributeDescriptor)
            }
        }

        when:
        def conversionParams = conversionParametersBuilder()
                .withAttributeName("DIFFERENT$attrName")
                .withIntegrationItem(item)
                .build()

        then:
        conversionParams.getTypeAttributeDescriptor() == null
    }

    @Test
    @Unroll
    def "isReplaceAttributesRequested() returns #expected when #msg"() {
        given:
        def conversionParams = conversionParametersBuilder().withContext(context).build()

        expect:
        conversionParams.isReplaceAttributesRequest() == expected

        where:
        msg                           | context          | expected
        "context is null"             | null             | false
        "context HttpMethod is POST"  | context("POST")  | false
        "context HttpMethod is PATCH" | context("PATCH") | true
    }

    @Test
    @Unroll
    def "isContentLanguagePresent() returns #expected when contentLanguage #msg"() {
        given:
        def context = Stub(ODataContext) {
            getRequestHeader(HttpHeaders.CONTENT_LANGUAGE) >> contentLanguage
        }
        def conversionParams = conversionParametersBuilder().withContext(context).build()

        expect:
        conversionParams.isContentLanguagePresent() == expected

        where:
        msg          | contentLanguage | expected
        "is null"    | null            | false
        "is present" | 'en'            | true
    }

    @Test
    def "isMapAttributeValue() returns false when integrationItem.itemType has no attribute matching ConversionParameters attribtueName"() {
        given:
        def attrName = 'someAttrName'
        def attributeDescriptor = Stub(TypeAttributeDescriptor)
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(attrName) >> Optional.of(attributeDescriptor)
            }
        }

        when:
        def conversionParams = conversionParametersBuilder()
                .withAttributeName("DIFFERENT" + attrName)
                .withIntegrationItem(item)
                .build()

        then:
        !conversionParams.isMapAttributeValue()
    }

    @Test
    @Unroll
    def "isMapAttributeValue() returns true when integrationItem has a matching attribute with isLocalized=#localized & isMap=#map"() {
        given:
        def attrName = 'someAttrName'
        def attributeDescriptor = Stub(TypeAttributeDescriptor) {
            isLocalized() >> false
            isMap() >> true
        }
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(attrName) >> Optional.of(attributeDescriptor)
            }
        }

        when:
        def conversionParams = conversionParametersBuilder()
                .withAttributeName(attrName)
                .withIntegrationItem(item)
                .build()

        then:
        conversionParams.isMapAttributeValue()

        where:
        localized | map   | expected
        false     | true  | true
        false     | false | false
        true      | true  | false
    }

    @Test
    def 'changes to builder state between invocations do not affect already created instances'() {
        given: 'a builder'
        def builder = conversionParametersBuilder()
        and: 'a ConversionParameters instance already created by the builder'
        def previous = builder.build()

        when: 'builder state changes and new instance is created'
        def current = builder.withAttributeName('doesNotMatter').build()

        then: 'previusly created and current instances have different state'
        previous.attributeName != current.attributeName
    }

    def context(final String httpMethod = null) {
        Stub(ODataContext) {
            getHttpMethod() >> httpMethod
        }
    }
}
