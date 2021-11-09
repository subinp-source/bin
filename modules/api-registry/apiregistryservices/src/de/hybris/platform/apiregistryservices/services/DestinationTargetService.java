/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Service layer interface for destination targets
 */
public interface DestinationTargetService
{
	/**
	 * Create a new destination target from the template destination target with the given id
	 *
	 * @param source
	 * 		the destination target which is used as a template
	 * @param newId
	 * 		the id of the new destination target
	 * @param destinationChannel
	 * 	   channel of the new destination target
	 * @return the new destination target
	 * @throws ApiRegistrationException
	 * 	  		in case the DestinationTargetCloningStrategy doesn't exist for the given destination target
	 */
	DestinationTargetModel createDestinationTarget(DestinationTargetModel source, String newId, DestinationChannel destinationChannel) throws ApiRegistrationException;

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
	void registerDestinationTarget(DestinationTargetModel destinationTarget, Map<String, String> params) throws ApiRegistrationException;

	/**
	 * Create destinations for the target destination.
	 *
	 * @param source
	 * 		the destination target which is used to create destinations
	 * @param target
	 * 		the destination target which owns the new destinations
	 * @param destinations
	 * 		the list of destinations
	 * @throws ApiRegistrationException
	 * 	  		in case the DestinationTargetCloningStrategy doesn't exist for the given destination target
	 */
	void createDestinations(DestinationTargetModel source, DestinationTargetModel target,
			List<AbstractDestinationModel> destinations) throws ApiRegistrationException;

	/**
	 * Create event configurations for the target destination.
	 *
	 * @param source
	 * 		the destination target which is used to create event configurations
	 * @param target
	 * 		the destination target which owns the new event configurations
	 * @param eventConfigurations
	 * 		the list of event configurations
	 * @throws ApiRegistrationException
	 * 		in case the DestinationTargetCloningStrategy doesn't exist for the given destination target
	 */
	void createEventConfigurations(DestinationTargetModel source, DestinationTargetModel target,
			List<EventConfigurationModel> eventConfigurations) throws ApiRegistrationException;

	/**
	 * Register all exposed destinations of the given destination target.
	 *
	 * @param destinationTarget
	 * 		the destination target of exposed destinations
	 * @return the map of failed exposed destinations and error messages
	 */
	Map<String, String> registerExposedDestinations(DestinationTargetModel destinationTarget);

	/**
	 * Unregister (if the destination target is not a template) then delete exposed destinations of the given destination target
	 *
	 * @param destinationTarget
	 * 		the destination target
	 * @throws ApiRegistrationException
	 * 		in case the deletion fails
	 * @return the collection of service warnings if any
	 */
	Collection<ServiceWarning<DestinationTargetModel>> deregisterAndDeleteDestinationTarget(DestinationTargetModel destinationTarget) throws ApiRegistrationException;

	/**
	 * Delete destination target and all related objects of it such as destinations, event configurations.
	 *
	 * @param destinationTarget
	 * 		the destinations target
	 * @throws ApiRegistrationException
	 * 		in case the DestinationTargetCloningStrategy doesn't exist for the given destination target
	 */
	void deleteDestinationTarget(DestinationTargetModel destinationTarget) throws ApiRegistrationException;

	/**
	 * Sync destination target with the remote system.
	 *
	 * @param destinationTarget
	 * 		the destination target
	 * @throws ApiRegistrationException
	 * 		in case the synchronization fails
	 */
	void syncDestinationTargetWithRemoteSystem(DestinationTargetModel destinationTarget) throws ApiRegistrationException;

	/**
	 * Get the destination target for a specific credential id
	 * @param credentialId
	 * 		id of the credential
	 * @return a destination target
	 */
	DestinationTargetModel getDestinationTargetByCredentialId (String credentialId);


	/**
	 * Get the destination target for a specific id
	 * @param id
	 * 		id of the destination target
	 * @return a destination target
	 */
	DestinationTargetModel getDestinationTargetById (String id);
}
