package de.hybris.platform.apiregistryservices.dao.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.dao.DestinationDao
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel
import de.hybris.platform.apiregistryservices.services.impl.DefaultDestinationService
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4295')
class DestinationServiceFindExposedDestionationByCredentialIdIntegrationTest extends ServicelayerTransactionalSpockSpecification
{
	@Resource
	private DefaultDestinationService<AbstractDestinationModel> destinationService ;

	@Test
	@Unroll
	def "find #expectResult #description #credentialId"()
	{
		given:  "Import 3 ExposedDestination (2 with kymaCred1, 1 with kymaCred2, 0 with kymaCred3) and 1 Consumed Destination with kymaCred4"
		importCsv("/test/findExposedDestinationByCredentialId.impex", "UTF-8");

		expect:
		destinationService.getExposedDestinationsByCredentialId(credentialId).size() == expectResult;

		where:
		description                                | credentialId  | expectResult
		"Exposed Destination with credential"      |"kymaCred1"    | 2
		"Exposed Destination with credential"      |"kymaCred2"    | 1
		"Exposed Destination with credential"      |"kymaCred3"    | 0
		"Consumed Destination with credential"     |"kymaCred4"    | 0
	}
}
