/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.ProductFeatureModelFactory
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LocalizedClassificationAttributeValueHandlerUnitTest extends Specification {
    private static final LanguageModel ENGLISH = new LanguageModel('en')
    private static final LanguageModel GERMAN = new LanguageModel('de')
    def product = new ProductModel()
    def modelService = Mock ModelService
    def featureFactory = Stub ProductFeatureModelFactory
    def i18NService = Stub(CommonI18NService) {
        getLanguage(Locale.ENGLISH.toLanguageTag()) >> ENGLISH
        getLanguage(Locale.GERMAN.toLanguageTag()) >> GERMAN
    }
    def handler = new LocalizedClassificationAttributeValueHandler(
            modelService: modelService,
            featureFactory: featureFactory,
            i18NService: i18NService)
    def classificationClass = new ClassificationClassModel()
    def classAttributeAssignment = Stub(ClassAttributeAssignmentModel) {
        getClassificationClass() >> classificationClass
    }

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition value"() {
        expect:
        handler.isApplicable(attribute, 'doesNotMatter') == applicable

        where:
        condition                     | applicable | attribute
        'localized and primitive'     | true       | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; isPrimitive() >> true }
        'single'                      | false      | Stub(ClassificationTypeAttributeDescriptor)
        'collection'                  | false      | Stub(ClassificationTypeAttributeDescriptor) { isCollection() >> true }
        'localized and collection'    | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; isCollection() >> true }
        'localized and non-primitive' | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; isPrimitive() >> false }
    }

    @Test
    def "new feature is created for localized value"() {
        given:
        def enValue = 'enValue'
        def feature = productFeature()
        featureFactory.create(product, classAttributeAssignment, enValue, 0, ENGLISH) >> feature

        when:
        handler.set(product, mapAttribute(), [(Locale.ENGLISH): enValue])

        then: 'feature is set on product'
        product.features == [feature]
    }

    @Test
    def "setting an empty map does not alter the number of localized features"() {
        given:
        product.features = [productFeature(ENGLISH), productFeature(GERMAN)]

        when:
        handler.set(product, mapAttribute(), [:])

        then:
        product.features.size == 2
    }

    @Test
    def "new value is updated for locale that already contains a value"() {
        given: 'feature value for EN already exists'
        def englishFeature = productFeature()
        product.features = [englishFeature]
        and:
        def newEnValue = 'newEnValue'
        def newEnFeature = productFeature(ENGLISH)
        def deValue = 'deValue'
        def germanFeature = productFeature(GERMAN)
        and:
        featureFactory.create(product, classAttributeAssignment, newEnValue, _ as Integer, ENGLISH) >> newEnFeature
        featureFactory.create(product, classAttributeAssignment, deValue, _ as Integer, GERMAN) >> germanFeature

        when:
        handler.set(product, mapAttribute(), [(Locale.ENGLISH): newEnValue, (Locale.GERMAN): deValue])

        then: 'product contains updated EN feature and new DE feature'
        product.features.containsAll newEnFeature, germanFeature
    }

    @Test
    def "locale with region is handled"() {
        given:
        def es_CO = new LanguageModel('es_CO')
        def feature = productFeature(es_CO)
        i18NService.getLanguage(es_CO.isocode) >> es_CO
        and:
        def esValue = 'value'
        featureFactory.create(product, classAttributeAssignment, esValue, 0, es_CO) >> feature

        when:
        handler.set(product, mapAttribute(), [(Locale.forLanguageTag('es-CO')): esValue])

        then: 'feature is set on product'
        product.features == [feature]
    }

    @Test
    def "product is added to classification class if not yet included"() {
        when:
        handler.set product, mapAttribute(), [(Locale.ENGLISH): 'v']

        then:
        classificationClass.products == [product]
    }

    @Test
    def "product is not added to classification class twice if it is already present"() {
        given:
        classificationClass.setProducts([product])

        when:
        handler.set(product, mapAttribute(), [(Locale.ENGLISH): 'v'])

        then:
        classificationClass.products == [product]
    }

    @Test
    def "map with keys that are not Locales do not get handled"() {
        when:
        handler.set(product, mapAttribute(), [str: 'string key', 5: 'integer key'])

        then:
        product.features == []
    }

    private ClassificationTypeAttributeDescriptor mapAttribute() {
        Stub(ClassificationTypeAttributeDescriptor) {
            isLocalized() >> true
            isCollection() >> false
            isPrimitive() >> true
            getClassAttributeAssignment() >> classAttributeAssignment
        }
    }

    private def productFeature(LanguageModel language = ENGLISH) {
        Stub(ProductFeatureModel) {
            getClassificationAttributeAssignment() >> classAttributeAssignment
            getLanguage() >> language
            getValue() >> 'originalValue'
        }
    }
}
