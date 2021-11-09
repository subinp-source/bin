/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.exceptions;

import de.hybris.platform.subscriptionfacades.SubscriptionFacade;


/**
 * Exception type thrown by methods of the {@link SubscriptionFacade}.
 */
public class SubscriptionFacadeException extends Exception
{

	public SubscriptionFacadeException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SubscriptionFacadeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SubscriptionFacadeException(final String message)
	{
		super(message);
	}

	public SubscriptionFacadeException(final Throwable cause)
	{
		super(cause);
	}

}
