/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.AbortedOutboundSyncEvent
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class AbortedOutboundSyncEventHandlerUnitTest extends Specification {
    private static final PK SOME_JOB_PK = PK.fromLong(12)
    private static final int TOTAL_ITEMS = 34
    private static final int ERROR_COUNT = 10
    private static final int SUCCESS_COUNT = 22

    def handler = AbortedOutboundSyncEventHandler.createHandler()

    @Test
    def 'getHandledEventClass returns AbortedOutboundSyncEvent'() {
        expect:
        handler.getHandledEventClass() == AbortedOutboundSyncEvent
    }

    @Test
    @Unroll
    def "job result is #result and status is RUNNING when there are #condition and total item is unknown"() {
        given: 'initial state with all attributes initialized'
        def initialState = initialOutboundSyncState()
                .withSuccessCount(success)
                .withErrorCount(error)
                .withUnprocessedCount(1)
                .build()
        and: 'an event with one item processed'
        def event = new AbortedOutboundSyncEvent(SOME_JOB_PK, 1)

        when:
        def updatedState = handler.handle(event, initialState)

        then:
        with(updatedState) {
            totalItemsRequested.empty
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == result
            endTime == initialState.endTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == initialState.unprocessedCount + event.changesProcessed
            aborted != initialState.aborted
            systemicError == initialState.systemicError
        }

        where:
        condition   | success       | error       | result
        'errors'    | 0             | ERROR_COUNT | CronJobResult.ERROR
        'no errors' | SUCCESS_COUNT | 0           | CronJobResult.SUCCESS
    }

    @Test
    @Unroll
    def "job result is #result and status is RUNNING when there are #condition and not all items are processed"() {
        given: 'initial state with all attributes initialized'
        def initialState = initialOutboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withSuccessCount(success)
                .withErrorCount(error)
                .withUnprocessedCount(1)
                .build()
        and: 'an event with one item processed'
        def event = new AbortedOutboundSyncEvent(SOME_JOB_PK, 1)

        when:
        def updatedState = handler.handle(event, initialState)

        then:
        with(updatedState) {
            totalItemsRequested == initialState.totalItemsRequested
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == result
            endTime == initialState.endTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == initialState.unprocessedCount + event.changesProcessed
            aborted != initialState.aborted
            systemicError == initialState.systemicError
        }

        where:
        condition   | success       | error       | result
        'errors'    | 0             | ERROR_COUNT | CronJobResult.ERROR
        'no errors' | SUCCESS_COUNT | 0           | CronJobResult.SUCCESS
    }

    @Test
    @Unroll
    def "job result is #result and status is FINISHED when there are #condition and all items are processed"() {
        given:
        def initialState = outboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withSuccessCount(success)
                .withErrorCount(error)
                .build()
        and:
        def event = new AbortedOutboundSyncEvent(SOME_JOB_PK, 2)

        when:
        def updatedState = handler.handle(event, initialState)

        then:
        with(updatedState) {
            totalItemsRequested == initialState.totalItemsRequested
            startTime == initialState.startTime
            cronJobStatus == CronJobStatus.ABORTED
            cronJobResult == result
            endTime >= startTime
            successCount == initialState.successCount
            errorCount == initialState.errorCount
            unprocessedCount == event.changesProcessed
            aborted != initialState.aborted
            systemicError == initialState.systemicError
        }

        where:
        condition   | success     | error       | result
        'errors'    | 0           | TOTAL_ITEMS | CronJobResult.ERROR
        'no errors' | TOTAL_ITEMS | 0           | CronJobResult.SUCCESS
    }

    def outboundSyncState() {
        initialOutboundSyncState().withSuccessCount(SUCCESS_COUNT)
    }
}
