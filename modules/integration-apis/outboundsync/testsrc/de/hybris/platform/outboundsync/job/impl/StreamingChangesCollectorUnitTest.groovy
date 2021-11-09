/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.ItemChangeDTO
import de.hybris.platform.core.PK
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.dto.impl.DeltaDetectionOutboundItemChange
import de.hybris.platform.outboundsync.job.ItemChangeSender
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class StreamingChangesCollectorUnitTest extends Specification {
    private static final Long IO_PK = 1L
    private static final Long OUTBOUND_CHANNEL_CONFIG_PK = 2L
    private static final def JOB_PK = PK.fromLong(3)

    @Shared
    def itemChangeSender = Stub(ItemChangeSender)
    def streamChangesCollector = new StreamingChangesCollector(itemChangeSender, jobModel(), streamModel())

    @Test
    @Unroll
    def "cannot be created with a null #propertyName"() {
        when:
        new StreamingChangesCollector(itemChangeSenderVal, job, streamConfig)

        then:
        thrown(IllegalArgumentException)

        where:
        propertyName          | job        | streamConfig  | itemChangeSenderVal
        'itemChangeSender'    | jobModel() | streamModel() | null
        'streamConfiguration' | jobModel() | null          | itemChangeSender
        'cron job'            | null       | streamModel() | itemChangeSender
    }

    @Test
    def 'reports 0 changes collected upon instantiation'() {
        expect:
        streamChangesCollector.numberOfChangesCollected == 0
    }

    @Test
    def 'reports change count for every change collected'() {
        when:
        streamChangesCollector.collect Stub(ItemChangeDTO)
        then:
        streamChangesCollector.numberOfChangesCollected == 1

        when:
        streamChangesCollector.collect Stub(ItemChangeDTO)
        then:
        streamChangesCollector.numberOfChangesCollected == 2
    }

    @Test
    def "collect sends the expected OutboutboundItemChangeDTO"() {
        when:
        def collectResp = streamChangesCollector.collect(Stub(ItemChangeDTO))

        then:
        itemChangeSender.send(_ as OutboundItemDTO) >> { args ->
            with(args[0] as OutboundItemDTO) {
                integrationObjectPK == IO_PK
                channelConfigurationPK == OUTBOUND_CHANNEL_CONFIG_PK
                cronJobPK == JOB_PK
                item instanceof DeltaDetectionOutboundItemChange
            }
        }
        collectResp
    }

    OutboundSyncStreamConfigurationModel streamModel() {
        Stub(OutboundSyncStreamConfigurationModel) {
            getOutboundChannelConfiguration() >> Stub(OutboundChannelConfigurationModel) {
                getIntegrationObject() >> Stub(IntegrationObjectModel) {
                    getPk() >> PK.fromLong(IO_PK)
                }
                getPk() >> PK.fromLong(OUTBOUND_CHANNEL_CONFIG_PK)
            }
        }
    }

    CronJobModel jobModel() {
        Stub(CronJobModel) {
            getPk() >> JOB_PK
        }
    }
}
