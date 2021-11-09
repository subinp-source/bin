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
class SystemErrorOutboundSyncEventUnitTest extends Specification {

    private static final def A_CRONJOB_PK = PK.fromLong(1234)
    private static final def ANOTHER_CRONJOB_PK = PK.fromLong(4321)
    private static final int ITEMS_PROCESSED = 3

    private static final def EVENT = new SystemErrorOutboundSyncEvent(A_CRONJOB_PK, ITEMS_PROCESSED)

    @Test
    @Unroll
    def "#initialEvent equals #other is #expectedEquals"() {
        expect:
        (EVENT == other) == expectedEquals

        where:
        initialEvent | other                                                                 | expectedEquals
        EVENT        | EVENT                                                                 | true
        EVENT        | new SystemErrorOutboundSyncEvent(A_CRONJOB_PK, ITEMS_PROCESSED)       | true
        EVENT        | new SystemErrorOutboundSyncEvent(ANOTHER_CRONJOB_PK, ITEMS_PROCESSED) | false
        EVENT        | new SystemErrorOutboundSyncEvent(A_CRONJOB_PK, 6)                     | false
        EVENT        | new IgnoredOutboundSyncEvent(A_CRONJOB_PK)                            | false
        EVENT        | null                                                                  | false
    }

    @Test
    @Unroll
    def "hashCode is #expectedEquals between #initialEvent and #other"() {
        expect:
        (EVENT.hashCode() == other.hashCode()) == expectedEquals

        where:
        initialEvent | other                                                                 | expectedEquals
        EVENT        | EVENT                                                                 | true
        EVENT        | new SystemErrorOutboundSyncEvent(A_CRONJOB_PK, ITEMS_PROCESSED)       | true
        EVENT        | new SystemErrorOutboundSyncEvent(ANOTHER_CRONJOB_PK, ITEMS_PROCESSED) | false
        EVENT        | new SystemErrorOutboundSyncEvent(A_CRONJOB_PK, 6)                     | false
        EVENT        | new IgnoredOutboundSyncEvent(A_CRONJOB_PK)                            | false
    }

    @Test
    def "exception is thrown when instantiating the event with null cronjob pk"() {
        when:
        new SystemErrorOutboundSyncEvent(null, ITEMS_PROCESSED)

        then:
        thrown(IllegalArgumentException)
    }
}
