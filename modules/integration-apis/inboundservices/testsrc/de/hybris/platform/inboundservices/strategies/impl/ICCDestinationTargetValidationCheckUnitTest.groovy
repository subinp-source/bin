package de.hybris.platform.inboundservices.strategies.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import org.junit.Test
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4288')
class ICCDestinationTargetValidationCheckUnitTest extends Specification {
	def validationStrategy = new ICCDestinationTargetCloningValidationCheckStrategy();

	@Test
	@Unroll
	def "return #expectResult if #description"() {
		expect:
		validationStrategy.isValidExposedDestination(destinationItem) == expectResult

		where:
		description                     		| destinationItem                                                            								  | expectResult
		'Destination is null'           		| null                                                                       								  | true
		'Destination is Consumed Destination'   | Stub(ConsumedDestinationModel)                                                                              | true
		'ExposedDestination has no ICC' 		| Stub(ExposedDestinationModel) { getInboundChannelConfiguration() >> null }                                  | true
		'ExposedDestination has ICC'    		| Stub(ExposedDestinationModel) { getInboundChannelConfiguration()>> Stub(InboundChannelConfigurationModel) } | false
	}
}