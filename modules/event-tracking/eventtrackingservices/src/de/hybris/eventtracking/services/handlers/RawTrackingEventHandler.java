/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.handlers;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.services.converters.JsonToTrackingEventConverter;
import de.hybris.eventtracking.services.validators.TrackingEventJsonValidator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.event.EventService;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;


/**
 * @author stevo.slavic
 *
 */
public class RawTrackingEventHandler
{

	private static final Logger LOG = Logger.getLogger(RawTrackingEventHandler.class);

	private final TrackingEventJsonValidator validator;

	private final JsonToTrackingEventConverter converter;

	private final ObjectMapper mapper;

	private final EventService eventService;

	private final String tenantId;

	public RawTrackingEventHandler(final TrackingEventJsonValidator validator, final JsonToTrackingEventConverter converter,
			final ObjectMapper mapper, final EventService eventService)
	{
		this.validator = validator;
		this.converter = converter;
		this.mapper = mapper;
		this.eventService = eventService;
		this.tenantId = Registry.getCurrentTenant().getTenantID();
	}

	public void handle(final String rawTrackingEvent) throws IOException, ProcessingException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Handling raw tracking event:\n" + rawTrackingEvent);
		}

		final ProcessingReport processingReport = validator.validate(rawTrackingEvent);

		if (processingReport.isSuccess())
		{
			final Map<String, Object> trackingEventData = mapper.readValue(rawTrackingEvent, Map.class);
			try
			{
				Registry.setCurrentTenantByID(tenantId);
				final AbstractTrackingEvent trackingEvent = converter.convert(trackingEventData);
				if (trackingEvent != null)
				{
					eventService.publishEvent(trackingEvent);
				}
				else
				{
					LOG.warn("Processing failed for raw tracking event: " + rawTrackingEvent);
				}
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}
		}
		else
		{
			LOG.warn("Raw tracking event did not pass validation");

			for (final ProcessingMessage processingMessage : processingReport)
			{
				LOG.warn(processingMessage.getMessage(), processingMessage.asException());
			}
		}
	}
}
