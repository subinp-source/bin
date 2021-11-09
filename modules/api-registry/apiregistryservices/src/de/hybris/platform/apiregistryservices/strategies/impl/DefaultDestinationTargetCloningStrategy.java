/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies.impl;

import static com.google.common.base.Preconditions.checkArgument;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.apiregistryservices.dao.EventConfigurationDao;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetCloningStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;


/**
 * Default implementation of {@link DestinationTargetCloningStrategy}
 */
public class DefaultDestinationTargetCloningStrategy implements DestinationTargetCloningStrategy
{
	private ModelService modelService;
	private DestinationService<AbstractDestinationModel> destinationService;
	private EventConfigurationDao eventConfigurationDao;

	private static final String SOURCE_ERROR_MESSAGE = "source must not be null";
	private static final String DESTINATION_ERROR_MESSAGE = "destinationTarget must not be null";

	@Override
	public DestinationTargetModel createDestinationTarget(final DestinationTargetModel source, final String newId)
	{
		checkArgument(Objects.nonNull(source), SOURCE_ERROR_MESSAGE);
		checkArgument(Objects.nonNull(newId), "newId must not be null");
		
		final DestinationTargetModel cloneDestinationTarget = getModelService().clone(source);
		cloneDestinationTarget.setId(newId);
		cloneDestinationTarget.setTemplate(false);
		getModelService().save(cloneDestinationTarget);

		return cloneDestinationTarget;
	}

	@Override
	public void createDestinations(final DestinationTargetModel source, final DestinationTargetModel destinationTarget,
			final List<AbstractDestinationModel> destinations)
	{
		checkArgument(Objects.nonNull(source), SOURCE_ERROR_MESSAGE);
		checkArgument(Objects.nonNull(destinationTarget), DESTINATION_ERROR_MESSAGE);
		
		List<AbstractDestinationModel> destinationsToBeCloned = destinations;
		final List<AbstractDestinationModel> clonedDestinations = new ArrayList<>();

		if (CollectionUtils.isEmpty(destinations))
		{
			destinationsToBeCloned = getDestinationService().getDestinationsByDestinationTargetId(source.getId());
		}

		for (final AbstractDestinationModel destination : destinationsToBeCloned)
		{
			final AbstractDestinationModel clonedDestination = getModelService().clone(destination);
			clonedDestination.setDestinationTarget(destinationTarget);
			clonedDestination.setCredential(null);

			clonedDestinations.add(clonedDestination);
		}

		getModelService().saveAll(clonedDestinations);
	}

	@Override
	public void createEventConfigurations(final DestinationTargetModel source, final DestinationTargetModel destinationTarget,
			final List<EventConfigurationModel> eventConfigurations)
	{
		checkArgument(Objects.nonNull(source), SOURCE_ERROR_MESSAGE);
		checkArgument(Objects.nonNull(destinationTarget), DESTINATION_ERROR_MESSAGE);
		
		List<EventConfigurationModel> eventConfigurationsToBeCopied = eventConfigurations;
		final List<EventConfigurationModel> clonedEventConfigurations = new ArrayList<>();

		if (CollectionUtils.isEmpty(eventConfigurations))
		{
			eventConfigurationsToBeCopied = getEventConfigurationDao().findEventConfigsByDestinationTargetId(source.getId());
		}

		for (final EventConfigurationModel eventConfiguration : eventConfigurationsToBeCopied)
		{
			final EventConfigurationModel cloneEventConfiguration = getModelService().clone(eventConfiguration);
			cloneEventConfiguration.setDestinationTarget(destinationTarget);

			clonedEventConfigurations.add(cloneEventConfiguration);
		}

		getModelService().saveAll(clonedEventConfigurations);
	}

	@Override
	public void deleteDestinationTarget(final DestinationTargetModel destinationTarget)
	{
		checkArgument(Objects.nonNull(destinationTarget), DESTINATION_ERROR_MESSAGE);
		
		final List<AbstractDestinationModel> destinations = getDestinationService()
				.getDestinationsByDestinationTargetId(destinationTarget.getId());

		final List<EventConfigurationModel> eventConfigurations = getEventConfigurationDao()
				.findEventConfigsByDestinationTargetId(destinationTarget.getId());

		final Set<AbstractCredentialModel> credentialsToBeDeleted = new HashSet<>();
		final Set<EndpointModel> endpointsToBeDeleted = new HashSet<>();
		final Set<OAuthClientDetailsModel> clientDetailsToBeDeleted = new HashSet<>();
		for (final AbstractDestinationModel destination : destinations)
		{
			if (destination instanceof ExposedDestinationModel)
			{
				((ExposedDestinationModel) destination).setTargetId(null);
			}
			else if (destination instanceof ConsumedDestinationModel)
			{
				endpointsToBeDeleted.add(destination.getEndpoint());
			}

			if (destination.getCredential() != null)
			{
				credentialsToBeDeleted.add(destination.getCredential());

				if (destination.getCredential() instanceof ExposedOAuthCredentialModel)
				{
					clientDetailsToBeDeleted.add(((ExposedOAuthCredentialModel) destination.getCredential()).getOAuthClientDetails());
				}
			}
		}

		getModelService().removeAll(clientDetailsToBeDeleted);
		getModelService().removeAll(endpointsToBeDeleted);
		getModelService().removeAll(credentialsToBeDeleted);
		getModelService().removeAll(destinations);
		getModelService().removeAll(eventConfigurations);
		getModelService().removeAll(destinationTarget);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	protected EventConfigurationDao getEventConfigurationDao()
	{
		return eventConfigurationDao;
	}

	@Required
	public void setEventConfigurationDao(final EventConfigurationDao eventConfigurationDao)
	{
		this.eventConfigurationDao = eventConfigurationDao;
	}
}
