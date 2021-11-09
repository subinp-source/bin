/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultProductFeatureModelFactoryUnitTest extends Specification {

    private def modelService = Stub ModelService
    private def factory = new DefaultProductFeatureModelFactory(modelService: modelService)
    private def defaultUnit = Stub ClassificationAttributeUnitModel

    @Unroll
    @Test
    def "valid constructor arguments enforced when #condition"() {
        when:
        factory.create(product, classAttrAssignment, value, valuePosition)

        then:
        thrown IllegalArgumentException

        where:
        condition                             | product            | classAttrAssignment                 | value        | valuePosition
        'product is null'                     | null               | Stub(ClassAttributeAssignmentModel) | Stub(Object) | 0
        'classAttributeAssignment is null'    | Stub(ProductModel) | null                                | Stub(Object) | 0
        'value is null'                       | Stub(ProductModel) | Stub(ClassAttributeAssignmentModel) | null         | 0
        'value position is a negative number' | Stub(ProductModel) | Stub(ClassAttributeAssignmentModel) | Stub(Object) | -1
    }

    @Test
    @Unroll
    def "valid constructor enforced with language when #condition"() {
        when:
        factory.create(product, classAttrAssignment, value, valuePosition, language)

        then:
        thrown IllegalArgumentException

        where:
        condition                             | product            | classAttrAssignment                 | value        | valuePosition | language
        'product is null'                     | null               | Stub(ClassAttributeAssignmentModel) | Stub(Object) | 0             | Stub(LanguageModel)
        'classAttributeAssignment is null'    | Stub(ProductModel) | null                                | Stub(Object) | 0             | Stub(LanguageModel)
        'value is null'                       | Stub(ProductModel) | Stub(ClassAttributeAssignmentModel) | null         | 0             | Stub(LanguageModel)
        'value position is a negative number' | Stub(ProductModel) | Stub(ClassAttributeAssignmentModel) | Stub(Object) | -1            | Stub(LanguageModel)
    }

    @Unroll
    @Test
    def "#value is valid valuePosition is enforced"() {
        given:
        modelService.create(ProductFeatureModel.class) >> Stub(ProductFeatureModel)

        when:
        factory.create(Stub(ProductModel), classAttributeAssignment(), Stub(Object), value)

        then:
        notThrown IllegalArgumentException

        where:
        value << [0, 1, 100]
    }

    @Test
    def "properties are set on new feature"() {
        given: 'product feature model is returned from the ModelService'
        def productFeature = new ProductFeatureModel()
        modelService.create(ProductFeatureModel.class) >> productFeature
        and:
        def value = 'RockShox Reba RL'
        and:
        def product = Stub ProductModel
        and:
        def unit = Stub ClassificationAttributeUnitModel
        and: 'class attribute assignment'
        def attributeAssignment = classAttributeAssignment(unit)

        when:
        def feature = factory.create(product, attributeAssignment, value, 0)

        then:
        with(feature) {
            product == product
            classificationAttributeAssignment == attributeAssignment
            value == value
            valuePosition == 0
            unit == unit
            // should change classification attribute code to all lowercase
            qualifier == 'mountainbike/Spring2020/hardtail.frontsuspension'
        }
    }

    @Test
    def "properties are set on new feature with language"() {
        given: 'product feature model is returned from the ModelService'
        def productFeature = new ProductFeatureModel()
        modelService.create(ProductFeatureModel.class) >> productFeature
        and:
        def value = 'RockShox Reba RL'
        and:
        def product = Stub ProductModel
        and:
        def unit = Stub ClassificationAttributeUnitModel
        and: 'class attribute assignment'
        def attributeAssignment = classAttributeAssignment(unit)
        and:
        def lang = Stub LanguageModel

        when:
        def feature = factory.create(product, attributeAssignment, value, 0, lang)

        then:
        with(feature) {
            product == product
            classificationAttributeAssignment == attributeAssignment
            value == value
            valuePosition == 0
            unit == unit
            // should change classification attribute code to all lowercase
            qualifier == 'mountainbike/Spring2020/hardtail.frontsuspension'
            language == lang
        }
    }

    private ClassAttributeAssignmentModel classAttributeAssignment(unit = defaultUnit) {
        Stub(ClassAttributeAssignmentModel) {
            getUnit() >> unit
            getSystemVersion() >> Stub(ClassificationSystemVersionModel) {
                getVersion() >> 'Spring2020'
                getCatalog() >> Stub(ClassificationSystemModel) {
                    getId() >> 'mountainbike'
                }
            }
            getClassificationClass() >> Stub(ClassificationClassModel) {
                getCode() >> 'hardtail'
            }
            getClassificationAttribute() >> Stub(ClassificationAttributeModel) {
                getCode() >> 'FrontSuspension'
            }
        }
    }
}
