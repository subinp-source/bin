/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.directpersistence.exception.ModelPersistenceException
import de.hybris.platform.outboundsync.config.impl.OutboundSyncConfiguration
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.outboundsync.job.impl.OutboundSyncState.Builder.initialOutboundSyncState

@UnitTest
class DefaultOutboundSyncJobRegisterUnitTest extends Specification {
    private static final def EXISTING_JOB_PK = PK.fromLong 200
    private static final def NOT_EXISTING_JOB_PK = PK.fromLong 404
    private static final def NOT_OUTBOUNDSYNC_JOB_PK = PK.fromLong 500

    def modelService = Mock(ModelService) {
        get(EXISTING_JOB_PK) >> outboundSyncJob(EXISTING_JOB_PK)
        get(NOT_EXISTING_JOB_PK) >> { throw new ModelLoadingException('') }
        get(NOT_OUTBOUNDSYNC_JOB_PK) >> new CronJobModel()
    }
    def flexibleSearchService = Stub FlexibleSearchService
    def outboundSyncConfiguration = Stub(OutboundSyncConfiguration) {
        getOutboundSyncCronjobModelSearchSleep() >> 500
    }

    def jobRegister = new DefaultOutboundSyncJobRegister(modelService)

    def setup() {
        jobRegister.setFlexibleSearchService(flexibleSearchService)
        jobRegister.setOutboundSyncConfiguration(outboundSyncConfiguration)
    }

    @Test
    @Unroll
    def "does not create new job when its model primary key is not for an #condition"() {
        expect:
        jobRegister.getJob(pk).empty

        where:
        condition                 | pk
        'existing item model'     | NOT_EXISTING_JOB_PK
        'outbound sync job model' | NOT_OUTBOUNDSYNC_JOB_PK
    }

    @Test
    def 'creates new job when it does not exist'() {
        expect:
        jobRegister.getJob(EXISTING_JOB_PK).present
    }

    @Test
    def 'adds itself as observer to the created job instance'() {
        given:
        def job = jobRegister.getJob outboundSyncJob(EXISTING_JOB_PK)

        expect:
        job.stateObservers.contains jobRegister
    }

    @Test
    def 'retrieves existing job instance by its primary key when it has been already requested'() {
        given:
        def job = jobRegister.getJob(EXISTING_JOB_PK).get()

        expect:
        jobRegister.getJob(EXISTING_JOB_PK).get().is job
    }

    @Test
    def 'retrieves existing job instance by its model when it has been already requested'() {
        given:
        def jobModel = outboundSyncJob(EXISTING_JOB_PK)
        def jobState = jobRegister.getJob(jobModel)

        expect:
        jobRegister.getJob(jobModel).is jobState
    }

    @Test
    def 'retrieves different job instances when the job models are different'() {
        given: 'a job is registered'
        def existingJob = jobRegister.getJob(EXISTING_JOB_PK).get()
        and: 'another another job exists with a different key'
        def differentPk = PK.fromLong 201
        modelService.get(differentPk) >> outboundSyncJob(differentPk)

        expect: 'different job for the different model'
        jobRegister.getJob(differentPk).get() != existingJob
    }

    @Test
    def 'always retrieves new fresh instance of the job when getNewJob() is called'() {
        given:
        def jobModel = outboundSyncJob(EXISTING_JOB_PK)
        def origJob = jobRegister.getNewJob(jobModel)

        expect:
        !jobRegister.getNewJob(jobModel).is(origJob)
    }

    @Test
    def 'persists and unregisters the job when the job finishes'() {
        given: 'job is registered'
        def jobModel = outboundSyncJob(EXISTING_JOB_PK)
        def origJob = jobRegister.getJob(jobModel)
        and: 'the job is found by flex search service'
        def searchJobModel = outboundSyncJob(EXISTING_JOB_PK)
        flexibleSearchService.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> [searchJobModel]
        }
        and: 'new job is finished'
        def changedState = initialOutboundSyncState()
                .withStartTime(new Date())
                .withTotalItems(1)
                .withSuccessCount(1)
                .build()

        when:
        jobRegister.stateChanged(jobModel, changedState)

        then:
        1 * searchJobModel.setStatus(changedState.cronJobStatus)
        1 * searchJobModel.setResult(changedState.cronJobResult)
        1 * searchJobModel.setStartTime(changedState.startTime)
        1 * searchJobModel.setEndTime(changedState.endTime)
        1 * searchJobModel.setRequestAbort(null)
        then:
        1 * modelService.save(searchJobModel)
        and: 'the original job was released'
        jobRegister.getJob(jobModel) != origJob
    }

    @Test
    def 'persists but keeps the job registered when the job is not finished'() {
        given: 'job is registered'
        def jobModel = outboundSyncJob(EXISTING_JOB_PK)
        def origJob = jobRegister.getJob(jobModel)
        and: 'the job is found by flex search service'
        def searchJobModel = outboundSyncJob(EXISTING_JOB_PK)
        flexibleSearchService.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> [searchJobModel]
        }
        and: 'the job is running'
        def changedState = initialOutboundSyncState()
                .withStartTime(new Date())
                .withTotalItems(1)
                .build()

        when:
        jobRegister.stateChanged(jobModel, changedState)

        then:
        1 * searchJobModel.setStatus(changedState.cronJobStatus)
        1 * searchJobModel.setResult(changedState.cronJobResult)
        1 * searchJobModel.setStartTime(changedState.startTime)
        0 * searchJobModel.setEndTime(changedState.endTime)
        0 * searchJobModel.setRequestAbort(null)
        then:
        1 * modelService.save(searchJobModel)
        and: 'the original job is still kept'
        jobRegister.getJob(jobModel).is origJob
    }

    @Test
    def 'unregisters a finished job state even when it failed to persist'() {
        given: 'a job is registered'
        def jobModel = outboundSyncJob(EXISTING_JOB_PK)
        def origJob = jobRegister.getJob(jobModel)
        and: 'the job is finished'
        def changedState = initialOutboundSyncState()
                .withStartTime(new Date())
                .withTotalItems(1)
                .withSuccessCount(1)
                .build()
        and: 'the job fails to persist'
        flexibleSearchService.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> []
        }

        when:
        jobRegister.stateChanged(jobModel, changedState)

        then:
        thrown ModelPersistenceException
        and: 'the original job was released'
        jobRegister.getJob(jobModel) != origJob
    }

    OutboundSyncCronJobModel outboundSyncJob(PK key) {
        Mock(OutboundSyncCronJobModel) {
            getPk() >> key
        }
    }
}