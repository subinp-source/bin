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

package de.hybris.platform.integrationservices.model

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class KeyAttributesAttributeHandlerUnitTest extends Specification {
    def handler = new KeyAttributesAttributeHandler()

    @Test
    @Unroll
    def "empty collection returned when item does not have #desc"() {
        expect:
        handler.get(model)?.isEmpty()

        where:
        desc             | model
        'attributes'     | item()
        'key attributes' | item([nonUniqueAttribute(), nonUniqueAttribute(item())])
    }

    @Test
    def "returns key attributes when item has only simple key attributes unique"() {
        given:
        def key1 = uniqueAttribute()
        def key2 = uniqueAttribute()
        def nonKey = nonUniqueAttribute()

        when:
        def keyAttributes = handler.get(item([key1, nonKey, key2]))

        then:
        keyAttributes.containsAll([key1, key2])
        ! keyAttributes.contains(nonKey)
    }

    @Test
    def "returns key attributes when item has only reference attribute unique"() {
        given:
        def key1 = uniqueAttribute(item())
        def key2 = uniqueAttribute(item())
        def nonKey = nonUniqueAttribute()

        when:
        def keys = handler.get(item([nonKey, key1, key2]))

        then:
        keys.containsAll([key1, key2])
        ! keys.contains(nonKey)
    }

    @Test
    def "returns key attribute when item has simple and reference key attributes"() {
        given:
        def key1 = uniqueAttribute()
        def key2 = uniqueAttribute(item())
        def nonKey = nonUniqueAttribute()

        when:
        def keys = handler.get(item([nonKey, key1, key2]))

        then:
        keys.containsAll([key1, key2])
        ! keys.contains(nonKey)
    }

    def item(List<IntegrationObjectItemAttributeModel> attributes = []) {
        Stub(IntegrationObjectItemModel) {
            getAttributes() >> attributes
        }
    }

    def nonUniqueAttribute(IntegrationObjectItemModel item = null) {
        Stub(IntegrationObjectItemAttributeModel) {
            getReturnIntegrationObjectItem() >> item
        }
    }

    def uniqueAttribute(IntegrationObjectItemModel item = null) {
        Stub(IntegrationObjectItemAttributeModel) {
            getUnique() >> true
            getReturnIntegrationObjectItem() >> item
        }
    }
}
