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
class CollectionClassificationAttributeValueHandlerUnitTest extends Specification {
    def product = new ProductModel()
    def featureFactory = Stub ProductFeatureModelFactory
    def modelService = Mock ModelService
    def handler = new CollectionClassificationAttributeValueHandler(modelService: modelService, featureFactory: featureFactory)
    def classificationClass = new ClassificationClassModel()
    def classAttributeAssignment = Stub(ClassAttributeAssignmentModel) {
        getClassificationClass() >> classificationClass
    }

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition value"() {
        expect:
        handler.isApplicable(attribute, 'someValueThatDoesNotMatter') == applicable

        where:
        condition                  | applicable | attribute
        'single'                   | false      | Stub(ClassificationTypeAttributeDescriptor) { isCollection() >> false; isLocalized() >> false }
        'collection'               | true       | collectionAttribute()
        'localized'                | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true }
        'collection and localized' | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; isCollection() >> true }
    }

    @Unroll
    @Test
    def "set #condition attribute value on product for a feature that does not yet have a value"() {
        given:
        def feature1 = productFeature()
        def feature2 = productFeature()
        featureFactory.create(product, classAttributeAssignment, value1, 0) >> feature1
        featureFactory.create(product, classAttributeAssignment, value2, 1) >> feature2

        when:
        handler.set(product, collectionAttribute(), [value1, value2])

        then:
        product.features == [feature1, feature2]

        where:
        condition                  | value1          | value2
        'collection of primitives' | 'v1'            | 'v2'
        'collection of items'      | Stub(ItemModel) | Stub(ItemModel)
    }

    @Test
    def "set empty collection attribute value on product for a feature that does not yet have a value"() {
        when:
        handler.set(product, collectionAttribute(), [])

        then:
        product.features == []
    }

    @Test
    def "set empty collection attribute value on product for a feature that has value"() {
        given:
        product.setFeatures([productFeature()])

        when:
        handler.set(product, collectionAttribute(), [])

        then:
        product.features == []
    }

    @Unroll
    @Test
    def "items are saved before they are set on product for a feature that does not yet have a value"() {
        given:
        def value1 = Stub ItemModel
        def value2 = Stub ItemModel

        and: 'value2 is new'
        modelService.isNew(value2) >> true
        def feature1 = productFeature()
        def feature2 = productFeature()
        featureFactory.create(product, classAttributeAssignment, value1, 0) >> feature1
        featureFactory.create(product, classAttributeAssignment, value2, 1) >> feature2

        when:
        handler.set(product, collectionAttribute(), [value1, value2])

        then: 'value2 is saved'
        1 * modelService.save(value2)
        and: 'features are set on product'
        product.features == [feature1, feature2]
    }

    @Test
    def "new features are added to existing features on product"() {
        given:
        def value = 'v'
        def feature = productFeature()
        featureFactory.create(product, classAttributeAssignment, value, 0) >> feature

        and: 'product already contains existing feature'
        def existingFeature = Stub(ProductFeatureModel)
        product.setFeatures([existingFeature])

        when:
        handler.set(product, collectionAttribute(), [value])

        then: 'feature is added to existing feature'
        product.features == [existingFeature, feature]
    }

    @Test
    def "old value for attribute is removed and new feature value is added"() {
        given:
        product.setFeatures([productFeature()])
        def newValue = 'v'

        and:
        def newFeature = productFeature()
        featureFactory.create(product, classAttributeAssignment, newValue, 0) >> newFeature

        when:
        handler.set(product, collectionAttribute(), [newValue])

        then:
        product.features == [newFeature]
    }

    @Test
    def "product is added to Classification class if not already included"() {
        given:
        classificationClass.setProducts([])

        when:
        handler.set(product, collectionAttribute(), ['v'])

        then:
        classificationClass.products == [product]
    }

    @Test
    def "product is not added to Classification class twice if it is already present"() {
        given:
        classificationClass.setProducts([product])

        when:
        handler.set(product, collectionAttribute(), ['v'])

        then:
        classificationClass.products == [product]
    }

    private ClassificationTypeAttributeDescriptor collectionAttribute() {
        Stub(ClassificationTypeAttributeDescriptor) {
            isLocalized() >> false
            isCollection() >> true
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
