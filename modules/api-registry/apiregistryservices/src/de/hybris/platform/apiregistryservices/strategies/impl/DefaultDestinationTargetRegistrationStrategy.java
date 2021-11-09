/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetRegistrationStrategy;


/**
 * Default implementation of {@link DestinationTargetRegistrationStrategy}
 */
public class DefaultDestinationTargetRegistrationStrategy implements DestinationTargetRegistrationStrategy
{

	@Override
	public void registerDestinationTarget(final DestinationTargetModel destinationTarget, final Map<String, String> params)
			throws ApiRegistrationException
	{
		// empty
	}

	@Override
	public void deregisterDestinationTarget(final DestinationTargetModel destinationTarget) throws ApiRegistrationException,
			DeleteDestinationTargetNotPossibleException
	{
		// empty
	}

	@Override
	public List<String> syncDestinationTargetWithRemoteSystem(final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		return Collections.emptyList();
	}

}
