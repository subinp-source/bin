/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.ProductFeatureModelFactory
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SingleClassificationAttributeValueHandlerUnitTest extends Specification {
    def product = new ProductModel()
    def featureFactory = Stub ProductFeatureModelFactory
    def modelService = Mock ModelService
    def handler = new SingleClassificationAttributeValueHandler(modelService: modelService, featureFactory: featureFactory)
    def classificationClass = new ClassificationClassModel()
    def classAttributeAssignment = Stub(ClassAttributeAssignmentModel) {
        getClassificationClass() >> classificationClass
    }

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition value"() {
        expect:
        handler.isApplicable(attribute, value) == applicable

        where:
        condition    | applicable | value     | attribute
        'single'     | true       | 'v'       | primitiveAttribute()
        'collection' | false      | ['v']     | Stub(ClassificationTypeAttributeDescriptor) { isCollection() >> true }
        'localized'  | false      | [k1: 'v'] | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true }
        'null'       | false      | null      | primitiveAttribute()
    }

    @Unroll
    @Test
    def "set #condition attribute value on product for a feature that does not yet have a value"() {
        given:
        def feature = productFeature()
        featureFactory.create(product, classAttributeAssignment, value, 0) >> feature

        when:
        handler.set(product, primitiveAttribute(), value)

        then: 'feature is set on product'
        product.features == [feature]

        where:
        condition       | value
        'new primitive' | 5
        'existing item' | Stub(ItemModel)
    }

    @Test
    def "item is saved before it is set on product for a feature that does not yet have a value"() {
        given:
        def value = Stub ItemModel
        and: 'item is new'
        modelService.isNew(value) >> true

        def feature = productFeature()
        featureFactory.create(product, classAttributeAssignment, value, 0) >> feature

        when:
        handler.set(product, itemAttribute(), value)

        then: 'new item is saved'
        1 * modelService.save(value)
        and: 'feature is set on product'
        product.features == [feature]
    }

    @Test
    def "feature is added to existing features on product"() {
        given:
        def value = 5
        def feature = productFeature()
        featureFactory.create(product, classAttributeAssignment, value, 0) >> feature

        and: 'product contains existing feature'
        def existingFeature = Stub(ProductFeatureModel)
        product.setFeatures([existingFeature])

        when:
        handler.set(product, primitiveAttribute(), value)

        then: 'feature is set on product'
        product.features == [existingFeature, feature]
    }

    @Test
    def "existing primitive value for feature is overridden by new value"() {
        given:
        def feature = productFeature()
        product.setFeatures([feature])
        def newValue = 100

        when:
        handler.set(product, primitiveAttribute(), newValue)

        then: 'new value is set on feature'
        1 * feature.setValue(newValue)
        and: 'feature is set on product'
        product.features == [feature]
    }

    @Test
    def "existing item value for feature is overridden by new item value"() {
        given:
        def feature = productFeature()
        product.setFeatures([feature])
        def newItem = Stub ItemModel

        and:
        modelService.isNew(newItem) >> true

        when:
        handler.set(product, itemAttribute(), newItem)

        then: 'new items is saved'
        1 * modelService.save(newItem)
        and: 'new item is set on feature'
        1 * feature.setValue(newItem)
        and: 'feature is set on product'
        product.features == [feature]
    }

    @Test
    def "product is added to Classification class if not already included"() {
        given:
        classificationClass.setProducts([])

        when:
        handler.set(product, primitiveAttribute(), 'v')

        then:
        classificationClass.products == [product]
    }

    @Test
    def "product is not added to Classification class twice if it is already present"() {
        given:
        classificationClass.setProducts([product])

        when:
        handler.set(product, primitiveAttribute(), 'v')

        then:
        classificationClass.products == [product]
    }

    private ClassificationTypeAttributeDescriptor primitiveAttribute() {
        Stub(ClassificationTypeAttributeDescriptor) {
            isLocalized() >> false
            isCollection() >> false
            isPrimitive() >> true
            getClassAttributeAssignment() >> classAttributeAssignment
        }
    }

    private ClassificationTypeAttributeDescriptor itemAttribute() {
        Stub(ClassificationTypeAttributeDescriptor) {
            isLocalized() >> false
            isCollection() >> false
            isPrimitive() >> false
            getClassAttributeAssignment() >> classAttributeAssignment
        }
    }

    private def productFeature() {
        Mock(ProductFeatureModel) {
            getClassificationAttributeAssignment() >> classAttributeAssignment
        }
    }
}
