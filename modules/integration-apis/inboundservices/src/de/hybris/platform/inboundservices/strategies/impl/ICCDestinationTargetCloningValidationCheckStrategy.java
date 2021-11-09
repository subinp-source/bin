/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.strategies.impl;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetCloningValidationCheckStrategy;


/**
 * InboundChannelConfiguration impl of @{@link DestinationTargetCloningValidationCheckStrategy}
 */
public class ICCDestinationTargetCloningValidationCheckStrategy implements DestinationTargetCloningValidationCheckStrategy
{
	@Override
	public boolean isValidExposedDestination(final AbstractDestinationModel destination)
	{
		return  !(destination instanceof ExposedDestinationModel && ((ExposedDestinationModel) destination).getInboundChannelConfiguration() != null);
	}
}
