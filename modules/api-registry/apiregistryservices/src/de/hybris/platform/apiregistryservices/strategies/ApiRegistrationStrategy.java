/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;

import java.util.List;


/**
 * Service for exporting/registration webservices specifications
 */
public interface ApiRegistrationStrategy
{
	/**
	 * Send api specification to register exposed destination
	 *
	 * @param destination
     */
	void registerExposedDestination(ExposedDestinationModel destination) throws ApiRegistrationException;


	/**
	 * Send api specification to unregister exposed destination
	 *
	 * @param destination
	 */
	void unregisterExposedDestination(ExposedDestinationModel destination) throws ApiRegistrationException;

	/**
	 * Retrieves all registered exposed destination from target system
	 *
	 * @return list of destination data
	 * @throws ApiRegistrationException
	 * 		in case when retrieving destinations fails
	 */
	List<RegisteredDestinationData> retrieveRegisteredExposedDestinations(DestinationTargetModel destinationTarget) throws ApiRegistrationException;

	/**
	 * Unregister exposed destination from target system
	 *
	 * @param targetId
	 * 		target id of the registered destination
	 * @param destinationTargetId
	 * 		id of the destination target
	 * @throws ApiRegistrationException
	 */
	void unregisterExposedDestinationByTargetId(String targetId, String destinationTargetId) throws ApiRegistrationException;
}
