package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Issue

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4299')
class IntegrationObjectRemoveInterceptorIntegrationTest extends ServicelayerSpockSpecification {
	private static final def IO_ICC = "InboundProductIO_ICC"
	private static final def IO_NOICC = "InboundProductIO_NOICC"

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
		IntegrationTestUtil.remove InboundChannelConfigurationModel, {icc->icc.integrationObject.code == IO_ICC}
		IntegrationTestUtil.remove IntegrationObjectModel, {io->io.code == IO_ICC}
		IntegrationTestUtil.remove IntegrationObjectModel, {io->io.code == IO_NOICC}
	}

	@Test
	def "Impex:expect exception when deleting Integration Object assigned InboundChannelConfugration"() {
		when:
		IntegrationTestUtil.importImpEx(
				'REMOVE IntegrationObject; code[unique=true]',
				"                        ; $IO_ICC"
		)
		then:
		def e = thrown AssertionError
		e.message.contains "Please delete the related InboundChannelConfiguration and try again"
	}

	@Test
	def "there is no exception when deleting Integration Object without InboundChannelConfugration"() {
		given: 'integration object exists in the persistent storage'
		def integrationObject = IntegrationTestUtil.findAny(IntegrationObjectModel, { io ->io.code == IO_NOICC })
				.orElseThrow {new AssertionError('the integration object is not persisted')}

		when:
		IntegrationTestUtil.remove integrationObject

		then:
		IntegrationTestUtil.findAny(IntegrationObjectModel, { io ->io.code == IO_NOICC }).empty
	}
}
