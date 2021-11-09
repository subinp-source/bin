/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.monitoring;

import de.hybris.platform.integrationservices.util.timeout.IntegrationExecutionException;

/**
 * The OutboundRequestExecutionException is thrown when a request encountered an exception during processing.
 * It extends the {@link IntegrationExecutionException} to provide an error message that is more relevant
 * to outbound requests.
 */
public class OutboundRequestExecutionException extends IntegrationExecutionException
{
	/**
	 * Instantiates an OutboundRequestExecutionException
	 *
	 * @param cause The cause of the exception
	 */
	public OutboundRequestExecutionException(final Throwable cause)
	{
		super("Request encountered an exception.", cause);
	}
}
