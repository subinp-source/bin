/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.lifecycle;

/**
 * A utility to indicate the lifecycle of the tenant
 */
public interface TenantLifecycle
{
	/**
	 * Indicates whether the tenant is at the operational state,
	 * i.e. tenant is not initializing, starting up, nor shutting down
	 *
	 * @return {@code true} if tenant is at the operational state, else {@code false}
	 */
	boolean isOperational();
}
