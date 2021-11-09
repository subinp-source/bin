/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.populators;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author stevo.slavic
 *
 */
public abstract class AbstractTrackingEventGenericPopulator implements
		GenericPopulator<Map<String, Object>, AbstractTrackingEvent>
{
	private final ObjectMapper mapper;

	public AbstractTrackingEventGenericPopulator(final ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	public ObjectMapper getMapper()
	{
		return mapper;
	}

	protected Map<String, Object> getPageScopedCvar(final Map<String, Object> trackingEventData)
	{
		final String cvar = (String) trackingEventData.get(TrackingEventJsonFields.COMMON_CVAR_PAGE.getKey());
		Map<String, Object> customVariablesPageScoped = null;
		if (StringUtils.isNotBlank(cvar))
		{
			try
			{
				customVariablesPageScoped = getMapper().readValue(cvar, Map.class);
			}
			catch (final IOException e)
			{
				throw new ConversionException("Error extracting custom page scoped variables from: " + cvar, e);
			}
		}
		return customVariablesPageScoped;
	}
}
