/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.kymaintegrationservices.services.SSLContextFactoryService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class RestTemplateWrapperTest extends ServicelayerTransactionalTest
{
	private static final String DESTINATION_ID = "testDestination";
	private static final String KYMA_DEFAULT = "kymaDefault";

	private static final String DESTINATION_ID1 = "testDestination1";
	private static final String KYMA_DEFAULT1 = "kymaDefault1";

	private static final String DESTINATION_ID2 = "testDestination2";
	private static final String KYMA_DEFAULT2 = "kymaDefault2";

	@Resource(name = "kymaExportRestTemplate")
	private RestTemplate restTemplate;

	@Resource
	private DestinationService<AbstractDestinationModel> destinationService;

	@Resource(name = "defaultMessageConverters")
	private List<HttpMessageConverter<Object>> messageConverters;

	@Resource
	private SSLContextFactoryService sslContextFactoryService;

	private RestTemplateWrapper restTemplateWrapper;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/certificate.impex", "UTF-8");

		restTemplateWrapper = new RestTemplateWrapper();
		restTemplateWrapper.setMessageConverters(messageConverters);
		restTemplateWrapper.setRestTemplate(restTemplate);
		restTemplateWrapper.setDestinationService(destinationService);
		restTemplateWrapper.setSslContextFactoryService(sslContextFactoryService);
	}

	@Test
	public void testUpdateCredentials()
	{
		assertTrue(restTemplateWrapper.getUpdatedRestTemplate().getRequestFactory() != null);
	}

	@Test
	public void testUpdateCredentialsWithParameters() throws CredentialException
	{
		final AbstractDestinationModel apiDestination = destinationService.getDestinationByIdAndByDestinationTargetId(DESTINATION_ID,
				KYMA_DEFAULT);
		assertTrue(restTemplateWrapper.getRestTemplate(apiDestination.getCredential()).getRequestFactory() != null);
	}

	@Test
	public void multipleRestTemplatesForDifferentCredentials() throws CredentialException
	{
		final AbstractDestinationModel apiDestination = destinationService.getDestinationByIdAndByDestinationTargetId(DESTINATION_ID,
				KYMA_DEFAULT);

		final AbstractDestinationModel apiDestination1 = destinationService.getDestinationByIdAndByDestinationTargetId(DESTINATION_ID1,
				KYMA_DEFAULT1);

		final AbstractDestinationModel apiDestination2 = destinationService.getDestinationByIdAndByDestinationTargetId(DESTINATION_ID2,
				KYMA_DEFAULT2);

		final RestTemplate restTemplate = restTemplateWrapper.getRestTemplate(apiDestination.getCredential());
		assertTrue(restTemplate != null);
		assertTrue(restTemplate.getRequestFactory() != null);

		final RestTemplate restTemplate1 = restTemplateWrapper.getRestTemplate(apiDestination1.getCredential());
		assertTrue(restTemplate1 != null);
		assertTrue(restTemplate1.getRequestFactory() != null);

		final RestTemplate restTemplate2 = restTemplateWrapper.getRestTemplate(apiDestination2.getCredential());
		assertTrue(restTemplate2 != null);
		assertTrue(restTemplate2.getRequestFactory() != null);

		assertNotEquals(restTemplate.getRequestFactory(), restTemplate1.getRequestFactory());
		assertNotEquals(restTemplate1.getRequestFactory(), restTemplate2.getRequestFactory());
		assertNotEquals(restTemplate2.getRequestFactory(), restTemplate1.getRequestFactory());


	}
}
