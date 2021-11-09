/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.AbortedOutboundSyncEvent
import de.hybris.platform.outboundsync.events.CompletedOutboundSyncEvent
import de.hybris.platform.outboundsync.events.IgnoredOutboundSyncEvent
import de.hybris.platform.outboundsync.events.OutboundSyncEvent
import de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent
import de.hybris.platform.outboundsync.events.SystemErrorOutboundSyncEvent
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class OutboundSyncJobStateAggregatorUnitTest extends Specification {
    private static final def JOB_PK = PK.fromLong 8855
    private static final def CONTEXT_JOB = new OutboundSyncCronJobModel()
    private static final def STARTED_EVENT = startedEvent(1)
    private static final def COMPLETED_EVENT = completedSuccessfullyEvent(1)
    private static final def ABORTED_EVENT = abortedEvent(1)
    private static final def IGNORED_EVENT = ignoredEvent()
    private static final def SYSTEM_ERROR_EVENT = systemErrorEvent(1)

    def outboundSyncJob = OutboundSyncJobStateAggregator.create(CONTEXT_JOB)

    @Test
    def "cannot be created with null parameter"() {
        when:
        OutboundSyncJobStateAggregator.create(null)

        then:
        def e = thrown IllegalArgumentException
        e.message.contains OutboundSyncCronJobModel.class.getSimpleName()
    }

    @Test
    def 'initial status is RUNNING and result is UNKNOWN when the aggregator is created'() {
        expect:
        with(outboundSyncJob.currentState) {
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == CronJobResult.UNKNOWN
            !startTime
            !endTime
        }
    }

    @Test
    @Unroll
    def "non-final #event.class.simpleName does not change the job status"() {
        when:
        outboundSyncJob.applyEvent event

        then:
        with(outboundSyncJob.currentState) {
            cronJobStatus == CronJobStatus.RUNNING
            cronJobResult == result
            startTime == time
            !endTime
        }

        where:
        event              | result                | time
        STARTED_EVENT      | CronJobResult.UNKNOWN | STARTED_EVENT.startTime
        COMPLETED_EVENT    | CronJobResult.SUCCESS | null
        ABORTED_EVENT      | CronJobResult.UNKNOWN | null
        IGNORED_EVENT      | CronJobResult.SUCCESS | null
        SYSTEM_ERROR_EVENT | CronJobResult.FAILURE | null
    }

    @Test
    @Unroll
    def "final #event.class.simpleName changes the job status and result"() {
        given: 'state is one event away from comletion'
        outboundSyncJob.applyEvent initEvent

        when:
        outboundSyncJob.applyEvent event

        then:
        with(outboundSyncJob.currentState) {
            cronJobStatus == status
            cronJobResult == result
        }

        where:
        initEvent       | event              | status                 | result
        COMPLETED_EVENT | STARTED_EVENT      | CronJobStatus.FINISHED | CronJobResult.SUCCESS
        STARTED_EVENT   | COMPLETED_EVENT    | CronJobStatus.FINISHED | CronJobResult.SUCCESS
        STARTED_EVENT   | ABORTED_EVENT      | CronJobStatus.ABORTED  | CronJobResult.UNKNOWN
        STARTED_EVENT   | IGNORED_EVENT      | CronJobStatus.FINISHED | CronJobResult.SUCCESS
        STARTED_EVENT   | SYSTEM_ERROR_EVENT | CronJobStatus.FINISHED | CronJobResult.FAILURE
    }

    @Test
    def "job state does not change when the event does not have an applicable handler"() {
        given:
        def origState = outboundSyncJob.currentState

        when:
        outboundSyncJob.applyEvent new UnknownOutboundSyncEvent(JOB_PK)

        then:
        outboundSyncJob.currentState.is origState
    }

    @Test
    def 'aggregates multiple events til final state'() {
        when:
        outboundSyncJob.applyEvent ignoredEvent()
        then:
        outboundSyncJob.currentState.cronJobStatus == CronJobStatus.RUNNING

        when:
        outboundSyncJob.applyEvent completedSuccessfullyEvent(3)
        then:
        outboundSyncJob.currentState.cronJobStatus == CronJobStatus.RUNNING

        when:
        outboundSyncJob.applyEvent startedEvent(7)
        then:
        outboundSyncJob.currentState.cronJobStatus == CronJobStatus.RUNNING

        when:
        outboundSyncJob.applyEvent completedWithErrorEvent(2)
        then:
        outboundSyncJob.currentState.cronJobStatus == CronJobStatus.RUNNING

        when:
        outboundSyncJob.applyEvent abortedEvent(1)
        then:
        outboundSyncJob.currentState.cronJobStatus == CronJobStatus.ABORTED
    }
    
    @Test
    @Unroll
    def "notifies state observers when #condition"() {
        given: 'job state is initialized with an event'
        outboundSyncJob.applyEvent initEvent
        def prevState = outboundSyncJob.currentState
        and: 'observers added'
        def observer1 = Mock OutboundSyncStateObserver
        outboundSyncJob.addStateObserver observer1
        def observer2 = Mock OutboundSyncStateObserver
        outboundSyncJob.addStateObserver observer2

        when: 'an event changing state is applied'
        outboundSyncJob.applyEvent stateChangingEvent

        then: 'all observers are notified'
        1 * observer1.stateChanged(CONTEXT_JOB, { it != prevState })
        1 * observer2.stateChanged(CONTEXT_JOB, { it != prevState })

        where:
        condition                        | initEvent                     | stateChangingEvent
        'Started event received'         | ABORTED_EVENT                 | startedEvent(100)
        'job result changes to success'  | startedEvent(10)              | completedSuccessfullyEvent(1)
        'job result changes to error'    | completedSuccessfullyEvent(1) | completedWithErrorEvent(1)
        'job status changes to finished' | startedEvent(1)               | ignoredEvent()
    }

    @Test
    @Unroll
    def "does not notify state observers when #condition"() {
        given: 'job state is initialized with an event'
        outboundSyncJob.applyEvent initEvent
        and: 'an observer added'
        def observer = Mock OutboundSyncStateObserver
        outboundSyncJob.addStateObserver observer

        when: 'an event changing state is applied'
        outboundSyncJob.applyEvent stateChangingEvent

        then: 'observers not notified'
        0 * observer._

        where:
        condition                   | initEvent                     | stateChangingEvent
        'success count incremented' | completedSuccessfullyEvent(2) | completedSuccessfullyEvent(1)
        'error count incremented'   | completedWithErrorEvent(1)    | completedWithErrorEvent(2)
        'aborted event received'    | startedEvent(3)               | abortedEvent(2)
    }

    @Test
    def 'keeps notifying observers even when the first one throws unexpected exception'() {
        given: 'job state is initialized with an event'
        outboundSyncJob.applyEvent startedEvent(1)
        def prevState = outboundSyncJob.currentState
        and: 'observers added'
        def observer1 = Stub(OutboundSyncStateObserver) {
            stateChanged(CONTEXT_JOB, _) >> { throw new RuntimeException() }
        }
        outboundSyncJob.addStateObserver observer1
        def observer2 = Mock OutboundSyncStateObserver
        outboundSyncJob.addStateObserver observer2

        when: 'an event changing state is applied'
        outboundSyncJob.applyEvent ignoredEvent()

        then: 'second observer is still notified'
        1 * observer2.stateChanged(CONTEXT_JOB, { it != prevState })
    }

    static StartedOutboundSyncEvent startedEvent(int count) {
        new StartedOutboundSyncEvent(JOB_PK, new Date(), count)
    }

    static CompletedOutboundSyncEvent completedSuccessfullyEvent(int count) {
        new CompletedOutboundSyncEvent(JOB_PK, true, count)
    }

    static CompletedOutboundSyncEvent completedWithErrorEvent(int count) {
        new CompletedOutboundSyncEvent(JOB_PK, false, count)
    }

    static IgnoredOutboundSyncEvent ignoredEvent() {
        new IgnoredOutboundSyncEvent(JOB_PK)
    }

    static AbortedOutboundSyncEvent abortedEvent(int count) {
        new AbortedOutboundSyncEvent(JOB_PK, count)
    }

    static SystemErrorOutboundSyncEvent systemErrorEvent(int count) {
        new SystemErrorOutboundSyncEvent(JOB_PK, count)
    }

    private static class UnknownOutboundSyncEvent extends OutboundSyncEvent {
        UnknownOutboundSyncEvent(final PK cronJobPk) {
            super(cronJobPk)
        }
    }
}