/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingmock.service.impl;

import de.hybris.platform.consignmenttrackingservices.delivery.data.ConsignmentEventData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jersey.repackaged.com.google.common.collect.Lists;


/**
 * A Mock Service to return trackingEvents.
 */
public class MockConsignmentTrackingService
{
	private static final Logger LOG = Logger.getLogger(MockConsignmentTrackingService.class);

	/**
	 * Get consignmentEventData form map by tracking id
	 *
	 * @param trackingId
	 *           the tracking number
	 * @return all the consignmentEvents corresponds to the tracking number
	 */
	public List<ConsignmentEventData> getConsignmentEvents(final String trackingId)
	{
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			final byte[] jsonData = Files
					.readAllBytes(Paths.get(new ClassPathResource("/consignmenttrackingmock/ConsignmentEventData.json").getURI()));
			final List<ConsignmentEventData> data = objectMapper.readValue(jsonData, new TypeReference<List<ConsignmentEventData>>()
			{});
			return data;
		}
		catch (final IOException e)
		{
			LOG.info(e);
			return Lists.newArrayList();
		}
	}
}
