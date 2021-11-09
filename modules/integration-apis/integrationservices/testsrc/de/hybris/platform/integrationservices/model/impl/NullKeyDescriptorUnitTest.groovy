/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.integrationkey.KeyValue
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NullKeyDescriptorUnitTest extends Specification {
    def descriptor = new NullKeyDescriptor()

    @Test
    @Unroll
    def "calculates empty key for #item data item"() {
        expect:
        descriptor.calculateKey(item) == new KeyValue()

        where:
        item << [[attribute: 'value'], [:], null]
    }

    @Test
    @Unroll
    def "isKeyAttribute returns false for #attr attribute"() {
        expect:
        ! descriptor.isKeyAttribute(attr)

        where:
        attr << ['value', 'any', null, '']
    }
}
