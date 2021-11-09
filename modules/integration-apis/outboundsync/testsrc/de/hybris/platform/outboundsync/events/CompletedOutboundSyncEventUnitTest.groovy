/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CompletedOutboundSyncEventUnitTest extends Specification {

    @Shared
    def aCronJobPk = PK.fromLong(1234)
    @Shared
    def anotherCronJobPk = PK.fromLong(4321)

    @Test
    @Unroll
    def "equals is #expectedEquals when #condition"() {
        given:
        def event1 = new CompletedOutboundSyncEvent(cronjobPk1, successFlag1, itemsCompleted1)
        def event2 = new CompletedOutboundSyncEvent(cronjobPk2, successFlag2, itemsCompleted2)

        expect:
        (event1 == event2) == expectedEquals

        where:
        condition                     | cronjobPk1 | cronjobPk2       | successFlag1 | successFlag2 | itemsCompleted1 | itemsCompleted2 | expectedEquals
        "all fields are equal"        | aCronJobPk | aCronJobPk       | true         | true         | 3               | 3               | true
        "cronjob pk's are different"  | aCronJobPk | anotherCronJobPk | true         | true         | 3               | 3               | false
        "success flags are different" | aCronJobPk | aCronJobPk       | true         | false        | 3               | 3               | false
        "item counts are different"   | aCronJobPk | aCronJobPk       | true         | false        | 3               | 6               | false
    }

    @Test
    def "equals is true when events are the same object"() {
        given:
        def event1 = new CompletedOutboundSyncEvent(aCronJobPk, false, 7)

        expect:
        event1 == event1
    }

    @Test
    def 'is not equal to even of a different type'() {
        given:
        def event1 = new AbortedOutboundSyncEvent(aCronJobPk, 7)

        expect:
        new CompletedOutboundSyncEvent(aCronJobPk, false, 7) != event1
    }

    @Test
    def "equals is false when comparing to null event"() {
        expect:
        !(new CompletedOutboundSyncEvent(aCronJobPk, true, 5) == null)
    }

    @Test
    def "exception is thrown when instantiating the event with null cronjob pk"() {
        when:
        new CompletedOutboundSyncEvent(null, true, 8)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "hashCode is #expectedEquals when #condition"() {
        given:
        def event1 = new CompletedOutboundSyncEvent(cronjobPk1, successFlag1, itemsCompleted1)
        def event2 = new CompletedOutboundSyncEvent(cronjobPk2, successFlag2, itemsCompleted2)

        expect:
        (event1.hashCode() == event2.hashCode()) == expectedEquals

        where:
        condition                     | cronjobPk1 | cronjobPk2       | successFlag1 | successFlag2 | itemsCompleted1 | itemsCompleted2 | expectedEquals | hashCodesEquals
        "all fields are equal"        | aCronJobPk | aCronJobPk       | true         | true         | 3               | 3               | true           | 'equals'
        "cronjob pk's are different"  | aCronJobPk | anotherCronJobPk | true         | true         | 3               | 3               | false          | 'not equals'
        "success flags are different" | aCronJobPk | aCronJobPk       | true         | false        | 3               | 3               | false          | 'not equals'
        "item counts are different"   | aCronJobPk | aCronJobPk       | true         | false        | 3               | 6               | false          | 'not equals'
    }
}
