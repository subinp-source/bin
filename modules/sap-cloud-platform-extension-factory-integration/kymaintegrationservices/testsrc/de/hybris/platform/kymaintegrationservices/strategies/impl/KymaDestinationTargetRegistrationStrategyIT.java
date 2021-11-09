/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;

import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_GETINFO_DESTINATION_ID;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.kymaintegrationservices.services.CertificateService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class KymaDestinationTargetRegistrationStrategyIT extends ServicelayerTransactionalTest
{

	private static final String TEST_CREDENTIAL_ID = "testCredential";
	private static final String TEMPLATE_DESTINATION_TARGET_ID = "template_kyma";
	private static final String TEST_ENDPOINT_ID = "testEndpointId";
	private static final String TEST_ENDPOINT_NAME = "testEndpointName";
	private static final String ENDPOINT_VERSION = "v1";
	private static final String TEST_ENDPOINT_DESCRIPTION = "testEndpointDescription";
	private static final String KYMA_SERVICES = "kyma-services";
	private static final String KYMA_EVENTS = "kyma-events";

	@Resource
	private KymaDestinationTargetRegistrationStrategy kymaDestinationTargetRegistrationStrategy;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private DestinationService<AbstractDestinationModel> destinationService;

	@Resource
	private CronJobService cronJobService;

	@Mock
	private CertificateService certificateService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateConsumedCertificateCredential()
	{
		kymaDestinationTargetRegistrationStrategy.createConsumedCertificateCredential(TEST_CREDENTIAL_ID);

		final ConsumedCertificateCredentialModel credential = new ConsumedCertificateCredentialModel();
		credential.setId(TEST_CREDENTIAL_ID + "-cert");

		assertNotNull(flexibleSearchService.getModelByExample(credential));
	}

	@Test
	public void testCreateConsumedDestinations() throws ImpExException
	{
		importCsv("/test/apiConfigurations-lifecycleTest.impex", "UTF-8");

		final ConsumedCertificateCredentialModel credential = new ConsumedCertificateCredentialModel();
		credential.setId(TEST_CREDENTIAL_ID);

		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel destinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		kymaDestinationTargetRegistrationStrategy.createConsumedDestinations(destinationTarget, credential);

		assertNotNull(destinationService.getDestinationByIdAndByDestinationTargetId(KYMA_SERVICES, TEMPLATE_DESTINATION_TARGET_ID));
		assertNotNull(destinationService.getDestinationByIdAndByDestinationTargetId(KYMA_EVENTS, TEMPLATE_DESTINATION_TARGET_ID));
		assertNotNull(destinationService.getDestinationByIdAndByDestinationTargetId(DEFAULT_GETINFO_DESTINATION_ID,
				TEMPLATE_DESTINATION_TARGET_ID));
	}

	@Test
	public void testRegisterDestinationTarget() throws ImpExException, ApiRegistrationException, CredentialException
	{
		importCsv("/test/apiConfigurations-lifecycleTest.impex", "UTF-8");

		final ConsumedCertificateCredentialModel credential = new ConsumedCertificateCredentialModel();
		credential.setId(TEST_CREDENTIAL_ID);

		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel destinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final Map<String, String> params = new HashMap<>();
		params.put(KymaDestinationTargetRegistrationStrategy.TOKEN_URL, "testTokenUrl");

		kymaDestinationTargetRegistrationStrategy.setCertificateService(certificateService);

		when(certificateService.retrieveCertificate(any(), any())).thenReturn(credential);

		kymaDestinationTargetRegistrationStrategy.registerDestinationTarget(destinationTarget, params);

		assertNotNull(cronJobService.getCronJob(destinationTarget.getId()));
	}

	@Test
	public void testCreateEndpoint()
	{
		kymaDestinationTargetRegistrationStrategy.createEndpoint(TEST_ENDPOINT_ID, TEST_ENDPOINT_NAME, ENDPOINT_VERSION,
				TEST_ENDPOINT_DESCRIPTION);

		final EndpointModel endpoint = new EndpointModel();
		endpoint.setId(TEST_ENDPOINT_ID);

		assertNotNull(flexibleSearchService.getModelByExample(endpoint));
	}

	@Test(expected = DeleteDestinationTargetNotPossibleException.class)
	public void testDeregisterDestinationTargetNotPossible()
			throws DeleteDestinationTargetNotPossibleException, ApiRegistrationException
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);
		exampleDestinationTarget.setRegistrationStatus(RegistrationStatus.IN_PROGRESS);

		kymaDestinationTargetRegistrationStrategy.deregisterDestinationTarget(exampleDestinationTarget);

	}

}
