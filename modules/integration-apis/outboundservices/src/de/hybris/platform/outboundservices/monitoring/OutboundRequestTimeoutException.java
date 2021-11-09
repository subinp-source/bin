/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.monitoring;

import de.hybris.platform.integrationservices.util.timeout.IntegrationTimeoutException;

/**
 * The OutboundRequestTimeoutException is thrown when a request timeout occurs.
 * It extends {@link IntegrationTimeoutException} so to provide an error message
 * that is more relevant to outbound requests.
 */
public class OutboundRequestTimeoutException extends IntegrationTimeoutException
{
	/**
	 * Instantiates the OutboundRequestTimeoutException
	 *
	 * @param timeout The timeout value
	 */
	public OutboundRequestTimeoutException(final long timeout)
	{
		super(String.format("Request timed out after %d ms.", timeout), timeout);
	}
}
