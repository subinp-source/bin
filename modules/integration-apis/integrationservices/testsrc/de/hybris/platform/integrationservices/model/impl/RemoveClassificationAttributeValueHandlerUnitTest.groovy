/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.ProductFeatureModel
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.ProductFeatureModelFactory
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class RemoveClassificationAttributeValueHandlerUnitTest extends Specification {
    def product = new ProductModel()
    def modelService = Stub ModelService
    def featureFactory = Stub ProductFeatureModelFactory
    def handler = new RemoveClassificationAttributeValueHandler(modelService: modelService, featureFactory: featureFactory)
    def classAttributeAssignment = Stub ClassAttributeAssignmentModel

    @Unroll
    @Test
    def "isApplicable is #result when value is #value"() {
        expect:
        handler.isApplicable(Stub(ClassificationTypeAttributeDescriptor), value) == result

        where:
        value        | result
        null         | true
        Stub(Object) | false
    }

    @Test
    def "removes product features relating to the attribute"() {
        given: 'product contains features for attribute'
        def feature1 = productFeature(classAttributeAssignment)
        def feature2 = productFeature(classAttributeAssignment)
        product.setFeatures([feature1, feature2])

        when:
        handler.set(product, attribute(classAttributeAssignment), null)

        then: 'features for the same attribute assignment are removed'
        product.features == []
    }

    @Test
    def "does not remove features for other attributes"() {
        given: 'product contains features for a different attribute'
        def otherAssignment = Stub ClassAttributeAssignmentModel
        def feature1 = productFeature(otherAssignment)
        def feature2 = productFeature(otherAssignment)
        product.setFeatures([feature1, feature2])

        when:
        handler.set product, attribute(classAttributeAssignment), null

        then: 'features for other attribute assignment are not removed'
        product.features == [feature1, feature2]
    }

    private ClassificationTypeAttributeDescriptor attribute(ClassAttributeAssignmentModel attributeAssignment = classAttributeAssignment) {
        Stub(ClassificationTypeAttributeDescriptor) {
            getClassAttributeAssignment() >> attributeAssignment
        }
    }

    private def productFeature(ClassAttributeAssignmentModel attributeAssignment = classAttributeAssignment) {
        Stub(ProductFeatureModel) {
            getClassificationAttributeAssignment() >> attributeAssignment
        }
    }
}
