/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.jobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetCronJobModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.impl.DefaultDestinationTargetService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.kymaintegrationservices.dto.KymaInfoData;
import de.hybris.platform.kymaintegrationservices.dto.KymaServicesUrls;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Collections;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;


@IntegrationTest
public class KymaUpdateConsumedDestinationsJobPerformableTest extends ServicelayerTransactionalTest
{
	public static final String EVENTS_URL = "https://eventsurl.com";
	public static final String METADATA_URL = "https://metadataurl.com";
	public static final String RENEWAL_URL = "https://renewalurl.com";
	public static final String TEMPLATE_DESTINATION_TARGET_ID = "kymaDefaultTargetGetInfo";
	private final KymaServicesUrls urls = new KymaServicesUrls();
	private final KymaInfoData kymaInfoData = new KymaInfoData();
	@Resource
	private KymaUpdateConsumedDestinationsJobPerformable destinationTargetJobPerformable;
	@Resource
	private DefaultDestinationTargetService destinationTargetService;
	@Resource
	private DestinationService destinationService;
	@Resource
    private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Mock
	private DefaultDestinationTargetService destinationTargetServiceSpy;
	@Mock
	private final RestTemplateWrapper restTemplateWrapper = mock(RestTemplateWrapper.class);
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private CronJobModel jobModel;
	@Mock
	private DestinationTargetCronJobModel job;
	private DestinationTargetModel destinationTarget;

	@Before
	public void setUp() throws ImpExException, CredentialException, ApiRegistrationException
	{
		MockitoAnnotations.initMocks(this);
		importCsv("/test/apiConfigurations-info.impex", "UTF-8");
		importCsv("/test/constraints.impex", "UTF-8");

		when(jobModel.getJob()).thenReturn(job);
		when(jobModel.getCreationtime()).thenReturn(DateTime.now().toDate());
		destinationTarget = destinationTargetService.getDestinationTargetById(TEMPLATE_DESTINATION_TARGET_ID);

		when(job.getDestinationTargetId()).thenReturn(TEMPLATE_DESTINATION_TARGET_ID);


		destinationService.getDestinationByIdAndByDestinationTargetId("kyma-getinfo", TEMPLATE_DESTINATION_TARGET_ID);

		when(restTemplateWrapper.getRestTemplate(any())).thenReturn(restTemplate);
		kymaInfoData.setUrls(urls);
		when(restTemplate.getForObject("https://localhost:8081/v1/metadata/info", KymaInfoData.class)).thenReturn(kymaInfoData);

		destinationTargetServiceSpy = spy(destinationTargetService);
		doNothing().when(destinationTargetServiceSpy).syncDestinationTargetWithRemoteSystem(any());
		doReturn(Collections.emptyMap()).when(destinationTargetServiceSpy).registerExposedDestinations(any());
		destinationTargetJobPerformable.setRestTemplate(restTemplateWrapper);
		destinationTargetJobPerformable.setDestinationTargetService(destinationTargetServiceSpy);
	}

	@Test
	public void testValidInfoUpdateCompleted() throws ApiRegistrationException
	{
		urls.setEventsUrl(EVENTS_URL);
		urls.setMetadataUrl(METADATA_URL);
		urls.setRenewCertUrl(RENEWAL_URL);

		assertEquals(CronJobResult.SUCCESS, destinationTargetJobPerformable.perform(jobModel).getResult());
		verify(destinationTargetServiceSpy, times(1)).syncDestinationTargetWithRemoteSystem(destinationTarget);
		final ConsumedDestinationModel eventsDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-events", destinationTarget.getId());
		final ConsumedDestinationModel metadataDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-services", destinationTarget.getId());
		final ConsumedDestinationModel renewalDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-renewal", destinationTarget.getId());
		assertEquals(EVENTS_URL, eventsDestination.getUrl());
		assertEquals(METADATA_URL, metadataDestination.getUrl());
		assertEquals(RENEWAL_URL, renewalDestination.getUrl());
	
		modelService.refresh(destinationTarget);
		assertEquals(RegistrationStatus.REGISTERED, destinationTarget.getRegistrationStatus());
	}

	@Test
	public void testNoUpdateIfUrlsNotUpdated() throws ApiRegistrationException
	{
		final ConsumedDestinationModel eventsDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-events", destinationTarget.getId());
		final ConsumedDestinationModel metadataDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-services", destinationTarget.getId());
		final ConsumedDestinationModel renewalDestination = (ConsumedDestinationModel) destinationService
				.getDestinationByIdAndByDestinationTargetId("kyma-renewal", destinationTarget.getId());
		eventsDestination.setUrl(EVENTS_URL);
		metadataDestination.setUrl(METADATA_URL);
		renewalDestination.setUrl(RENEWAL_URL);
		urls.setEventsUrl(EVENTS_URL);
		urls.setMetadataUrl(METADATA_URL);
		urls.setRenewCertUrl(RENEWAL_URL);

		modelService.saveAll(eventsDestination, metadataDestination, renewalDestination);
		
		assertFalse(destinationTargetJobPerformable.needUpdateConsumedDestinations(urls, destinationTarget));

		assertEquals(CronJobResult.SUCCESS, destinationTargetJobPerformable.perform(jobModel).getResult());
		verify(destinationTargetServiceSpy, times(0)).syncDestinationTargetWithRemoteSystem(destinationTarget);

		assertEquals(EVENTS_URL, eventsDestination.getUrl());
		assertEquals(METADATA_URL, metadataDestination.getUrl());
		assertEquals(RENEWAL_URL, renewalDestination.getUrl());

		modelService.refresh(destinationTarget);
	}

	@Test
	public void testInvalidInfoUpdateRegistered() throws ApiRegistrationException
	{
		testValidInfoUpdateCompleted();
		assertEquals(StringUtils.EMPTY, destinationTarget.getRegistrationStatusInfo());
		urls.setEventsUrl(null);
		urls.setMetadataUrl(StringUtils.EMPTY);
		urls.setRenewCertUrl(RENEWAL_URL);
		assertEquals(CronJobResult.FAILURE, destinationTargetJobPerformable.perform(jobModel).getResult());
		assertEquals(RegistrationStatus.REGISTERED, destinationTarget.getRegistrationStatus());
		assertEquals("Invalid response from {kyma-getinfo} endpoint", destinationTarget.getRegistrationStatusInfo());
		testValidInfoUpdateCompleted();
		assertEquals(StringUtils.EMPTY, destinationTarget.getRegistrationStatusInfo());
	}

	@Test
	public void testInvalidInfoUpdateInProgress() throws ApiRegistrationException
	{
		testInvalidInfoUpdateInProgress(null, EVENTS_URL, RENEWAL_URL);
		testInvalidInfoUpdateInProgress("", EVENTS_URL, RENEWAL_URL);
		testInvalidInfoUpdateInProgress(METADATA_URL, null, RENEWAL_URL);
		testInvalidInfoUpdateInProgress(METADATA_URL, "", RENEWAL_URL);
		testInvalidInfoUpdateInProgress(null, null, RENEWAL_URL);
		testInvalidInfoUpdateInProgress("", "", RENEWAL_URL);
		testInvalidInfoUpdateInProgress(null, "", RENEWAL_URL);
		testInvalidInfoUpdateInProgress("", null, RENEWAL_URL);
		testInvalidInfoUpdateInProgress(METADATA_URL, EVENTS_URL, null);
		testInvalidInfoUpdateInProgress(METADATA_URL, EVENTS_URL, "");
	}

	@Test
	public void testDestinationTargetInProgressAndLifespanExpired ()
	{
		urls.setEventsUrl(EVENTS_URL);
		urls.setMetadataUrl(METADATA_URL);
		urls.setRenewCertUrl(RENEWAL_URL);

		when(jobModel.getCreationtime()).thenReturn(
				DateTime.now().minusMinutes(Config.getInt(KymaUpdateConsumedDestinationsJobPerformable.CRONJOB_LIFESPAN_PROP_KEY, 20)+1)
						.toDate());

		assertEquals(CronJobResult.FAILURE, destinationTargetJobPerformable.perform(jobModel).getResult());
		assertEquals(RegistrationStatus.ERROR, destinationTarget.getRegistrationStatus());

	}

	protected void testInvalidInfoUpdateInProgress(final String eventsUrl, final String metadataUrl, final String renewalUrl) throws ApiRegistrationException
	{
		urls.setEventsUrl(eventsUrl);
		urls.setMetadataUrl(metadataUrl);
		urls.setRenewCertUrl(renewalUrl);

		assertEquals(CronJobResult.FAILURE, destinationTargetJobPerformable.perform(jobModel).getResult());
		verify(destinationTargetServiceSpy, times(0)).syncDestinationTargetWithRemoteSystem(destinationTarget);

		assertEquals(RegistrationStatus.IN_PROGRESS, destinationTarget.getRegistrationStatus());
	}


}
