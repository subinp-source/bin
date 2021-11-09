/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.strategies.ApiRegistrationStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ApiRegistrationService}
 */
public class DefaultApiRegistrationService implements ApiRegistrationService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultApiRegistrationService.class);

	private DestinationService<AbstractDestinationModel> destinationService;
	private Map<DestinationChannel, ApiRegistrationStrategy> apiRegistrationStrategyMap;

	@Override
	public void registerExposedDestination(final ExposedDestinationModel destination) throws ApiRegistrationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("destination", destination);

		if (destination.getDestinationTarget() == null)
		{
			throw new ApiRegistrationException("ExposedDestination must have non-null DestinationTarget");
		}

		final DestinationChannel channel = destination.getDestinationTarget().getDestinationChannel();
		if (null == channel)
		{
			throw new ApiRegistrationException("ExposedDestination must have non-null Channel");
		}

		checkIfChannelSupported(channel);

		getApiRegistrationStrategyMap().get(channel).registerExposedDestination(destination);
	}

	@Override
	public void unregisterExposedDestination(final ExposedDestinationModel destination) throws ApiRegistrationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("destination", destination);
		if (destination.getDestinationTarget() == null)
		{
			throw new ApiRegistrationException("ExposedDestination must have non-null DestinationTarget");
		}

		if (StringUtils.isEmpty(destination.getTargetId()))
		{
			final String errorMessage = String.format(
					"The Destination [{%s}] is not registered. Only registered Exposed Destination(s) can be unregistered",
					destination.getId());
			LOG.error(errorMessage);
			throw new ApiRegistrationException(errorMessage);
		}

		checkIfChannelSupported(destination.getDestinationTarget().getDestinationChannel());

		getApiRegistrationStrategyMap().get(destination.getDestinationTarget().getDestinationChannel())
				.unregisterExposedDestination(destination);
	}

	@Override
	public List<RegisteredDestinationData> retrieveRegisteredExposedDestinations(final DestinationChannel destinationChannel, final
			DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		checkIfChannelSupported(destinationChannel);
		return getApiRegistrationStrategyMap().get(destinationChannel).retrieveRegisteredExposedDestinations(destinationTarget);
	}

	@Override
	public void unregisterExposedDestinationByTargetId(final String targetId, final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		checkIfChannelSupported(destinationTarget.getDestinationChannel());
		getApiRegistrationStrategyMap().get(destinationTarget.getDestinationChannel())
				.unregisterExposedDestinationByTargetId(targetId, destinationTarget.getId());
	}

	protected void checkIfChannelSupported(final DestinationChannel channel) throws ApiRegistrationException
	{
		if (!getApiRegistrationStrategyMap().containsKey(channel))
		{
			final String errorMessage = String.format("Channel [{%s}] is not supported by system, check your spring config",
					channel);
			LOG.error(errorMessage);
			throw new ApiRegistrationException(errorMessage);
		}
	}

	protected Map<DestinationChannel, ApiRegistrationStrategy> getApiRegistrationStrategyMap()
	{
		return apiRegistrationStrategyMap;
	}

	@Required
	public void setApiRegistrationStrategyMap(final Map<DestinationChannel, ApiRegistrationStrategy> apiRegistrationStrategyMap)
	{
		this.apiRegistrationStrategyMap = apiRegistrationStrategyMap;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService destinationService)
	{
		this.destinationService = destinationService;
	}
}
