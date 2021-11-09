/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.util

import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importCatalogVersion
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.outboundsync.OutboundChannelConfigurationBuilder.outboundChannelConfigurationBuilder

class OutboundSyncTestUtil {

	private static final String DEFAULT_CATALOG_VERSION = "Default:Staged"

	static OutboundChannelConfigurationModel outboundChannelConfigurationExists(final String channelCode, final String integrationObjectCode) {
		outboundChannelConfigurationBuilder()
				.withCode(channelCode)
				.withIntegrationObjectCode(integrationObjectCode)
				.withConsumedDestination(consumedDestinationBuilder().withId("destination_" + channelCode))
				.build()
	}

	static cleanup() {
		ConsumedDestinationBuilder.cleanup()
		IntegrationTestUtil.removeAll OutboundSyncStreamConfigurationModel
		IntegrationTestUtil.removeAll OutboundSyncJobModel
		IntegrationTestUtil.removeAll OutboundSyncCronJobModel
		IntegrationTestUtil.removeAll OutboundChannelConfigurationModel
        IntegrationTestUtil.removeAll OutboundSyncStreamConfigurationContainerModel
        IntegrationTestUtil.removeAll CronJobModel
	}

	static ProductModel importProductWithCode(final String code)
	{
		importCatalogVersion("Staged", "Default", true)
		importImpEx(
				"INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)",
				"                     ; $code              ; $DEFAULT_CATALOG_VERSION"
		)
		getProductByCode(code)
	}

	static void outboundSyncRetryExists(final Long itemPk, final String channelConfigurationCode) throws ImpExException {
		importImpEx(
				"INSERT_UPDATE OutboundSyncRetry; itemPk[unique = true]; syncAttempts; channel(code)",
				"                               ; $itemPk              ; 3           ; $channelConfigurationCode ")
	}

	static ProductModel getProductByCode(final String code)
	{
		return IntegrationTestUtil.findAny(ProductModel, { it.code == code }).orElse(null)
	}

	static OutboundChannelConfigurationModel getChannelConfigurationByAttributes(String code, IntegrationObjectModel integrationObject) {

		return IntegrationTestUtil.findAny(OutboundChannelConfigurationModel, { it.code == code && it.integrationObject == integrationObject }).orElse(null)
	}

    /**
     * Retrieves the outbound sync job configured in the persistence storage.
     * @return the job model, if there is only one job configured in the storage; {@code null}, if there are no outbound
     * sync jobs in the persistent storage.
     * @throws IllegalStateException when there is more than one job exists in the persistent storage. This method is not
     * suitable for such cases and the ambiguity needs to be resolved by the test.
     */
	static CronJobModel outboundCronJob() {
        def jobs = IntegrationTestUtil.findAll OutboundSyncCronJobModel
        if (jobs.size() > 1) {
            throw new IllegalStateException("More than one context jobs found: " + jobs)
        }
		return jobs.empty ? null : jobs.first()
	}
}
