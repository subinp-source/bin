/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import javax.validation.constraints.NotNull;

/**
 * An exception indicating an Integration Object {@link de.hybris.platform.integrationservices.model.IntegrationObjectModel}
 * cannot be deleted if it was assigned an Inbounc Channel Configuration
 * {@link de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel}
 */
public class IntegrationObjectDeletionException extends InterceptorException
{
	private static final String ERROR_MESSAGE_TEMPLATE = "The [%s] cannot be deleted because it is exposed as an API. Please " +
			"delete the related InboundChannelConfiguration and try again.";

	private final String errorMessage;

	/**
	 * Constructor
	 *
	 * @param integrationObject integration object code
	 */
	public IntegrationObjectDeletionException(@NotNull final String integrationObject)
	{
		super(String.format(ERROR_MESSAGE_TEMPLATE, integrationObject));
		this.errorMessage = String.format(ERROR_MESSAGE_TEMPLATE, integrationObject);
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
}
