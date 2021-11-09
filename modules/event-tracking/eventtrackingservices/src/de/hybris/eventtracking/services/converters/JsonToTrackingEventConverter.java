/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;

import java.util.Map;


/**
 * @author stevo.slavic
 *
 */
public class JsonToTrackingEventConverter extends AbstractPopulatingDynamicConverter<Map<String, Object>, AbstractTrackingEvent>
{

	public JsonToTrackingEventConverter(final TypeResolver<Map<String, Object>, AbstractTrackingEvent> typeResolver)
	{
		super(typeResolver);
	}
}
