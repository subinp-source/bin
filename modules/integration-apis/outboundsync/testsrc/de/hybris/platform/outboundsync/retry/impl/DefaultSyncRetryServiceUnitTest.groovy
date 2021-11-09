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
package de.hybris.platform.outboundsync.retry.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.outboundsync.config.impl.OutboundSyncConfiguration
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel
import de.hybris.platform.outboundsync.retry.RetrySearchService
import de.hybris.platform.outboundsync.retry.RetryUpdateException
import de.hybris.platform.outboundsync.retry.SyncRetryNotFoundException
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException
import de.hybris.platform.servicelayer.exceptions.ModelSavingException
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultSyncRetryServiceUnitTest extends Specification {

    private static final int MAX_RETRIES = 3

    def retryService = new DefaultSyncRetryService()

    def modelService = Mock(ModelService)
    def retrySearchService = Stub(RetrySearchService)

    def setup() {
        retryService.setModelService(modelService)
        retryService.setRetrySearchService(retrySearchService)
        retryService.outboundSyncConfiguration = Stub(OutboundSyncConfiguration) {
            getMaxOutboundSyncRetries() >> MAX_RETRIES
        }
    }

    @Test
    @Unroll
    def "returns #maxReached to indicate #attemptDesc attempt when retry exists with #origAttemptCnt attempts and MAX_RETRIES=3"() {
        given: "retry for the DTO exists with #attempts attempts"
        def dto = Stub OutboundItemDTOGroup
        def retry = new OutboundSyncRetryModel(syncAttempts: origAttemptCnt)
        retrySearchService.findRetry(dto) >> retry

        when:
        def isAttemptsExceeded = retryService.handleSyncFailure dto

        then: 'no retries removed'
        0 * modelService.remove(_)
        and: 'the retried is saved/updated'
        1 * modelService.save(retry)
        and: 'the saved retry counts are incremented'
        retry.syncAttempts == incrementedAttemptCnt
        retry.remainingSyncAttempts == remainingAttempts
        retry.reachedMaxRetries == maxReached
        and: "the method result indicate #attemptDesc attempt"
        isAttemptsExceeded == maxReached

        where:
        attemptDesc | origAttemptCnt  | incrementedAttemptCnt | remainingAttempts | maxReached
        'not last'  | 1               | 2                     | 2                 | false
        'not last'  | MAX_RETRIES - 1 | MAX_RETRIES           | 1                 | false
        'last'      | MAX_RETRIES     | MAX_RETRIES + 1       | 0                 | true
        'last'      | MAX_RETRIES + 1 | MAX_RETRIES + 2       | 0                 | true
    }

    @Test
    @Unroll
    def "returns #maxReached to indicate #attemptDesc attempt when MAX_RETRIES=#maxRetries and retry does not exist"() {
        given: 'retry does not exist'
        def channelCfg = Stub OutboundChannelConfigurationModel
        def dto = Stub(OutboundItemDTOGroup) {
            getChannelConfiguration() >> channelCfg
        }
        retrySearchService.findRetry(dto) >> { throw new SyncRetryNotFoundException(123, "testChannel") }
        and: 'new retry can be created'
        def retry = new OutboundSyncRetryModel()
        modelService.create(OutboundSyncRetryModel) >> retry
        and: "max retries configured in the system is #maxRetries"
        retryService.outboundSyncConfiguration = Stub(OutboundSyncConfiguration) {
            getMaxOutboundSyncRetries() >> maxRetries
        }

        when:
        def isAttemptsExceeded = retryService.handleSyncFailure dto

        then: 'no retries deleted'
        0 * modelService.remove(_)
        and: "the retry is saved #saveCnt times"
        saveCallCnt * modelService.save(retry) >> { args ->
            with(args[0] as OutboundSyncRetryModel) {
                assert getItemPk() == dto.rootItemPk
                assert getChannel().is(channelCfg)
                assert getSyncAttempts() == 1
                assert getRemainingSyncAttempts() == remainingAttempts
                assert getReachedMaxRetries() == maxReached
            }
        }
        isAttemptsExceeded == maxReached

        where:
        attemptDesc | maxRetries | remainingAttempts | maxReached | saveCallCnt
        'not last'  | 2          | 2                 | false      | 1
        'not last'  | 1          | 1                 | false      | 1
        'last'      | 0          | 0                 | true       | 0
        'last'      | -1         | 0                 | true       | 0
    }

    @Test
    def "retry is not created or removed when handling successful original synchronization"() {
        given: 'the item is successfully synchronized from the first attempt, so no retries exist'
        def dto = Stub OutboundItemDTOGroup
        retrySearchService.findRetry(dto) >> { throw new SyncRetryNotFoundException(123, "testChannel") }

        when:
        retryService.handleSyncSuccess dto

        then: 'no retries deleted'
        0 * modelService.remove(_)
        and: 'no retries created'
        0 * modelService.create(OutboundSyncRetryModel)
    }

    @Test
    def "retry is deleted when handling success and retry is found"() {
        given:
        def dto = Stub OutboundItemDTOGroup
        def retry = new OutboundSyncRetryModel()
        retrySearchService.findRetry(dto) >> retry

        when:
        retryService.handleSyncSuccess dto

        then:
        1 * modelService.remove(retry)
    }

    @Test
    def "exception is thrown when deleting retry that does not exist anymore"() {
        given:
        def dto = Stub OutboundItemDTOGroup
        def retry = new OutboundSyncRetryModel(channel: Stub(OutboundChannelConfigurationModel))
        retrySearchService.findRetry(dto) >> retry
        modelService.remove(retry) >> { throw new ModelRemovalException("test error", new Throwable()) }

        when:
        retryService.handleSyncSuccess dto

        then:
        def e = thrown(RetryUpdateException)
        e.retry.is retry
    }

    @Test
    def "exception is thrown when updating retry that does not exist anymore"() {
        given:
        def dto = Stub OutboundItemDTOGroup
        def retry = new OutboundSyncRetryModel(syncAttempts: 1, channel: Stub(OutboundChannelConfigurationModel))
        retrySearchService.findRetry(dto) >> retry
        modelService.save(retry) >> { throw new ModelSavingException("test error", new Throwable()) }

        when:
        retryService.handleSyncFailure dto

        then:
        def e = thrown(RetryUpdateException)
        e.retry.is retry
    }
}
