/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.retention

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel
import de.hybris.platform.outboundsync.util.OutboundSyncTestUtil
import de.hybris.platform.processing.model.AfterRetentionCleanupRuleModel
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.cronjob.CronJobService
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.*
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.*
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.*
import static de.hybris.platform.outboundsync.util.OutboundSyncTestUtil.*
import static de.hybris.platform.outboundsync.util.OutboundSyncTestUtil.*

@IntegrationTest
class OutboundRetryRetentionCleanupIntegrationTest extends ServicelayerTransactionalSpockSpecification
{
	static final def SEVEN_DAYS_SECONDS = 604800
	
	@Resource
	CronJobService cronJobService
	CatalogModel catalog
	OutboundChannelConfigurationModel channel

	def setup() {
		importCsv "/impex/essentialdata-outboundsync-retry-cleanup-jobs.impex", "UTF-8"
		importCsv "/impex/essentialdata-outboundsync.impex", "UTF-8"
		IntegrationObjectTestUtil.createIntegrationObject('TestOutboundItem')
		outboundChannelConfigurationExists('retryRetentionTest', 'TestOutboundItem')
		catalog = createCatalogWithId("testCatalog")
		channel = getModelByExample(channel("retryRetentionTest"))
	}

	def cleanup() {
		OutboundSyncTestUtil.cleanup()
		IntegrationObjectTestUtil.cleanup()
	}

	@Test
	def "outbound retry cleanup rule cleans records older than the retention time with max retries reached = true"() {
		given: "change retention time to 1 second"
		changeRetentionTime(1)
		and:
		def catalogVersion = importCatalogVersion("Staged", "testCatalog", true)
		and: "insert a retry for catalog to be cleaned up, and a catalogVersion not ready to be cleaned up"
		importImpEx(
			"INSERT_UPDATE OutboundSyncRetry; itemPk[unique=true]; channel[unique=true]; remainingSyncAttempts",
			"                               ; $catalog.pk.longValue;        $channel.pk; 0",
			"                               ; $catalogVersion.pk.longValue; $channel.pk; 1")

		expect: "verify the retry record is created"
		def retryCatalogExample = retry(catalog, channel)
		def retryCatalogVersionExample = retry(catalogVersion, channel)
		assertModelExists(retryCatalogExample)
		assertModelExists(retryCatalogVersionExample)

		when: "wait so the retry record passes the retention time before calling the clean up cronjob"
		Thread.sleep(2000)
		retryCleanupCronJobIsRun()

		then: "retry record for catalog is deleted, but catalogVersion is not deleted"
		assertModelDoesNotExist(retryCatalogExample)
		assertModelExists(retryCatalogVersionExample)
	}

	@Test
	@Unroll
	def "outbound retry cleanup rule does not remove retry records when #condition"()
	{
		given:
		changeRetentionTime(retentionTimeInSeconds)
		and:
		importImpEx(
				"INSERT_UPDATE OutboundSyncRetry; itemPk[unique=true]; channel[unique=true]; remainingSyncAttempts",
				"; $catalog.pk.longValue; $channel.pk; $remainingAttempts")

		expect: "verify the retry record is created"
		def retryExample = retry(catalog, channel)
		assertModelExists(retryExample)

		when:
		Thread.sleep(sleepTime)
		retryCleanupCronJobIsRun()

		then:
		assertModelExists(retryExample)

		where:
		condition 																| retentionTimeInSeconds	| sleepTime | remainingAttempts
		"retry has not passed retention time and has reached max retries"		| 5							| 0			| 0
		"retry has passed retention time and has not reached max retries"		| 1							| 2			| 1
		"retry has not passed retention time and has not reached max retries"	| 5							| 0			| 1
	}

	@Test
	def "retention rule has the expected values"()
	{
		given:
		def cleanupRuleExample = new AfterRetentionCleanupRuleModel()
		cleanupRuleExample.setCode("outboundRetryCleanupRule")

		when:
		def cleanupRule = getModelByExample(cleanupRuleExample)

		then:
		with(cleanupRule) {
			getRetirementItemType().getCode() == "OutboundSyncRetry"
			getItemFilterExpression() == "{remainingSyncAttempts} <= 0"
			getActionReference() == "basicRemoveCleanupAction"
			getRetentionTimeSeconds() == SEVEN_DAYS_SECONDS
			with(getRetirementDateAttribute()) {
				getEnclosingType().getCode() == "OutboundSyncRetry"
				getQualifier() == "modifiedtime"
			}
		}
	}

	@Test
	def "cronjob executes every night at midnight"()
	{
		given:
		def cronjobExample = new CronJobModel()
		cronjobExample.setCode("outboundRetryCleanupCronJob")

		when:
		def cronjob = getModelByExample(cronjobExample)

		then:
		cronjob.getTriggers().get(0).getCronExpression() == "0 0 0 * * ?"
	}

	private retryCleanupCronJobIsRun() {
		cronJobService.performCronJob(cronJobService.getCronJob('outboundRetryCleanupCronJob'), true)
	}

	def retry(final ItemModel item, final OutboundChannelConfigurationModel channel) {
		def retry = new OutboundSyncRetryModel()
		retry.setItemPk(item.pk.longValue)
		retry.setChannel(channel)
		retry
	}

	def channel(final String code) {
		def channel = new OutboundChannelConfigurationModel()
		channel.setCode(code)
		channel
	}

	def changeRetentionTime(def seconds)
	{
		importImpEx(
				"INSERT_UPDATE AfterRetentionCleanupRule; code[unique = true]; retentionTimeSeconds;",
				"; outboundRetryCleanupRule ; $seconds")
	}
}