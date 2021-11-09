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

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.retry.RetrySearchService
import de.hybris.platform.outboundsync.retry.SyncRetryNotFoundException
import de.hybris.platform.outboundsync.util.OutboundSyncTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class RetrySearchServiceIntegrationTest extends ServicelayerTransactionalSpockSpecification {
	static final int SYNC_ATTEMPTS = 3
	static final String CHANNEL_CODE_1 = "channel1"
	static final String OBJECT_CODE = "OutboundProduct"
	static final String ITEM_CODE = "Product"

	@Resource
	RetrySearchService retrySearchService

	def cleanup() {
		OutboundSyncTestUtil.cleanup()
		IntegrationObjectTestUtil.cleanup()
	}

	@Test
	def "finds no retry when itemPk matches but channel does not"() {

		given:
		def integrationObject = IntegrationObjectTestUtil.createIntegrationObject(OBJECT_CODE)

		and:
		def integrationObjectItem = IntegrationObjectTestUtil.createIntegrationObjectItem(integrationObject, ITEM_CODE)
		final Long itemPk = integrationObjectItem.getPk().getLong()

		and:
		OutboundSyncTestUtil.outboundChannelConfigurationExists(CHANNEL_CODE_1, OBJECT_CODE)
		OutboundSyncTestUtil.outboundChannelConfigurationExists("differentChannelCode", OBJECT_CODE)

		and:
		final OutboundChannelConfigurationModel existingChannelConfiguration = OutboundSyncTestUtil.getChannelConfigurationByAttributes(CHANNEL_CODE_1, integrationObject)

		and:
		OutboundSyncTestUtil.outboundSyncRetryExists(itemPk, "differentChannelCode")

		and:
		def outboundItemDTOGroup = outboundItemDTOGroup(itemPk, existingChannelConfiguration)

		when:
		retrySearchService.findRetry(outboundItemDTOGroup)

		then:
		SyncRetryNotFoundException ex = thrown()
		ex.getItemPk() == itemPk
		ex.getChannelConfigurationCode() == CHANNEL_CODE_1
	}

	@Test
	def "finds no retry when channel matches but itemPk does not"() {
		given:
		def integrationObject = IntegrationObjectTestUtil.createIntegrationObject(OBJECT_CODE)

		and:
		def integrationObjectItem = IntegrationObjectTestUtil.createIntegrationObjectItem(integrationObject, ITEM_CODE)
		final Long itemPk = integrationObjectItem.getPk().getLong()

		and:
		OutboundSyncTestUtil.outboundChannelConfigurationExists(CHANNEL_CODE_1, OBJECT_CODE)

		and:
		final OutboundChannelConfigurationModel existingChannelConfiguration = OutboundSyncTestUtil.getChannelConfigurationByAttributes(CHANNEL_CODE_1, integrationObject)

		and:
		OutboundSyncTestUtil.outboundSyncRetryExists(itemPk + 1, CHANNEL_CODE_1)

		and:
		def outboundItem = outboundItemDTOGroup(itemPk, existingChannelConfiguration)

		when:
		retrySearchService.findRetry(outboundItem)

		then:
		SyncRetryNotFoundException ex = thrown()
		ex.getItemPk() == itemPk
		ex.getChannelConfigurationCode() == CHANNEL_CODE_1
	}

	@Test
	def "finds no retry when no retry exists in the database"() {
		given:
		def integrationObject = IntegrationObjectTestUtil.createIntegrationObject(OBJECT_CODE)

		and:
		def integrationObjectItem = IntegrationObjectTestUtil.createIntegrationObjectItem(integrationObject, ITEM_CODE)
		final Long itemPk = integrationObjectItem.getPk().getLong()

		and:
		OutboundSyncTestUtil.outboundChannelConfigurationExists(CHANNEL_CODE_1, OBJECT_CODE)

		and:
		final OutboundChannelConfigurationModel existingChannelConfiguration = OutboundSyncTestUtil.getChannelConfigurationByAttributes(CHANNEL_CODE_1, integrationObject)

		and:
		def outboundItem = outboundItemDTOGroup(itemPk, existingChannelConfiguration)

		when:
		retrySearchService.findRetry(outboundItem)

		then:
		SyncRetryNotFoundException ex = thrown()
		ex.getItemPk() == itemPk
		ex.getChannelConfigurationCode() == CHANNEL_CODE_1
	}

	@Test
	def "finds retry when it exists in the database"() {
		given:
		def integrationObject = IntegrationObjectTestUtil.createIntegrationObject(OBJECT_CODE)

		and:
		def integrationObjectItem = IntegrationObjectTestUtil.createIntegrationObjectItem(integrationObject, ITEM_CODE)
		final Long itemPk = integrationObjectItem.getPk().getLong()

		and:
		OutboundSyncTestUtil.outboundChannelConfigurationExists(CHANNEL_CODE_1, OBJECT_CODE)

		and:
		final OutboundChannelConfigurationModel existingChannelConfiguration = OutboundSyncTestUtil.getChannelConfigurationByAttributes(CHANNEL_CODE_1, integrationObject)

		and:
		OutboundSyncTestUtil.outboundSyncRetryExists(itemPk, CHANNEL_CODE_1)

		and:
		def outboundItem = outboundItemDTOGroup(itemPk, existingChannelConfiguration)


		when:
		def retryFound = retrySearchService.findRetry(outboundItem)

		then:
		retryFound != null
		retryFound.getItemPk() == itemPk
		retryFound.getChannel() == existingChannelConfiguration
		retryFound.getSyncAttempts() == SYNC_ATTEMPTS
	}

	def outboundItemDTOGroup(pk, channelConfig) {
		Stub(OutboundItemDTOGroup) {
			getRootItemPk() >> pk
			getChannelConfiguration() >> channelConfig
		}
	}
}
