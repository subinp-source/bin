/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.services.DestinationCredentialService;


/**
 * Default implementation of {@link de.hybris.platform.apiregistryservices.services.DestinationCredentialService}
 */
public class DefaultDestinationCredentialService implements DestinationCredentialService
{

	@Override
	public boolean isValidDestinationCredential(final AbstractDestinationModel destination)
	{
		return destination instanceof ConsumedDestinationModel ? isValidConsumedDestination((ConsumedDestinationModel) destination) :
				isValidExposedDestination((ExposedDestinationModel) destination);
	}

	private boolean isValidConsumedDestination(final ConsumedDestinationModel destination)
	{
		return !(destination.getCredential() instanceof ExposedOAuthCredentialModel);
	}

	private boolean isValidExposedDestination(final ExposedDestinationModel destination)
	{
		return !(destination.getCredential() instanceof ConsumedOAuthCredentialModel);
	}
}
