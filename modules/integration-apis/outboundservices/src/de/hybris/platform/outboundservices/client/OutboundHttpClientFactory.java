/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client;

import org.apache.http.client.HttpClient;

/**
 * Defines methods to creating a {@link HttpClient}
 */
public interface OutboundHttpClientFactory
{
	/**
	 * Creates a {@link HttpClient}
	 *
	 * @return HttpClient
	 */
	HttpClient create();
}
