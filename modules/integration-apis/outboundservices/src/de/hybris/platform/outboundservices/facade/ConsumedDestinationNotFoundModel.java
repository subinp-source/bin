/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import java.util.Collections;

/**
 * This {@link ConsumedDestinationModel} indicates that an actual ConsumedDestinationModel was not found
 */
public class ConsumedDestinationNotFoundModel extends ConsumedDestinationModel
{
	private static final String DESTINATION_NOT_FOUND = "Destination '%s' was not found.";

	public ConsumedDestinationNotFoundModel(final String destinationId)
	{
		super.setId(destinationId);
		super.setUrl(String.format(DESTINATION_NOT_FOUND, destinationId));
		super.setAdditionalProperties(Collections.emptyMap());
	}

	/**
	 * @deprecated use {@link ConsumedDestinationModel#getId()} instead
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	public String getDestinationId()
	{
		return getId();
	}
}
