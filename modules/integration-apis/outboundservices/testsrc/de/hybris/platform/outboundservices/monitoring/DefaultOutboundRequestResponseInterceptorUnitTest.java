/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundservices.monitoring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;
import de.hybris.platform.integrationservices.model.MonitoredRequestErrorModel;
import de.hybris.platform.integrationservices.monitoring.MonitoredRequestErrorParser;
import de.hybris.platform.integrationservices.service.MediaPersistenceService;
import de.hybris.platform.integrationservices.util.timeout.IntegrationExecutionException;
import de.hybris.platform.integrationservices.util.timeout.IntegrationTimeoutException;
import de.hybris.platform.integrationservices.util.timeout.TimeoutService;
import de.hybris.platform.outboundservices.config.OutboundServicesConfiguration;
import de.hybris.platform.outboundservices.model.OutboundRequestMediaModel;
import de.hybris.platform.outboundservices.model.OutboundRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import com.google.common.collect.Lists;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOutboundRequestResponseInterceptorUnitTest
{
	private static final String OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME = "X-OutboundMonitoring-MessageId";
	private static final String MY_MESSAGE_ID = "MY_MESSAGE_ID";
	private static final String RESPONSE_PAYLOAD = "{ error = 'error' }";
	private static final String REQUEST_PAYLOAD = "my_payload";
	private static final byte[] REQUEST_PAYLOAD_BYTES = REQUEST_PAYLOAD.getBytes(StandardCharsets.UTF_8);
	private static final String ERROR_MESSAGE = "My error message";
	private final List<MonitoredRequestErrorParser<MonitoredRequestErrorModel>> errorParsers = Lists.newArrayList();
	private final HttpHeaders httpHeadersRequest = new HttpHeaders();
	private final HttpHeaders httpHeadersResponse = new HttpHeaders();
	private static final long TIMEOUT = 10;

	@Mock
	private TimeoutService timeoutService;
	@Mock
	private ModelService modelService;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private MediaPersistenceService mediaPersistenceService;
	@Mock
	private OutboundServicesConfiguration outboundServicesConfiguration;
	@InjectMocks
	private DefaultOutboundRequestResponseInterceptor interceptor;
	@Mock
	private ClientHttpRequestExecution requestExecution;
	@Mock
	private HttpRequest httpRequest;
	@Mock
	private OutboundRequestModel outboundRequestModel;
	@Mock
	private ClientHttpResponse httpResponse;
	@Mock
	private OutboundRequestMediaModel requestMediaModel;
	@Mock
	private MonitoredRequestErrorParser<MonitoredRequestErrorModel> errorParser;
	@Mock
	private MonitoredRequestErrorParser<MonitoredRequestErrorModel> fallbackErrorParser;
	@Mock
	private MonitoredRequestErrorParser<MonitoredRequestErrorModel> exceptionErrorParser;
	@Mock
	private MonitoredRequestErrorModel errorModel;
	
	@Before
	public void setUp() throws IOException
	{
		when(httpRequest.getHeaders()).thenReturn(httpHeadersRequest);
		when(modelService.create(OutboundRequestModel.class)).thenReturn(outboundRequestModel);

		final SearchResult<OutboundRequestModel> result = searchResult(outboundRequestModel);
		when(flexibleSearchService.search(anyString(), anyMap())).thenReturn(result);
		when(requestExecution.execute(any(), any())).thenReturn(httpResponse);
		when(timeoutService.execute(any(Callable.class), eq(TIMEOUT))).thenReturn(httpResponse);
		when(outboundServicesConfiguration.getRequestExecutionTimeout()).thenReturn(TIMEOUT);
		when(mediaPersistenceService.persistMedias(any(), any())).thenReturn(Collections.singletonList(requestMediaModel));
		when(httpResponse.getHeaders()).thenReturn(httpHeadersResponse);
		when(httpResponse.getRawStatusCode()).thenReturn(200);
		when(httpResponse.getBody()).thenReturn(new ByteArrayInputStream("{ error = 'error' }".getBytes()));

		when(errorModel.getMessage()).thenReturn(ERROR_MESSAGE);
		when(errorParser.isApplicable(any(), anyInt())).thenReturn(true);
		when(errorParser.parseErrorFrom(any(), anyInt(), any())).thenReturn(errorModel);

		when(fallbackErrorParser.isApplicable(any(), anyInt())).thenReturn(true);
		when(fallbackErrorParser.parseErrorFrom(any(), anyInt(), any())).thenReturn(errorModel);

		when(exceptionErrorParser.isApplicable(any(), anyInt())).thenReturn(true);
		when(exceptionErrorParser.parseErrorFrom(any(), anyInt(), any())).thenReturn(errorModel);

		interceptor.setErrorParsers(errorParsers);
		interceptor.setTimeoutService(timeoutService);

		when(outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()).thenReturn(true);
		when(outboundServicesConfiguration.isPayloadRetentionForSuccessEnabled()).thenReturn(true);
		when(outboundServicesConfiguration.isMonitoringEnabled()).thenReturn(true);
		when(outboundServicesConfiguration.getMaximumResponsePayloadSize()).thenReturn(1024);
	}

	@Test
	public void testIntercept_success() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(200);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.SUCCESS);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void testIntercept_success_successRetentionFalse() throws IOException
	{
		when(outboundServicesConfiguration.isPayloadRetentionForSuccessEnabled()).thenReturn(false);

		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(200);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.SUCCESS);
		verify(outboundRequestModel, never()).setPayload(any());
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService, never()).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void testIntercept_error_fallbackErrorParser() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(fallbackErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, 400, RESPONSE_PAYLOAD);
		assertThat(response).isNotSameAs(httpResponse); // response was wrapped into another class.
	}

	@Test
	public void testIntercept_error_errorParser() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		errorParsers.add(errorParser);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(errorParser).parseErrorFrom(MonitoredRequestErrorModel.class, 400, RESPONSE_PAYLOAD);
		assertThat(response).isNotSameAs(httpResponse); // response was wrapped into another class.
	}

	@Test
	public void testIntercept_error_errorParsersNotApplicable() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		errorParsers.add(errorParser);
		when(errorParser.isApplicable(any(), anyInt())).thenReturn(false);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(fallbackErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, 400, RESPONSE_PAYLOAD);
		assertThat(response).isNotSameAs(httpResponse); // response was wrapped into another class.
	}

	@Test
	public void testIntercept_error_errorRetentionFalse() throws IOException
	{
		when(outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()).thenReturn(false);

		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		errorParsers.add(errorParser);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel, never()).setPayload(any());
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService, never()).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(errorParser).parseErrorFrom(MonitoredRequestErrorModel.class, 400, RESPONSE_PAYLOAD);
		assertThat(response).isNotSameAs(httpResponse); // response was wrapped into another class.
	}

	@Test
	public void testInterceptIntegrationTimeoutExceptionWhenIssuingRequest() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		when(timeoutService.execute(any(Callable.class), eq(TIMEOUT))).thenThrow(new IntegrationTimeoutException(TIMEOUT));

		assertThatThrownBy(() -> interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution))
				.isInstanceOf(OutboundRequestTimeoutException.class)
				.hasMessage("Request timed out after 10 ms.");

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(exceptionErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, -1, "Request timed out after 10 ms.");
	}

	@Test
	public void testInterceptIntegrationTimeoutExceptionWhenIssuingRequestErrorRetentionFalse() throws IOException
	{
		when(outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()).thenReturn(false);

		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		when(timeoutService.execute(any(Callable.class), eq(TIMEOUT))).thenThrow(new IntegrationTimeoutException(TIMEOUT));

		assertThatThrownBy(() -> interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution))
				.isInstanceOf(OutboundRequestTimeoutException.class)
				.hasMessage("Request timed out after 10 ms.");

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel, never()).setPayload(any());  // No payload generated
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService, never()).persistMedias(any(), any());
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(exceptionErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, -1, "Request timed out after 10 ms.");
	}
	
	@Test
	public void testInterceptExecutionExceptionWhenIssuingRequest() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		when(timeoutService.execute(any(Callable.class), eq(TIMEOUT))).thenThrow(new IntegrationExecutionException(new IOException("IOException msg")));

		assertThatThrownBy(() -> interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution))
				.isInstanceOf(OutboundRequestExecutionException.class)
				.hasMessage("Request encountered an exception.");

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel).setPayload(requestMediaModel);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(exceptionErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, -1, "Request encountered an exception.");
	}

	@Test
	public void testInterceptExecutionExceptionWhenIssuingRequestErrorRetentionFalse() throws IOException
	{
		when(outboundServicesConfiguration.isPayloadRetentionForErrorEnabled()).thenReturn(false);

		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		when(timeoutService.execute(any(Callable.class), eq(TIMEOUT))).thenThrow(new IntegrationExecutionException(new IOException("IOException msg")));

		assertThatThrownBy(() -> interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution))
				.isInstanceOf(OutboundRequestExecutionException.class)
				.hasMessage("Request encountered an exception.");

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.ERROR);
		verify(outboundRequestModel, never()).setPayload(any());  // No payload generated
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService, never()).persistMedias(any(), any());
		verify(outboundRequestModel).setError(ERROR_MESSAGE);
		verify(exceptionErrorParser).parseErrorFrom(MonitoredRequestErrorModel.class, -1, "Request encountered an exception.");
	}

	@Test
	public void testIntercept_noMessageIdPresent() throws IOException
	{
		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		assertThat(response.getRawStatusCode()).isEqualTo(200);
		verify(outboundRequestModel, never()).setStatus(any());
		verify(outboundRequestModel, never()).setPayload(any());
	}

	@Test
	public void testIntercept_noMessageIdPresent_inCaseOfEmptyValue() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, "");
		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		assertThat(response.getRawStatusCode()).isEqualTo(200);
		verify(outboundRequestModel, never()).setStatus(any());
		verify(outboundRequestModel, never()).setPayload(any());
	}

	@Test
	public void testIntercept_noPayloadGenerated_emptyList() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(mediaPersistenceService.persistMedias(any(), any())).thenReturn(Collections.emptyList());

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.SUCCESS);
		verify(outboundRequestModel).setPayload(null);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void testIntercept_noPayloadGenerated_null() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(mediaPersistenceService.persistMedias(any(), any())).thenReturn(null);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setStatus(IntegrationRequestStatus.SUCCESS);
		verify(outboundRequestModel).setPayload(null);
		verify(modelService).save(outboundRequestModel);
		verify(mediaPersistenceService).persistMedias(any(), any());
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void testIntercept_noOutboundRequestModelPresent() throws IOException
	{
		final SearchResult<OutboundRequestModel> result = searchResult();
		when(flexibleSearchService.search(anyString(), anyMap())).thenReturn(result);
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verifyNoMoreInteractions(modelService);
		verifyNoMoreInteractions(mediaPersistenceService);
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void testIntercept_multipleOutboundRequestModelsPresent() throws IOException
	{
		final SearchResult<OutboundRequestModel> result = searchResult(outboundRequestModel,
				Mockito.mock(OutboundRequestModel.class));
		when(flexibleSearchService.search(anyString(), anyMap())).thenReturn(result);
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);

		final ClientHttpResponse response = interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verifyNoMoreInteractions(modelService);
		verifyNoMoreInteractions(mediaPersistenceService);
		verify(timeoutService).execute(any(Callable.class), eq(TIMEOUT));

		assertThat(response).isSameAs(httpResponse);
	}

	@Test
	public void test_returnPayloadExceedsMaxSize_NoContentLengthHeader() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		responsePayloadBiggerThanMaxAllowedSize();

		interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setError("The error response message exceeded the allowed size");
	}

	@Test
	public void test_returnPayloadExceedsMaxSize_ContentLengthHeaderPresent() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(outboundServicesConfiguration.getMaximumResponsePayloadSize()).thenReturn(2);
		final HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_LENGTH,"3");
		when(httpResponse.getHeaders()).thenReturn(responseHeaders);
		when(httpResponse.getRawStatusCode()).thenReturn(400);

		interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setError("The error response message exceeded the allowed size");
	}

	@Test
	public void test_returnPayloadExceedsMaxSize_ContentLengthAndTransferEncodingHeaderPresent() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		final HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_LENGTH,"50");
		responseHeaders.add(HttpHeaders.TRANSFER_ENCODING,"anyValue");
		when(httpResponse.getHeaders()).thenReturn(responseHeaders);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		responsePayloadBiggerThanMaxAllowedSize();

		interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setError("The error response message exceeded the allowed size");
	}

	@Test
	public void test_returnPayloadEqualsMaxSize_NoContentLenghHeader() throws IOException
	{
		httpHeadersRequest.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, MY_MESSAGE_ID);
		when(httpResponse.getRawStatusCode()).thenReturn(400);
		responsePayloadEqualsThanMaxAllowedSize();

		interceptor.intercept(httpRequest, REQUEST_PAYLOAD_BYTES, requestExecution);

		verify(outboundRequestModel).setError(ERROR_MESSAGE);
	}

	private void responsePayloadBiggerThanMaxAllowedSize() throws IOException
	{
		when(outboundServicesConfiguration.getMaximumResponsePayloadSize()).thenReturn(2);
		when(httpResponse.getBody()).thenReturn(new ByteArrayInputStream("abc".getBytes()));
	}

	private void responsePayloadEqualsThanMaxAllowedSize() throws IOException
	{
		when(outboundServicesConfiguration.getMaximumResponsePayloadSize()).thenReturn(2);
		when(httpResponse.getBody()).thenReturn(new ByteArrayInputStream("ab".getBytes()));
	}

	private SearchResult<OutboundRequestModel> searchResult(OutboundRequestModel... models)
	{
		final SearchResult<OutboundRequestModel> result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(new ArrayList<>(Arrays.asList(models)));
		return result;
	}
}
