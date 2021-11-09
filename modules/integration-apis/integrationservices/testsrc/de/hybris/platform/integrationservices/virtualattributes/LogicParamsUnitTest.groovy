/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.product.ProductModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LogicParamsUnitTest extends Specification {
    private static final ItemModel ITEM = new ItemModel()

    @Test
    @Unroll
    def "LogicParams stores the parameter #param"() {
        given:
        def params = LogicParams.create(param)

        expect:
        params.item == param

        where:
        param << [ITEM, null]
    }

    @Test
    @Unroll
    def "two LogicParams are equal when they #condition"() {
        expect:
        param1 == param2

        where:
        condition               | param1                   | param2
        'are the same object'   | LogicParams.create(ITEM) | param1
        'have the same content' | LogicParams.create(null) | LogicParams.create(null)
    }

    @Test
    @Unroll
    def "two LogicParams are different when param2 #condition"() {
        given:
        def param1 = LogicParams.create(Stub(CatalogModel))

        expect:
        param1 != param2

        where:
        condition                    | param2
        'is null'                    | null
        'have the different content' | LogicParams.create(Stub(ProductModel))
        'is a different object type' | new Object()
    }

    @Test
    @Unroll
    def "two LogicParams have the same hashcode when they #condition"() {
        expect:
        param1.hashCode() == param2.hashCode()

        where:
        condition               | param1                   | param2
        'are the same object'   | LogicParams.create(ITEM) | param1
        'have the same content' | LogicParams.create(null) | LogicParams.create(null)
    }

    @Test
    @Unroll
    def "two LogicParams different hashcodes when param2 #condition"() {
        given:
        def param1 = LogicParams.create(Stub(CatalogModel))

        expect:
        param1.hashCode() != param2.hashCode()

        where:
        condition                    | param2
        'have the different content' | LogicParams.create(Stub(ProductModel))
        'is a different object type' | new Object()
    }

    @Test
    @Unroll
    def "converts parameters with #item item to a Map"() {
        given:
        def params = LogicParams.create(item)
        expect:
        params.asMap() == map

        where:
        item | map
        ITEM | [itemModel: ITEM]
        null | [:]
    }
}