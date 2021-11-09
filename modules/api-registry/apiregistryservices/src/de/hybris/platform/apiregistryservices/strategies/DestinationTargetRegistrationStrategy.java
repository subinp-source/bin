/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;

import java.util.List;
import java.util.Map;


/**
 * Strategy for creating required target system specific objects and registration to the target system.
 */
public interface DestinationTargetRegistrationStrategy
{
	/**
	 * Register a destination target and its assigned destinations and events in the target system
	 *
	 * @param destinationTarget
	 * 		the destination target
	 * @param params
	 * 		the parameters map which is used for passing additional parameters
	 * @throws ApiRegistrationException
	 * 		in case the registration fails
	 */
	void registerDestinationTarget(DestinationTargetModel destinationTarget, Map<String, String> params)
			throws ApiRegistrationException;

	/**
	 * Unregister exposed destinations of the given destination target
	 *
	 * @param destinationTarget
	 * 		the destination target
	 * @throws ApiRegistrationException
	 * 		in case the unregistration fails
	 * @throws DeleteDestinationTargetNotPossibleException
	 *       in case of deregistering the destination target is not allowed
	 */
	void deregisterDestinationTarget(DestinationTargetModel destinationTarget) throws ApiRegistrationException,
			DeleteDestinationTargetNotPossibleException;

	/**
	 * Sync destination target with the remote system.
	 *
	 * @param destinationTarget
	 * 		the destination target
	 * @return list of sync failed destinations
	 * @throws ApiRegistrationException
	 * 		in case the synchronization fails
	 */
	List<String> syncDestinationTargetWithRemoteSystem(DestinationTargetModel destinationTarget) throws ApiRegistrationException;
}
