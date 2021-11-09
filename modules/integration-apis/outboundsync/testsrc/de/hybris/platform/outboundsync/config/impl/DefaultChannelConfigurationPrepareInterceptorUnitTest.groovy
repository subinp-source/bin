/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.model.StreamConfigurationModel
import de.hybris.platform.outboundsync.config.ChannelConfigurationFactory
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultChannelConfigurationPrepareInterceptorUnitTest extends Specification {

    private DefaultChannelConfigurationPrepareInterceptor prepareInterceptor = new DefaultChannelConfigurationPrepareInterceptor()

    def setup() {
        prepareInterceptor.factory = Stub(ChannelConfigurationFactory) {
            createStreamContainer(_) >> Stub(OutboundSyncStreamConfigurationContainerModel)
            createStreams(_, _) >> [Stub(StreamConfigurationModel)]
            createCronJob(_) >> Stub(OutboundSyncCronJobModel)
            createJob() >> Stub(OutboundSyncJobModel)
        }
    }

    @Test
    def "all expected objects are registered on the interceptorContext"() {
        given:
        def channel = Stub(OutboundChannelConfigurationModel) {
            getAutoGenerate() >> true
        }
        def interceptorContext = Mock(InterceptorContext) {
            getModelService() >> Stub(ModelService) {
                isNew(channel) >> true
            }
        }

        when:
        prepareInterceptor.onPrepare(channel, interceptorContext)

        then:
        1 * interceptorContext.registerElement(_ as OutboundSyncStreamConfigurationContainerModel)
        1 * interceptorContext.registerElement(_ as StreamConfigurationModel)
        1 * interceptorContext.registerElement(_ as OutboundSyncJobModel)
        1 * interceptorContext.registerElement(_ as OutboundSyncCronJobModel)
    }

    @Unroll
    @Test
    def "no elements are registered when getAutoGenerate=#autoGenerate and isNew=#isNewChannel"() {
        given:
        def modelService = Stub(ModelService) {
            isNew() >> isNewChannel
        }
        def interceptorContext = Mock(InterceptorContext) {
            getModelService() >> modelService
        }
        def channel = Stub(OutboundChannelConfigurationModel) {
            getAutoGenerate() >> autoGenerate
        }

        when:
        prepareInterceptor.onPrepare(channel, interceptorContext)


        then:
        0 * interceptorContext.registerElement(_)

        where:
        isNewChannel | autoGenerate
        false        | false
        false        | true
        true         | false
    }
}
