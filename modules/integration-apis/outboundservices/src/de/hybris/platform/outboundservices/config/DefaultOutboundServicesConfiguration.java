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

import de.hybris.platform.integrationservices.config.BaseIntegrationServicesConfiguration;

/**
 * Provides access methods to configurations related to the Outbound Services
 */
public class DefaultOutboundServicesConfiguration extends BaseIntegrationServicesConfiguration implements OutboundServicesConfiguration
{
	private static final String PAYLOAD_RETENTION_SUCCESS_KEY = "outboundservices.monitoring.success.payload.retention";
	private static final String PAYLOAD_RETENTION_ERROR_KEY = "outboundservices.monitoring.error.payload.retention";

	private static final boolean PAYLOAD_RETENTION_SUCCESS_FALLBACK = false;
	private static final boolean PAYLOAD_RETENTION_ERROR_FALLBACK = true;

	private static final String MONITORING_ENABLED_KEY = "outboundservices.monitoring.enabled";
	private static final boolean MONITORING_ENABLED_FALLBACK = true;

	private static final String MAX_RESPONSE_PAYLOAD_SIZE_KEY = "outboundservices.response.payload.max.size.bytes";
	private static final int MAX_RESPONSE_PAYLOAD_SIZE_FALLBACK = 1024;

	private static final String REQUEST_EXECUTION_TIMEOUT_KEY = "outboundservices.request.execution.timeout.millisecs";
	private static final long REQUEST_EXECUTION_TIMEOUT_MILLISECS_FALLBACK = DEFAULT_REQUEST_EXECUTION_TIMEOUT_MS;

	@Override
	public boolean isPayloadRetentionForSuccessEnabled()
	{
		return getBooleanProperty(PAYLOAD_RETENTION_SUCCESS_KEY, PAYLOAD_RETENTION_SUCCESS_FALLBACK);
	}

	@Override
	public boolean isPayloadRetentionForErrorEnabled()
	{
		return getBooleanProperty(PAYLOAD_RETENTION_ERROR_KEY, PAYLOAD_RETENTION_ERROR_FALLBACK);
	}

	@Override
	public boolean isMonitoringEnabled()
	{
		return getBooleanProperty(MONITORING_ENABLED_KEY, MONITORING_ENABLED_FALLBACK);
	}

	@Override
	public int getMaximumResponsePayloadSize(){
		return getIntegerProperty(MAX_RESPONSE_PAYLOAD_SIZE_KEY, MAX_RESPONSE_PAYLOAD_SIZE_FALLBACK);
	}

	/**
	 * Sets the value of the maximum payload size accepted in outbound responses.
	 * @param maxPayloadSize Number of bytes allowed in the response payload
	 */
	public void setMaximumResponsePayloadSize(final int maxPayloadSize){
		setProperty(MAX_RESPONSE_PAYLOAD_SIZE_KEY, maxPayloadSize);
	}

	@Override
	public long getRequestExecutionTimeout()
	{
		return getLongProperty(REQUEST_EXECUTION_TIMEOUT_KEY, REQUEST_EXECUTION_TIMEOUT_MILLISECS_FALLBACK);
	}

	/**
	 * Sets the outbound request timeout in milliseconds
	 * @param timeout The number of milliseconds the outbound request can execute before timing out
	 */
	public void setRequestExecutionTimeout(final long timeout)
	{
		final var value = timeout < 0 ? REQUEST_EXECUTION_TIMEOUT_MILLISECS_FALLBACK : timeout;
		setProperty(REQUEST_EXECUTION_TIMEOUT_KEY, value);
	}
}
