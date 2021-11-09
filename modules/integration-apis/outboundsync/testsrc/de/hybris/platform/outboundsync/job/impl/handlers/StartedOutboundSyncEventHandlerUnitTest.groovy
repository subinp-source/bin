/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class StartedOutboundSyncEventHandlerUnitTest extends Specification {
    private static final PK CRONJOB_PK = PK.fromLong(12)
    private static final def EVENT = new StartedOutboundSyncEvent(CRONJOB_PK, new Date(), 34)

    def handler = StartedOutboundSyncEventHandler.createHandler()

    @Test
    def 'getHandledEventClass returns StartedOutboundSyncEvent'() {
        expect:
        handler.getHandledEventClass() == StartedOutboundSyncEvent
    }

    @Test
    def 'handle changes only total items count in the state'() {
        given:
        def origState = initialOutboundSyncState()
                .withSuccessCount(3)
                .withErrorCount(2)
                .withUnprocessedCount(1)
                .build()

        when:
        def newState = handler.handle(EVENT, origState)

        then:
        with(newState) {
            startTime == EVENT.startTime
            totalItemsRequested.orElse(-1) == EVENT.itemCount
            successCount == origState.successCount
            errorCount == origState.errorCount
            unprocessedCount == origState.unprocessedCount
        }
    }
}
