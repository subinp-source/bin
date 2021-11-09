/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
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
class IntegrationObjectItemVirtualAttributeModelInterceptorUnitTest extends Specification {

    def interceptor = new IntegrationObjectItemVirtualAttributeModelInterceptor()

    @Test
    def "throws an exception on validate when returnIntegrationObjectItem is provided"() {
        given:
        def attributeName = "testAttrName"
        def itemCode = "testItem"
        def attr = Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> attributeName
            getReturnIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getCode() >> itemCode
            }
        }

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Virtual attribute cannot have a returnIntegrationObjectItem. Only primitive virtual " +
                "attributes are currently supported. Found [$attributeName] attribute with a " +
                "returnIntegrationObjectItem set to [$itemCode]."
    }

    @Test
    def "does not throw an exception when returnIntegrationObjectItem is null"() {
        given:
        def attr = Stub(IntegrationObjectItemVirtualAttributeModel) {
            getReturnIntegrationObjectItem() >> null
        }

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()
    }

    @Test
    @Unroll
    def "exception is thrown when the virtual attribute name is a duplicate of a #attributeType attribute"() {
        given:
        def attrName = 'duplicateName'
        def attribute = attribute attrName, integrationObjectItem(standarAttributeNames, classAttributeNames)

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "The attribute [$attrName] already exists in this integration object item. Please provide a different name for one of the [$attrName] attributes."

        where:
        attributeType    | standarAttributeNames | classAttributeNames
        'standard'       | ['duplicateName']     | null
        'classification' | null                  | ['duplicateName']
    }

    def attribute(String attrName, IntegrationObjectItemModel intObjectItem) {
        Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> attrName
            getIntegrationObjectItem() >> intObjectItem
            getReturnIntegrationObjectItem() >> null
        }
    }

    def integrationObjectItem(List standardAttributeNames, List classAttributeNames) {
        Stub(IntegrationObjectItemModel) {
            getAttributes() >> standardAttributeNames.collect { attrName ->
                Stub(IntegrationObjectItemAttributeModel) { getAttributeName() >> attrName }
            }
            getClassificationAttributes() >> classAttributeNames.collect { attrName ->
                Stub(IntegrationObjectItemClassificationAttributeModel) { getAttributeName() >> attrName }
            }
        }
    }
}
