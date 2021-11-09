/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.router;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;


@IntegrationTest
public class EventSourceDataRouterTest extends ServicelayerTransactionalTest
{
	private static final String CHANNEL_NAME = DestinationChannel.DEFAULT.getCode();

	private EventSourceDataRouter eventSourceDataRouter;

	@Resource
	private ModelService modelService;

	@Resource
	private Map<String, String> eventRoutingMap;

	@Before
	public void setUp()
	{
		eventRoutingMap.put(CHANNEL_NAME, DestinationChannel.DEFAULT.getCode());
		eventSourceDataRouter = new EventSourceDataRouter();
		eventSourceDataRouter.setEventRoutingMap(eventRoutingMap);
	}

	@Test
	public void testRouteForKymaChannel()
	{
		final EventSourceData eventSourceData = new EventSourceData();
		final EventConfigurationModel eventConfigurationModel = modelService.create(EventConfigurationModel.class);
		final DestinationTargetModel target = new DestinationTargetModel();
		target.setDestinationChannel(DestinationChannel.DEFAULT);
		target.setId(CHANNEL_NAME);
		eventConfigurationModel.setDestinationTarget(target);
		eventSourceData.setEventConfig(eventConfigurationModel);
		final Message<EventSourceData> message = MessageBuilder.withPayload(eventSourceData).build();

		assertEquals(DestinationChannel.DEFAULT.getCode(), eventSourceDataRouter.route(message));
	}
}
