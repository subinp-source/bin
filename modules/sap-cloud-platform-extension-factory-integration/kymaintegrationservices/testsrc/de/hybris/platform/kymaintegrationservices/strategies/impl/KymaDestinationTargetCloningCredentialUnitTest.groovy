package de.hybris.platform.kymaintegrationservices.strategies.impl

import com.google.common.collect.Lists
import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import org.junit.Test
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class KymaDestinationTargetCloningCredentialUnitTest extends Specification {

	private static final String FIRST_TEMPLATE_DESTINATION_TARGET_ID = "template_kyma";
	private static final String SECOND_TEMPLATE_DESTINATION_TARGET_ID = "template_default";
	private static final String DESTINATION_ID = "testDestinationId";

	def modelService = Spy(ModelService);
	@Shared
	def cred = new BasicCredentialModel();

	def destination = new ExposedDestinationModel();
	def secondDestination = new ExposedDestinationModel();

	def cloneDestination = new ExposedDestinationModel();
	@Shared
	def firstDestinationTarget = new DestinationTargetModel();
	@Shared
	def secondDestinationTarget = new DestinationTargetModel();
	@Shared
	def exposedOAuthCredential = new ExposedOAuthCredentialModel();
	def oauthClientDetails = Stub(OAuthClientDetailsModel);

	def setup() {
		cred.setId("basicCredential");
		cred.setUsername("admin");
		cred.setPassword("admin");

		firstDestinationTarget.setId(FIRST_TEMPLATE_DESTINATION_TARGET_ID);
		secondDestinationTarget.setId(SECOND_TEMPLATE_DESTINATION_TARGET_ID);

		destination.setId(DESTINATION_ID);
		destination.setDestinationTarget(firstDestinationTarget);
		secondDestination.setId("testSecondDestinationId");

		cloneDestination.setId(DESTINATION_ID);
		cloneDestination.setDestinationTarget(firstDestinationTarget);

		exposedOAuthCredential.setId("Kyma");
		exposedOAuthCredential.setOAuthClientDetails(oauthClientDetails);
	}

	@Test
	@Unroll
	@Issue('https://cxjira.sap.com/browse/GRIFFIN-4183')
	def "create new destination, exposed credential will be #description"() {

		given:
		destination.setCredential(originalCredential);

		and:
		def kymaDestinationTargetCloningStrategy = Spy(KymaDestinationTargetCloningStrategy) {
			createCredential(_) >> exposedOAuthCredential
			getModelService() >> modelService

			//by this one, we assume the crednetinal is null or is not assigned ICC
			isResetExposedDestinationKymaCredentialAllowed(_) >> allowResetED;

		}

		cloneDestination.setCredential(originalCredential);
		modelService.clone(_) >> cloneDestination;
		modelService.saveAll(_) >> { null };

		final List<AbstractDestinationModel> destinations = Lists.newArrayList(destination);
		firstDestinationTarget.setDestinations(destinations);

		when:
		kymaDestinationTargetCloningStrategy.createDestinations(null, firstDestinationTarget, destinations);

		then:
		cloneDestination.getCredential() == expectedCredential

		where:
		description                                         | originalCredential | allowResetED  | expectedCredential
		"assigned new when original credential is null"     | null               | true          | exposedOAuthCredential
		"assigned new when destination not assigned ICC"    | cred               | true          | exposedOAuthCredential
		"kept when when destination is assigned ICC"        | cred               | false         | cred

	}

	@Test
	@Unroll
	@Issue('https://cxjira.sap.com/browse/GRIFFIN-4295')
	def "return #expectResult when credential is used by #description"() {
		given:
		destination.setCredential(cred);

		and:
		secondDestination.setDestinationTarget(secondTargetId);
		secondDestination.setCredential(cred);

		and:
		def destinationService = Stub(DestinationService);
		def kymaDestinationTargetCloningStrategy = Spy(KymaDestinationTargetCloningStrategy) {
			getDestinationService() >> destinationService;

			isResetExposedDestinationKymaCredentialAllowed(_) >> allowResetED;
		}
		destinationService.getExposedDestinationsByCredentialId(_) >> Lists.newArrayList(destination, secondDestination);

		expect:
		kymaDestinationTargetCloningStrategy.isCredentialUsedByDifferentExposedDestination(destination, cred) == expectResult

		where:
		description                                            | allowResetED | secondTargetId          | expectResult
		"Exposed Destination with different target and icc"    | false        | secondDestinationTarget | true
		"Exposed Destination with same target and icc"         | false        | firstDestinationTarget  | false
		"Exposed Destination with different target and no icc" | true         | secondDestinationTarget | false
		"Exposed Destination with same target and no icc"      | true         | firstDestinationTarget  | false
	}
}
