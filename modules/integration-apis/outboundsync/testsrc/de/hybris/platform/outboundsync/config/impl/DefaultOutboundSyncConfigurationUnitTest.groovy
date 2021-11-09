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
package de.hybris.platform.outboundsync.config.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.apache.commons.configuration.Configuration
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultOutboundSyncConfigurationUnitTest extends Specification {

    private static final String OUTBOUNDSYNC_MAX_RETRIES = "outboundsync.max.retries"
    private static final String OUTBOUNDSYNC_CRONJOBMODEL_SEARCH_SLEEP = "outboundsync.cronjob.search.sleep.milliseconds";

    def syncConfig = new DefaultOutboundSyncConfiguration()

    def configurationService = Stub(ConfigurationService)
    def configuration = Stub(Configuration)

    def setup() {
        configurationService.getConfiguration() >> configuration

        syncConfig.setConfigurationService(configurationService)
    }

    @Test
    def "when max retries property is configured its value is returned"() {
        given:
        configuration.getInt(OUTBOUNDSYNC_MAX_RETRIES) >> 5

        expect:
        syncConfig.getMaxOutboundSyncRetries() == 5
    }

    @Test
    def "when max retries property is not found we use the fallback value"() {
        given:
        configuration.getInt(OUTBOUNDSYNC_MAX_RETRIES) >> { throw new NoSuchElementException() }

        expect:
        syncConfig.getMaxOutboundSyncRetries() == 0
    }

    @Test
    def "when OutboundSyncCronJobModel search sleep property is configured its value is returned"() {
        given:
        configuration.getInt(OUTBOUNDSYNC_CRONJOBMODEL_SEARCH_SLEEP) >> 1234

        expect:
        syncConfig.getOutboundSyncCronjobModelSearchSleep() == 1234
    }

    @Test
    def "when OutboundSyncCronJobModel search sleep property is not found we use the fallback value"() {
        given:
        configuration.getInt(OUTBOUNDSYNC_CRONJOBMODEL_SEARCH_SLEEP) >> { throw new NoSuchElementException() }

        expect:
        syncConfig.getOutboundSyncCronjobModelSearchSleep() == 1000
    }
}
