/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.listeners;

import de.hybris.platform.outboundsync.events.OutboundSyncEvent;
import de.hybris.platform.outboundsync.job.impl.OutboundSyncJobRegister;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import javax.validation.constraints.NotNull;

public class OutboundSyncEventListener extends AbstractEventListener<OutboundSyncEvent>
{
	private final OutboundSyncJobRegister jobRegister;

	public OutboundSyncEventListener(@NotNull final OutboundSyncJobRegister register)
	{
		jobRegister = register;
	}

	@Override
	public synchronized void onEvent(final OutboundSyncEvent event)
	{
		jobRegister.getJob(event.getCronJobPk())
		           .ifPresent(job -> job.applyEvent(event));
	}
}
