/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;

import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.DATE_FORMAT_PROP;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.DEFAULT_DATE_FORMAT;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.DEFAULT_VALIDATION_ERROR;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENT_RETRY_DELAY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.MAX_CONSECUTIVE_RETRIES;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.MAX_RETRIES;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.VALIDATION_ERROR_KEY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.eventPayloadToString;
import static de.hybris.platform.kymaintegrationservices.utils.KymaHttpHelper.getDefaultHeaders;
import static java.util.Objects.isNull;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.EventDlqService;
import de.hybris.platform.apiregistryservices.strategies.EventEmitStrategy;
import de.hybris.platform.kymaintegrationservices.dto.JsonPublishRequestData;
import de.hybris.platform.kymaintegrationservices.dto.PublishRequestData;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.security.cert.CertificateRevokedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Implementation of @{@link EventEmitStrategy} for kyma event emitting with retries
 */
public class KymaEventEmitStrategy implements EventEmitStrategy
{
	private static final int DEFAULT_EVENT_RETRY_DELAY_MS = 5000;
	private static final int DEFAULT_MAX_CONSECUTIVE_RETRIES = 3;
	private static final int DEFAULT_MAX_RETRIES = 3;
	private static final Logger LOG = LoggerFactory.getLogger(KymaEventEmitStrategy.class);

	private RestTemplateWrapper restTemplateWrapper;
	private DestinationService<AbstractDestinationModel> destinationService;
	private EventDlqService eventDlqService;
	private ObjectMapper jacksonObjectMapper;
	private MessageChannel eventChannel;
	private ModelService modelService;
	private TaskService taskService;
	private Converter<PublishRequestData, JsonPublishRequestData> kymaJsonEventConverter;

	@Override
	public void sendEvent(final Object object)
	{
		if (!(object instanceof Message) || !(((Message) object).getPayload() instanceof PublishRequestData))
		{
			LOG.error("Provided payload is not instance of PublishRequestData");
			return;
		}

		final Message message = (Message) object;

		final PublishRequestData publishRequestData = (PublishRequestData) message.getPayload();

		if (Config.getInt(MAX_RETRIES, DEFAULT_MAX_RETRIES) <= 0
				|| Config.getInt(MAX_CONSECUTIVE_RETRIES, DEFAULT_MAX_CONSECUTIVE_RETRIES) <= 0)
		{
			LOG.info("Event emission is disabled as no (re)tries are configured; event '{}' with id '{}' has been dropped.",
					publishRequestData.getEventType(), publishRequestData.getEventId());
			return;
		}

		sendPublishRequestData(message, publishRequestData);
	}

	protected void sendPublishRequestData(Message message, PublishRequestData publishRequestData)
	{
		final AbstractDestinationModel destination = getEventDestination(publishRequestData.getDestinationTargetId());
		if (destination == null || StringUtils.isEmpty(destination.getUrl()) || destination.getCredential() == null)
		{
			LOG.error("The Destination for exporting events is invalid or missing.");
			return;
		}
		if (LOG.isInfoEnabled())
		{
			LOG.info(eventPayloadToString(publishRequestData));
		}

		final JsonPublishRequestData jsonPublishRequestData = getKymaJsonEventConverter().convert(publishRequestData);

		final HttpHeaders headers = getDefaultHeaders();
		final String url = destination.getUrl();
		final HttpEntity<String> request = new HttpEntity(jsonPublishRequestData, headers);

		try
		{
			final ResponseEntity<String> response = getRestTemplateWrapper().getRestTemplate(destination.getCredential())
					.postForEntity(url, request,
					String.class);

			LOG.info("Event (EventId : {}) sending response : {}", publishRequestData.getEventId(), response);
		}
		catch (final ResourceAccessException | CredentialException e)
		{
			handleFailedEvent(e, publishRequestData);
			scheduleEventEmitTask(message, publishRequestData);
		}
		catch (final RestClientException e)
		{
			handleFailedEvent(e, publishRequestData);
			if (shouldSendToDlq(e))
			{
				sendToDlq(publishRequestData, destination.getDestinationTarget(),
						((HttpClientErrorException) e).getResponseBodyAsString());
			}
			else if (!retryEventEmitting(request, publishRequestData, url, destination))
			{
				scheduleEventEmitTask(message, publishRequestData);
			}
		}
	}

	protected void handleFailedEvent(final Exception e, final PublishRequestData publishRequestData)
	{
		LOG.error("Cannot send event with ID {} to {}, event type : {}, cause: {}", publishRequestData.getEventId(),
				getTargetName(),
				publishRequestData.getEventType(),
				e.getMessage());

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Can not send event to the %s", getTargetName()), e);
		}
	}

	protected void scheduleEventEmitTask(final Message message, final PublishRequestData publishRequestData)
	{
		if (Config.getInt(MAX_RETRIES, DEFAULT_MAX_RETRIES) > 1)
		{
			LOG.info("Scheduling EventEmit task, Event (EventId : {})", publishRequestData.getEventId());
			final TaskModel task = getModelService().create(TaskModel.class);
			task.setRunnerBean("kymaEventEmitTaskRunner");
			task.setContext(message);
			task.setExecutionDate(new Date(System.currentTimeMillis() + Config.getInt(EVENT_RETRY_DELAY, DEFAULT_EVENT_RETRY_DELAY_MS)));
			getTaskService().scheduleTask(task);
		}
		else
		{
			LOG.info("Maximum number of retries is reached, putting Event back to queue");
			getEventChannel().send(message);
		}
	}

	protected boolean retryEventEmitting(final HttpEntity<String> request, final PublishRequestData publishRequestData,
			final String url, final AbstractDestinationModel destination)
	{
		final int maxConsecutiveRetries = Config.getInt(MAX_CONSECUTIVE_RETRIES, 3);
		for (int i = 1; i <= maxConsecutiveRetries; i++)
		{
			try
			{
				final ResponseEntity<String> response = getRestTemplateWrapper().getRestTemplate(destination.getCredential())
						.postForEntity(url, request,
						String.class);
				LOG.info("Consecutive attempt (Number : {}), Event (EventId : {}) sending response : {}", i,
						publishRequestData.getEventId(), response);
				return true;
			}
			catch (final ResourceAccessException e)
			{
				LOG.debug("Certificate has been revoked", e);
				if (e.getRootCause() instanceof CertificateRevokedException)
				{
					LOG.error("Certificate has been revoked, cause: {}", e.getRootCause().getMessage());
					return false;
				}
			}
			catch (final RestClientException | CredentialException e)
			{
				LOG.error("Consecutive attempt failed (Number : {}), Event (EventId : {}), cause: {}", i,
						publishRequestData.getEventId(), e.getMessage());
				LOG.debug("Consecutive attempt failed", e);
			}
		}
		return false;
	}

	protected boolean shouldSendToDlq(final Exception e)
	{
		if (!(e instanceof HttpClientErrorException))
		{
			return false;
		}
		final HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
		if (HttpStatus.BAD_REQUEST.equals(httpClientErrorException.getStatusCode()))
		{
			final String responseBodyAsString = httpClientErrorException.getResponseBodyAsString();
			if (StringUtils.isEmpty(responseBodyAsString))
			{
				LOG.info("Error with status code {} and message '{}' have empty response body",
						httpClientErrorException.getStatusCode(), httpClientErrorException.getMessage());
				return false;
			}
			LOG.info("Error response body: {}", responseBodyAsString);
			final String validationErrorType = Config.getString(VALIDATION_ERROR_KEY, DEFAULT_VALIDATION_ERROR);
			final String errorType;
			try
			{
				final JsonNode jsonTypeNode = getJacksonObjectMapper().readTree(responseBodyAsString).get("type");
				if (isNull(jsonTypeNode))
				{
					LOG.info("Error does not have type.");
					return false;
				}
				errorType = jsonTypeNode.asText();
			}
			catch (final IOException e1)
			{
				LOG.info("Error does not have type. Details: {}", e1.getMessage());
				LOG.debug("Error does not have type", e1);
				return false;
			}

			return validationErrorType.equals(errorType);
		}
		else
		{
			LOG.info("Error with status code {} and message '{}'",
					httpClientErrorException.getStatusCode(), httpClientErrorException.getMessage());
			return false;
		}
	}

	protected void sendToDlq(final PublishRequestData publishRequestData, final DestinationTargetModel destinationTarget,
			final String error)
	{

		final EventExportDeadLetterData deadLetterData = new EventExportDeadLetterData();
		deadLetterData.setDestinationTarget(destinationTarget);
		deadLetterData.setError(error);
		deadLetterData.setEventType(publishRequestData.getEventType());
		deadLetterData.setId(publishRequestData.getEventId());

		try
		{
			final String requestBody = getJacksonObjectMapper().writeValueAsString(publishRequestData);
			deadLetterData.setPayload(requestBody);
			final String dateFormat = Config.getString(DATE_FORMAT_PROP, DEFAULT_DATE_FORMAT);
			deadLetterData.setTimestamp(new SimpleDateFormat(dateFormat).parse(publishRequestData.getEventTime()));
		}
		catch (final JsonProcessingException e1)
		{
			LOG.error("Cannot parse event export requestBody, cause: {}", e1.getMessage());
			LOG.debug("Cannot parse event export requestBody", e1);
			return;
		}
		catch (final ParseException e2)
		{
			LOG.error("Cannot parse event time, cause: {}", e2.getMessage());
			LOG.debug("Cannot parse event time", e2);
			return;
		}

		LOG.error("Event is parked in Dead Letter Queue. EventId : {}", publishRequestData.getEventId());
		getEventDlqService().sendToQueue(deadLetterData);
	}

	/**
	 * @deprecated since 1905. Use {@link KymaEventEmitStrategy#getEventDestination(String)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected AbstractDestinationModel getEventDestination()
	{
		final String eventsDestinationId = Config.getParameter(EVENTS_SERVICE_ID);
		final AbstractDestinationModel destination = getDestinationService().getDestinationById(eventsDestinationId);
		getModelService().refresh(destination);
		return destination;
	}

	protected AbstractDestinationModel getEventDestination(final String destinationTargetId)
	{
		final String eventsDestinationId = Config.getParameter(EVENTS_SERVICE_ID);
		final AbstractDestinationModel destination = getDestinationService().getDestinationByIdAndByDestinationTargetId(eventsDestinationId, destinationTargetId);
		getModelService().refresh(destination);
		return destination;
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

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}

	/**
	 * @deprecated since 1905, for naming convention.
	 *             Use getRestTemplateWrapper instead.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected RestTemplateWrapper getRestTemplate()
	{
		return getRestTemplateWrapper();
	}

	protected RestTemplateWrapper getRestTemplateWrapper()
	{
		return restTemplateWrapper;
	}

	/**
	 * @deprecated since 1905, for naming convention.
	 *             Use setRestTemplateWrapper instead.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Required
	public void setRestTemplate(final RestTemplateWrapper restTemplate)
	{
		setRestTemplateWrapper(restTemplate);
	}

	public void setRestTemplateWrapper(final RestTemplateWrapper restTemplateWrapper)
	{
		this.restTemplateWrapper = restTemplateWrapper;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<AbstractDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}

	protected EventDlqService getEventDlqService()
	{
		return eventDlqService;
	}

	@Required
	public void setEventDlqService(final EventDlqService eventDlqService)
	{
		this.eventDlqService = eventDlqService;
	}

	protected ObjectMapper getJacksonObjectMapper()
	{
		return jacksonObjectMapper;
	}

	@Required
	public void setJacksonObjectMapper(final ObjectMapper jacksonObjectMapper)
	{
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

	protected MessageChannel getEventChannel()
	{
		return eventChannel;
	}

	@Required
	public void setEventChannel(final MessageChannel eventChannel)
	{
		this.eventChannel = eventChannel;
	}

	protected Converter<PublishRequestData, JsonPublishRequestData> getKymaJsonEventConverter()
	{
		return kymaJsonEventConverter;
	}

	@Required
	public void setKymaJsonEventConverter(final Converter<PublishRequestData, JsonPublishRequestData> kymaJsonEventConverter)
	{
		this.kymaJsonEventConverter = kymaJsonEventConverter;
	}
}
