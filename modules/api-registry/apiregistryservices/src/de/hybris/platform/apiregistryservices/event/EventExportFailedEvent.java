/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Event export failed cluster event
 */
public class EventExportFailedEvent extends AbstractEvent implements ClusterAwareEvent
{

	private String message;
	private Integer retry;

	public EventExportFailedEvent(final String message, final Integer retry)
	{
		this.message = message;
		this.retry = retry;
	}

	@Override
	public boolean canPublish(final PublishEventContext publishEventContext)
	{
		return true;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(final String message)
	{
		this.message = message;
	}

	public Integer getRetry()
	{
		return retry;
	}

	public void setRetry(final Integer retry)
	{
		this.retry = retry;
	}
}
