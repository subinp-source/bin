/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundservices.config;

import de.hybris.platform.integrationservices.monitoring.MonitoringConfiguration;

/**
 * Configuration for Outbound Services extension
 */
public interface OutboundServicesConfiguration extends MonitoringConfiguration
{
	int DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE = 1024;
	long DEFAULT_REQUEST_EXECUTION_TIMEOUT_MS = 5000;

	/**
	 * Retrieves the maximum response payload size
	 *
	 * @return the maximum payload size
	 */
	default int getMaximumResponsePayloadSize()
	{
		return DEFAULT_MAX_RESPONSE_PAYLOAD_SIZE;
	}

	/**
	 * Gets the outbound request timeout in milliseconds
	 * @return timeout value
	 */
	default long getRequestExecutionTimeout()
	{
		return DEFAULT_REQUEST_EXECUTION_TIMEOUT_MS;
	}
}
