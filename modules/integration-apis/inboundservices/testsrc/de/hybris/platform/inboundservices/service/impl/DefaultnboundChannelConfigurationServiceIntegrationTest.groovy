package de.hybris.platform.inboundservices.service.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.service.InboundChannelConfigurationService
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4299')
class DefaultnboundChannelConfigurationServiceIntegrationTest extends ServicelayerTransactionalSpockSpecification
{
	@Resource
	private InboundChannelConfigurationService defaultInboundChannelConfigurationService;

	private static final def IO_ICC = "InboundProductIO_ICC"
	private static final def IO_NOICC = "InboundProductIO_NOICC"
	private static final def IO_NOTEXIST= "InboundProductIO_NOTEXIST"

	def setup() {
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE IntegrationObject; code[unique = true]',
				"                               ; $IO_ICC ",
				"                               ; $IO_NOICC ",
				'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)',
				"                                         ; $IO_ICC                               ; BASIC"
		)
	}
	def cleanup() {
		IntegrationTestUtil.remove InboundChannelConfigurationModel, {icc->icc.integrationObject.code==IO_ICC}
		IntegrationTestUtil.remove IntegrationObjectModel, {io->io.code == IO_ICC}
		IntegrationTestUtil.remove IntegrationObjectModel, {io->io.code == IO_NOICC}
	}

	@Test
	@Unroll
	def "#description"()
	{
		given:
		def integrationObject = IntegrationTestUtil.findAny(IntegrationObjectModel, { io ->io.code == IOModel })
				.orElse(null)

		expect:
		defaultInboundChannelConfigurationService.findInboundChannelConfigurationByIntegrationObject(integrationObject).isPresent() == expectResult;

		where:
		description                                                              | IOModel      | expectResult
		"find integration object was assigned inbound channel configuration"     | IO_ICC       | true
		"not find integration object was assigned inbound channel configuration" | IO_NOICC     | false
		"not find integration object was assigned inbound channel configuration" | IO_NOTEXIST  | false
	}
}


