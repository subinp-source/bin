/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.SystemErrorOutboundSyncEvent
import org.junit.Test
import spock.lang.Specification

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class SystemErrorOutboundSyncEventHandlerUnitTest extends Specification {
    private static final PK SOME_JOB_PK = PK.fromLong(12)
    private static final int TOTAL_ITEMS = 34

    def handler = SystemErrorOutboundSyncEventHandler.createHandler()

    @Test
    def 'getHandledEventClass returns SystemErrorOutboundSyncEvent'() {
        expect:
        handler.getHandledEventClass() == SystemErrorOutboundSyncEvent
    }

    @Test
    def 'job result is FAILURE and status is RUNNING when total item is unknown'() {
        given: 'initial state with all attributes initialized'
        def initialState = initialOutboundSyncState()
                .withUnprocessedCount(1)
                .build()
        and: 'an event with one item processed'
        def event = new SystemErrorOutboundSyncEvent(SOME_JOB_PK, 1)

        when:
        def updatedState = handler.handle(event, initialState)

        then: 'unprocessed item count and systemic error flag are changed'
        with(updatedState) {
            totalItemsRequested.empty
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == CronJobResult.FAILURE
            endTime == initialState.endTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == initialState.unprocessedCount + event.changesProcessed
            aborted == initialState.aborted
            systemicError != initialState.systemicError
        }
    }

    @Test
    def 'job result is FAILURE and status is RUNNING when not all items are processed'() {
        given: 'initial state with all attributes initialized'
        def initialState = initialOutboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withUnprocessedCount(1)
                .build()
        and: 'an event with one item processed'
        def event = new SystemErrorOutboundSyncEvent(SOME_JOB_PK, 1)

        when:
        def updatedState = handler.handle(event, initialState)

        then:
        with(updatedState) {
            totalItemsRequested == initialState.totalItemsRequested
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == CronJobResult.FAILURE
            endTime == initialState.endTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == initialState.unprocessedCount + event.changesProcessed
            aborted == initialState.aborted
            systemicError != initialState.systemicError
        }
    }

    @Test
    def 'job result is FAILURE and status is FINISHED when all items are processed'() {
        given:
        def initialState = initialOutboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withUnprocessedCount(TOTAL_ITEMS-2)
                .build()
        and:
        def event = new SystemErrorOutboundSyncEvent(SOME_JOB_PK, 2)

        when:
        def updatedState = handler.handle(event, initialState)

        then:
        with(updatedState) {
            totalItemsRequested == initialState.totalItemsRequested
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.FINISHED
            cronJobResult == CronJobResult.FAILURE
            endTime >= startTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == initialState.unprocessedCount + event.changesProcessed
            aborted == initialState.aborted
            systemicError != initialState.systemicError
        }
    }
}
