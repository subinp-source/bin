/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.servicelayer.interceptor.InterceptorException;

/**
 * An exception indicating that the InboundChannelConfiguration cannot be deleted because some ExposedDestination is linked to this ICC.
 */
public class InboundChannelConfigurationDeletionException extends InterceptorException
{
	private static final String ERROR_MESSAGE_TEMPLATE = "The InboundChannelConfiguration cannot be deleted, because it is used by destination targets: [%s]. Please delete the ExposedDestinations: [%s] and try again.";

	private final String destinationTargets;
	private final String exposedDestinations;
	private final String errorMessage;

	public InboundChannelConfigurationDeletionException(final String destinationTargets, final String exposedDestinations)
	{
		super(String.format(ERROR_MESSAGE_TEMPLATE,	destinationTargets, exposedDestinations));
		this.errorMessage = String.format(ERROR_MESSAGE_TEMPLATE,	destinationTargets, exposedDestinations);
		this.destinationTargets = destinationTargets;
		this.exposedDestinations = exposedDestinations;
	}

	public String getDestinationTargets()
	{
		return destinationTargets;
	}

	public String getExposedDestinations()
	{
		return exposedDestinations;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

}
