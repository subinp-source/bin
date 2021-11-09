/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundsync.TestItemChangeDetector
import de.hybris.platform.outboundsync.TestItemChangeSender
import de.hybris.platform.outboundsync.job.ChangesCollectorFactory
import de.hybris.platform.outboundsync.job.ItemChangeSender
import de.hybris.platform.outboundsync.util.OutboundSyncTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.cronjob.CronJobService
import org.junit.Rule
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class WhereClauseFilterIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    public static final String FILTER_EXPR = "{item.id}='catalog1'"
    @Resource(name = "outboundSyncCronJobPerformable")
    OutboundSyncCronJobPerformable outboundSyncCronJobPerformable
    @Resource
    CronJobService cronJobService
    @Resource
    ItemChangeSender itemChangeSender
    @Resource(name = 'outboundSyncChangesCollectorFactory')
    ChangesCollectorFactory collectorFactory

    @Rule
    TestItemChangeSender testItemSender = new TestItemChangeSender()
    @Rule
    TestItemChangeDetector changeDetector = new TestItemChangeDetector()
    ChangesCollectorFactory testCollectorFactory = new DefaultChangesCollectorFactory(itemChangeSender: testItemSender)
    CronJobModel cronJob

    def setup() {
        outboundSyncCronJobPerformable.setChangesCollectorFactory testCollectorFactory
        importCsv '/impex/essentialdata-outboundsync.impex', 'UTF-8'
        catalogIntegrationObject()

        def channel = OutboundSyncTestUtil.outboundChannelConfigurationExists('testWhereClauseFilterChannel', 'TestOutboundCatalog')
        changeDetector.createChangeStream channel, 'Catalog', FILTER_EXPR
        cronJob = OutboundSyncTestUtil.outboundCronJob()
    }

    def cleanup() {
        outboundSyncCronJobPerformable.setChangesCollectorFactory collectorFactory
        OutboundSyncTestUtil.cleanup()
        IntegrationObjectTestUtil.cleanup()
    }

    @Test
    def "catalogs other than catalog1 are filtered out by whereClause"() {
        given:
        def catalog1 = IntegrationTestUtil.createCatalogWithId('catalog1')
        IntegrationTestUtil.createCatalogWithId('catalog2')
        IntegrationTestUtil.createCatalogWithId('catalog3')

        when:
        cronJobService.performCronJob(cronJob, true)

        then:
        testItemSender.getQueueSize() == 1
        testItemSender.getNextItem().item.PK == catalog1.pk.longValue
    }

    def catalogIntegrationObject() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject	; code[unique = true]',
                '; TestOutboundCatalog	;',
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                '; TestOutboundCatalog  ; Catalog              ; Catalog',
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                '; TestOutboundCatalog:Catalog              ; id                      ; Catalog:id                      ;',
        )
    }
}
