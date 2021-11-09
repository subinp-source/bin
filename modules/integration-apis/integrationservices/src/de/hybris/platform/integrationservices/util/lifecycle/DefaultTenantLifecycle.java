/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.lifecycle;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link TenantLifecycle}
 */
public class DefaultTenantLifecycle extends AbstractEventListener<AbstractEvent> implements TenantLifecycle, TenantListener
{
	private static final Log LOGGER = Log.getLogger(DefaultTenantLifecycle.class);

	private final AtomicBoolean initializing = new AtomicBoolean(false);
	private final AtomicBoolean tenantReady = new AtomicBoolean(false);
	private final Tenant tenant;

	/**
	 * Instantiates the TenantLifecycle object for the given tenant
	 *
	 * @param tenant Tenant this lifecycle object is running under
	 */
	public DefaultTenantLifecycle(@NotNull final Tenant tenant)
	{
		Preconditions.checkArgument(tenant != null, "Tenant must be provided.");
		this.tenant = tenant;
		Registry.registerTenantListener(this);
	}

	@Override
	public synchronized boolean isOperational()
	{
		return tenantReady.get();
	}

	@Override
	public synchronized void afterTenantStartUp(final Tenant tenant)
	{
		if (this.tenant.equals(tenant) && !initializing.get())
		{
			LOGGER.debug("Tenant <<{}>> is started", tenant.getTenantID());
			tenantReady.set(true);
		}
	}

	@Override
	public synchronized void beforeTenantShutDown(final Tenant tenant)
	{
		if (this.tenant.equals(tenant) && !initializing.get())
		{
			LOGGER.debug("Tenant <<{}>> is shutting down", tenant.getTenantID());
			tenantReady.set(false);
		}
	}

	@Override
	public void afterSetActivateSession(final Tenant tenant)
	{
		// not implemented
	}

	@Override
	public void beforeUnsetActivateSession(final Tenant tenant)
	{
		// not implemented
	}

	@Override
	protected synchronized void onEvent(final AbstractEvent event)
	{
		if (event instanceof AfterInitializationStartEvent)
		{
			LOGGER.debug("Tenant <<{}>> is not operational because initialization has started", tenant.getTenantID());
			tenantReady.set(false);
			initializing.set(true);
		}
		else if (event instanceof AfterInitializationEndEvent)
		{
			LOGGER.debug("Tenant <<{}>> is operational because initialization has ended", tenant.getTenantID());
			tenantReady.set(true);
			initializing.set(false);
		}
	}
}