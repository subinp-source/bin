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
class IntegrationClassificationAttributeUniqueNameValidateInterceptorUnitTest extends Specification {

    def interceptor = new IntegrationClassificationAttributeUniqueNameValidateInterceptor()

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
        condition                               | intObjItem
        'integration object item is null'       | null
        'standard attribute collection is null' | Stub(IntegrationObjectItemModel) { getAttributes() >> null }
        'attribute names are unique'            | integrationObjectItem(['standardName1', 'standardName2'], null)
    }

    @Test
    def "exception is thrown when attribute name duplicates an existing standard attribute name"() {
        given:
        def attrName = 'duplicateName'
        def attribute = attribute attrName, integrationObjectItem(['standardName1', attrName, 'standardName2'], null)

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The attribute [$attrName] already exists in this integration object item. Please provide a different name for one of the [$attrName] attributes."
    }

    @Test
    def "exception is thrown when attribute name duplicates an existing virtual attribute name"() {
        given:
        def attrName = 'duplicateName'
        def attribute = attribute attrName, integrationObjectItem(null, ['duplicateName'])

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The attribute [$attrName] already exists in this integration object item. Please provide a different name for one of the [$attrName] attributes."
    }

    def attribute(String attrName, IntegrationObjectItemModel intObjectItem) {
        Stub(IntegrationObjectItemClassificationAttributeModel) {
            getAttributeName() >> attrName
            getIntegrationObjectItem() >> intObjectItem
        }
    }

    def integrationObjectItem(List standardAttributeNames, List virtualAttributeNames) {
        Stub(IntegrationObjectItemModel) {
            getAttributes() >> standardAttributeNames.collect { attrName ->
                Stub(IntegrationObjectItemAttributeModel) { getAttributeName() >> attrName }
            }
            getVirtualAttributes() >> virtualAttributeNames.collect { attrName ->
                Stub(IntegrationObjectItemVirtualAttributeModel) { getAttributeName() >> attrName }
            }
        }
    }

}
