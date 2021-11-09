/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl.handlers

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.CompletedOutboundSyncEvent
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class CompletedOutboundSyncEventHandlerUnitTest extends Specification {
    private static final PK SOME_JOB_PK = PK.fromLong(12)
    private static final int TOTAL_ITEMS = 100

    def handler = CompletedOutboundSyncEventHandler.createHandler()

    @Test
    @Unroll
    def 'getHandledEventClass returns a CompletedOutboundSyncEvent'() {
        expect:
        handler.getHandledEventClass() == CompletedOutboundSyncEvent
    }

    @Test
    @Unroll
    def 'job result is #expectedResult and status to FINISHED when all items have been processed and updated state is returned for a completed event with #eventSuccessCase'() {
        given:
        def initState = initialOutboundSyncState().withTotalItems(100).withSuccessCount(96).build()
        and:
        def event = new CompletedOutboundSyncEvent(SOME_JOB_PK, isSuccess, 4)

        when:
        def updatedState = handler.handle(event, initState)

        then:
        with(updatedState) {
            totalItemsRequested == initState.totalItemsRequested
            startTime == initState.startTime
            cronJobStatus == CronJobStatus.FINISHED
            endTime >= startTime
            successCount == expectedSuccessCount
            errorCount == expectedErrorCount
        }

        where:
        eventSuccessCase | isSuccess | expectedResult        | expectedSuccessCount | expectedErrorCount
        'success'        | true      | CronJobResult.SUCCESS | 100                  | 0
        'error'          | false     | CronJobResult.ERROR   | 96                   | 4
    }

    @Test
    @Unroll
    def 'job state is not finished when not all items processed for a completed event with #eventSuccessCase'() {
        given:
        def event = new CompletedOutboundSyncEvent(SOME_JOB_PK, isSuccess, 2)
        and:
        def initState = initialOutboundSyncState().withTotalItems(3).build()

        when:
        def updatedState = handler.handle event, initState

        then:
        with(updatedState) {
            totalItemsRequested == initState.totalItemsRequested
            startTime == initState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            endTime == null
            successCount == expectedSuccessCount
            errorCount == expectedErrorCount
            unprocessedCount == initState.unprocessedCount
        }

        where:
        eventSuccessCase | isSuccess | expectedSuccessCount | expectedErrorCount
        'success'        | true      | 2                    | 0
        'error'          | false     | 0                    | 2
    }

    @Test
    @Unroll
    def 'job state is not finished when number of items to process is unknown for a completed event with #eventSuccessCase'() {
        given:
        def event = new CompletedOutboundSyncEvent(SOME_JOB_PK, isSuccess, 2)
        and:
        def initState = initialOutboundSyncState().build()

        when:
        def updatedState = handler.handle event, initState

        then:
        with(updatedState) {
            totalItemsRequested.empty
            startTime == initState.startTime
            cronJobStatus == CronJobStatus.RUNNING
            endTime == null
            successCount == expectedSuccessCount
            errorCount == expectedErrorCount
            unprocessedCount == initState.unprocessedCount
        }

        where:
        eventSuccessCase | isSuccess | expectedSuccessCount | expectedErrorCount
        'success'        | true      | 2                    | 0
        'error'          | false     | 0                    | 2
    }
}
