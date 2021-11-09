/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.classification.ClassificationService
import de.hybris.platform.classification.features.Feature
import de.hybris.platform.classification.features.FeatureValue
import de.hybris.platform.classification.features.LocalizedFeature
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Locale.CHINESE
import static java.util.Locale.ENGLISH
import static java.util.Locale.GERMAN
import static java.util.Locale.JAPANESE

class ClassificationAttributeValueGetterUnitTest extends Specification {
    def classAttributeAssignment = Stub ClassAttributeAssignmentModel
    def attributeDescriptor = Stub(ClassificationTypeAttributeDescriptor) {
        getClassAttributeAssignment() >> classAttributeAssignment
    }
    def service = Stub ClassificationService
    def modelService = Stub ModelService

    @Test
    @Unroll
    def "create classification attribute value getter when class attribute assignment getLocalized() is #value"() {
        given: "class attribute assignment getLocalized() is #value"
        classAttributeAssignment.getLocalized() >> value
        def valueGetter = new ClassificationAttributeValueGetter(attributeDescriptor, service, modelService)

        expect:
        with(valueGetter) {
            attribute == attributeDescriptor
            classificationService == service
            localized == expected
        }

        where:
        value | expected
        true  | true
        false | false
        null  | false
    }

    @Test
    @Unroll
    def "call to #method () with non-product args #args, #dynamicArgs returns #result"() {
        expect:
        localizedValueGetter()."$method"(args, *dynamicArgs) == result

        where:
        args            | method      | dynamicArgs         | result
        null            | 'getValue'  | []                  | null
        Stub(ItemModel) | 'getValue'  | []                  | null
        null            | 'getValue'  | [ENGLISH]           | null
        Stub(ItemModel) | 'getValue'  | [ENGLISH]           | null
        null            | 'getValues' | []                  | [:]
        Stub(ItemModel) | 'getValues' | []                  | [:]
        null            | 'getValues' | [ENGLISH, JAPANESE] | [:]
        Stub(ItemModel) | 'getValues' | [ENGLISH, JAPANESE] | [:]
    }

    @Test
    @Unroll
    def "create classification attribute value getter with null #property throws exception"() {
        when:
        new ClassificationAttributeValueGetter(attr, classService, modService)

        then:
        thrown(IllegalArgumentException)

        where:
        property                  | attr                                        | classService                | modService
        "typeAttributeDescriptor" | null                                        | Stub(ClassificationService) | Stub(ModelService)
        "classificationService"   | Stub(ClassificationTypeAttributeDescriptor) | null                        | Stub(ModelService)
        "modelService"            | Stub(ClassificationTypeAttributeDescriptor) | Stub(ClassificationService) | null
    }

    @Test
    @Unroll
    def "get value is null when the #msg"() {
        given:
        featureExists feature

        expect:
        nonLocalizedValueGetter().getValue(product()) == null

        where:
        feature                     | msg
        featureWithValue(null)      | "classification attribute does not have a value"
        featureWithNoFeatureValue() | "classification feature does not have a value"
        null                        | "classification attribute is not associated to the product"
    }

    @Test
    @Unroll
    def "get value returns the classification attribute value '#value' of the product"() {
        given:
        featureExists featureWithValue(value)

        expect:
        nonLocalizedValueGetter().getValue(product()) == value

        where:
        value << ['someVal', null]

    }

    @Test
    def "ClassAttributeAssignmentModel is not localized"() {
        expect:
        nonLocalizedValueGetter().getValue(product(), ENGLISH) == null
    }

    @Test
    @Unroll
    def "feature is not localized"() {
        given:
        featureExists feature

        expect:
        localizedValueGetter().getValue(product(), ENGLISH) == null

        where:
        feature << [null, Stub(Feature)]
    }

    @Test
    def "feature is localized but no value exists for the product"() {
        given:
        featureExists localizedFeatureWithValue(null)

        expect:
        localizedValueGetter().getValue(product(), JAPANESE) == null
    }

    @Unroll
    @Test
    def "feature is localized and value '#value' exists for the given locale"() {
        given:
        featureExists localizedFeatureWithValue(featureValue(value))

        expect:
        localizedValueGetter().getValue(product(), ENGLISH) == value

        where:
        value << [null, 'english value']
    }

    @Unroll
    @Test
    def "attribute values returned: #result for requested locales #locales when only value for japanese exists"() {
        given:
        featureExists localizedFeatureWithValues([(JAPANESE): [featureValue('japanese value')]])

        expect:
        localizedValueGetter().getValues(product(), *locales) == result

        where:
        locales                      | result
        [ENGLISH]                    | [:]
        [JAPANESE]                   | [(JAPANESE): 'japanese value']
        [ENGLISH, CHINESE, JAPANESE] | [(JAPANESE): 'japanese value']
    }

    @Unroll
    @Test
    def "get localized values returns an empty map when getting all localized values from localized feature returns #allLocalizedValues"() {
        given:
        featureExists localizedFeatureWithValues(allLocalizedValues)

        expect:
        localizedValueGetter().getValues(product(), ENGLISH) == [:]

        where:
        allLocalizedValues << [null as Map, [:], [ENGLISH: null]]
    }

    @Unroll
    @Test
    def "get localized values when feature is not localized but attribute is localized returns an empty map"() {
        given:
        featureExists feature

        expect:
        localizedValueGetter().getValues(product(), ENGLISH, CHINESE) == [:]

        where:
        feature << [null, Stub(Feature)]
    }

    @Test
    def "get localized values when feature is localized but attribute is not localized returns an empty map"() {
        given:
        featureExists localizedFeatureWithValues([
                (ENGLISH): [featureValue('en value')],
                (CHINESE): [featureValue('zh value')]]
        )

        expect:
        nonLocalizedValueGetter().getValues(product(), ENGLISH, CHINESE) == [:]
    }

    @Test
    def "null feature exists for locale"() {
        given:
        featureExists localizedFeatureWithValues([(ENGLISH): [null]])

        expect:
        localizedValueGetter().getValues(product(), ENGLISH) == [(ENGLISH): null]
    }

    @Test
    def "get localized valueList feature for multiple locales"() {
        given:
        featureExists localizedFeatureWithValues(
                (ENGLISH): [featureValue(classAttributeValue('en value'))],
                (CHINESE): [featureValue(classAttributeValue('zh value'))]
        )
        and: 'attribute type is enum'
        classAttributeAssignment.getAttributeType() >> ClassificationAttributeTypeEnum.ENUM

        expect:
        localizedValueGetter().getValues(product(), ENGLISH, CHINESE) == [(ENGLISH): 'en value', (CHINESE): 'zh value']
    }

    @Test
    def "get localized valueList feature for single locale"() {
        given:
        featureExists localizedFeatureWithValue(
                featureValue(classAttributeValue('zh value'))
        )

        and: 'attribute type is enum'
        classAttributeAssignment.getAttributeType() >> ClassificationAttributeTypeEnum.ENUM

        expect:
        localizedValueGetter().getValue(product(), CHINESE) == 'zh value'
    }

    @Unroll
    @Test
    def "get value for multi value classification attributes with values of type #type"() {
        given:
        featureExists multiValueFeature(values)

        expect:
        nonLocalizedCollectionValueGetter().getValue(product()) == values

        where:
        values                                                   | type
        [10.0D, 15.0D, 20.0D]                                    | 'double'
        ['a', 'b', 'c']                                          | 'string'
        [false, true, false]                                     | 'boolean'
        [new Date(25200000), new Date(), new Date(475077600000)] | 'date'
        [product(), product(), product()]                        | 'reference'
    }

    @Unroll
    @Test
    def "get value is #value for single value enum class attribute"() {
        given:
        featureExists enumFeatureWithValue(value)

        expect:
        nonLocalizedValueGetter().getValue(product()) == value

        where:
        value << ['enum_value', null]
    }

    @Unroll
    @Test
    def "get value is #expected for multi-value enum class attribute"() {
        given:
        featureExists enumFeatureWithValues(values)

        expect:
        nonLocalizedCollectionValueGetter().getValue(product()) == expected

        where:
        values                         | expected
        ['enum_value1', 'enum_value2'] | ['enum_value1', 'enum_value2']
        null                           | []
        []                             | []
    }

    @Unroll
    @Test
    def "get localized feature values when attribute is collection"() {
        given:
        featureExists localizedFeatureWithCollection([
                (ENGLISH) : [featureValue('value 1 English'), featureValue('value 2 English')],
                (GERMAN) : [featureValue('value 1 German'), featureValue('value 2 German'), featureValue('value 3 German')],
                (JAPANESE) : [featureValue('value 1 Japanese')]
        ])

        expect:
        localizedCollectionValueGetter().getValue(product(), locale) == expected

        where:
        locale      | expected
        ENGLISH     | ["value 1 English", "value 2 English"]
        JAPANESE    | ["value 1 Japanese"]
        GERMAN      | ["value 1 German", "value 2 German", "value 3 German"]
    }

    private Feature multiValueFeature(final List<Object> values) {
        def featureValues = values.collect { featureValue(it) }
        featureWithValues(featureValues)
    }

    private FeatureValue featureValue(Object val) {
        Stub(FeatureValue) { getValue() >> val }
    }

    private void featureExists(Feature feature) {
        service.getFeature(_ as ProductModel, _ as ClassAttributeAssignmentModel) >> feature
    }

    private ProductModel product() {
        Stub(ProductModel)
    }

    private featureWithValue(Object value) {
        Stub(Feature) {
            getValue() >> featureValue(value)
        }
    }

    private enumFeatureWithValues(List values) {
        def featureValues = values.collect { featureValue(classAttributeValue(it)) }
        enumFeatureWithFeatureValues(featureValues)
    }

    private enumFeatureWithFeatureValues(final List<FeatureValue> featureValues) {
        classAttributeAssignment.getAttributeType() >> ClassificationAttributeTypeEnum.ENUM
        classAttributeAssignment.getMultiValued() >> true
        Stub(Feature) {
            getValues() >> featureValues
        }
    }

    private enumFeatureWithValue(Object value) {
        classAttributeAssignment.getAttributeType() >> ClassificationAttributeTypeEnum.ENUM
        Stub(Feature) {
            getValue() >> featureValue(classAttributeValue(value))
        }
    }

    private classAttributeValue(Object value) {
        Stub(ClassificationAttributeValueModel) {
            getCode() >> value
        }
    }

    private featureWithValues(List<FeatureValue> values) {
        classAttributeAssignment.getMultiValued() >> true
        Stub(Feature) {
            getValues() >> values
        }
    }

    private featureWithNoFeatureValue() {
        Stub(Feature) {
            getValue() >> null
        }
    }

    private localizedFeatureWithValue(FeatureValue value) {
        Stub(LocalizedFeature) {
            getValue(_ as Locale) >> value
        }
    }

    private localizedFeatureWithValues(Map<Locale, List<FeatureValue>> values) {
        Stub(LocalizedFeature) {
            getValuesForAllLocales() >> values
        }
    }

    private ClassificationAttributeValueGetter nonLocalizedValueGetter() {
        classAttributeAssignment.getLocalized() >> false
        new ClassificationAttributeValueGetter(attributeDescriptor, service, modelService)
    }

    private ClassificationAttributeValueGetter nonLocalizedCollectionValueGetter() {
        classAttributeAssignment.getLocalized() >> false
        attributeDescriptor.isCollection() >> true
        new ClassificationAttributeValueGetter(attributeDescriptor, service, modelService)
    }

    private ClassificationAttributeValueGetter localizedCollectionValueGetter() {
        classAttributeAssignment.getLocalized() >> true
        attributeDescriptor.isCollection() >> true
        new ClassificationAttributeValueGetter(attributeDescriptor, service, modelService)
    }

    private ClassificationAttributeValueGetter localizedValueGetter() {
        classAttributeAssignment.getLocalized() >> true
        new ClassificationAttributeValueGetter(attributeDescriptor, service, modelService)
    }

    private localizedFeatureWithCollection(Map<Locale, List<FeatureValue>> values) {
        Stub(LocalizedFeature) {
            getValues(_ as Locale) >> { Locale locale ->
                values.get(locale)
            }
        }
    }
}
