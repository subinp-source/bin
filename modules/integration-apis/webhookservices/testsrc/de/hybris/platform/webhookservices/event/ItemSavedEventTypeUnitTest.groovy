/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ItemSavedEventTypeUnitTest extends Specification {

    @Test
    def "converts event type #eventTypeInteger to ItemSavedEventType enum #eventTypeEnum"() {
        when:
        def eventType = ItemSavedEventType.fromEventTypeCode(eventTypeInteger)

        then:
        eventType == eventTypeEnum

        where:
        eventTypeInteger | eventTypeEnum
        1                | ItemSavedEventType.UPDATE
        4                | ItemSavedEventType.CREATE
    }

    @Test
    def "exception is thrown if eventType is an invalid integer"() {
        when:
        ItemSavedEventType.fromEventTypeCode(5)

        then:
        def e = thrown InvalidEventTypeException
        e.message == "The event type [5] is invalid"
    }
}