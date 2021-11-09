/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import org.junit.Test
import spock.lang.Specification

@UnitTest
class IntegrationObjectAndItemMismatchExceptionUnitTest extends Specification {
    @Test
    def "provided context can be read back from the exception"() {
        given:
        def itemModel = Stub(ItemModel)
        def io = Stub(IntegrationObjectDescriptor)

        expect:
        def e = new IntegrationObjectAndItemMismatchException(itemModel, io)
        e.dataItem == itemModel
        e.integrationObject == io
    }

    @Test
    def "message includes provided context"() {
        given:
        def itemModel = Stub(ItemModel)
        def io = Stub(IntegrationObjectDescriptor)

        expect:
        def e = new IntegrationObjectAndItemMismatchException(itemModel, io)
        e.message.contains itemModel.toString()
        e.message.contains io.toString()
    }
}
