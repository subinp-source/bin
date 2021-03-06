/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.event.impl;

import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.enums.EventMappingType;
import de.hybris.platform.apiregistryservices.event.impl.EventServiceActivator;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.kymaintegrationservices.dto.PublishRequestData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;


/**
 * Kyma-specific channel activator.
 * Only difference with default - type conversion.
 */
public class KymaEventServiceActivator extends EventServiceActivator
{
	private static final Logger LOGGER = LoggerFactory.getLogger(KymaEventServiceActivator.class);
	private Converter<EventSourceData, PublishRequestData> kymaEventConverter;

	@Override
	public void handle(final Message message)
	{
		final Object payload = message.getPayload();
		if (payload instanceof PublishRequestData)
		{
			super.handle(message);
			return;
		}
		if (payload instanceof EventSourceData)
		{
			final EventSourceData eventSourceData = (EventSourceData) payload;
			final PublishRequestData requestData = getConverter(eventSourceData.getEventConfig()).convert(eventSourceData);
			super.handle(new GenericMessage<>(requestData, message.getHeaders()));
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}

	protected Converter<EventSourceData, PublishRequestData> getConverter(final EventConfigurationModel eventConfiguration)
	{
		final Converter<EventSourceData, PublishRequestData> converter;
		if (EventMappingType.GENERIC.equals(eventConfiguration.getMappingType()) || EventMappingType.PROCESS
				.equals(eventConfiguration.getMappingType()))
		{
			converter = kymaEventConverter;
		}
		else
		{
			converter = (Converter) Registry.getApplicationContext().getBean(eventConfiguration.getConverterBean());
		}

		if (converter == null)
		{
			LOGGER.error("No Converter found for event: [{}]", eventConfiguration.getEventClass());
		}

		return converter;
	}

	@Required
	public void setKymaEventConverter(final Converter<EventSourceData, PublishRequestData> kymaEventConverter)
	{
		this.kymaEventConverter = kymaEventConverter;
	}
}
