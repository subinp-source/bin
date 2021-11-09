/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.kymaintegrationservices.dto.KymaRegistrationRequest;
import de.hybris.platform.kymaintegrationservices.exceptions.KymaDestinationTargetRegistrationException;
import de.hybris.platform.kymaintegrationservices.services.KymaDestinationTargetRegistrationService;
import de.hybris.platform.kymaintegrationservices.strategies.impl.KymaDestinationTargetRegistrationStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of {@link KymaDestinationTargetRegistrationService}.
 */
public class DefaultKymaDestinationTargetRegistrationService implements KymaDestinationTargetRegistrationService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKymaDestinationTargetRegistrationService.class);

	private DestinationTargetService destinationTargetService;
	private ApiRegistrationService apiRegistrationService;

	@Override
	public void registerDestinationTarget(final KymaRegistrationRequest kymaRegistrationRequest,
	                                      final boolean isReRegistrationAllowed)
	{
		if (isReRegistrationAllowed && isDestinationTargetRegistered(kymaRegistrationRequest.getDestinationTargetId()))
		{
			reRegisterExistingDestinationTarget(kymaRegistrationRequest);
		}
		else
		{
			registerNewDestinationTarget(kymaRegistrationRequest);
		}
	}

	protected void reRegisterExistingDestinationTarget(final KymaRegistrationRequest kymaRegistrationRequest)
	{
		final DestinationTargetModel destinationTarget = destinationTargetService.getDestinationTargetById(
				kymaRegistrationRequest.getDestinationTargetId());
		destinationTarget.getDestinations()
		                 .stream()
		                 .filter(destination -> destination instanceof ExposedDestinationModel)
		                 .forEach(exposedDestination -> reRegisterExposedDestination(
				                 (ExposedDestinationModel) exposedDestination, destinationTarget.getId()));
	}

	protected void registerNewDestinationTarget(final KymaRegistrationRequest kymaRegistrationRequest)
	{
		DestinationTargetModel newDestinationTarget = null;
		try
		{
			final DestinationTargetModel templateDestinationTarget = readDestinationTargetTemplate(
					kymaRegistrationRequest.getTemplateDestinationTargetId());

			newDestinationTarget = destinationTargetService.createDestinationTarget(templateDestinationTarget,
					kymaRegistrationRequest.getDestinationTargetId(), DestinationChannel.KYMA);

			final Map<String, String> params = new HashMap<>();
			params.put(KymaDestinationTargetRegistrationStrategy.TOKEN_URL, kymaRegistrationRequest.getTokenUrl());

			destinationTargetService.registerDestinationTarget(newDestinationTarget, params);
			destinationTargetService.createDestinations(templateDestinationTarget, newDestinationTarget, null);
			destinationTargetService.createEventConfigurations(templateDestinationTarget, newDestinationTarget, null);
		}
		catch (final ApiRegistrationException | AmbiguousIdentifierException | ModelCreationException | IllegalArgumentException
				| ModelSavingException ex)
		{
			if (newDestinationTarget != null)
			{
				LOGGER.info("Delete the new destination target [{}] after registration failure!", newDestinationTarget.getId());
				deleteDestinationTarget(newDestinationTarget);
			}
			throw new KymaDestinationTargetRegistrationException(ex.getMessage(), ex);
		}
	}

	protected DestinationTargetModel readDestinationTargetTemplate(final String templateDestinationTargetId)
	{
		try
		{
			final DestinationTargetModel destinationTarget = destinationTargetService.getDestinationTargetById(
					templateDestinationTargetId);

			if ((Boolean.FALSE).equals(destinationTarget.getTemplate()))
			{
				throw new KymaDestinationTargetRegistrationException(
						String.format("The destination target [%s] is not a template! Choose a template destination target.",
								templateDestinationTargetId));
			}

			return destinationTarget;
		}
		catch (final ModelNotFoundException e)
		{
			throw new KymaDestinationTargetRegistrationException(
					String.format("The destination target template [%s] does not exist!", templateDestinationTargetId));
		}

	}

	protected void reRegisterExposedDestination(final ExposedDestinationModel exposedDestination,
	                                            final String destinationTargetId)
	{

		LOGGER.info("Re-registering exposed destination [{}] for destination target [{}]", exposedDestination.getId(),
				destinationTargetId);
		try
		{
			apiRegistrationService.registerExposedDestination(exposedDestination);
		}
		catch (final ApiRegistrationException e)
		{
			LOGGER.error("Error re-registering exposed destination [{}] for destination target [{}]", exposedDestination.getId(),
					destinationTargetId, e);
		}

	}

	protected void deleteDestinationTarget(final DestinationTargetModel destinationTarget)
	{
		if (destinationTarget != null)
		{
			try
			{
				destinationTargetService.deleteDestinationTarget(destinationTarget);
			}
			catch (final ApiRegistrationException e)
			{
				LOGGER.error("Error while deleting the new destination target [{}]!", destinationTarget.getId());
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	protected boolean isDestinationTargetRegistered(final String destinationId)
	{
		try
		{
			final DestinationTargetModel destinationTarget = destinationTargetService.getDestinationTargetById(destinationId);

			if ((Boolean.TRUE).equals(destinationTarget.getTemplate()))
			{
				throw new KymaDestinationTargetRegistrationException(
						String.format(
								"A template destination target with ID [%s] has been found! Choose another ID for the destination target.",
								destinationId));
			}

			LOGGER.info("The destination target [{}] has been found. It will be re-registered!", destinationId);
			return true;

		}
		catch (final ModelNotFoundException e)
		{
			LOGGER.info("The destination target [{}] has not been fount. It will be registered.", destinationId);
			return false;
		}
	}

	public void setDestinationTargetService(
			final DestinationTargetService destinationTargetService)
	{
		this.destinationTargetService = destinationTargetService;
	}

	public void setApiRegistrationService(final ApiRegistrationService apiRegistrationService)
	{
		this.apiRegistrationService = apiRegistrationService;
	}

}
