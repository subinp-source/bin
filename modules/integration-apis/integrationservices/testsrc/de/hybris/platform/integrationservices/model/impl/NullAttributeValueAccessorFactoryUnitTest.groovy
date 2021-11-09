/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class NullAttributeValueAccessorFactoryUnitTest extends Specification {

    def factory = new NullAttributeValueAccessorFactory()

    @Test
    def 'create with type attribute descriptor returns null attribute value accessor'() {
        expect:
        factory.create Stub(TypeAttributeDescriptor)
    }

    @Test
    def 'create with type attribute descriptor and class attribute assignment returns null attribute value accessor'() {
        expect:
        factory.create Stub(TypeAttributeDescriptor), Stub(ClassAttributeAssignmentModel)
    }
}
