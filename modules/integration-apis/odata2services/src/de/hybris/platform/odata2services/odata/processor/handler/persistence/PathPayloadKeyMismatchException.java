/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

/**
 * Throws this exception when the integration key provided in the request payload does not match the key in the path
 */
public class PathPayloadKeyMismatchException extends RuntimeException
{
	private static final String MSG = "Key [%s] in the payload does not match the key [%s] in the path";

	private final String payloadIntegrationKey;
	private final String pathIntegrationKey;

	public PathPayloadKeyMismatchException(final String pathIntegrationKey, final String payloadIntegrationKey)
	{
		super(String.format(MSG, payloadIntegrationKey, pathIntegrationKey));
		this.payloadIntegrationKey = payloadIntegrationKey;
		this.pathIntegrationKey = pathIntegrationKey;
	}

	public String getPayloadIntegrationKey()
	{
		return payloadIntegrationKey;
	}

	public String getPathIntegrationKey()
	{
		return pathIntegrationKey;
	}
}
