/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationoccaddon.mapping.converters;

import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

import java.util.Locale;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;


/**
 * Bidirectional converter between {@link NotificationChannel} and String
 */
@WsDTOMapping
public class NotificationChannelConverter extends BidirectionalConverter<NotificationChannel, String>
{
	@Override
	public String convertTo(final NotificationChannel source, final Type<String> destinationType, final MappingContext mappingContext)
	{
		return source.toString();
	}

	@Override
	public NotificationChannel convertFrom(final String source, final Type<NotificationChannel> destinationType,
			final MappingContext mappingContext)
	{
		return NotificationChannel.valueOf(source.toUpperCase(Locale.ROOT));
	}
}
