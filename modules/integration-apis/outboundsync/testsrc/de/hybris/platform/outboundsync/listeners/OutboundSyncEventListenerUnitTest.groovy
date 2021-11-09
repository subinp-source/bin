/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.listeners

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.outboundsync.events.OutboundSyncEvent
import de.hybris.platform.outboundsync.job.impl.OutboundSyncJobRegister
import de.hybris.platform.outboundsync.job.impl.OutboundSyncJob
import org.junit.Test
import spock.lang.Specification

@UnitTest
class OutboundSyncEventListenerUnitTest extends Specification {
    private static final def SOME_PK = PK.fromLong 1

    def jobRegister  = Stub OutboundSyncJobRegister
    def eventListener = new OutboundSyncEventListener(jobRegister)

    @Test
    def 'ignores an event when its primary key is not for an outbound sync job'() {
        given:
        jobRegister.getJob(SOME_PK) >> Optional.empty()
        and:
        def event = outboundSyncEvent(SOME_PK)

        when:
        eventListener.onEvent event

        then:
        0 * _.applyEvent(event)
    }

    @Test
    def 'passes an event to the appropriate state aggregator when its primary key is for an outbound sync job'() {
        given:
        def stateAggregator = Mock OutboundSyncJob
        jobRegister.getJob(SOME_PK) >> Optional.of(stateAggregator)
        and:
        def event = outboundSyncEvent(SOME_PK)

        when:
        eventListener.onEvent event

        then:
        1 * stateAggregator.applyEvent(event)
    }

    OutboundSyncEvent outboundSyncEvent(PK pk) {
        Stub(OutboundSyncEvent) {
            getCronJobPk() >> pk
        }
    }
}