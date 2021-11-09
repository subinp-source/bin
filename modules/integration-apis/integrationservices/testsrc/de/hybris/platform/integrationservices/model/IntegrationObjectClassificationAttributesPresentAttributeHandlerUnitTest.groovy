/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationObjectClassificationAttributesPresentAttributeHandlerUnitTest extends Specification {
    def handler = new IntegrationObjectClassificationAttributesPresentAttributeHandler()

    @Test
    @Unroll
    def "returns false when integration object #condition"() {
        given: "the integration object #condition"
        def io = Stub(IntegrationObjectModel) {
            getItems() >> items
        }

        expect:
        !handler.get(io)

        where:
        condition                                | items
        'does not have items'                    | []
        'has items without attributes'           | [item(), item()]
        'has items with regular attributes only' | [item([attribute(), attribute()]), item([attribute()])]
    }

    @Test
    def 'returns true when integration object contains at least one classification attribute in at least one integration item'() {
        given: 'the integration object contains one classification attribute in one of the items'
        def io = Stub(IntegrationObjectModel) {
            getItems() >> [
                    item([attribute(), attribute()]),
                    item([attribute()], [classificationAttribute()])
            ]
        }

        expect:
        handler.get(io)
    }

    IntegrationObjectItemModel item(List<IntegrationObjectItemAttributeModel> attributes=[], List<IntegrationObjectItemClassificationAttributeModel> classificationAttributes=[]) {
        Stub(IntegrationObjectItemModel) {
            getAttributes() >> attributes
            getClassificationAttributes() >> classificationAttributes
        }
    }

    IntegrationObjectItemAttributeModel attribute() {
        Stub(IntegrationObjectItemAttributeModel)
    }

    IntegrationObjectItemClassificationAttributeModel classificationAttribute() {
        Stub(IntegrationObjectItemClassificationAttributeModel)
    }
}
