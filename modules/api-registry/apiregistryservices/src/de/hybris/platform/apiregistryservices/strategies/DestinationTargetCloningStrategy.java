/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;

import java.util.List;


/**
 * Strategy for creating a new destination target and its required objects from the template objects
 */
public interface DestinationTargetCloningStrategy
{
	/**
	 * Create a new destination target from the template destination target with the given id
	 *
	 * @param source
	 * 		the destination target which is used as a template
	 * @param newId
	 * 		the id of the new destination target
	 * @return a destination target
	 */
	DestinationTargetModel createDestinationTarget(DestinationTargetModel source, String newId);

	/**
	 * Create destinations for the target destination. Use destinations list to create new destinations unless it is empty,
	 * otherwise, use destination of the source destination target as a template for creating new destinations
	 *
	 * @param source
	 * 		the destination target which is used to create destinations
	 * @param target
	 * 		the destination target which owns the new destinations
	 * @param destinations
	 * 		the list of destinations
	 */
	void createDestinations(DestinationTargetModel source, DestinationTargetModel target,
			List<AbstractDestinationModel> destinations);

	/**
	 * Create event configurations for the target destination. Use eventConfigurations list to create new event configurations
	 * unless it is empty, otherwise, use event configurations of the source destination target as a template for creating new
	 * event configurations.
	 *
	 * @param source
	 * 		the destination target which is used to create event configurations
	 * @param target
	 * 		the destination target which owns the new event configurations
	 * @param eventConfigurations
	 * 		the list of event configurations
	 */
	void createEventConfigurations(DestinationTargetModel source, DestinationTargetModel target,
			List<EventConfigurationModel> eventConfigurations);

	/**
	 * Delete destination target and all related objects of it such as destinations, event configurations.
	 *
	 * @param destinationTarget
	 * 		the destinations target
	 */
	void deleteDestinationTarget(DestinationTargetModel destinationTarget);
}
