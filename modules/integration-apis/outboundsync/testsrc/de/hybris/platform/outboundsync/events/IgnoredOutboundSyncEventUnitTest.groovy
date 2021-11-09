/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.events

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IgnoredOutboundSyncEventUnitTest extends Specification {
    private static final def JOB_PK = PK.fromLong(1)
    private static final def OTHER_JOB_PK = PK.fromLong(2)
    private static final int ITEMS_IGNORED = 3
    private static final def EVENT = new IgnoredOutboundSyncEvent(JOB_PK)

    @Test
    @Unroll
    def "IgnoredOutboundSyncEvent(cronJobPk=1, itemsIgnored=3) equals #other is #equalsResult"() {
        expect:
        (EVENT == other) == equalsResult

        where:
        other                                               | equalsResult
        new IgnoredOutboundSyncEvent(JOB_PK)                | true
        new IgnoredOutboundSyncEvent(OTHER_JOB_PK)          | false
        new AbortedOutboundSyncEvent(JOB_PK, ITEMS_IGNORED) | false
    }

    @Test
    def "equals itself"() {
        expect:
        EVENT == EVENT
    }

    @Test
    def "equals is false when comparing to null event"() {
        expect:
        !(EVENT == null)
    }

    @Test
    def "exception is thrown when instantiating the event with null cronjob pk"() {
        when:
        new IgnoredOutboundSyncEvent(null)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "hashCode is #expectedEquals between IgnoredOutboundSyncEvent{cronJobPk=1} and #other"() {
        expect:
        (EVENT.hashCode() == other.hashCode()) == expectedEquals

        where:
        other                                      | expectedEquals | isEquals
        new IgnoredOutboundSyncEvent(JOB_PK)       | true           | "is equals"
        new IgnoredOutboundSyncEvent(OTHER_JOB_PK) | false          | "is not equals"
    }
}
