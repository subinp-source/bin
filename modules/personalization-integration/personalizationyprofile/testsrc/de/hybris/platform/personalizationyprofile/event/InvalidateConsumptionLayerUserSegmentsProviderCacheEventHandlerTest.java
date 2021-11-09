/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.event;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationyprofile.segment.ConsumptionLayerUserSegmentsProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class InvalidateConsumptionLayerUserSegmentsProviderCacheEventHandlerTest
{
	private InvalidateConsumptionLayerUserSegmentsProviderCacheEventHandler eventHandler;

	@Mock
	private ConsumptionLayerUserSegmentsProvider segmentsProvider;

	private InvalidateConsumptionLayerUserSegmentsProviderCacheEvent event;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		eventHandler = new InvalidateConsumptionLayerUserSegmentsProviderCacheEventHandler();
		eventHandler.setConsumptionLayerUserSegmentsProvider(segmentsProvider);

		event = new InvalidateConsumptionLayerUserSegmentsProviderCacheEvent();
	}

	@Test
	public void testOnEvent()
	{
		//when

		eventHandler.onEvent(event);

		//then

		verify(segmentsProvider, times(1)).resetFields();
	}
}
