/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import spock.lang.Specification

@UnitTest
class IntegrationObjectItemNotFoundExceptionUnitTest extends Specification {
    def "context provided during instantiation can be read back"() {
        given:
        def ex = new IntegrationObjectItemNotFoundException('Object', 'Item')

        expect:
        ex.integrationObjectCode == 'Object'
        ex.integrationItemType == 'Item'
    }

    def "human readable message is generated from the context"() {
        given:
        def ex = new IntegrationObjectItemNotFoundException('Object', 'Item')

        expect:
        ex.message.contains 'Object'
        ex.message.contains 'Item'
    }
}
