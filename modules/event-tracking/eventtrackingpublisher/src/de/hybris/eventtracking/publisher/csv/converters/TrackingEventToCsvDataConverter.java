/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.publisher.csv.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;


/**
 * @author stevo.slavic
 *
 */
public class TrackingEventToCsvDataConverter extends AbstractPopulatingConverter<AbstractTrackingEvent, TrackingEventCsvData>
{

	public TrackingEventToCsvDataConverter()
	{
		this.setTargetClass(TrackingEventCsvData.class);
		
	}
}
