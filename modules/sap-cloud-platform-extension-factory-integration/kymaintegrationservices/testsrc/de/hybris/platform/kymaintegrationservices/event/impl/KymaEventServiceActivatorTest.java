/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.event.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.enums.EventMappingType;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.strategies.EventEmitStrategy;
import de.hybris.platform.kymaintegrationservices.dto.PublishRequestData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.integration.support.MutableMessage;
import org.springframework.integration.support.MutableMessageHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;


@UnitTest
public class KymaEventServiceActivatorTest
{
	private final KymaEventServiceActivator activator = new KymaEventServiceActivator();
	public static final String ERROR_CHANNEL = "errorChannel";

	@Mock
	private EventEmitStrategy eventEmitStrategy;
	@Mock
	private Converter<EventSourceData, PublishRequestData> converter;

	private final Map<String, Object> headersMap = new HashMap<>();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(eventEmitStrategy).sendEvent(any());
		when(converter.convert(any())).thenReturn(new PublishRequestData());
		activator.setEventEmitStrategy(eventEmitStrategy);
		activator.setKymaEventConverter(converter);
		headersMap.put(MessageHeaders.REPLY_CHANNEL, ERROR_CHANNEL);
		headersMap.put(MessageHeaders.ERROR_CHANNEL, ERROR_CHANNEL);
	}

	@Test
	public void publishRequestDataTest()
	{
		final Message message = new MutableMessage(new PublishRequestData());
		activator.handle(message);
		Mockito.verify(eventEmitStrategy).sendEvent(message);
	}

	@Test
	public void eventSourceDataTest()
	{
		final EventSourceData data = getEventSourceData();
		activator.handle(new GenericMessage<>(data));

		Mockito.verify(converter).convert(any());
		Mockito.verify(eventEmitStrategy).sendEvent(any());
	}

	@Test
	public void eventSourceDataWithMutableMessageHeadersTest()
	{
		final EventSourceData data = getEventSourceData();
		final MutableMessageHeaders mutableMessageHeaders = new MutableMessageHeaders(headersMap);
		activator.handle(new GenericMessage<>(data, mutableMessageHeaders));

		Mockito.verify(converter).convert(any());
		Mockito.verify(eventEmitStrategy).sendEvent(any());
	}

	@Test
	public void eventSourceDataWithImmutableMessageHeadersTest()
	{
		final EventSourceData data = getEventSourceData();
		final MessageHeaders immutableMessageHeader = new MessageHeaders(headersMap);
		activator.handle(new GenericMessage<>(data, immutableMessageHeader));

		Mockito.verify(converter).convert(any());
		Mockito.verify(eventEmitStrategy).sendEvent(any());
	}

	@Test
	public void eventSourceDataWithMutableMessageWithMutableMessageHeadersTest()
	{
		final EventSourceData data = getEventSourceData();
		final MutableMessageHeaders mutableMessageHeaders = new MutableMessageHeaders(headersMap);
		activator.handle(new MutableMessage<>(data, mutableMessageHeaders));

		Mockito.verify(converter).convert(any());
		Mockito.verify(eventEmitStrategy).sendEvent(any());
	}

	@Test
	public void eventSourceDataWithMutableMessageWithImmutableMessageHeadersTest()
	{
		final EventSourceData data = getEventSourceData();
		final MessageHeaders immutableMessageHeader = new MessageHeaders(headersMap);
		activator.handle(new MutableMessage<>(data, immutableMessageHeader));

		Mockito.verify(converter).convert(any());
		Mockito.verify(eventEmitStrategy).sendEvent(any());
	}


	@Test(expected = UnsupportedOperationException.class)
	public void unsupportedTest()
	{
		final Message message = new MutableMessage("Test");
		activator.handle(message);
	}

	private EventSourceData getEventSourceData()
	{
		final EventConfigurationModel configuration = mock(EventConfigurationModel.class);
		when(configuration.getMappingType()).thenReturn(EventMappingType.GENERIC);

		final EventSourceData data = new EventSourceData();
		data.setEventConfig(configuration);
		return data;
	}
}
