/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.servicelayer.cronjob.PerformResult
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class OutboundSyncStateUnitTest extends Specification {
    private static final def TOTAL_ITEMS = 5
    private static final def SUCCESS_ITEMS = 1
    private static final def ERROR_ITEMS = 2
    private static final def UNPROCESSED_ITEMS = 1
    private static final def START_TIME = new Date()
    private static final def END_TIME = Date.from Instant.now().plusSeconds(30)

    @Test
    def "builds successfully initial state when provided field is valid"() {
        given:
        def syncState = initialOutboundSyncState().build()

        expect:
        with(syncState) {
            totalItemsRequested.empty
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == CronJobResult.UNKNOWN
            startTime == null
            endTime == null
            successCount == 0
            errorCount == 0
            unprocessedCount == 0
            !aborted
            !systemicError
        }
    }

    @Test
    @Unroll
    def "exception is thrown when #fieldCondition"() {
        when:
        initialOutboundSyncState()
                .withTotalItems(items)
                .withSuccessCount(successes)
                .withErrorCount(errors)
                .withUnprocessedCount(ignored)
                .build()

        then:
        thrown IllegalArgumentException

        where:
        fieldCondition                  | items | successes | errors | ignored
        'total items is negative'       | -1    | 0         | 0      | 0
        'success items is negative'     | 0     | -1        | 0      | 0
        'error items is negative'       | 0     | 0         | -1     | 0
        'unprocessed items is negative' | 0     | 0         | 0      | -1
    }

    @Test
    @Unroll
    def "job status is RUNNING when total number of items is unknown and #field is applied"() {
        given: 'total number of successful items, error items and unprocessed items is less then total items count'
        def state = initialOutboundSyncState()
                .withSuccessCount(success)
                .withErrorCount(error)
                .withUnprocessedCount(ignored)
                .build()

        expect:
        state.cronJobStatus == CronJobStatus.RUNNING

        where:
        field               | success | error | ignored
        'success count'     | 1       | 0     | 0
        'error count'       | 0       | 1     | 0
        'unprocessed count' | 0       | 0     | 1
    }

    @Test
    @Unroll
    def "job status is RUNNING while #condition"() {
        given: 'total number of successful items, error items and unprocessed items is less then total items count'
        def state = initialOutboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withSuccessCount(success)
                .withErrorCount(error)
                .withUnprocessedCount(ignored)
                .build()

        expect:
        state.cronJobStatus == CronJobStatus.RUNNING

        where:
        condition                 | success | error | ignored
        'no items processed'      | 0       | 0     | 0
        'not all items processed' | 1       | 1     | 1
    }

    @Test
    @Unroll
    def "job status is #status when abort flag #abort"() {
        given: 'total number of successful items, error items and unprocessed items equal the total items count'
        def state = initialOutboundSyncState()
                .withTotalItems(TOTAL_ITEMS)
                .withSuccessCount(3)
                .withErrorCount(1)
                .withUnprocessedCount(1)
                .withAborted(aborted)
                .build()

        expect:
        state.cronJobStatus == status
        state.endTime

        where:
        aborted | status
        true    | CronJobStatus.ABORTED
        false   | CronJobStatus.FINISHED
    }

    @Test
    @Unroll
    def "job result is #result when #condition"() {
        given:
        def state = initialOutboundSyncState()
                .withSuccessCount(success)
                .withErrorCount(error)
                .withSystemicError(systemError)
                .build()

        expect:
        state.cronJobResult == result

        where:
        condition                         | success | error | systemError | result
        'no items processed'              | 0       | 0     | false       | CronJobResult.UNKNOWN
        'no error items present'          | 1       | 0     | false       | CronJobResult.SUCCESS
        'at least one error item present' | 1       | 1     | false       | CronJobResult.ERROR
        'systemic error occurred'         | 0       | 1     | true        | CronJobResult.FAILURE
    }

    @Test
    @Unroll
    def "allItemsProcessed is #res when totalItems is #total, success is 3, errors is 2, and ignored is 1"() {
        given:
        def state = initialOutboundSyncState()
                .withTotalItems(total)
                .withSuccessCount(3)
                .withErrorCount(2)
                .withUnprocessedCount(1)
                .build()

        expect:
        state.allItemsProcessed == res

        where:
        total | res
        7     | false
        6     | true
        5     | true
    }

    @Test
    def 'allItemsProcessed is false when totalItems is unknown and success, errors, and ignored items applied'() {
        given:
        def state = initialOutboundSyncState()
                .withSuccessCount(30)
                .withErrorCount(20)
                .withUnprocessedCount(10)
                .build()

        expect:
        !state.allItemsProcessed
    }

    @Test
    def 'state is finished when there are no items to process'() {
        given:
        def state = initialOutboundSyncState()
                .withTotalItems(0)
                .build()

        expect:
        state.allItemsProcessed
        state.cronJobStatus == CronJobStatus.FINISHED
        state.cronJobResult == CronJobResult.SUCCESS
    }

    @Test
    @Unroll
    def "two outbound sync states are not equals when #condition"() {
        given:
        def state1 = outboundSyncStateBuilder().build()


        expect:
        state1 != state2

        where:
        condition                          | state2
        "totalItemsRequested is different" | outboundSyncStateBuilder().withTotalItems(35).build()
        "startDate is different"           | outboundSyncStateBuilder().withStartTime(END_TIME).build()
        "successCount is different"        | outboundSyncStateBuilder().withSuccessCount(77777).build()
        "errorCount is different"          | outboundSyncStateBuilder().withErrorCount(9999999).build()
        "postAbortCount is different"      | outboundSyncStateBuilder().withUnprocessedCount(19283746).build()
        "endDate is different"             | outboundSyncStateBuilder().withEndTime(START_TIME).build()
        "abort is different"               | outboundSyncStateBuilder().withAborted(true).build()
        "systemicError is different"       | outboundSyncStateBuilder().withSystemicError(true).build()
    }

    @Test
    def "two outbound sync states are equals"() {
        given:
        def state1 = outboundSyncState()
        def state2 = outboundSyncState()

        expect:
        state1 == state2
        and:
        state1 == state1
    }

    @Test
    @Unroll
    def "two outbound sync states have different hashCodes when #condition"() {
        given:
        def state1 = outboundSyncState()

        expect:
        state1.hashCode() != state2.hashCode()

        where:
        condition                          | state2
        "totalItemsRequested is different" | outboundSyncStateBuilder().withTotalItems(35).build()
        "startDate is different"           | outboundSyncStateBuilder().withStartTime(END_TIME).build()
        "successCount is different"        | outboundSyncStateBuilder().withSuccessCount(77777).build()
        "errorCount is different"          | outboundSyncStateBuilder().withErrorCount(9999999).build()
        "postAbortCount is different"      | outboundSyncStateBuilder().withUnprocessedCount(19283746).build()
        "endDate is different"             | outboundSyncStateBuilder().withEndTime(START_TIME).build()
        "abort is different"               | outboundSyncStateBuilder().withAborted(true).build()
        "systemicError is different"       | outboundSyncStateBuilder().withSystemicError(true).build()
    }

    @Test
    def "two outbound sync states have the same hashCode"() {
        given:
        def state1 = outboundSyncState()
        def state2 = outboundSyncState()

        expect:
        state1.hashCode() == state2.hashCode()
    }

    @Test
    def 'getStartTime() does not leak immutability'() {
        given:
        def original = outboundSyncStateBuilder().withStartTime(START_TIME).build()

        when:
        original.startTime.setTime(START_TIME.time - 1000)

        then:
        original.startTime == START_TIME
    }

    @Test
    def 'getEndTime() does not leak immutability'() {
        given:
        def original = outboundSyncStateBuilder().withEndTime(END_TIME).build()

        when:
        original.endTime.setTime(END_TIME.time - 1000)

        then:
        original.endTime == END_TIME
    }

    @Test
    def 'initial state can be changed through the builder'() {
        given:
        def initState = initialOutboundSyncState().build()

        when:
        def changedState = OutboundSyncState.Builder.from(initState)
                .withTotalItems(TOTAL_ITEMS)
                .withSuccessCount(1)
                .withErrorCount(1)
                .withUnprocessedCount(1)
                .withEndTime(new Date())
                .withAborted(true)
                .withSystemicError(true)
                .build()

        then:
        !changedState.is(initState)
        with(changedState) {
            changedState.totalItemsRequested != initState.totalItemsRequested
            changedState.successCount != initState.successCount
            changedState.errorCount != initState.errorCount
            changedState.unprocessedCount != initState.unprocessedCount
            changedState.endTime != initState.endTime
            changedState.aborted != initState.aborted
            changedState.systemicError != initState.systemicError
        }
    }

    @Test
    def 'totalItemsCount cannot be changed once set'() {
        given:
        def stateWithTotalCounts = initialOutboundSyncState().withTotalItems(TOTAL_ITEMS).build()

        when:
        def updatedState = OutboundSyncState.Builder.from(stateWithTotalCounts).withTotalItems(TOTAL_ITEMS + 1).build()

        then:
        updatedState.totalItemsRequested == OptionalInt.of(TOTAL_ITEMS)
    }

    @Test
    def 'cannot create a state from a null state'() {
        when:
        OutboundSyncState.Builder.from(null)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "can be presented as #result"() {
        expect:
        with(state.asPerformResult()) {
            getResult() == result.result
            getStatus() == result.status
        }

        where:
        state                                                      | result
        initialOutboundSyncState().build()                         | new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.RUNNING)
        initialOutboundSyncState().withSuccessCount(1).build()     | new PerformResult(CronJobResult.SUCCESS, CronJobStatus.RUNNING)
        initialOutboundSyncState().withSystemicError(true).build() | new PerformResult(CronJobResult.FAILURE, CronJobStatus.RUNNING)
    }

    @Test
    @Unroll
    def "state changed is #res when #condition"() {
        given:
        def prevState = prevStateBuilder.build()
        def newState = newStateBuilder.build()

        expect:
        newState.isChangedFrom(prevState) == res

        where:
        condition                       | res   | prevStateBuilder                               | newStateBuilder
        'total items is known'          | true  | initialOutboundSyncState()                     | initialOutboundSyncState().withTotalItems(1)
        'result changes to SUCCESS'     | true  | initialOutboundSyncState()                     | initialOutboundSyncState().withSuccessCount(1)
        'result changes to ERROR'       | true  | initialOutboundSyncState().withSuccessCount(1) | initialOutboundSyncState().withSuccessCount(1).withErrorCount(1)
        'job is finished'               | true  | initialOutboundSyncState().withTotalItems(1)   | initialOutboundSyncState().withUnprocessedCount(1)
        'error count incremented'       | false | initialOutboundSyncState().withErrorCount(1)   | initialOutboundSyncState().withErrorCount(2)
        'success count incremented'     | false | initialOutboundSyncState().withErrorCount(1)   | initialOutboundSyncState().withErrorCount(1).withSuccessCount(1)
        'unprocessed count incremented' | false | initialOutboundSyncState()                     | initialOutboundSyncState().withUnprocessedCount(1)
        'start time changed'            | false | initialOutboundSyncState()                     | initialOutboundSyncState().withStartTime(new Date())
        'end time changed'              | false | initialOutboundSyncState()                     | initialOutboundSyncState().withEndTime(new Date())
        'aborted changed'               | false | initialOutboundSyncState()                     | initialOutboundSyncState().withAborted(true)
        'systemicError changed'         | true  | initialOutboundSyncState()                     | initialOutboundSyncState().withSystemicError(true)
    }

    def outboundSyncState() {
        outboundSyncStateBuilder().build()
    }

    private static OutboundSyncState.Builder outboundSyncStateBuilder() {
        initialOutboundSyncState()
                .withSuccessCount(SUCCESS_ITEMS)
                .withErrorCount(ERROR_ITEMS)
                .withUnprocessedCount(UNPROCESSED_ITEMS)
                .withStartTime(START_TIME)
                .withEndTime(END_TIME)
    }
}
