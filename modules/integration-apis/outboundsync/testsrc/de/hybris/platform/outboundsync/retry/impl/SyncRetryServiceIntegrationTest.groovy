/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.retry.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.IntegrationAttributeProcessingException
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.util.TestOutboundFacade
import de.hybris.platform.outboundsync.TestItemChangeDetector
import de.hybris.platform.outboundsync.TestOutboundItemConsumer
import de.hybris.platform.outboundsync.activator.OutboundItemConsumer
import de.hybris.platform.outboundsync.activator.impl.DefaultOutboundSyncService
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel
import de.hybris.platform.outboundsync.retry.SyncRetryService
import de.hybris.platform.outboundsync.util.OutboundSyncTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.servicelayer.cronjob.CronJobService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Rule
import org.junit.Test

import javax.annotation.Resource
import java.time.Duration

import static de.hybris.platform.integrationservices.util.EventualCondition.eventualCondition
import static de.hybris.platform.outboundsync.util.OutboundSyncTestUtil.outboundCronJob

@IntegrationTest
class SyncRetryServiceIntegrationTest extends ServicelayerSpockSpecification {
    @Resource
    CronJobService cronJobService
    @Resource
    DefaultOutboundSyncService outboundSyncService
    @Resource
    SyncRetryService syncRetryService
    @Resource(name = 'outboundServiceFacade')
    private OutboundServiceFacade outboundServiceFacade
    @Resource(name = 'outboundItemConsumer')
    private OutboundItemConsumer outboundItemConsumer
    @Resource
    private FlexibleSearchService flexibleSearchService
    @Rule
    TestOutboundFacade testOutboundFacade = new TestOutboundFacade().respondWithNotFound()
    @Rule
    TestOutboundItemConsumer testOutboundItemConsumer = new TestOutboundItemConsumer()
    @Rule
    TestItemChangeDetector changeDetector = new TestItemChangeDetector()

    @Resource(name = "defaultConfigurationService")
    private ConfigurationService configurationService

    private static final String OBJECT_CODE = 'OutboundCatalog'
    private static final String ITEM_CODE = 'Catalog'
    private static final String CHANNEL_CODE = 'outboundSyncRetryTestChannel'
    private static final def CATALOG_ID = 'catalogA'
    private static final def RETRIES_TO_DO = 1

    def setup() {
        importCsv '/impex/essentialdata-outboundsync.impex', 'UTF-8'

        IntegrationObjectTestUtil.createIntegrationObject(OBJECT_CODE)
        def channel = OutboundSyncTestUtil.outboundChannelConfigurationExists CHANNEL_CODE, OBJECT_CODE
        changeDetector.createChangeStream channel, ITEM_CODE

        outboundSyncService.setOutboundServiceFacade(testOutboundFacade)
        outboundSyncService.setOutboundItemConsumer(testOutboundItemConsumer)
    }

    def cleanup() {
        outboundSyncService.setOutboundServiceFacade(outboundServiceFacade)
        outboundSyncService.setOutboundItemConsumer(outboundItemConsumer)
        IntegrationTestUtil.removeAll OutboundSyncRetryModel
        IntegrationObjectTestUtil.cleanup()
        OutboundSyncTestUtil.cleanup()
        IntegrationTestUtil.removeSafely(CatalogModel) { it.id == CATALOG_ID }
    }

    @Test
    def "changes are consumed when retry exceeds max retries"() {
        given:
        setMaxRetries(RETRIES_TO_DO)
        and: 'a change is present'
        def catalog = IntegrationTestUtil.createCatalogWithId(CATALOG_ID)

        when: "the job is executed first time"
        cronJobService.performCronJob outboundCronJob(), true

        then: "the failed change publication is not consumed"
        eventualCondition().expect {
            assert testOutboundItemConsumer.invocations() == 0
            and: 'retry is created'
            def retry = retryFor catalog
            with(retry) {
                assert syncAttempts == 1
                assert remainingSyncAttempts == RETRIES_TO_DO
            }
        }

        when: "the job is executed second time and the retry max has been reached"
        cronJobService.performCronJob outboundCronJob(), true

        then: "the failed change is consumed"
        eventualCondition().expect {
            assert testOutboundItemConsumer.invocations() == 1
            and: 'retry has been updated for no remaining attempts'
            def retry = retryFor(catalog)
            assert with(retry) {
                syncAttempts == 2
                remainingSyncAttempts == 0
            }
        }
    }

    @Test
    def "changes are consumed when max retries set to 0 and sync fails"() {
        given: 'retries disabled'
        setMaxRetries(0)
        and: 'a change is present'
        def catalog = IntegrationTestUtil.createCatalogWithId(CATALOG_ID)

        when:
        cronJobService.performCronJob outboundCronJob(), true

        then: 'the change is consumed'
        eventualCondition().expect {
            assert testOutboundItemConsumer.invocations() == 1
            and: 'no retry is created'
            assert !retryFor(catalog)
        }
    }

    @Test
    def 'retry is created when IntegrationAttributeProcessingException is thrown for the item'() {
        given: 'retries are enabled'
        setMaxRetries(RETRIES_TO_DO)
        and: 'a change is present'
        def catalog = IntegrationTestUtil.createCatalogWithId CATALOG_ID
        and: 'exception is thrown for the catalog when it is sent'
        def exception = Stub IntegrationAttributeProcessingException
        outboundSyncService.outboundServiceFacade = new TestOutboundFacade().throwException(exception)

        when:
        cronJobService.performCronJob outboundCronJob(), true

        then: 'the retry is created'
        eventualCondition().expect {
            def retry = retryFor catalog
            assert with(retry) {
                syncAttempts == 1
                remainingSyncAttempts == RETRIES_TO_DO
            }
            and: 'the change is not consumed'
            assert testOutboundItemConsumer.invocations() == 0
        }
    }

    @Test
    def 'retries not created when IntegrationAttributeException is thrown for the item'() {
        given: 'retries are enabled'
        setMaxRetries(RETRIES_TO_DO)
        and: 'a change is present'
        def catalog = IntegrationTestUtil.createCatalogWithId CATALOG_ID
        and: 'systemic exception is thrown for the catalog when it is sent'
        def exception = Stub IntegrationAttributeException
        outboundSyncService.outboundServiceFacade = new TestOutboundFacade().throwException(exception)

        when:
        cronJobService.performCronJob outboundCronJob(), true

        then: 'the retry is not created within 6 seconds'
        eventualCondition().within(Duration.ofSeconds(6)).retains {
            assert !retryFor(catalog)
            and: 'the change is not consumed'
            assert testOutboundItemConsumer.invocations() == 0
        }
    }

    def setMaxRetries(int maxRetries) {
        configurationService.getConfiguration().setProperty 'outboundsync.max.retries', String.valueOf(maxRetries)
    }

    private OutboundSyncRetryModel retryFor(CatalogModel catalog) {
        def sample = new OutboundSyncRetryModel(itemPk: catalog.pk.longValue)
        def matches = flexibleSearchService.getModelsByExample sample
        matches.empty ? null : matches[0]
    }
}
