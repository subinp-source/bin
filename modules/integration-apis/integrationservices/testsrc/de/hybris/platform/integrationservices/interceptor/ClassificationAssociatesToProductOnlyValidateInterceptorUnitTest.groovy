/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ClassificationAssociatesToProductOnlyValidateInterceptorUnitTest extends Specification {

    def interceptor = new ClassificationAssociatesToProductOnlyValidateInterceptor()

    @Test
    @Unroll
    def "no exception is thrown when the attribute is associated to a #typeCode type"() {
        given:
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getType() >> composedType(typeCode, superTypeCodes)
            }
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        typeCode         | superTypeCodes
        'ProductSubType' | ['Product', 'SuperType']
        'Product'        | ['SuperType']
    }

    @Test
    def "exception is thrown when the attribute is not associated to a Product type"() {
        given:
        def attrName = 'not-a-product-attr'
        def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getType() >> composedType('CType', ['ASuperType', 'BSuperType'])
            }
            getAttributeName() >> attrName
        }

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The classification attribute [$attrName] can only be assigned to an Integration Object Item that is a Product or its subtype."
    }

    def composedType(def typeCode, def superTypeCodes) {
        Stub(ComposedTypeModel) {
            getCode() >> typeCode
            getAllSuperTypes() >> superTypeCodes.collect {
                code -> Stub(ComposedTypeModel) { getCode() >> code }
            }
        }
    }
}

