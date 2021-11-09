package de.hybris.platform.kymaintegrationservices.strategies.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import spock.lang.Issue
import javax.annotation.Resource

@IntegrationTest
@Issue('https://cxjira.sap.com/browse/GRIFFIN-4183')
class KymaDestinationTargetCloningCredentialTest extends ServicelayerTransactionalSpockSpecification
{

	private static final String TEMPLATE_DESTINATION_TARGET_ID = "template_kyma";
	private static final String TEST_DESTINATION_TARGET_ID = "KYMA";
	private static final String EXIST_BASIC_CREDENTIAL_ID = "basicCred";
	private static final String EXISTED_EXPOSED_CREDENTIAL_ID = "testExistedExposedCredential";
	private static final String EXPECTED_NEW_EXPOSED_CREDENTIAL_ID = "KYMA";
	@Resource
	FlexibleSearchService flexibleSearchService;
	@Resource
	KymaDestinationTargetCloningStrategy kymaDestinationTargetCloningStrategy;
	@Resource
	DestinationService<AbstractDestinationModel> destinationService;

	def setup() {
		importCsv("/test/apiConfigurations-credentialCloneTest.impex", "UTF-8");
	}

	@Test
	def "create a new destination, an new exposed oauth credential will be assigned."()
	{
		given: "a basic credential"
		final BasicCredentialModel exampleBasicCredential = new BasicCredentialModel();
		exampleBasicCredential.setId(EXIST_BASIC_CREDENTIAL_ID);
		final BasicCredentialModel cred = flexibleSearchService.getModelByExample(exampleBasicCredential);

		and:"OAuth Credential"
		final ExposedOAuthCredentialModel exampleOAuthCredential = new ExposedOAuthCredentialModel();

		and:"an assigned Exposed OAuth Credential"
		exampleOAuthCredential.setId(EXISTED_EXPOSED_CREDENTIAL_ID);
		final ExposedOAuthCredentialModel oAuthCred = flexibleSearchService.getModelByExample(exampleOAuthCredential);

		and: "destinations"
		final List<AbstractDestinationModel> destinations = destinationService
				.getDestinationsByDestinationTargetId(TEMPLATE_DESTINATION_TARGET_ID);

		and:"destincation doesn't have credential"
		destinations.get(0).setCredential(null);

		and:"a destination target"
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		when:
		kymaDestinationTargetCloningStrategy.createDestinations(null, testDestinationTarget, destinations);

		final List<AbstractDestinationModel> clonedDestinations = destinationService.getDestinationsByDestinationTargetId(testDestinationTarget.getId());

		then:"assign newOauth credential for cloned destination"
		clonedDestinations.get(0).getCredential() instanceof ExposedOAuthCredentialModel && clonedDestinations.get(0).getCredential().getId().equals(EXPECTED_NEW_EXPOSED_CREDENTIAL_ID);
	}
}
