/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.EventDlqService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultEventDlqServiceIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private EventDlqService eventDlqService;


	@Test
	public void testSendingDataWithNotUniqueIdToDlqShouldNotCauseFail()
	{
		final EventExportDeadLetterData data = new EventExportDeadLetterData();
		data.setId("testDataId");
		data.setError("error");
		data.setTimestamp(new Date());
		data.setEventType("testName");
		final DestinationTargetModel dest = new DestinationTargetModel();
		dest.setId("testDestId");
		dest.setDestinationChannel(DestinationChannel.DEFAULT);

		data.setDestinationTarget(dest);
		data.setPayload("testPayload");

		eventDlqService.sendToQueue(data);

		data.setTimestamp(new Date());
		eventDlqService.sendToQueue(data);
	}
}
