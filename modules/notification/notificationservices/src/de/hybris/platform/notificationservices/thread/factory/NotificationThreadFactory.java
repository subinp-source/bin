/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.thread.factory;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloSession;

import java.util.concurrent.ThreadFactory;

/**
 * Thread factory used to create threads
 */
public class NotificationThreadFactory implements ThreadFactory
{
	private final Tenant currentTenant;

	public NotificationThreadFactory()
	{
		currentTenant = Registry.getCurrentTenant();
	}

	@Override
	public Thread newThread(final Runnable runnable)
	{
		return new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(currentTenant);
					JaloSession.getCurrentSession().activate();
					runnable.run();
				}
				finally
				{
					JaloSession.getCurrentSession().close();
					JaloSession.deactivate();
					Registry.unsetCurrentTenant();
				}
			}
		};
	}


}
