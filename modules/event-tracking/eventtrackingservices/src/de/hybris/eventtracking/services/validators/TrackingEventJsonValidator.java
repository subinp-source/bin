/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author stevo.slavic
 *
 */
public class TrackingEventJsonValidator
{
	private final ObjectMapper mapper;

	private final JsonSchema eventTrackingSchema;

	private static final Logger LOG = Logger.getLogger(TrackingEventJsonValidator.class);

	public TrackingEventJsonValidator(final ObjectMapper mapper, final Resource eventTrackingSchema)
			throws ProcessingException, IOException
	{
		this.mapper = mapper;

		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		try {
			this.eventTrackingSchema = factory.getJsonSchema(mapper.readTree(eventTrackingSchema.getInputStream()));
		} finally {
			if (eventTrackingSchema != null){
				safeClose(eventTrackingSchema.getInputStream());
			}
		}
	}

	public static void safeClose(final InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				LOG.warn("Unexpected error occurred closing input stream",e);
			}
		}
	}

	public ProcessingReport validate(final String rawTrackingEvent) throws IOException,
			ProcessingException
	{
		final JsonNode instance = mapper.readTree(rawTrackingEvent);
		return eventTrackingSchema.validate(instance);
	}
}
