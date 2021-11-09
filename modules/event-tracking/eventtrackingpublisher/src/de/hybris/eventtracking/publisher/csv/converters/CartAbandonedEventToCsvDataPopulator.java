/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.publisher.csv.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.model.events.CartAbandonedEvent;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author stevo.slavic
 *
 */
public class CartAbandonedEventToCsvDataPopulator implements Populator<AbstractTrackingEvent, TrackingEventCsvData>
{

	/**
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractTrackingEvent source, final TrackingEventCsvData target) throws ConversionException
	{
		if (CartAbandonedEvent.class.isAssignableFrom(source.getClass()))
		{
			// target.setCartAbandonmentReason(((CartAbandonedEvent) source).getCartAbandonmentReason());
			// target.setEventType(((setCartAbandonmentReason) source).getEventType());
		}
	}

}
