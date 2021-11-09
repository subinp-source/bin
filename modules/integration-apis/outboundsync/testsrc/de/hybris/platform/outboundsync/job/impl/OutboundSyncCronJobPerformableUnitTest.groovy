/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.ChangeDetectionService
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.outboundsync.events.StartedOutboundSyncEvent
import de.hybris.platform.outboundsync.job.ChangesCollectorFactory
import de.hybris.platform.outboundsync.job.CountingChangesCollector
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel
import de.hybris.platform.servicelayer.event.EventService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class OutboundSyncCronJobPerformableUnitTest extends Specification {

    def static CRON_JOB_PK = PK.fromLong(123L)
    def static JOB_STATE = initialOutboundSyncState().build()

    def changeDetectionService = Stub ChangeDetectionService
    def changesCollectorFactory = Stub ChangesCollectorFactory
    def eventService = Mock(EventService)
    def jobRegister = Stub OutboundSyncJobRegister

    def cronJobPerformable = new OutboundSyncCronJobPerformable(
            changeDetectionService: changeDetectionService,
            changesCollectorFactory: changesCollectorFactory,
            eventService: eventService,
            jobRegister: jobRegister)

    @Test
    @Unroll
    def "perform results in error when #field is null"() {
        when:
        def result = cronJobPerformable.perform cronJob

        then:
        0 * eventService._
        and:
        CronJobResult.FAILURE == result.getResult()
        CronJobStatus.FINISHED == result.getStatus()

        where:
        field                            | cronJob
        'job model'                      | cronJobWith(null)
        'stream configuration container' | cronJobWith(job(null))
    }

    @Test
    def "perform results in error when an exception is thrown while processing the changes"() {
        given:
        changeDetectionService.collectChangesForType(_, _, _) >> { throw new RuntimeException() }

        when:
        def result = cronJobPerformable.perform defaultCronJob([productStream: "Product"])

        then:
        0 * eventService._
        and:
        CronJobResult.FAILURE == result.getResult()
        CronJobStatus.FINISHED == result.getStatus()
    }

    @Test
    def "perform results in error when configurations are null"() {
        given:
        def job = defaultCronJob(null as List)

        when:
        def result = cronJobPerformable.perform job

        then:
        0 * eventService._
        and:
        CronJobResult.FAILURE == result.getResult()
        CronJobStatus.FINISHED == result.getStatus()
    }

    @Test
    def "perform results in error when exception is thrown while change collector is created"() {
        given:
        def job = defaultCronJob([productStream: "Product"])
        and:
        changesCollectorFactory.createCountingCollector(job, _ as OutboundSyncStreamConfigurationModel) >> {
            throw new RuntimeException()
        }

        when:
        def result = cronJobPerformable.perform job

        then:
        0 * eventService._
        and:
        CronJobResult.FAILURE == result.getResult()
        CronJobStatus.FINISHED == result.getStatus()
    }

    @Test
    def "perform reports job state when there are no change streams associated with the job"() {
        given: 'the job has no streams associated with it'
        def job = defaultCronJob([])
        and: 'current job state'
        jobRegister.getNewJob(job) >> Stub(OutboundSyncJob) {
            getCurrentState() >> JOB_STATE
        }

        when:
        def result = cronJobPerformable.perform job

        then:
        1 * eventService.publishEvent(_) >> { args ->
            def event = args[0] as StartedOutboundSyncEvent
            assert event.cronJobPk == CRON_JOB_PK
            assert event.itemCount == 0
            assert event.startTime
        }
        and:
        result.status == JOB_STATE.cronJobStatus
        result.result == JOB_STATE.cronJobResult
    }

    @Test
    def "perform reports job state when no changes are present in the streams"() {
        given:
        def stream1 = streamConfiguration('products', 'Product')
        def stream2 = streamConfiguration('categories', 'Category')
        and:
        def job = defaultCronJob([stream1, stream2])
        and: 'collectors corresponding to the streams'
        def collector1 = Stub CountingChangesCollector
        def collector2 = Stub CountingChangesCollector
        changesCollectorFactory.createCountingCollector(job, stream1) >> collector1
        changesCollectorFactory.createCountingCollector(job, stream2) >> collector2
        and: 'collectors detect no changes in the streams'
        changeDetectionService.collectChangesForType({ it.code == 'Product' }, { it.streamId == 'products' }, collector1) >> { args ->
            args[2].getNumberOfChangesCollected() >> 0
        }
        changeDetectionService.collectChangesForType({ it.code == 'Category' }, { it.streamId == 'categories' }, collector2) >> { args ->
            args[2].getNumberOfChangesCollected() >> 0
        }
        and: 'current job state'
        jobRegister.getNewJob(job) >> Stub(OutboundSyncJob) {
            getCurrentState() >> JOB_STATE
        }

        when:
        def result = cronJobPerformable.perform job

        then:
        1 * eventService.publishEvent(_) >> { args ->
            def event = args[0] as StartedOutboundSyncEvent
            assert event.cronJobPk == CRON_JOB_PK
            assert event.itemCount == 0
            assert event.startTime
        }
        and:
        result.status == JOB_STATE.cronJobStatus
        result.result == JOB_STATE.cronJobResult
    }

    @Test
    @Unroll
    def "perform result is determine by the job state when one stream has #changes1 changes and second stream has #changes2 changes"() {
        given:
        def stream1 = streamConfiguration('products', 'Product')
        def stream2 = streamConfiguration('categories', 'Category')
        and:
        def jobModel = defaultCronJob([stream1, stream2])
        and: 'collectors corresponding to the streams'
        def collector1 = Stub CountingChangesCollector
        def collector2 = Stub CountingChangesCollector
        changesCollectorFactory.createCountingCollector(jobModel, stream1) >> collector1
        changesCollectorFactory.createCountingCollector(jobModel, stream2) >> collector2
        and: 'collectors detect no changes in the streams'
        changeDetectionService.collectChangesForType({ it.code == 'Product' }, { it.streamId == 'products' }, collector1) >> { args ->
            args[2].getNumberOfChangesCollected() >> changes1
        }
        changeDetectionService.collectChangesForType({ it.code == 'Category' }, { it.streamId == 'categories' }, collector2) >> { args ->
            args[2].getNumberOfChangesCollected() >> changes2
        }
        and: 'the job is running'
        jobRegister.getNewJob(jobModel) >> Stub(OutboundSyncJob) {
            getCurrentState() >> JOB_STATE
        }

        when:
        def result = cronJobPerformable.perform jobModel

        then:
        1 * eventService.publishEvent(_) >> { args ->
            def event = args[0] as StartedOutboundSyncEvent
            assert event.cronJobPk == CRON_JOB_PK
            assert event.itemCount == changes1 + changes2
            assert event.startTime
        }
        and:
        result.status == JOB_STATE.cronJobStatus
        result.result == JOB_STATE.cronJobResult

        where:
        changes1 | changes2
        0        | 1
        1        | 0
        1        | 1
    }

    @Test
    def "jobPerformable can be aborted"() {
        expect:
        cronJobPerformable.isAbortable()
    }

    def defaultCronJob(Map streamIdTypeCodeMap = [:]) {
        defaultCronJob streamConfigurations(streamIdTypeCodeMap)
    }

    def defaultCronJob(List streams) {
        def container = Stub(OutboundSyncStreamConfigurationContainerModel) {
            getConfigurations() >> streams
            getId() >> "outboundSyncDataStreams"
        }
        cronJobWith job(container)
    }

    OutboundSyncCronJobModel cronJobWith(OutboundSyncJobModel job) {
        Stub(OutboundSyncCronJobModel) {
            getJob() >> job
            getPk() >> CRON_JOB_PK
        }
    }

    OutboundSyncJobModel job(OutboundSyncStreamConfigurationContainerModel container) {
        Stub(OutboundSyncJobModel) {
            getStreamConfigurationContainer() >> container
        }
    }

    def streamConfigurations(Map streamIdTypeCodeMap) {
        streamIdTypeCodeMap.collect {
            streamId, typeCode -> streamConfiguration(streamId, typeCode)
        }
    }

    def streamConfiguration(def streamId, def typeCode) {
        Stub(OutboundSyncStreamConfigurationModel) {
            getStreamId() >> streamId
            getItemTypeForStream() >> Stub(ComposedTypeModel) {
                getCode() >> typeCode
            }
        }
    }
}
