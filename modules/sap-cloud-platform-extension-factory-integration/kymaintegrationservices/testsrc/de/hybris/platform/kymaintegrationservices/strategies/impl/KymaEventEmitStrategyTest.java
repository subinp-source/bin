/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;

import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.DEFAULT_VALIDATION_ERROR;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENT_RETRY_DELAY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.VALIDATION_ERROR_KEY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaHttpHelper.getDefaultHeaders;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.codec.binary.StringUtils.getBytesUtf8;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.EventDlqService;
import de.hybris.platform.kymaintegrationservices.dto.JsonPublishRequestData;
import de.hybris.platform.kymaintegrationservices.dto.PublishRequestData;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MutableMessage;
import org.springframework.integration.support.MutableMessageHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


@IntegrationTest
public class KymaEventEmitStrategyTest extends ServicelayerTransactionalTest
{
	private static final String URL = "https://localhost:8081/v1/events";
	private static final String EXCEPTION_MESSAGE = "msg";

	private KymaEventEmitStrategy kymaEventEmitStrategy;

	@Mock
	private RestTemplateWrapper restTemplateWrapper;

	@Mock
	private RestTemplate restTemplate;

	@Resource
	private DestinationService destinationService;

	@Resource
	private EventDlqService eventDlqService;

	private EventDlqService eventDlqServiceSpy;

	@Resource(name = "kymaExportJacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@Resource
	private ModelService modelService;

	@Resource
	private TaskService taskService;

	private TaskService taskServiceSpy;

	@Resource(name = "kymaChannel")
	private MessageChannel messageChannel;

	private PublishRequestData data;
	private MessageHeaders headers;
	private HttpEntity<String> request;

	@Resource
	private Converter<PublishRequestData, JsonPublishRequestData> kymaJsonEventConverter;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		eventDlqServiceSpy = spy(eventDlqService);
		taskServiceSpy = spy(taskService);

		kymaEventEmitStrategy = new KymaEventEmitStrategy();
		kymaEventEmitStrategy.setDestinationService(destinationService);
		kymaEventEmitStrategy.setEventChannel(messageChannel);
		kymaEventEmitStrategy.setEventDlqService(eventDlqServiceSpy);
		kymaEventEmitStrategy.setJacksonObjectMapper(jacksonObjectMapper);
		kymaEventEmitStrategy.setModelService(modelService);
		kymaEventEmitStrategy.setTaskService(taskServiceSpy);
		kymaEventEmitStrategy.setRestTemplateWrapper(restTemplateWrapper);
		kymaEventEmitStrategy.setKymaJsonEventConverter(kymaJsonEventConverter);

		importCsv("/test/apiConfigurations.impex", "UTF-8");

		data = new PublishRequestData();
		data.setEventType("type");
		data.setEventTypeVersion("v1");
		data.setData(new HashMap<>());
		data.setEventId(UUID.randomUUID().toString());
		data.setEventTime("2002-10-02T10:00:00-05:00");
		data.setDestinationTargetId("kymaDefaultTarget");
		headers = new MutableMessageHeaders(null);

		when(restTemplateWrapper.getRestTemplate(any())).thenReturn(restTemplate);
		final HttpHeaders httpHeaders = getDefaultHeaders();
		final JsonPublishRequestData jsonPublishRequestData = kymaJsonEventConverter.convert(data);
		request = new HttpEntity(jsonPublishRequestData, httpHeaders);
		when(restTemplate.postForEntity(any(String.class), any(), eq(String.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));

		Config.setParameter(EVENTS_SERVICE_ID, "kyma-events");
	}

	@Test
	public void sendEvent()
	{
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(restTemplate).postForEntity(URL, request, String.class);
	}

	@Test
	public void invalidMessage() throws CredentialException
	{
		kymaEventEmitStrategy.sendEvent(null);
		kymaEventEmitStrategy.sendEvent("");
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>("", null));
		verify(restTemplateWrapper, never()).getRestTemplate(any());
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidEventDestination()
	{
		Config.setParameter(EVENTS_SERVICE_ID, "kyma-events-missed");
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
	}

	@Test
	public void invalidEventDestinationNoCredentials() throws CredentialException
	{
		Config.setParameter(EVENTS_SERVICE_ID, "kyma-events-no-credentials");
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(restTemplateWrapper, never()).getRestTemplate(any());
	}

	@Test
	public void sendEventFailedWithPayloadException() throws CredentialException
	{
		final String validationErrorMessage = Config.getString(VALIDATION_ERROR_KEY, DEFAULT_VALIDATION_ERROR);
		final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "",
				getBytesUtf8("{\"type\":\"" + validationErrorMessage + "\"}"), UTF_8);
		when(restTemplateWrapper.getRestTemplate(any())).thenThrow(exception);
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(eventDlqServiceSpy, timeout(10000).times(1)).sendToQueue(any());
		verify(taskServiceSpy, never()).scheduleTask(any());
	}

	@Test
	public void sendEventFailedWithPayloadExceptionNoType() throws CredentialException
	{
		final String validationErrorMessage = Config.getString(VALIDATION_ERROR_KEY, DEFAULT_VALIDATION_ERROR);
		final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "",
				getBytesUtf8("{\"no_type\":\"" + validationErrorMessage + "\"}"), UTF_8);
		when(restTemplateWrapper.getRestTemplate(any())).thenThrow(exception);
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(eventDlqServiceSpy, timeout(10000).times(0)).sendToQueue(any());
	}

	@Test
	public void sendEventFailedWithPayloadExceptionNotFound() throws CredentialException
	{
		final String validationErrorMessage = Config.getString(VALIDATION_ERROR_KEY, DEFAULT_VALIDATION_ERROR);
		final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "",
				getBytesUtf8("{\"type\":\"" + validationErrorMessage + "\"}"), UTF_8);
		when(restTemplateWrapper.getRestTemplate(any())).thenThrow(exception);
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(eventDlqServiceSpy, timeout(10000).times(0)).sendToQueue(any());
	}

	@Test
	public void sendEventFailedWithConnectExceptionConsecutiveRetrySuccess() throws CredentialException
	{
		when(restTemplateWrapper.getRestTemplate(any()))
				.thenThrow(new RestClientException(EXCEPTION_MESSAGE, new ConnectException())).thenReturn(restTemplate);
		sendEvent();
	}

	@Test
	public void sendEventFailedWithCredentialExceptionConsecutiveRetrySuccess() throws CredentialException
	{
		doThrow(new CredentialException(EXCEPTION_MESSAGE)).when(restTemplateWrapper).getRestTemplate(any());
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(restTemplate, never()).postForEntity(URL, request, String.class);
	}

	@Test
	public void sendEventFailedWithConnectExceptionRetryTaskTriggered() throws CredentialException
	{
		when(restTemplateWrapper.getRestTemplate(any()))
				.thenThrow(new RestClientException(EXCEPTION_MESSAGE, new ConnectException()));
		final ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
		final Date taskExecutionDate = new Date(System.currentTimeMillis() + Config.getInt(EVENT_RETRY_DELAY, 5000));
		kymaEventEmitStrategy.sendEvent(new MutableMessage<>(data, headers));
		verify(taskServiceSpy, atLeast(1)).scheduleTask(taskCaptor.capture());
		assertTrue(taskCaptor.getValue().getExecutionDate().after(taskExecutionDate));
	}


}

