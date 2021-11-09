/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.task;

import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENT_RETRY_DELAY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.MAX_CONSECUTIVE_RETRIES;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.MAX_RETRIES;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.eventPayloadToString;

import de.hybris.platform.apiregistryservices.event.EventExportFailedEvent;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.kymaintegrationservices.dto.JsonPublishRequestData;
import de.hybris.platform.kymaintegrationservices.dto.PublishRequestData;
import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;

import java.security.cert.CertificateRevokedException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import static de.hybris.platform.kymaintegrationservices.utils.KymaHttpHelper.getDefaultHeaders;


/**
 * A Task Runner for Event Emitting
 */
public class KymaEventEmitTaskRunner implements TaskRunner<TaskModel>
{
	private static final int DEFAULT_MAX_CONSECUTIVE_RETRIES = 3;
	private static final int DEFAULT_EVENT_RETRY_DELAY_MS = 5000;
	private static final int DEFAULT_MAX_RETRIES = 3;

	private static final Logger LOG = Logger.getLogger(KymaEventEmitTaskRunner.class);

	private RestTemplateWrapper restTemplate;
	private DestinationService<AbstractDestinationModel> destinationService;
	private MessageChannel eventChannel;
	private EventService eventService;
	private Converter<PublishRequestData, JsonPublishRequestData> kymaJsonEventConverter;

	@Override
	public void run(final TaskService taskService, final TaskModel task)
	{
		if (!(task.getContext() instanceof Message) && !(((Message) task.getContext()).getPayload() instanceof PublishRequestData))
		{
			LOG.error("Provided payload is not instance of PublishRequestData");
			return;
		}
		final Message message = (Message) task.getContext();

		final String eventsDestinationId = Config.getParameter(EVENTS_SERVICE_ID);
		final PublishRequestData publishRequestData = (PublishRequestData) message.getPayload();
		final AbstractDestinationModel destination = getDestinationService()
				.getDestinationByIdAndByDestinationTargetId(eventsDestinationId, publishRequestData.getDestinationTargetId());

		if (destination == null || destination.getUrl() == null || destination.getCredential() == null)
		{
			LOG.error("The Destination for exporting events is invalid or missing.");
			return;
		}

		LOG.info(eventPayloadToString(publishRequestData));

		final HttpHeaders headers = getDefaultHeaders();
		final String url = destination.getUrl();

		final JsonPublishRequestData jsonPublishRequestData = getKymaJsonEventConverter().convert(publishRequestData);
		final HttpEntity<String> request = new HttpEntity(jsonPublishRequestData, headers);

		try
		{
			final ResponseEntity<String> response = getRestTemplate().getRestTemplate(destination.getCredential()).postForEntity(url, request,
					String.class);

			LOG.info(String.format("Event (EventId : %s) was sent. Response: %s", sanitize(publishRequestData.getEventId()), sanitize(response.toString())));
		}
		catch (final ResourceAccessException | CredentialException e)
		{
			logException(e, publishRequestData);

			scheduleEventEmitTask(message, publishRequestData, 1);
		}
		catch (final RestClientException e)
		{
			logException(e, publishRequestData);

			if (!retryEventEmitting(request, publishRequestData, url, destination))
			{
				final Integer retry = task.getRetry() + 2;
				scheduleEventEmitTask(message, publishRequestData, retry);
			}
		}
	}

	protected void scheduleEventEmitTask(final Message message, final PublishRequestData publishRequestData, final Integer retry)
	{
		final Integer totalAmountOfIndividualEntries = retry * Config.getInt(MAX_CONSECUTIVE_RETRIES, 3);
		final String exportEventFailedMessage = String.format(
				"There have already been %d attempt(s) to send the Event (EventId : %s).",
				totalAmountOfIndividualEntries, publishRequestData.getEventId());
		getEventService().publishEvent(
				new EventExportFailedEvent(exportEventFailedMessage, totalAmountOfIndividualEntries));

		if (retry >= Config.getInt(MAX_RETRIES, DEFAULT_MAX_RETRIES))
		{
			LOG.info(exportEventFailedMessage + " Maximum of retries is reached, putting Event back to queue");
			getEventChannel().send(message);
			return;
		}

		LOG.info(exportEventFailedMessage + " Maximum of retries is not reached, triggering retry");
		scheduleRetry();
	}

	protected void logException(final Exception e, final PublishRequestData publishRequestData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(e.getMessage(), e);
		}
		LOG.error(String.format("Can not send event to the %s, event type : %s, cause: %s", getTargetName(),
				publishRequestData.getEventType(), e.getMessage()));
		LOG.info(String.format("Starting consecutive retries to send event to the %s, event type : %s",
				getTargetName(),
				publishRequestData.getEventType()));
	}

	@Override
	public void handleError(final TaskService taskService, final TaskModel taskModel, final Throwable error)
	{
		LOG.error(String.format("Failed to emit the Event, cause: %s", error.getMessage()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug(error.getMessage(), error);
		}
	}

	protected void scheduleRetry()
	{
		final RetryLaterException ex = new RetryLaterException(StringUtils.EMPTY); // to trigger the retry
		ex.setDelay(Config.getInt(EVENT_RETRY_DELAY, DEFAULT_EVENT_RETRY_DELAY_MS));
		throw ex;
	}

	protected boolean retryEventEmitting(final HttpEntity<String> request, final PublishRequestData publishRequestData,
			final String url, final AbstractDestinationModel destination)
	{

		final int maxConsecutiveRetries = Config.getInt(MAX_CONSECUTIVE_RETRIES, DEFAULT_MAX_CONSECUTIVE_RETRIES);
		for (int i = 1; i <= maxConsecutiveRetries; i++)
		{
			try
			{
				final ResponseEntity<String> response = getRestTemplate().getRestTemplate(destination.getCredential()).postForEntity(url, request,
						String.class);

				LOG.info(String.format("Consecutive attempt (Number : %d), Event (EventId : %s) sending response : %s", i,
						sanitize(publishRequestData.getEventId()), sanitize(response.toString())));
				return true;
			}
			catch (final ResourceAccessException e)
			{
				if(e.getRootCause() instanceof CertificateRevokedException)
				{
					LOG.error(String.format("Certificate has been revoked with the  cause: %s", e.getRootCause().getMessage()));
					LOG.debug("Certificate has been revoked", e.getRootCause());
					return false;
				}
				LOG.error(String.format("Consecutive attempt failed (Number : %d), Event (EventId : %s), cause: %s", i,
						publishRequestData.getEventId(), e.getMessage()));
				LOG.debug("Consecutive attempt failed", e);
			}
			catch (final RestClientException | CredentialException e)
			{
				LOG.error(String.format("Consecutive attempt failed (Number : %d), Event (EventId : %s), cause: %s", i,
						publishRequestData.getEventId(), e.getMessage()));
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage(), e);
				}
			}
		}
		return false;
	}

	protected String sanitize(final String logMsg)
	{
		String output = StringUtils.defaultString(logMsg).trim();
		output = output.replaceAll("(\\r\\n|\\r|\\n)+", " ");

		return output;
	}

	protected RestTemplateWrapper getRestTemplate()
	{
		return restTemplate;
	}

	@Required
	public void setRestTemplate(final RestTemplateWrapper restTemplate)
	{
		this.restTemplate = restTemplate;
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

	protected MessageChannel getEventChannel()
	{
		return eventChannel;
	}

	@Required
	public void setEventChannel(final MessageChannel eventChannel)
	{
		this.eventChannel = eventChannel;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
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
