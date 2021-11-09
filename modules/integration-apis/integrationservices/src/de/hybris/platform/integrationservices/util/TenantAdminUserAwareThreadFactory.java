/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.jalo.JaloSession;

/**
 * A {@link java.util.concurrent.ThreadFactory} that is aware of the tenant and Admin user for the {@link JaloSession}.
 */
public final class TenantAdminUserAwareThreadFactory extends TenantAwareThreadFactory
{
	/**
	 * Instantiates a TenantAdminUserAwareThreadFactory
	 *
	 * @param tenant The tenant to use
	 */
	public TenantAdminUserAwareThreadFactory(final Tenant tenant)
	{
		super(tenant);
	}

	/**
	 * Instantiates a TenantAdminUserAwareThreadFactory
	 *
	 * @param tenant  The tenant to use
	 * @param session The session to use
	 */
	public TenantAdminUserAwareThreadFactory(final Tenant tenant, final JaloSession session)
	{
		super(tenant, session);
	}

	@Override
	protected void afterPrepareThread()
	{
		super.afterPrepareThread();
		final JaloSession session = JaloSession.getCurrentSession();
		session.getSessionContext().setUser(session.getUserManager().getAdminEmployee());
	}
}
