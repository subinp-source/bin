/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.apiregistryservices.dao.DestinationTargetDao;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.apiregistryservices.services.ServiceWarning;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetCloningStrategy;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetRegistrationStrategy;


/**
 * Default implementation of {@link DestinationTargetService}
 */
public class DefaultDestinationTargetService implements DestinationTargetService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultDestinationTargetService.class);

	private ApiRegistrationService apiRegistrationService;
	private Map<DestinationChannel, DestinationTargetCloningStrategy> destinationTargetCloningStrategyMap;
	private Map<DestinationChannel, DestinationTargetRegistrationStrategy> destinationTargetRegistrationStrategyMap;
	private DestinationService<AbstractDestinationModel> destinationService;
	private DestinationTargetDao destinationTargetDao;

	@Override
	public DestinationTargetModel createDestinationTarget(final DestinationTargetModel source, final String newId, final DestinationChannel destinationChannel)
			throws ApiRegistrationException
	{
		return getDestinationTargetCloningStrategy(destinationChannel).createDestinationTarget(source, newId);
	}

	@Override
	public void registerDestinationTarget(final DestinationTargetModel destinationTarget, final Map<String, String> params)
			throws ApiRegistrationException
	{
		getDestinationTargetRegistrationStrategy(destinationTarget.getDestinationChannel())
				.registerDestinationTarget(destinationTarget, params);
	}

	@Override
	public void createDestinations(final DestinationTargetModel source, final DestinationTargetModel target,
			final List<AbstractDestinationModel> destinations) throws ApiRegistrationException
	{
		getDestinationTargetCloningStrategy(target.getDestinationChannel()).createDestinations(source, target, destinations);
	}

	@Override
	public void createEventConfigurations(final DestinationTargetModel source, final DestinationTargetModel target,
			final List<EventConfigurationModel> eventConfigurations) throws ApiRegistrationException
	{
		getDestinationTargetCloningStrategy(target.getDestinationChannel())
				.createEventConfigurations(source, target, eventConfigurations);
	}

	@Override
	public Map<String, String> registerExposedDestinations(final DestinationTargetModel destinationTarget)
	{
		final Map<String, String> errors = new HashMap<>();
		final List<ExposedDestinationModel> exposedDestinations = getDestinationService()
				.getActiveExposedDestinationsByDestinationTargetId(destinationTarget.getId());

		for (final ExposedDestinationModel destination : exposedDestinations)
		{
			try
			{
				getApiRegistrationService().registerExposedDestination(destination);
			}
			catch (final ApiRegistrationException e)
			{
				LOG.error("Registration failed for the destination: {}" + destination.getId(), e);
				errors.put(destination.getId(), e.getMessage());
			}
		}

		return errors;
	}

	@Override
	public Collection<ServiceWarning<DestinationTargetModel>> deregisterAndDeleteDestinationTarget(
			final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		checkArgument(Objects.nonNull(destinationTarget), "Destination Target should be provided");

		final Collection<ServiceWarning<DestinationTargetModel>> result = new ArrayList<>();

		if (BooleanUtils.isNotTrue(destinationTarget.getTemplate()))
		{
			try
			{
				final DestinationTargetRegistrationStrategy destinationTargetRegistrationStrategy =
						getDestinationTargetRegistrationStrategy(destinationTarget.getDestinationChannel());

				destinationTargetRegistrationStrategy.deregisterDestinationTarget(destinationTarget);
			}
			catch (final DeleteDestinationTargetNotPossibleException e)
			{
				LOG.error(e.getMessage(), e);
				throw new ApiRegistrationException(e.getMessage());
			}
			catch (final ApiRegistrationException e)
			{
				LOG.warn(e.getMessage(), e);
			}
			catch (final RuntimeException e)
			{
				final String warningMessage = String.format("Failed to deregister '%s' Destination Target ",
						destinationTarget.getId());
				LOG.warn(warningMessage, e);
			}
		}
		deleteDestinationTarget(destinationTarget);
		return result;
	}

	@Override
	public void deleteDestinationTarget(final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		getDestinationTargetCloningStrategy(destinationTarget.getDestinationChannel())
				.deleteDestinationTarget(destinationTarget);
	}

	@Override
	public void syncDestinationTargetWithRemoteSystem(final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		getDestinationTargetRegistrationStrategy(destinationTarget.getDestinationChannel())
				.syncDestinationTargetWithRemoteSystem(destinationTarget);
	}

	protected DestinationTargetRegistrationStrategy getDestinationTargetRegistrationStrategy(
			final DestinationChannel destinationChannel)
	{
		DestinationTargetRegistrationStrategy destinationTargetRegistrationStrategy = getDestinationTargetRegistrationStrategyMap()
				.get(destinationChannel);

		if (destinationTargetRegistrationStrategy == null)
		{
			LOG.warn("DestinationTargetRegistrationStrategy for the destination channel {} not found, so falling back to default channel registration strategy", destinationChannel);
			destinationTargetRegistrationStrategy = getDestinationTargetRegistrationStrategyMap().get(DestinationChannel.DEFAULT);
		}

		return destinationTargetRegistrationStrategy;
	}

	protected DestinationTargetCloningStrategy getDestinationTargetCloningStrategy(final DestinationChannel destinationChannel)
	{

		DestinationTargetCloningStrategy destinationTargetCloningStrategy = getDestinationTargetCloningStrategyMap()
				.get(destinationChannel);

		if (destinationTargetCloningStrategy == null)
		{
			LOG.warn("DestinationTargetCloningStrategy for the destination channel {} not found, so falling back to default channel cloning strategy", destinationChannel);
			destinationTargetCloningStrategy = getDestinationTargetCloningStrategyMap().get(DestinationChannel.DEFAULT);
		}

		return destinationTargetCloningStrategy;
	}

	@Override
	public DestinationTargetModel getDestinationTargetByCredentialId(final String credentialId)
	{
		return getDestinationTargetDao().findDestinationTargetByCredentialId(credentialId);
	}

	@Override
	public DestinationTargetModel getDestinationTargetById(final String id) {
		return getDestinationTargetDao().findDestinationTargetById(id);
	}

	protected Map<DestinationChannel, DestinationTargetCloningStrategy> getDestinationTargetCloningStrategyMap()
	{
		return destinationTargetCloningStrategyMap;
	}

	@Required
	public void setDestinationTargetCloningStrategyMap(
			final Map<DestinationChannel, DestinationTargetCloningStrategy> destinationTargetCloningStrategyMap)
	{
		this.destinationTargetCloningStrategyMap = destinationTargetCloningStrategyMap;
	}

	protected Map<DestinationChannel, DestinationTargetRegistrationStrategy> getDestinationTargetRegistrationStrategyMap()
	{
		return destinationTargetRegistrationStrategyMap;
	}

	@Required
	public void setDestinationTargetRegistrationStrategyMap(
			final Map<DestinationChannel, DestinationTargetRegistrationStrategy> destinationTargetRegistrationStrategyMap)
	{
		this.destinationTargetRegistrationStrategyMap = destinationTargetRegistrationStrategyMap;
	}

	protected ApiRegistrationService getApiRegistrationService()
	{
		return apiRegistrationService;
	}

	@Required
	public void setApiRegistrationService(final ApiRegistrationService apiRegistrationService)
	{
		this.apiRegistrationService = apiRegistrationService;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<AbstractDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}

	protected DestinationTargetDao getDestinationTargetDao()
	{
		return destinationTargetDao;
	}

	@Required
	public void setDestinationTargetDao(final DestinationTargetDao destinationTargetDao)
	{
		this.destinationTargetDao = destinationTargetDao;
	}
}
