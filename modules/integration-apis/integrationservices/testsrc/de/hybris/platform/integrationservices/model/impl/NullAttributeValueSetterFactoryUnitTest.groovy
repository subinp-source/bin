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
class NullAttributeValueSetterFactoryUnitTest extends Specification {

    def factory = new NullAttributeValueSetterFactory()

    @Test
    def 'create with type attribute descriptor and attribute assignment returns null attribute value setter'() {
        when:
        def setter = factory.create Stub(TypeAttributeDescriptor)

        then:
        setter instanceof NullAttributeValueSetter
    }
}
