package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import de.hybris.platform.inboundservices.service.impl.DefaultInboundChannelConfigurationService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test;
import spock.lang.Issue;
import spock.lang.Specification

@UnitTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4299')
class IntegrationObjectRemoveInterceptorUnitTest extends Specification
{
	def defaultInboundChannelConfigurationService = Stub DefaultInboundChannelConfigurationService
	def icc = Stub InboundChannelConfigurationModel
	def interceptor = new IntegrationObjectRemoveInterceptor(defaultInboundChannelConfigurationService)
	def integrationObject = Stub IntegrationObjectModel
	private static final PK IO_PK = PK.fromLong(456)

	@Test
	def "throw exception when deleting Integration Object assigned InboundChannelConfugration"()
	{
		given:
		defaultInboundChannelConfigurationService.findInboundChannelConfigurationByIntegrationObject(_) >> Optional.of(icc)

		when:
		interceptor.onRemove integrationObject, Stub(InterceptorContext)

		then:
		def e = thrown InterceptorException
		e.message.contains "Please delete the related InboundChannelConfiguration and try again"
	}

	@Test
	def "there is no excetpion when deleting Integration Object without InboundChannelConfugration"()
	{
		given:
		defaultInboundChannelConfigurationService.findInboundChannelConfigurationByIntegrationObject(_) >> Optional.empty()

		when:
		interceptor.onRemove integrationObject, Stub(InterceptorContext)

		then:
		noExceptionThrown()
	}
}