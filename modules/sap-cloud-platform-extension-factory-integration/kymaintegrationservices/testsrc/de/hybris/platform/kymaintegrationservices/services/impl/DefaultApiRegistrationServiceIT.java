/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;

import static de.hybris.platform.apiregistryservices.enums.DestinationChannel.KYMA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.impl.DefaultApiRegistrationService;
import de.hybris.platform.apiregistryservices.strategies.ApiRegistrationStrategy;
import de.hybris.platform.kymaintegrationservices.strategies.impl.KymaApiRegistrationStrategy;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


@IntegrationTest
public class DefaultApiRegistrationServiceIT extends ServicelayerTransactionalTest
{
	private static final String URL = "https://localhost:8081/v1/metadata/services";
	private static final String KYMA_DEFAULT_TARGET = "kymaDefaultTarget";
	private static final String TEST_DEPLOYMENT_END_POINT="deployment.end.point.test.scope";
	private static final String TEST_DEPLOYMENT_END_POINT_URL="https://some.url";
	@Resource(name = "destinationService")
	private DestinationService<AbstractDestinationModel> destinationService;

	@Resource(name = "kymaDestinationRestTemplateWrapper")
	private RestTemplateWrapper restTemplate;

	private RestTemplateWrapper restTemplateSpy;

	@Resource(name = "kymaApiRegistrationStrategy")
	private KymaApiRegistrationStrategy apiRegistrationStrategy;

	private DefaultApiRegistrationService apiRegistrationService;

	@Before
	public void setUp() throws Exception
	{
		Map<DestinationChannel, ApiRegistrationStrategy> apiRegistrationStrategyMap;

		restTemplateSpy = spy(restTemplate);

		importCsv("/test/apiConfigurations.impex", "UTF-8");

		apiRegistrationStrategyMap = new HashMap();
		apiRegistrationStrategyMap.put(KYMA, apiRegistrationStrategy);

		apiRegistrationService = new DefaultApiRegistrationService();
		apiRegistrationService.setDestinationService(destinationService);

		apiRegistrationService.setApiRegistrationStrategyMap(apiRegistrationStrategyMap);
		apiRegistrationStrategy.setRestTemplate(restTemplateSpy);

		Config.setParameter(TEST_DEPLOYMENT_END_POINT, TEST_DEPLOYMENT_END_POINT_URL);
	}

	@Test
	public void testRegisterWebservices() throws ApiRegistrationException, CredentialException
	{
	   Mockito.doReturn(new RestTemplate()).when(restTemplateSpy).getRestTemplate(any());

		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplateSpy.getRestTemplate(any())).build();

		final List<ExposedDestinationModel> models = destinationService.getDestinationsByDestinationTargetId(KYMA_DEFAULT_TARGET).stream()
			.filter(ExposedDestinationModel.class::isInstance).map(ExposedDestinationModel.class::cast)
			.collect(Collectors.toList());
		assertFalse(CollectionUtils.isEmpty(models));

		final ExposedDestinationModel destinationModel = models.get(0);
		destinationModel.setTargetId(null);
		destinationModel.getEndpoint().setDescription(null);

		mockServer.expect(times(1), requestTo(URL)).andExpect(method(HttpMethod.POST))
			.andExpect(clientHttpRequest -> {
				final MockClientHttpRequest mockRequest = (MockClientHttpRequest) clientHttpRequest;
				assertFalse("Should be no null description in result ", mockRequest.getBodyAsString().contains("description"));
			})
			.andRespond(withSuccess());

		assertEquals(KYMA, destinationModel.getDestinationTarget().getDestinationChannel());

		apiRegistrationService.registerExposedDestination(destinationModel);
	}


	@Test
	public void testUpdateRegisteredWebservices() throws ApiRegistrationException, CredentialException
	{
		Mockito.doReturn(new RestTemplate()).when(restTemplateSpy).getRestTemplate(any());

		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplateSpy.getRestTemplate(any())).build();

		final List<ExposedDestinationModel> models = destinationService.getDestinationsByDestinationTargetId(KYMA_DEFAULT_TARGET).stream()
				.filter(ExposedDestinationModel.class::isInstance).map(ExposedDestinationModel.class::cast)
				.collect(Collectors.toList());
		assertFalse(CollectionUtils.isEmpty(models));

		final ExposedDestinationModel destinationModel = models.get(0);
		destinationModel.setTargetId(KYMA_DEFAULT_TARGET);

		mockServer.expect(times(1), requestTo(URL + "/" + KYMA_DEFAULT_TARGET)).andExpect(method(HttpMethod.PUT))
				.andRespond(withNoContent());

		assertEquals(KYMA, destinationModel.getDestinationTarget().getDestinationChannel());

		apiRegistrationService.registerExposedDestination(destinationModel);
	}

	@Test
	public void testUnregisterWebservices() throws ApiRegistrationException, CredentialException
	{
		Mockito.doReturn(new RestTemplate()).when(restTemplateSpy).getRestTemplate(any());

		final MockRestServiceServer mockServer;
		mockServer = MockRestServiceServer.bindTo(restTemplateSpy.getRestTemplate(any())).build();

		final List<ExposedDestinationModel> models = destinationService.getDestinationsByDestinationTargetId(KYMA_DEFAULT_TARGET).stream()
				.filter(ExposedDestinationModel.class::isInstance).map(ExposedDestinationModel.class::cast)
				.collect(Collectors.toList());
		assertFalse(CollectionUtils.isEmpty(models));

		final ExposedDestinationModel destinationModel = models.get(0);
		destinationModel.setTargetId(KYMA_DEFAULT_TARGET);

		mockServer.expect(times(1), requestTo(URL + "/" + KYMA_DEFAULT_TARGET)).andExpect(method(HttpMethod.DELETE))
				.andRespond(withNoContent());

		assertEquals(KYMA, destinationModel.getDestinationTarget().getDestinationChannel());
		apiRegistrationService.unregisterExposedDestination(destinationModel);

		assertTrue(StringUtils.isEmpty(destinationModel.getTargetId()));
	}


}
