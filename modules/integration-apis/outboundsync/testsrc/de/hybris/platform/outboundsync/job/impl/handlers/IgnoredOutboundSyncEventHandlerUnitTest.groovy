/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.IgnoredOutboundSyncEvent
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class IgnoredOutboundSyncEventHandlerUnitTest extends Specification {
    private static final PK SOME_JOB_PK = PK.fromLong(1)
    private static final int ITEMS_COUNT = 10
    private static final def EVENT = new IgnoredOutboundSyncEvent(SOME_JOB_PK)

    def handler = IgnoredOutboundSyncEventHandler.createHandler()

    @Test
    def 'getHandledEventClass returns IgnoredOutboundSyncEvent'() {
        expect:
        handler.getHandledEventClass() == IgnoredOutboundSyncEvent
    }

    @Test
    def 'event changes only success count in the job state when total number of changed items is not known'() {
        given:
        def initialState = initialOutboundSyncState().build()

        when:
        def newState = handler.handle EVENT, initialState

        then:
        with(newState) {
            startTime == initialState.startTime
            endTime == initialState.endTime
            cronJobStatus == initialState.cronJobStatus
            unprocessedCount == initialState.unprocessedCount
            successCount == initialState.successCount + 1
            errorCount == initialState.errorCount
        }
    }

    @Test
    def 'event changes only success count in the job state when not all items yet processed'() {
        given:
        def initState = initialOutboundSyncState().withTotalItems(2).build()

        when:
        def updatedState = handler.handle EVENT, initState

        then:
        with(updatedState) {
            totalItemsRequested == initState.totalItemsRequested
            startTime == initState.startTime
            cronJobStatus == initState.cronJobStatus
            endTime == null
            successCount == initState.successCount + 1
            errorCount == initState.errorCount
            unprocessedCount == initState.unprocessedCount
        }
    }

    @Test
    def 'job result is SUCCESS and status is FINISHED when all items are ignored'() {
        given:
        def initState = initialOutboundSyncState()
                .withTotalItems(2)
                .withSuccessCount(1)
                .build()

        when:
        def updatedState = handler.handle EVENT, initState

        then:
        with(updatedState) {
            totalItemsRequested == initState.totalItemsRequested
            startTime == initState.startTime
            cronJobStatus == CronJobStatus.FINISHED
            cronJobResult == CronJobResult.SUCCESS
            endTime >= startTime
            successCount == initState.successCount + 1
            errorCount == initState.errorCount
            unprocessedCount == initState.unprocessedCount
        }
    }
}
