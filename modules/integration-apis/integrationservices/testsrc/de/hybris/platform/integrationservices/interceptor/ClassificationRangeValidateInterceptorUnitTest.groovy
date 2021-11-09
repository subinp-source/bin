/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification

class ClassificationRangeValidateInterceptorUnitTest extends Specification {

    def interceptor = new ClassificationRangeValidateInterceptor()

    @Test
    def "no exception is thrown when class attribute assignment's range is disabled"() {
        given:
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getRange() >> false
            }
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()
    }

    @Test
    def "interceptor exception is thrown when class attribute assignment's range is enabled"() {
        given:
        def attributeName = 'RangeClassAttr'
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getRange() >> true
            }
            getAttributeName() >> attributeName
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def exception = thrown(InterceptorException)
        exception.message.contains "Modeling classification attribute [$attributeName] does not support range being enabled."
    }
}
