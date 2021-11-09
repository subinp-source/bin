/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class KymaDestinationTargetRegistrationException extends SystemException
{

	public KymaDestinationTargetRegistrationException(final String message)
	{
		super(message);
	}

	public KymaDestinationTargetRegistrationException(final Throwable cause)
	{
		super(cause);
	}

	public KymaDestinationTargetRegistrationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
