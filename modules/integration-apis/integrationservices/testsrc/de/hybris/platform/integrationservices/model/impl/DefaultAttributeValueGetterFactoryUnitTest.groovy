/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.classification.ClassificationService
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorFactory
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultAttributeValueGetterFactoryUnitTest extends Specification {

    def factory = new DefaultAttributeValueGetterFactory(
            modelService: Stub(ModelService),
            classificationService: Stub(ClassificationService),
            logicExecutorFactory: Stub(LogicExecutorFactory)
    )

    @Test
    @Unroll
    def "create with #descriptor type attribute descriptor returns #valueGetterType value getter"() {
        when:
        def getter = factory.create descriptor

        then:
        valueGetterType.isInstance getter

        where:
        descriptor                                  | valueGetterType
        Stub(DefaultTypeAttributeDescriptor)        | StandardAttributeValueGetter
        virtualTypeAttributeDescriptor()            | VirtualAttributeValueGetter
        Stub(ClassificationTypeAttributeDescriptor) | ClassificationAttributeValueGetter
        null                                        | NullAttributeValueGetter
    }

    def virtualTypeAttributeDescriptor() {
        Stub(VirtualTypeAttributeDescriptor) {
            getLogicLocation() >> "model://someScriptName"
        }
    }
}
