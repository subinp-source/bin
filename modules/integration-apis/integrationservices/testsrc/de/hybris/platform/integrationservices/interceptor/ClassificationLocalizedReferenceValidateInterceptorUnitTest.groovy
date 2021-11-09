/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ClassificationLocalizedReferenceValidateInterceptorUnitTest extends Specification {

    def interceptor = new ClassificationLocalizedReferenceValidateInterceptor()

    @Test
    @Unroll
    def "interceptor does not throw exception when attribute type #attrType and localized #localized"() {
        given:
        def attr = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getLocalized() >> localized
                getAttributeType() >> attrType
            }
        }

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        localized | attrType
        false     | ClassificationAttributeTypeEnum.REFERENCE
        null      | ClassificationAttributeTypeEnum.REFERENCE
        true      | ClassificationAttributeTypeEnum.STRING
    }

    @Test
    def "interceptor throws exception when attribute is a localized reference"() {
        given:
        def name = 'validAttr'
        def attr = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getAttributeName() >> name
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getLocalized() >> true
                getAttributeType() >> ClassificationAttributeTypeEnum.REFERENCE
            }
        }

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Modeling classification attribute [$name] does not support localized reference."
    }
}