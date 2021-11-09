/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.SAP_PASSPORT_HEADER_NAME;
import static de.hybris.platform.outboundservices.constants.OutboundservicesConstants.OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME;

import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.MonitoredRequestErrorModel;
import de.hybris.platform.integrationservices.monitoring.MonitoredRequestErrorParser;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator;
import de.hybris.platform.outboundservices.model.OutboundRequestModel;
import de.hybris.platform.outboundservices.monitoring.OutboundMonitoringException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class DefaultOutboundMonitoringRequestDecorator implements OutboundRequestDecorator
{
	private static final Logger LOG = Log.getLogger(DefaultOutboundMonitoringRequestDecorator.class);

	private ModelService modelService;
	private MonitoredRequestErrorParser<MonitoredRequestErrorModel> exceptionErrorParser;

	@Override
	public HttpEntity<Map<String, Object>> decorate(final HttpHeaders httpHeaders, final Map<String, Object> payload,
													final DecoratorContext context, final DecoratorExecution execution)
	{
		final String messageId = UUID.randomUUID().toString();
		httpHeaders.add(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME, messageId);

		final HttpEntity<Map<String, Object>> httpEntity = extractHttpEntityFrom(httpHeaders, payload, context, execution);

		final String sapPassport = extractSapPassportFrom(httpEntity, context);
		final String integrationKey = extractIntegrationKey(httpEntity);
		saveOutboundRequest(messageId, sapPassport, integrationKey, IntegrationRequestStatus.ERROR, context);

		return httpEntity;
	}

	/**
	 * Extracts SAP-PASSPORT from the HTTP entity, to be associated with the {@link de.hybris.platform.inboundservices.model.InboundRequestModel}.
	 * If SAP-PASSPORT is not present, then an ERROR {@code InboundRequestModel} is persisted for the entity.
	 * @param httpEntity an entity to extract SAP-PASSPORT from.
	 * @param context context for the HTTP entity
	 * @return SAP-PASSPORT value
	 * @throws OutboundMonitoringException when SAP-PASSPORT value is not found in the entity
	 */
	protected String extractSapPassportFrom(final HttpEntity<Map<String, Object>> httpEntity, final DecoratorContext context)
	{
		try
		{
			final List<String> list = httpEntity.getHeaders().get(SAP_PASSPORT_HEADER_NAME);
			if (CollectionUtils.isEmpty(list) || StringUtils.isEmpty(list.get(0)))
			{
				throw new OutboundMonitoringException(String.format("No %s header present in request.", SAP_PASSPORT_HEADER_NAME));
			}
			return list.get(0);
		}
		catch(final OutboundMonitoringException e)
		{
			final String messageId = httpEntity.getHeaders().getFirst(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME);
			final String integrationKey = extractIntegrationKey(httpEntity);
			saveOutboundRequest(messageId, null, integrationKey, context, e);
			throw e;
		}
	}

	protected HttpEntity<Map<String, Object>> extractHttpEntityFrom(final HttpHeaders httpHeaders,
			final Map<String, Object> payload, final DecoratorContext context, final DecoratorExecution execution)
	{
		// because of the decorator framework, after getting the value from the execution chain, the SAP-PASSPORT
		// and other headers should be present.
		try
		{
			return execution.createHttpEntity(httpHeaders, payload, context);
		}
		catch (final RuntimeException e)
		{
			final String messageId = httpHeaders.getFirst(OUTBOUND_MONITORING_MESSAGE_ID_HEADER_NAME);
			final String sapPassport = httpHeaders.getFirst(SAP_PASSPORT_HEADER_NAME);
			final String integrationKey = extractIntegrationKey(new HttpEntity<>(payload));
			saveOutboundRequest(messageId, sapPassport, integrationKey, context, e);
			throw e;
		}
	}

	/**
	 * @deprecated what done in this method is done in {@link #saveOutboundRequest(String, String, String, DecoratorContext, Throwable)}
	 * but without the second update of the {@code OutboundRequestModel}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected void updateOutboundRequestWithException(final OutboundRequestModel outboundRequestModel, final Throwable t)
	{
		//this is empty, because this method is deprecated and no longer used.
	}

	protected String extractError(final Throwable t)
	{
		return getExceptionErrorParser().parseErrorFrom(MonitoredRequestErrorModel.class, -1, t.getMessage()).getMessage();
	}

	/**
	 * Creates and persists {@link OutboundRequestModel} with the specified parameters.
	 * @param messageId value for {@link OutboundRequestModel#setMessageId(String)}
	 * @param sapPassport value for {@link OutboundRequestModel#setSapPassport(String)}
	 * @param integrationKey value for {@link OutboundRequestModel#setIntegrationKey(String)}
	 * @param status value for {@link OutboundRequestModel#setStatus(IntegrationRequestStatus)}
	 * @param context context to derive other {@link OutboundRequestModel} properties from.
	 * @return persisted outbound request
	 */
	protected OutboundRequestModel saveOutboundRequest(final String messageId, final String sapPassport,
													   final String integrationKey, final IntegrationRequestStatus status,
													   final DecoratorContext context)
	{
		return saveOutboundRequest(messageId, sapPassport, integrationKey, status, context, null);
	}

	/**
	 * Creates and persists {@link OutboundRequestModel} having {@link IntegrationRequestStatus#ERROR} and the specified parameters.
	 * @param messageId value for {@link OutboundRequestModel#setMessageId(String)}
	 * @param sapPassport value for {@link OutboundRequestModel#setSapPassport(String)}
	 * @param integrationKey value for {@link OutboundRequestModel#setIntegrationKey(String)}
	 * @param context context to derive other {@link OutboundRequestModel} properties from.
	 * @param exception an exception that happened while processing the outbound request.
	 * @return persisted outbound request
	 */
	protected OutboundRequestModel saveOutboundRequest(final String messageId, final String sapPassport,
			final String integrationKey, final DecoratorContext context,
			final Throwable exception)
	{
		final OutboundRequestModel request = saveOutboundRequest(messageId, sapPassport, integrationKey, IntegrationRequestStatus.ERROR, context, extractError(exception));
		updateOutboundRequestWithException(request, exception);
		return request;
	}

	private OutboundRequestModel saveOutboundRequest(final String messageId, final String sapPassport,
													   final String integrationKey, final IntegrationRequestStatus status,
													   final DecoratorContext context, final String error)
	{
		final OutboundRequestModel outboundRequest = getModelService().create(OutboundRequestModel.class);
		outboundRequest.setMessageId(messageId);
		outboundRequest.setDestination(context.getDestinationModel().getUrl());
		outboundRequest.setSapPassport(sapPassport);
		outboundRequest.setIntegrationKey(integrationKey);
		outboundRequest.setStatus(status);
		outboundRequest.setType(context.getIntegrationObjectCode());
		outboundRequest.setError(error);
		outboundRequest.setSource(context.getSource());
		getModelService().save(outboundRequest);
		return outboundRequest;
	}

	/**
	 * Extracts integration key value from the HTTP entity, to be associated with the {@link de.hybris.platform.inboundservices.model.InboundRequestModel}.
	 * @param httpEntity an entity to extract SAP-PASSPORT from.
	 * @return integration key value of {@code null}, if the payload does not contain integration key.
	 */
	protected String extractIntegrationKey(final HttpEntity<Map<String, Object>> httpEntity)
	{
		final Map<String, Object> payload = httpEntity.getBody();
		if (containsIntegrationKey(payload))
		{
			final String value = (String) payload.get(INTEGRATION_KEY_PROPERTY_NAME);
			return decode(value);
		}
		LOG.warn("No integrationKey was present in payload.");
		return null;
	}

	/**
	 * Decodes integration key value.
	 * @param value value of the integration key in the payload, which might be encoded.
	 * @return a decoded integration key value
	 */
	protected String decode(final String value)
	{
		return URLDecoder.decode(value, StandardCharsets.UTF_8);
	}

	private boolean containsIntegrationKey(final Map<String, Object> payload)
	{
		return payload != null && payload.containsKey(INTEGRATION_KEY_PROPERTY_NAME) &&
				! StringUtils.isEmpty((String) payload.get(INTEGRATION_KEY_PROPERTY_NAME));
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected MonitoredRequestErrorParser<MonitoredRequestErrorModel> getExceptionErrorParser()
	{
		return exceptionErrorParser;
	}

	@Required
	public void setExceptionErrorParser(final MonitoredRequestErrorParser<MonitoredRequestErrorModel> exceptionErrorParser)
	{
		this.exceptionErrorParser = exceptionErrorParser;
	}
}
