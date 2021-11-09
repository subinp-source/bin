/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;

/**
 * Strategy for verifying the validated exposed destination
 */
public interface DestinationTargetCloningValidationCheckStrategy
{
	boolean isValidExposedDestination(final AbstractDestinationModel destinationModel);
}
