/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ClassificationAttributeSettableCheckerUnitTest extends Specification {

    def checker = new ClassificationAttributeSettableChecker()

    @Test
    def "attribute settable checker always returns true for classification attributes"() {
        expect:
        checker.isSettable(Stub(ItemModel), Stub(TypeAttributeDescriptor))
    }
}
