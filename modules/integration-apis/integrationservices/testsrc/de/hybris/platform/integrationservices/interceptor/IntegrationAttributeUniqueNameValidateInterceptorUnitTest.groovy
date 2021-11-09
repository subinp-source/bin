/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationAttributeUniqueNameValidateInterceptorUnitTest extends Specification {

    def interceptor = new IntegrationAttributeUniqueNameValidateInterceptor()

    @Test
    @Unroll
    def 'no exception is thrown when #condition'() {
        given:
        def attrName = 'uniqueName'
        def attribute = attribute attrName, intObjItem

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        condition                                                   | intObjItem
        'integration object item is null'                           | null
        'classification attribute collection is null'               | Stub(IntegrationObjectItemModel) { getClassificationAttributes() >> null }
        'virtual attribute collection is null'                      | Stub(IntegrationObjectItemModel) { getVirtualAttributes() >> null }
        'virtual and classification attribute collections are null' | Stub(IntegrationObjectItemModel) {
            getClassificationAttributes() >> null
            getVirtualAttributes() >> null
        }
        'attribute names are unique'                                | integrationObjectItemWithClassAttributes(['className1', 'className2'])
    }

    @Test
    def "exception is thrown when attribute name matches classification attribute"() {
        given:
        def attrName = 'duplicateName'
        def attribute = attribute attrName, integrationObjectItemWithClassAttributes(['className1', attrName, 'className2'])

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The attribute [$attrName] already exists in this integration object item. Please provide a different name for one of the [$attrName] attributes."
    }

    @Test
    def "exception is thrown when attribute name matches virtual attribute"() {
        given:
        def attrName = 'duplicateName'
        def attribute = attribute attrName, integrationObjectItemWithVirtualAttributes(['virtualAttr1', attrName, 'virtualAttr2'])

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The attribute [$attrName] already exists in this integration object item. Please provide a different name for one of the [$attrName] attributes."
    }

    def attribute(String attrName, IntegrationObjectItemModel intObjectItem) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> attrName
            getIntegrationObjectItem() >> intObjectItem
        }
    }

    def integrationObjectItemWithClassAttributes(List classAttrNames) {
        Stub(IntegrationObjectItemModel) {
            getClassificationAttributes() >> classAttrNames.collect { attrName ->
                Stub(IntegrationObjectItemClassificationAttributeModel) { getAttributeName() >> attrName }
            }
        }
    }

    def integrationObjectItemWithVirtualAttributes(List virtualAttrNames) {
        Stub(IntegrationObjectItemModel) {
            getVirtualAttributes() >> virtualAttrNames.collect { attrName ->
                Stub(IntegrationObjectItemVirtualAttributeModel) { getAttributeName() >> attrName }
            }
        }
    }
}
