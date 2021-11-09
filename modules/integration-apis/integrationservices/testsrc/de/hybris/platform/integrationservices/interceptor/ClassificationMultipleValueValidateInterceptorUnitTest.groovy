/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ClassificationMultipleValueValidateInterceptorUnitTest extends Specification {

    def interceptor = new ClassificationMultipleValueValidateInterceptor()

    @Test
    @Unroll
    def "no exception is thrown when attribute's class attribute assignment multiValued=#multiValued and localized=#localized"() {
        given:
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getMultiValue() >> multiValued
                getLocalized() >> localized
            }
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        multiValued | localized
        true        | false
        false       | true
        false       | false
    }

    @Test
    def "interceptor exception is thrown when attribute's class attribute assignment is multiValued and localized"() {
        given:
        def attributeName = 'MultiValuedClassAttr'
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
                getMultiValued() >> true
                getLocalized() >> true
            }
            getAttributeName() >> attributeName
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def exception = thrown(InterceptorException)
        exception.message.contains "Modeling classification attribute [$attributeName] does not support both multiValued and localized being enabled."
    }
}
