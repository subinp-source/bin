/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.strategies.impl;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.persistence.populator.InboundChannelConfigurationDeletionException;
import de.hybris.platform.inboundservices.strategies.ICCDeletionValidationStrategy;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.stream.Collectors;

public class DefaultICCDeletionValidationStrategy implements ICCDeletionValidationStrategy
{

	public void checkICCLinkedWithExposedDestination(final InboundChannelConfigurationModel inboundChannelConfigurationModel)
			throws InterceptorException
	{
		if (!inboundChannelConfigurationModel.getExposedDestinations().isEmpty())
		{
			final String exposedDestinations = inboundChannelConfigurationModel.getExposedDestinations().stream()
			                                                                   .map(AbstractDestinationModel::getId)
			                                                                   .collect(Collectors.joining(","));
			final String destinationTargets = inboundChannelConfigurationModel.getExposedDestinations().stream()
			                                                                  .map(AbstractDestinationModel::getDestinationTarget)
			                                                                  .map(DestinationTargetModel::getId)
			                                                                  .collect(Collectors.joining(","));
			throw new InboundChannelConfigurationDeletionException(destinationTargets, exposedDestinations);
		}
	}
}
