/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.activator.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.IntegrationAttributeProcessingException
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.service.ItemModelSearchService
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.outboundsync.activator.OutboundItemConsumer
import de.hybris.platform.outboundsync.dto.OutboundChangeType
import de.hybris.platform.outboundsync.dto.OutboundItem
import de.hybris.platform.outboundsync.dto.OutboundItemChange
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup
import de.hybris.platform.outboundsync.events.AbortedOutboundSyncEvent
import de.hybris.platform.outboundsync.events.CompletedOutboundSyncEvent
import de.hybris.platform.outboundsync.events.SystemErrorOutboundSyncEvent
import de.hybris.platform.outboundsync.job.OutboundItemFactory
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel
import de.hybris.platform.outboundsync.retry.RetryUpdateException
import de.hybris.platform.outboundsync.retry.SyncRetryService
import de.hybris.platform.servicelayer.event.EventService
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import rx.Observable
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultOutboundSyncServiceUnitTest extends Specification {
    private static final def CHANGED_ITEM = new ItemModel()
    private static final int DEFAULT_PK = 4
    private static final int ROOT_ITEM_PK = 123
    private static final PK CRON_JOB_PK = PK.fromLong(456)
    private static final String TEST_INTEGRATION_OBJECT = "TestIntegrationObject"
    private static final String TEST_DESTINATION = "TestDestination"

    def outboundServiceFacade = Mock(OutboundServiceFacade)
    def outboundItemConsumer = Mock(OutboundItemConsumer)
    def modelService = Mock(ModelService)
    def syncRetryService = Mock(SyncRetryService)
    def eventService = Mock(EventService)
    def itemModelSearchService = Mock ItemModelSearchService

    def defaultOutboundSyncService = new DefaultOutboundSyncService(
            outboundServiceFacade: outboundServiceFacade,
            outboundItemConsumer: outboundItemConsumer,
            modelService: modelService,
            syncRetryService: syncRetryService,
            eventService: eventService,
            outboundItemFactory: outboundItemFactory(),
            itemModelSearchService: itemModelSearchService
    )

    OutboundItemFactory outboundItemFactory() {
        Stub(OutboundItemFactory) {
            createItem(_ as OutboundItemDTO) >> Stub(OutboundItem) {
                getIntegrationObject() >> Stub(IntegrationObjectDescriptor) {
                    getCode() >> TEST_INTEGRATION_OBJECT
                }
                getChannelConfiguration() >> Stub(OutboundChannelConfigurationModel) {
                    getDestination() >> Stub(ConsumedDestinationModel) {
                        getId() >> TEST_DESTINATION
                    }
                }
            }
        }
    }

    @Shared
    static final SyncParameters syncParameters = SyncParameters.syncParametersBuilder().withItem(CHANGED_ITEM)
            .withIntegrationObjectCode(TEST_INTEGRATION_OBJECT)
            .withDestinationId(TEST_DESTINATION)
            .withSource(OutboundSource.OUTBOUNDSYNC)
            .build()

    @Test
    @Unroll
    def "#changeType item change is received, item is found and outbound synced successfully when httpStatus is #status"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization is successful'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM, changeType)
        outboundServiceFacade.send(syncParameters) >> stubObservable(status)

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        1 * outboundItemConsumer.consume(outboundItemDTO)
        1 * syncRetryService.handleSyncSuccess(_ as OutboundItemDTOGroup)

        where:
        changeType                  | status
        OutboundChangeType.CREATED  | HttpStatus.CREATED
        OutboundChangeType.MODIFIED | HttpStatus.CREATED
        OutboundChangeType.CREATED  | HttpStatus.OK
        OutboundChangeType.MODIFIED | HttpStatus.OK
    }

    @Test
    @Unroll
    def "#changeType item change is received, item is found and outbound facade resulted in error"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization failed'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM, changeType)
        outboundServiceFacade.send(syncParameters) >> stubObservableError()

        and: 'more synchronization attempts are possible in future'
        syncRetryService.handleSyncFailure(_ as OutboundItemDTOGroup) >> false

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        0 * outboundItemConsumer.consume(_)
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))

        where:
        changeType << [OutboundChangeType.CREATED, OutboundChangeType.MODIFIED]
    }

    @Test
    def 'change is not consumed when outbound sync facade crashes while sending the item'() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()
        and: 'an item for the change'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM)
        and: 'the item send crashes'
        outboundServiceFacade.send(syncParameters) >> { throw new RuntimeException() }

        and: 'more synchronization attempts are possible in future'
        syncRetryService.handleSyncFailure(_ as OutboundItemDTOGroup) >> false

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        0 * outboundItemConsumer.consume(_)
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))
    }

    @Test
    def "facade returns error and it is the last retry"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization failed'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM, OutboundChangeType.CREATED)
        outboundServiceFacade.send(syncParameters) >> stubObservableError()

        and: 'it was last synchronization attempt possible'
        syncRetryService.handleSyncFailure(_ as OutboundItemDTOGroup) >> true

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        1 * outboundItemConsumer.consume(outboundItemDTO)
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))
    }

    @Test
    def "changes are not consumed when RetryUpdateException is thrown on last retry"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization failed'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM)
        outboundServiceFacade.send(syncParameters) >> stubObservableError()

        and: 'an exception is thrown while handling the failure'
        syncRetryService.handleSyncFailure(_ as OutboundItemDTOGroup) >> { throw new RetryUpdateException(Stub(OutboundSyncRetryModel)) }

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        0 * outboundItemConsumer.consume(_)
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))
    }

    @Test
    def "change not consumed on success case when a RetryUpdateException occurs"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization is successful'
        def outboundItemDTO = outboundItemDto(CHANGED_ITEM)
        outboundServiceFacade.send(syncParameters) >> stubObservable(HttpStatus.CREATED)

        and: 'an exception is thrown while handling the success'
        syncRetryService.handleSyncSuccess(_ as OutboundItemDTOGroup) >> { throw new RetryUpdateException(createTestRetry()) }

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        0 * outboundItemConsumer.consume(_)
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))
    }

    @Test
    def "CREATED item change is received, item is not found"() {
        given:
        def outboundItemDTO = Stub(OutboundItemDTO) {
            getItem() >> Stub(OutboundItemChange) {
                getChangeType() >> OutboundChangeType.CREATED
            }
            getRootItemPK() >> ROOT_ITEM_PK
            getCronJobPK() >> CRON_JOB_PK
        }

        and: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and:
        itemModelSearchService.nonCachingFindByPk(PK.fromLong(ROOT_ITEM_PK)) >> Optional.empty()

        when:
        defaultOutboundSyncService.sync([outboundItemDTO])

        then:
        0 * outboundServiceFacade.send(_)
        0 * outboundItemConsumer.consume(_)
    }

    @Test
    def 'abort event is published when cronjob is aborting and item is not synchronized'() {
        given: 'aborting cronjob'
        def cronJob = Stub(CronJobModel) {
            getPk() >> CRON_JOB_PK
            getRequestAbort() >> true
            getStatus() >> CronJobStatus.RUNNING
        }

        and: 'cronjob is found'
        cronJobByPkFound cronJob

        and: 'root item is found'
        itemModelSearchService.nonCachingFindByPk(PK.fromLong(ROOT_ITEM_PK)) >> Optional.of(Stub(ItemModel))

        and: 'a changed item'
        def outboundItemDto = outboundItemDto CHANGED_ITEM

        when:
        defaultOutboundSyncService.sync([outboundItemDto])

        then: 'abort event is published'
        1 * eventService.publishEvent(new AbortedOutboundSyncEvent(CRON_JOB_PK, 1))

        and: 'item is not synchronized'
        0 * outboundServiceFacade.send(_)
    }

    @Test
    def 'abort event is not published again when cronjob is aborted already and item is not synchronized'() {
        given: 'aborting cronjob'
        def cronJob = Stub(CronJobModel) {
            getPk() >> CRON_JOB_PK
            getRequestAbort() >> null
            getStatus() >> CronJobStatus.ABORTED
        }

        and: 'cronjob is found'
        cronJobByPkFound cronJob

        and: 'root item is found'
        itemModelSearchService.nonCachingFindByPk(PK.fromLong(ROOT_ITEM_PK)) >> Optional.of(Stub(ItemModel))

        and: 'a changed item'
        def outboundItemDto = outboundItemDto CHANGED_ITEM

        when:
        defaultOutboundSyncService.sync([outboundItemDto])

        then: 'no abort event published'
        0 * eventService.publishEvent(_ as AbortedOutboundSyncEvent)

        and: 'item is not synchronized'
        0 * outboundServiceFacade.send(_)
    }

    @Test
    def "completedOutboundSyncEvent is published when synchronization is successful"() {
        given: 'cronjob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'synchronization is successful'
        def outboundItemDTO1 = outboundItemDto(CHANGED_ITEM)
        def outboundItemDTO2 = outboundItemDto(CHANGED_ITEM)

        outboundServiceFacade.send(syncParameters) >> stubObservable(HttpStatus.CREATED)

        when:
        defaultOutboundSyncService.sync([outboundItemDTO1, outboundItemDTO2])

        then:
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, true, 2))
    }

    @Test
    def 'SystemErrorOutboundSyncEvent is published when there is a systemic error'() {
        given: 'cronJob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'a changed item'
        def outboundItemDto = outboundItemDto CHANGED_ITEM

        and: 'systemic error occurs'
        outboundServiceFacade.send(syncParameters) >> { throw Stub(IntegrationAttributeException) }

        when:
        defaultOutboundSyncService.sync([outboundItemDto])

        then:
        1 * eventService.publishEvent(new SystemErrorOutboundSyncEvent(CRON_JOB_PK, 1))
    }

    @Test
    def 'no more items are synchronized when a systemic error occurs'() {
        given: 'cronJob with systemic error'
        def cronJob = notAbortedCronJob()
        cronJob.getResult() >> CronJobResult.FAILURE
        and: 'cronJob is running'
        cronJobByPkFound cronJob

        and: 'a changed item'
        def outboundItemDto = outboundItemDto CHANGED_ITEM

        when:
        defaultOutboundSyncService.sync([outboundItemDto])

        then: 'no item sent'
        0 * outboundServiceFacade.send(_ as SyncParameters)
        and: 'SystemErrorOutboundSyncEvent is fired'
        1 * eventService.publishEvent(new SystemErrorOutboundSyncEvent(CRON_JOB_PK, 1))
    }

    @Test
    def 'only the item fails when a processing exception occurs'() {
        given: 'cronJob is running'
        cronJobByPkFound notAbortedCronJob()

        and: 'a changed item'
        def outboundItemDto = outboundItemDto CHANGED_ITEM

        and: 'failed processing item'
        outboundServiceFacade.send(syncParameters) >> { throw Stub(IntegrationAttributeProcessingException) }

        when:
        defaultOutboundSyncService.sync([outboundItemDto])

        then: 'event fired to indicate the item failed'
        1 * eventService.publishEvent(new CompletedOutboundSyncEvent(CRON_JOB_PK, false, 1))
        and: 'no event fired to indicate a systemic error occurred'
        0 * eventService.publishEvent(_ as SystemErrorOutboundSyncEvent)
    }

    def stubObservable(HttpStatus httpStatus) {
        Observable.just Stub(ResponseEntity) {
            getStatusCode() >> httpStatus
        }
    }

    def stubObservableError() {
        stubObservable(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    def outboundItemDto(itemModel, changeType = OutboundChangeType.CREATED) {
        itemModelSearchService.nonCachingFindByPk(PK.fromLong(ROOT_ITEM_PK)) >> Optional.of(itemModel)
        Stub(OutboundItemDTO) {
            getItem() >> Stub(OutboundItemChange) {
                getPK() >> DEFAULT_PK
                getChangeType() >> changeType
            }
            getRootItemPK() >> ROOT_ITEM_PK
            getCronJobPK() >> CRON_JOB_PK
        }
    }

    def notAbortedCronJob() {
        def cronJob = Stub(CronJobModel) {
            getPk() >> CRON_JOB_PK
        }
        cronJob
    }

    void cronJobByPkFound(CronJobModel cronJob) {
        itemModelSearchService.nonCachingFindByPk(CRON_JOB_PK) >> Optional.of(cronJob)
    }
}

