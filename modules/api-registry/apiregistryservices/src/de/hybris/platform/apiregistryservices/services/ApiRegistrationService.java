/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;

import java.util.List;


/**
 * Service for exporting/registering APIs
 */
public interface ApiRegistrationService
{
	/**
	 * Register an ExposedDestination
	 *
	 * @param destination
	 *           ExposedDestination to be registered
	 *
	 * @throws ApiRegistrationException
	 *            in case when failed to register Destination
	 */
	void registerExposedDestination(ExposedDestinationModel destination) throws ApiRegistrationException;

	/**
	 * Unregister an ExposedDestination
	 *
	 * @param destination
	 *           ExposedDestination to be unregistered
	 * @throws ApiRegistrationException
	 *            in case when failed to unregister Destination
	 */
	void unregisterExposedDestination(ExposedDestinationModel destination) throws ApiRegistrationException;

	/**
	 * Retrieves all registered exposed destinations from target system
	 *
	 * @param destinationChannel
	 * 		destination channel of the target system
	 * @param destinationTarget
	 * 		destination target
	 * @return list of destination data
	 * @throws ApiRegistrationException
	 * 		in case when retrieving destinations fails
	 */
	List<RegisteredDestinationData> retrieveRegisteredExposedDestinations(final DestinationChannel destinationChannel, DestinationTargetModel destinationTarget)
			throws ApiRegistrationException;

	/**
	 * Unregister exposed destination from target system
	 *
	 * @param targetId
	 * 		target id of the registered destination
	 * @param destinationTargetModel
	 * 		destination target
	 * @throws ApiRegistrationException
	 * 		in case when failed to unregister Destination
	 */
	void unregisterExposedDestinationByTargetId(final String targetId, DestinationTargetModel destinationTargetModel)
			throws ApiRegistrationException;
}
