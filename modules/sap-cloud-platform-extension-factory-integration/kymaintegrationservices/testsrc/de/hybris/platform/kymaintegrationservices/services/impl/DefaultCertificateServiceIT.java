/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;

import static de.hybris.platform.kymaintegrationservices.services.impl.DefaultCertificateService.CERTIFICATE_FOOTER;
import static de.hybris.platform.kymaintegrationservices.services.impl.DefaultCertificateService.CERTIFICATE_HEADER;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_RENEWAL_SERVICE_ID;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.kymaintegrationservices.dto.KymaCertificateCreation;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.validation.services.ValidationService;

import java.net.URI;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


@IntegrationTest
public class DefaultCertificateServiceIT extends ServicelayerTransactionalTest
{
	private static final String DEFAULT_EVENTS_DESTINATION_ID = "kyma-events";
	private static final String DEFAULT_SERVICES_DESTINATION_ID = "kyma-services";
	private static final String ENCODED_VALID_TEST_CERTIFICATE = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUVjVENDQWxtZ0F3SUJBZ0lCQWpBTkJna3Foa2lHOXcwQkFRc0ZBREJxTVFzd0NRWURWUVFHRXdKUVRERUsKTUFnR0ExVUVDQXdCVGpFUU1BNEdBMVVFQnd3SFIweEpWMGxEUlRFVE1CRUdBMVVFQ2d3S1UwRlFJRWg1WW5KcApjekVOTUFzR0ExVUVDd3dFUzNsdFlURVpNQmNHQTFVRUF3d1FkMjl5YldodmJHVXVhM2x0WVM1amVEQWVGdzB4Ck9EQTNNekF4TXpJNU5EZGFGdzB4T0RFd01qZ3hNekk1TkRkYU1HVXhDekFKQmdOVkJBWVRBa1JGTVJBd0RnWUQKVlFRSUV3ZFhZV3hrYjNKbU1SQXdEZ1lEVlFRSEV3ZFhZV3hrYjNKbU1Rd3dDZ1lEVlFRS0V3TlRRVkF4RHpBTgpCZ05WQkFzVEJrTTBZMjl5WlRFVE1CRUdBMVVFQXhNS1pXTXRaR1ZtWVhWc2REQ0NBU0l3RFFZSktvWklodmNOCkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFLZnRLaWpOT3pEUGdKVlZIREpoNnVmMmwrMjV2enMzVE5iZW81L3cKNjdvMytvYXZWaU5EUlBtMGFIY2EvdzlaUmIwWXp3anhyS1g4cDFobU5EVGRuZjZyNzJDU0hnaDdtN0gwT2NxbQpFMk5ZZzQyWTN4Rm9ncy9JdlRmS2tMWkxzdVZEUG9mVkVrQzVRZmRVNU5ycGRoV21KV1dLZnlYM1lCOS93bzJnCjJLbE9tTmQ3RCtaN3FrQmg2ZkVLVlpBUDY5VW5oeGxuRWEzbEM1ZktjbmlTZjJYRDhhb0gxSnpXQ21UMkh1ZHAKQ3orM1Z3bUl4RWs1V0NlQmxZZEpOaUkwQnFXV3lOZ2FCc2VPSk5oN2M3ODBvV0F1ZXJ4STFaRkVSUUxHbFBYMgp5cUJVWDEvZVpVbjVMZDlDR0FsS2VJZHJGbGNNRTVvYTJIVUhNWGhBbzVVd0Jlc0NBd0VBQWFNbk1DVXdEZ1lEClZSMFBBUUgvQkFRREFnZUFNQk1HQTFVZEpRUU1NQW9HQ0NzR0FRVUZCd01DTUEwR0NTcUdTSWIzRFFFQkN3VUEKQTRJQ0FRREdHbEVvM2lFRUV3YU5YYVFoRXVuZWJBZW5icXdlMCs1NnpvK2dSK0ZlZUlPckY4TEY4N01hVVEyQQpJaWMyZ3hOTWJvM1dvT2YzVEV0ZE4yZ2NvcVdXaUVrRnZFUVZOY3h2b1c5SGtBZmlxcEFlY2hTSTMxZ3hJZXJICnpzdzJlMWRON3RrMjFpZjRvUElDbUxFZFZOZll0TWVlaS93dEV1Wk8rbEZnRlRpNDBWbzBaNEg1eVMzZnZUc28KaDdFaTVlL0NoTC9vQzNYK05MZ2JyZnhvVFl2MHlzOXEycFgwb0pKMlU3aFlmMXVLViszR0U0RHpLeWxxTmJiMwpRc3hRNGN5bVJsTElsaVE0eDBKbFB5UkhpeDMzUnpsYW9rYkhHcmR0QVlCc1hGL1lxWXl4enNBQmF1SlFBR3QrCnp6WXc0L092Zi9wa0xWY1o5NitNS2Uzb240L3lzTzR2VG83Rk51eFJlUnpXbldmTzZNR0I2aU84WjVBOFRScVUKcGlDeHJBZXdQVHdSZEZqQWxHOFdvOEp6K1FiL3NLL2lOTEZxSWE2OENHZ1huQyttaXRWd21JWGpNNkpiU0dlaAoreGJWaElKOWZtSjYzeFB4dTU5N1VNRG1RV3gyZXRpdnRkZzFqbnovUHZXRi9oQTFxR2FlK0dVekMvVC9nU2FXCnlYdjdZKzQvRDdsTjBsZ2RtTlJFT0oweHpOVTJRUStYUW9TenJEV1lieDFCcHgzS2p5bG1lbEtKUURIR0Q3b0MKMThLTGNLbVFEdkRtMDFyUFRySW9Bd1ljRnNTWG43U29Jd1ZWcnZ5ZDJrdVEzaXVDK00wcWF4Z1l5MWJiclVwTQo5UmgzR1FiWkt3aUFrV01XcC9lVFYwZW9mWUIvMC8zOFl3RThZZUZweHJudFB4Y1duQT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
	private static final String ENCODED_INVALID_TEST_CERTIFICATE = "VGVzdENlcnRpZmljYXRl";
	private static final String CSR_TEST_URL = "http://localhost:8081/kyma/v1/certificate?token=testtoken";
	private static final String CRT_TEST_URL = "http://localhost:8081/kyma/v1/certificate";
	private static final String RENEWAL_TEST_URL = "https://localhost:8081/v1/renewal";
	private static final String GETINFO_TEST_URL = "https://localhost:8081/v1/metadata/info";
	private static final String TEST_JSON = "{\"csrUrl\": \"http://localhost:8081/kyma/v1/certificate\", "
			+ "\"api\":{\"metadataUrl\":\"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/metadata/services\", "
			+ "\"eventsUrl\": \"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/events\", "
			+ "\"infoUrl\": \"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/info\", "
			+ "\"certificatesUrl\":  \"http://localhost:8081/kyma/v1/certificate\"},"
			+ "\"certificate\":{\"subject\":\"OU=C4core,O=SAP,L=Waldorf,ST=Waldorf,C=DE,CN=ec-default\", "
			+ "\"extensions\": \"\", \"key-algorithm\": \"rsa2048\"}}";

	private static final String TEST_JSON_INVALID_PROTOCOL = "{\"csrUrl\": \"http://localhost:8081/kyma/v1/certificate\", "
			+ "\"api\":{\"metadataUrl\":\"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/metadata/services\", "
			+ "\"eventsUrl\": \"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/events\", "
			+ "\"infoUrl\": \"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/info\", "
			+ "\"certificatesUrl\":  \"http://localhost:8081/kyma/v1/certificate\"},"
			+ "\"certificate\":{\"subject\":\"OU=C4core,O=SAP,L=Waldorf,ST=Waldorf,C=DE,CN=ec-default\", "
			+ "\"extensions\": \"\", \"key-algorithm\": \"rsa2048\"}}";

	private static final String TEST_JSON_INVALID_SUBJECT = "{\"csrUrl\": \"http://localhost:8081/kyma/v1/certificate\", "
			+ "\"api\":{\"metadataUrl\":\"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/metadata/services\", "
			+ "\"eventsUrl\": \"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/events\", "
			+ "\"infoUrl\": \"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/info\", "
			+ "\"certificatesUrl\":  \"http://localhost:8081/kyma/v1/certificate\"},"
			+ "\"certificate\":{\"subject\":\"OU=C4core,O=SAP,L=Munich,ST=Waldorf,C=DE,CN=ec-default\", "
			+ "\"extensions\": \"\", \"key-algorithm\": \"rsa2048\"}}";

	private static final String TEST_JSON_INVALID_PUBLICKEY_ALGORITHM = "{\"csrUrl\": \"http://localhost:8081/kyma/v1/certificate\", "
			+ "\"api\":{\"metadataUrl\":\"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/metadata/services\", "
			+ "\"eventsUrl\": \"http://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/events\", "
			+ "\"infoUrl\": \"https://gateway.CLUSTER_NAME.kyma.cluster.cx/test/v1/info\", "
			+ "\"certificatesUrl\":  \"http://localhost:8081/kyma/v1/certificate\"},"
			+ "\"certificate\":{\"subject\":\"OU=C4core,O=SAP,L=Waldorf,ST=Waldorf,C=DE,CN=ec-default\", "
			+ "\"extensions\": \"\", \"key-algorithm\": \"rsa1024\"}}";

	private static final String KYMA_DEFAULT_TARGET = "kymaDefaultTarget";
	private static final String TEST_CERTIFICATE = "testCertificate";
	private static final String KEY_SUBJECT = "OU=C4core,O=SAP,L=Waldorf,ST=Waldorf,C=DE,CN=ec-default";
	private static final String KEY_ALG = "rsa1024";
	private static final String TEST_JSON_GETINFO_RESPONSE = "{  \n" + "   \"clientIdentity\":{  \n"
			+ "      \"application\":\"application\"\n" + "   },\n" + "   \"urls\":{  \n" + "      \"eventsUrl\":\"\",\n"
			+ "      \"metadataUrl\":\"\",\n" + "      \"renewCertUrl\":\"\",\n" + "      \"revocationCertUrl\":\"\"\n" + "   },\n"
			+ "   \"certificate\":{  \n" + "      \"subject\":\"" + KEY_SUBJECT + "\",\n" + "      \"extensions\":\"\",\n"
			+ "      \"key-algorithm\":\"" + KEY_ALG + "\"\n" + "   }\n" + "}";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	@Resource
	private ModelService modelService;
	@Resource
	private DestinationService<AbstractDestinationModel> destinationService;
	@Resource(name = "kymaCertificateRestTemplate")
	private RestTemplate restTemplate;
	@Resource
	private ValidationService validationService;
	private DefaultCertificateService defaultCertificateService;
	@Resource
	private DestinationTargetService destinationTargetService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private TaskService taskService;
	@Mock
	private X509Certificate x509Certificate;


	private String generateJsonForCertificate(final String certificate)
	{
		return "{\"crt\":\"" + certificate + "\"}";
	}

	@Before
	public void setUp() throws ImpExException
	{
		MockitoAnnotations.initMocks(this);

		importCsv("/test/apiConfigurations.impex", "UTF-8");
		importCsv("/test/constraints.impex", "UTF-8");

		defaultCertificateService = new DefaultCertificateService();
		defaultCertificateService.setModelService(modelService);
		defaultCertificateService.setDestinationService(destinationService);
		defaultCertificateService.setRestTemplate(restTemplate);
		defaultCertificateService.setDestinationTargetService(destinationTargetService);
		defaultCertificateService.setTaskService(taskService);

	}

	@Test
	public void testRetrieveCertificate() throws CredentialException, CertificateEncodingException
	{
		final URI testUri = URI.create(CSR_TEST_URL);
		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON, MediaType.APPLICATION_JSON));

		mockServer.expect(times(1), requestTo(CRT_TEST_URL)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(generateJsonForCertificate(ENCODED_VALID_TEST_CERTIFICATE), MediaType.APPLICATION_JSON));

		defaultCertificateService = Mockito.spy(new DefaultCertificateService());
		defaultCertificateService.setModelService(modelService);
		defaultCertificateService.setDestinationService(destinationService);
		defaultCertificateService.setRestTemplate(restTemplate);
		defaultCertificateService.setDestinationTargetService(destinationTargetService);
		defaultCertificateService.setTaskService(taskService);
		doReturn(x509Certificate).when(defaultCertificateService).verifyCredential(anyString(), any(), any());
		doNothing().when(defaultCertificateService).scheduleCertificateRenewalTask(any(), any());
		Mockito.when(x509Certificate.getTBSCertificate()).thenReturn(new byte[3]);
		final ConsumedCertificateCredentialModel exampleCredential = modelService.create(ConsumedCertificateCredentialModel.class);
		exampleCredential.setId("kyma-cert");

		final ConsumedCertificateCredentialModel credential = flexibleSearchService.getModelByExample(exampleCredential);

		defaultCertificateService.retrieveCertificate(testUri, credential);

		final ArgumentCaptor<String> rdnCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<KeyPair> keyPairCaptor = ArgumentCaptor.forClass(KeyPair.class);
		verify(defaultCertificateService).generateCSR(rdnCaptor.capture(), keyPairCaptor.capture());

		final String csrBody = new String(defaultCertificateService.generateCSR(rdnCaptor.getValue(), keyPairCaptor.getValue()));
		assertTrue(csrBody.startsWith(CERTIFICATE_HEADER) && csrBody.endsWith(CERTIFICATE_FOOTER));

		assertEquals(credential.getCertificateData(), ENCODED_VALID_TEST_CERTIFICATE);
	}

	@Test
	public void testRetrieveCertificateWithInvalidKeyPairs() throws CredentialException
	{
		final URI testUri = URI.create(CSR_TEST_URL);
		final MockRestServiceServer mockServer;

		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON, MediaType.APPLICATION_JSON));

		mockServer.expect(times(1), requestTo(CRT_TEST_URL)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(generateJsonForCertificate(ENCODED_VALID_TEST_CERTIFICATE), MediaType.APPLICATION_JSON));

		final ConsumedCertificateCredentialModel consumedCertificateCredential = modelService
				.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId(TEST_CERTIFICATE);

		expectedException.expect(CredentialException.class);
		expectedException.expectMessage("Credential verification is failed. Public key and private key don't match");

		defaultCertificateService.retrieveCertificate(testUri, consumedCertificateCredential);
	}

	@Test
	public void testRetrieveCertificateWithInvalidProtocol() throws CredentialException, CertificateEncodingException
	{
		final URI testUri = URI.create(CSR_TEST_URL);
		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON_INVALID_PROTOCOL, MediaType.APPLICATION_JSON));

		mockServer.expect(times(1), requestTo(CRT_TEST_URL)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(generateJsonForCertificate(ENCODED_INVALID_TEST_CERTIFICATE), MediaType.APPLICATION_JSON));

		defaultCertificateService = Mockito.spy(new DefaultCertificateService());
		defaultCertificateService.setModelService(modelService);
		defaultCertificateService.setDestinationService(destinationService);
		defaultCertificateService.setRestTemplate(restTemplate);
		defaultCertificateService.setDestinationTargetService(destinationTargetService);
		defaultCertificateService.setTaskService(taskService);
		doReturn(x509Certificate).when(defaultCertificateService).verifyCredential(anyString(), any(), any());
		doNothing().when(defaultCertificateService).scheduleCertificateRenewalTask(any(), any());
		Mockito.when(x509Certificate.getTBSCertificate()).thenReturn(new byte[3]);

		final ConsumedCertificateCredentialModel consumedCertificateCredential = modelService
				.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId("kyma-cert");

		final AbstractDestinationModel eventsDestination = destinationService
				.getDestinationByIdAndByDestinationTargetId(DEFAULT_EVENTS_DESTINATION_ID, KYMA_DEFAULT_TARGET);
		final AbstractDestinationModel servicesDestination = destinationService
				.getDestinationByIdAndByDestinationTargetId(DEFAULT_SERVICES_DESTINATION_ID, KYMA_DEFAULT_TARGET);
		final AbstractDestinationModel renewalDestination = destinationService
				.getDestinationByIdAndByDestinationTargetId(DEFAULT_RENEWAL_SERVICE_ID, KYMA_DEFAULT_TARGET);
		eventsDestination.setCredential(consumedCertificateCredential);
		servicesDestination.setCredential(consumedCertificateCredential);
		renewalDestination.setCredential(consumedCertificateCredential);

		validationService.reloadValidationEngine();

		expectedException.expectCause(allOf(instanceOf(ModelSavingException.class)));

		defaultCertificateService.retrieveCertificate(testUri, consumedCertificateCredential);
	}

	@Test
	public void testRetrieveCertificateWithInvalidPublicKeyAlgorithm() throws CredentialException
	{
		final URI testUri = URI.create(CSR_TEST_URL);
		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON_INVALID_PUBLICKEY_ALGORITHM, MediaType.APPLICATION_JSON));

		mockServer.expect(times(1), requestTo(CRT_TEST_URL)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(generateJsonForCertificate(ENCODED_VALID_TEST_CERTIFICATE), MediaType.APPLICATION_JSON));

		final ConsumedCertificateCredentialModel consumedCertificateCredential = modelService
				.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId(TEST_CERTIFICATE);

		expectedException.expect(CredentialException.class);
		expectedException.expectMessage("Credential verification is failed. Public key algorithm is not valid");

		defaultCertificateService.retrieveCertificate(testUri, consumedCertificateCredential);
	}

	@Test
	public void testRetrieveCertificateWithInvalidSubject() throws CredentialException
	{
		final URI testUri = URI.create(CSR_TEST_URL);
		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON_INVALID_SUBJECT, MediaType.APPLICATION_JSON));

		mockServer.expect(times(1), requestTo(CRT_TEST_URL)).andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(generateJsonForCertificate(ENCODED_VALID_TEST_CERTIFICATE), MediaType.APPLICATION_JSON));

		final ConsumedCertificateCredentialModel consumedCertificateCredential = modelService
				.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId(TEST_CERTIFICATE);

		expectedException.expect(CredentialException.class);
		expectedException.expectMessage("Credential verification is failed. Certificate subject is not valid");

		defaultCertificateService.retrieveCertificate(testUri, consumedCertificateCredential);
	}

	@Test
	public void testCertificateCreationDataRetrieve() throws CredentialException
	{
		final URI testUri = URI.create(GETINFO_TEST_URL);
		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplate).build();

		mockServer.expect(times(1), requestTo(testUri)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(TEST_JSON_GETINFO_RESPONSE, MediaType.APPLICATION_JSON));

		final ConsumedCertificateCredentialModel consumedCertificateCredential = modelService
				.create(ConsumedCertificateCredentialModel.class);
		consumedCertificateCredential.setId(TEST_CERTIFICATE);

		final KymaCertificateCreation certData = defaultCertificateService.getCertificateCreationData(GETINFO_TEST_URL,
				restTemplate);
		assertEquals(KEY_SUBJECT, certData.getSubject());
		assertEquals(KEY_ALG, certData.getKeyAlgorithm());
	}
}
